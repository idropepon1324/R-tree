import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Rectangle implements Geometry, HasGeometry{
    private double[] vector1;
    private double[] vector2;

    /*
     * Testing things about this class
     *
     */
    public static void main(String[] args) {

    }

    public Rectangle(){
        //
    }

    public Rectangle(double[] v1, double[] v2){
        this.vector1 = v1;
        this.vector2 = v2;
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

    /*
     * This Function does not operate right for 3 and more dimensions
     * It just takes the multiplication sum of all the possible combinations.
     * For example in 3D it takes half the value of the true area.
     * In 4 and more we can not visualize and so the loss can not be calculated.(maybe)
     */
    public double area(){
        double area = 0;
        for(int i=0; i<getSize() - 1; i++){         // Every axis * every other one
            for(int j=i+1; j<getSize(); j++){
                area += (vector2[i] - vector1[i]) * (vector2[j] - vector1[j]);
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

        List<Double> points;
        double[] vec1 = new double[getSize()];
        double[] vec2 = new double[getSize()];
        // For each axis   // 4 points on each axis... sort... take 2 in the middle cause intersect = true
        for (int i=0; i<getSize(); i++){
            points = new ArrayList<>();
            points.add(r.getVector1()[i]);
            points.add(r.getVector2()[i]);
            points.add(vector1[i]);
            points.add(vector2[i]);
            Collections.sort(points);
            vec1[i] = points.get(1);
            vec2[i] = points.get(2);
        }

        return new Rectangle(vec1,vec2).area();
    }

    public double perimeter()
    {
        double perimeter = Arrays.stream(vector1).sum();       // The math equation used to find the perimeter
        perimeter *= Math.pow(2, getSize()-1 );         // 2d: *2, 3d: *4, 4d: *8...    (Edges)
        return perimeter;
    }

    //Rectangle add(Rectangle r);

    /**
     * Checks if a point is part of a rectangle.
     * @param point A point vector
     * @return boolean: true when it is in rectangle boundaries
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

    public void print(){
        System.out.print("Lower Vector: ");
        for(double d: vector1){
            System.out.print(d+ " ");
        }
        System.out.println();
        System.out.print("Upper Vector: ");
        for(double d: vector2){
            System.out.print(d+ " ");
        }
        System.out.println();
    }

    //boolean isDoublePrecision();
}
