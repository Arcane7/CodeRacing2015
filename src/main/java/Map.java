import model.*;

import java.util.*;

public class Map {

    private List<MovingPoint> points = new ArrayList<>();

    private MovingPoint lastPoint;

    public List<MovingPoint> getMap() {
        return Collections.unmodifiableList(points);
    }

    public boolean isInitialized() {
        return !points.isEmpty();
    }

    // TODO sse: generate in points right away! use direction to restrict going back paths!
    public void buildMap(World world, Game game) {
        List<Waypoint> completePath = findPathInWaypoints(world);

        preparePointsPath(world, game, completePath);
    }

    public MovingPoint getPrevPoint(MovingPoint point) {
        if(point.getIndex() == 0){
            return points.get(points.size() -1);
        } else {
            return points.get(point.getIndex() -1);
        }
    }

    public MovingPoint getCurrentPoint(Car self, World world, Game game) {
        MovingPoint result = null;
        Waypoint tile = Utils.getTile(world, game, new Point(self.getX(), self.getY()));

        int startFromWP = self.getNextWaypointIndex() - 1;
        if(startFromWP < 0){
            startFromWP = world.getWaypoints().length-1;
        }

        boolean startPoint = false;
        int i = 0;
        for (MovingPoint point : points) {
            if (point.getWaypoint().getIndex() != null && point.getWaypoint().getIndex().equals(startFromWP)) {
                startPoint = true;
            } else {
                i++;
            }

            if (startPoint && point.getWaypoint().equals(tile)) {
                result = point;
                break;
            }
        }

        // search backwards - in case car has lost a path
        result = searchBackwards(result, tile, i);

        return rememberLastPoint(result);
    }

    private MovingPoint searchBackwards(MovingPoint result, Waypoint tile, int i) {
        for(;i>0; i--){
            MovingPoint movingPoint = points.get(i);
            if (movingPoint.getWaypoint().equals(tile)) {
                result = movingPoint;
                break;
            }
        }
        return result;
    }

    private MovingPoint rememberLastPoint(MovingPoint result) {
        if(result == null) {
            return lastPoint;
        } else {
            lastPoint = result;
            return result;
        }
    }

    public MovingPoint getNextTurn(MovingPoint start) {
        MovingPoint mp;
        boolean foundStart = false;
        for (int i = 0; i < points.size(); i++) {
            mp = points.get(i);

            if (foundStart) {
                if (mp.isTurn()) {
                    return mp;
                }
            }

            if (!foundStart && mp.equals(start)) {
                foundStart = true;
            }
        }

        return getNextTurn(points.get(0));
    }

    private void preparePointsPath(World world, Game game, List<Waypoint> completePath) {
        Direction prevDirection = world.getStartingDirection();
        Direction nextDirection;
        for (int i = 0; i < completePath.size() - 1; i++) {
            Waypoint waypoint = completePath.get(i);
            Waypoint nextWaypoint = completePath.get(i + 1);

            nextDirection = Utils.getDirection(waypoint, nextWaypoint);
            Point waypointCenter = Utils.getWaypointCenter(waypoint, game);
            MovingPoint mp = new MovingPoint(waypoint, waypointCenter, prevDirection, nextDirection, i);
            points.add(mp);
            prevDirection = nextDirection;
        }

        Waypoint waypoint = completePath.get(completePath.size() - 1);
        Point waypointCenter = Utils.getWaypointCenter(waypoint, game);
        MovingPoint mp = new MovingPoint(waypoint, waypointCenter, prevDirection, world.getStartingDirection(), completePath.size()-1);
        points.add(mp);
    }

    private List<Waypoint> findPathInWaypoints(World world) {
        List<Waypoint> completePath = new ArrayList<>();

        // adjustment for starting point - to make ring
        int[][] waypoints = world.getWaypoints();
        if (!Arrays.equals(waypoints[waypoints.length - 1], waypoints[0])) {
            waypoints = Arrays.copyOf(waypoints, waypoints.length + 1);
            waypoints[waypoints.length-1] = waypoints[0];
        }

        for (int i = 0; i < waypoints.length - 1; i++) {
            int[] wpts1 = waypoints[i];
            int[] wpts2 = waypoints[i + 1];
            int x = wpts1[0];
            int y = wpts1[1];
            Waypoint waypoint1 = new Waypoint(x, y, world.getTilesXY()[x][y], i);
            x = wpts2[0];
            y = wpts2[1];
            Waypoint waypoint2 = new Waypoint(x, y, world.getTilesXY()[x][y], i + 1);

            Set<Waypoint> visited = new HashSet<>();
            List<Waypoint> path = findPath(waypoint1, waypoint2, visited, world, 0, maxDepths(world));
            if (!completePath.isEmpty()) {
                path.remove(0);
            }

            completePath.addAll(path);
        }

        return completePath;
    }

    private int maxDepths(World world) {
        return world.getWidth() * 2;
    }

