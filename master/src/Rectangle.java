import java.util.Arrays;

public final class Rectangle implements Geometry, HasGeometry{
    private double[] vector1;
    private double[] vector2;

    public Rectangle(){
        //
    }
    public double[] getVector1(){
        return vector1;
    }

    public double[] getVector2(){
        return vector2;
    }

    public int getSize(){
        return vector1.length;
    }

    public double area(){
        double area = 0;
        for(int i=0; i<getSize() - 1; i++){         // Every axis * every other one
            for(int j=i+1; j<getSize(); j++){
                area += i*j;
            }
        }
        return area;
    }

    @Override
    public double distance(Rectangle r){
        return 0;
    }

    @Override
    public Rectangle mbr(){
        return this;
    }

    @Override
    public boolean intersects(Rectangle r){
        double[] featVec1 = r.getVector1();
        double[] featVec2 = r.getVector2();
        for(int i=0; i<getSize(); i++){
            if(vector1[i] <= featVec2[i] && featVec1[i] <= vector2[i]){
                // Intersection in at least one dimensions means intersection
                return true;
            }
        }
        return false;
    }

    public double intersectionArea(Rectangle r){
        if (!intersects(r)) {
            return 0;
        }
        double area = 0;

        return area;
    }

    public double perimeter()
    {
        double perimeter = 0;
        perimeter = Arrays.stream(vector1).sum();       // The math equation used to find the perimeter
        perimeter *= Math.pow(2, getSize()-1 );         // 2d: *2, 3d: *4, 4d: *8...    (Edges)
        return perimeter;
    }

    //Rectangle add(Rectangle r);

    /**
     * Checks if a point is part of a rectangle.
     * @param point
     * @return
     */
    public boolean contains(double[] point){
        if (point.length != getSize()){
            return false;
        }
        for(int i=0; i<getSize(); i++){
            if(!(point[i]>=vector1[i] && point[i]<=vector2[i]) ){
                return false;
            }
        }
        return true;
    }

    //boolean isDoublePrecision();
}
