public interface Geometry {

    double distance(Rectangle r);

    /**
     * Returns the minimum bounding rectangle of this geometry.
     *
     * @return minimum bounding rectangle
     */
    Rectangle mbr();

    boolean intersects(Rectangle r);

    boolean isDoublePrecision();
}
