public interface Rectangle extends Geometry{
    double vector1();

    double vector2();


    double area();

    double intersectionArea(Rectangle r);

    double perimeter();

    Rectangle add(Rectangle r);

    boolean contains(double x, double y);

    boolean isDoublePrecision();
}
