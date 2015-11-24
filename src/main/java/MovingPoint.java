import model.Direction;

public class MovingPoint {
    private Waypoint waypoint;
    private Point point;
    private Direction directionBefore;
    private Direction directionAfter;
    private int index;

    public MovingPoint(Waypoint waypoint, Point point, Direction directionBefore, Direction directionAfter, int index) {
        this.waypoint = waypoint;
        this.point = point;
        this.directionBefore = directionBefore;
        this.directionAfter = directionAfter;
        this.index = index;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Waypoint getWaypoint() {
        return waypoint;
    }

    public Direction getDirectionBefore() {
        return directionBefore;
    }

    public Direction getDirectionAfter() {
        return directionAfter;
    }

    public boolean isTurn(){
        return directionBefore != directionAfter;
    }

    @Override
    public String toString() {
        return "MovingPoint{" +
                "waypoint=" + waypoint +
                ", directionBefore=" + directionBefore +
                ", directionAfter=" + directionAfter +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MovingPoint that = (MovingPoint) o;

        if (getIndex() != that.getIndex()) return false;
        if (!getWaypoint().equals(that.getWaypoint())) return false;
        if (getDirectionBefore() != that.getDirectionBefore()) return false;
        return getDirectionAfter() == that.getDirectionAfter();

    }

    @Override
    public int hashCode() {
        int result = getWaypoint().hashCode();
        result = 31 * result + getDirectionBefore().hashCode();
        result = 31 * result + getDirectionAfter().hashCode();
        result = 31 * result + getIndex();
        return result;
    }

    public int getIndex() {
        return index;
    }
}
