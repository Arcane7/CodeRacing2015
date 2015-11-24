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
}
