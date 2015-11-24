import model.TileType;

public class Waypoint {
    private int x;
    private int y;
    private TileType tile;
    private Integer index;

    public Waypoint(int x, int y, TileType tile, Integer index) {
        this.x = x;
        this.y = y;
        this.tile = tile;
        this.index = index;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public TileType getTile() {
        return tile;
    }

    public Integer getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Waypoint waypoint = (Waypoint) o;

        if (getX() != waypoint.getX()) return false;
        return getY() == waypoint.getY();

    }

    @Override
    public int hashCode() {
        int result = getX();
        result = 31 * result + getY();
        return result;
    }

    @Override
    public String toString() {
        return "Waypoint{" +
                "x=" + x +
                ", y=" + y +
                ", tile=" + tile +
                ", index=" + index +
                '}';
    }
}