    private List<Waypoint> findPath(Waypoint waypoint1, Waypoint waypoint2, Set<Waypoint> visited, World world, int depth, int maxDepth) {
        if(depth >= maxDepth){
            return null;
        }

        depth++;
        visited.add(waypoint1);
        List<Waypoint> result = new ArrayList<>();
        result.add(waypoint1);

        Waypoint right = getWaypoint(waypoint1, Direction.RIGHT, visited, world);
        Waypoint left = getWaypoint(waypoint1, Direction.LEFT, visited, world);
        Waypoint up = getWaypoint(waypoint1, Direction.UP, visited, world);
        Waypoint down = getWaypoint(waypoint1, Direction.DOWN, visited, world);

        if (waypoint2.equals(right) ||
                waypoint2.equals(left) ||
                waypoint2.equals(up) ||
                waypoint2.equals(down)) {
            result.add(waypoint2);
            return result;
        }

        List<Waypoint> path1 = null;
        if (right != null) {
            Set<Waypoint> visited1 = new HashSet<>(visited);
            path1 = findPath(right, waypoint2, visited1, world, depth, maxDepth);
        }
        List<Waypoint> path2 = null;
        if (left != null) {
            Set<Waypoint> visited2 = new HashSet<>(visited);
            path2 = findPath(left, waypoint2, visited2, world, depth, maxDepth);
        }
        List<Waypoint> path3 = null;
        if (up != null) {
            Set<Waypoint> visited3 = new HashSet<>(visited);
            path3 = findPath(up, waypoint2, visited3, world, depth, maxDepth);
        }
        List<Waypoint> path4 = null;
        if (down != null) {
            Set<Waypoint> visited4 = new HashSet<>(visited);
            path4 = findPath(down, waypoint2, visited4, world, depth, maxDepth);
        }

        List<Waypoint> waypoints = shortestPath(path1, path2, path3, path4);
        if (waypoints == null) {
            return null;
        }

        result.addAll(waypoints);
        return result;
    }

    private List<Waypoint> shortestPath(List<Waypoint> path1, List<Waypoint> path2, List<Waypoint> path3, List<Waypoint> path4) {
        List<Waypoint>[] paths = new List[]{path1, path2, path3, path4};
        List<Waypoint> result = null;
        for (int i = 0; i < paths.length; i++) {
            if (paths[i] != null) {
                if (result == null || result.size() > paths[i].size()) {
                    result = paths[i];
                }
            }
        }

        return result;
    }

    private Waypoint getWaypoint(Waypoint waypoint1, Direction direction, Set<Waypoint> visited, World world) {
        Waypoint result = null;

        switch (direction) {
            case UP:
                if (waypoint1.getTile() == TileType.CROSSROADS ||
                        waypoint1.getTile() == TileType.VERTICAL ||
                        waypoint1.getTile() == TileType.LEFT_BOTTOM_CORNER ||
                        waypoint1.getTile() == TileType.RIGHT_BOTTOM_CORNER ||
                        waypoint1.getTile() == TileType.LEFT_HEADED_T ||
                        waypoint1.getTile() == TileType.RIGHT_HEADED_T ||
                        waypoint1.getTile() == TileType.TOP_HEADED_T) {
                    int x = waypoint1.getX();
                    int y = waypoint1.getY() - 1;
                    result = new Waypoint(x, y, world.getTilesXY()[x][y], null);
                }
                break;
            case DOWN:
                if (waypoint1.getTile() == TileType.CROSSROADS ||
                        waypoint1.getTile() == TileType.VERTICAL ||
                        waypoint1.getTile() == TileType.LEFT_TOP_CORNER ||
                        waypoint1.getTile() == TileType.RIGHT_TOP_CORNER ||
                        waypoint1.getTile() == TileType.LEFT_HEADED_T ||
                        waypoint1.getTile() == TileType.RIGHT_HEADED_T ||
                        waypoint1.getTile() == TileType.BOTTOM_HEADED_T) {
                    int x = waypoint1.getX();
                    int y = waypoint1.getY() + 1;
                    result = new Waypoint(x, y, world.getTilesXY()[x][y], null);
                }
                break;
            case RIGHT:
                if (waypoint1.getTile() == TileType.CROSSROADS ||
                        waypoint1.getTile() == TileType.HORIZONTAL ||
                        waypoint1.getTile() == TileType.LEFT_TOP_CORNER ||
                        waypoint1.getTile() == TileType.LEFT_BOTTOM_CORNER ||
                        waypoint1.getTile() == TileType.RIGHT_HEADED_T ||
                        waypoint1.getTile() == TileType.TOP_HEADED_T ||
                        waypoint1.getTile() == TileType.BOTTOM_HEADED_T) {
                    int x = waypoint1.getX() + 1;
                    int y = waypoint1.getY();
                    result = new Waypoint(x, y, world.getTilesXY()[x][y], null);
                }
                break;
            case LEFT:
                if (waypoint1.getTile() == TileType.CROSSROADS ||
                        waypoint1.getTile() == TileType.HORIZONTAL ||
                        waypoint1.getTile() == TileType.RIGHT_TOP_CORNER ||
                        waypoint1.getTile() == TileType.RIGHT_BOTTOM_CORNER ||
                        waypoint1.getTile() == TileType.LEFT_HEADED_T ||
                        waypoint1.getTile() == TileType.TOP_HEADED_T ||
                        waypoint1.getTile() == TileType.BOTTOM_HEADED_T) {
                    int x = waypoint1.getX() - 1;
                    int y = waypoint1.getY();
                    result = new Waypoint(x, y, world.getTilesXY()[x][y], null);
                }
                break;
        }

        if (result != null) {
            if (visited.contains(result)) {
                result = null;
            }
        }

        return result;
    }

}
