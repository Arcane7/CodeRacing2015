import model.Direction;
import model.Game;
import model.World;

public class Utils {

    private static Map map = new Map();

    public static Waypoint getWaypoint(World world, int waypointIndex) {
        if (waypointIndex >= world.getWaypoints().length) {
            waypointIndex = waypointIndex % world.getWaypoints().length;
        }

        if (waypointIndex < 0) {
            waypointIndex = world.getWaypoints().length + waypointIndex;
        }

        int[] waypointPos = world.getWaypoints()[waypointIndex];
        return new Waypoint(waypointPos[0], waypointPos[1], world.getTilesXY()[waypointPos[0]][waypointPos[1]], waypointIndex);
    }

    public static Direction getDirection(Waypoint waypoint1, Waypoint waypoint2) {
        if (waypoint2.getX() < waypoint1.getX()) {
            return Direction.LEFT;
        }
        if (waypoint2.getX() > waypoint1.getX()) {
            return Direction.RIGHT;
        }
        if (waypoint2.getY() < waypoint1.getY()) {
            return Direction.UP;
        }
        if (waypoint2.getY() > waypoint1.getY()) {
            return Direction.DOWN;
        }
        return null;
    }

    public static Point getWaypointCenter(Waypoint waypoint, Game game) {
        double waypointX = (waypoint.getX() + 0.5D) * game.getTrackTileSize();
        double waypointY = (waypoint.getY() + 0.5D) * game.getTrackTileSize();

        return new Point(waypointX, waypointY);
    }

    public static Waypoint getTile(World world, Game game, Point point) {
        int x = (int) (point.getX() / game.getTrackTileSize());
        int y = (int) (point.getY() / game.getTrackTileSize());

        return new Waypoint(x, y, world.getTilesXY()[x][y], null);
    }

    public static Map getMap() {
        return map;
    }

    public static boolean isOpposite(Direction direction1, Direction direction2){
        if(direction1 == Direction.RIGHT && direction2 == Direction.LEFT ||
                direction1 == Direction.LEFT && direction2 == Direction.RIGHT ||
                direction1 == Direction.UP && direction2 == Direction.DOWN ||
                direction1 == Direction.DOWN && direction2 == Direction.UP){
            return true;
        }
        return false;
    }

    public static Point adjustTargetPoint(Game game, MovingPoint point) {
        Point waypoint1Center = point.getPoint().clone();

        double cornerTileOffset = 0.2D * game.getTrackTileSize();

        switch (point.getDirectionBefore()) {
            case UP:
                switch (point.getDirectionAfter()) {
                    case LEFT:
                        waypoint1Center.addX(-cornerTileOffset);
                        waypoint1Center.addY(cornerTileOffset);
                        break;
                    case RIGHT:
                        waypoint1Center.addX(cornerTileOffset);
                        waypoint1Center.addY(cornerTileOffset);
                        break;
                }
                break;
            case DOWN:
                switch (point.getDirectionAfter()) {
                    case LEFT:
                        waypoint1Center.addX(-cornerTileOffset);
                        waypoint1Center.addY(-cornerTileOffset);
                        break;
                    case RIGHT:
                        waypoint1Center.addX(cornerTileOffset);
                        waypoint1Center.addY(-cornerTileOffset);
                        break;
                }
                break;
            case LEFT:
                switch (point.getDirectionAfter()) {
                    case UP:
                        waypoint1Center.addX(cornerTileOffset);
                        waypoint1Center.addY(-cornerTileOffset);
                        break;
                    case DOWN:
                        waypoint1Center.addX(cornerTileOffset);
                        waypoint1Center.addY(cornerTileOffset);
                        break;
                }
                break;
            case RIGHT:
                switch (point.getDirectionAfter()) {
                    case UP:
                        waypoint1Center.addX(-cornerTileOffset);
                        waypoint1Center.addY(-cornerTileOffset);
                        break;
                    case DOWN:
                        waypoint1Center.addX(-cornerTileOffset);
                        waypoint1Center.addY(cornerTileOffset);
                        break;
                }
                break;
        }

        return waypoint1Center;
    }

}
