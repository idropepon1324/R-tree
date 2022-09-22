import java.util.ArrayList;
import java.util.List;


/**
 * This class contains useful methods.
 * Methods: mbrRect(), mbrPoints().
 */
public class Utils {

    /*
     * Testing main for the Util functions.
     * mbrRect() and mbrPoints() work successfully. Maybe use try() to catch exceptions in the future
     */
    public static void main(String[] args) {
        List<Rectangle> rectangles = new ArrayList<>();
        List<double[]> points = new ArrayList<>();
        double[] tmp1 = new double[2];
        double[] tmp2 = new double[2];
        tmp1[0] = 2.2;
        tmp1[1] = 4.4;
        tmp2[0] = 3.5;
        tmp2[1] = 7.6;
        Rectangle r1 = new Rectangle(tmp1.clone(), tmp2.clone());
        rectangles.add(r1);
        points.add(tmp1.clone());
        points.add(tmp2.clone());

        tmp1[0] = 5.1;
        tmp1[1] = 2.1;
        tmp2[0] = 8.3;
        tmp2[1] = 4.9;
        Rectangle r2 = new Rectangle(tmp1.clone(), tmp2.clone());
        rectangles.add(r2);

        tmp1[0] = 11.7;
        tmp1[1] = -8.9;
        tmp2[0] = 15.1;
        tmp2[1] = 89.8;
        Rectangle r3 = new Rectangle(tmp1.clone(), tmp2.clone());
        rectangles.add(r3);
        points.add(tmp1.clone());
        points.add(tmp2.clone());

        Rectangle rnew = mbrRect(rectangles);
        Rectangle rnew2 = mbrPoints(points);

        rnew2.print();
    }

    private Utils(){
        // Prevent initializations
    }

    /**
     * This function takes rectangle objects and returns
     * the minimum bounding rectangle that includes all
     * the rectangles given.
     * @param rectangles A list of Rectangles
     * @return Minimum bounding rectangle
     */
    public static Rectangle mbrRect(List<Rectangle> rectangles){
        int MAX_DIMENSIONS = rectangles.get(0).getSize();
        Rectangle mbr;
        double[] vec1 = new double[MAX_DIMENSIONS];
        double[] vec2 = new double[MAX_DIMENSIONS];
        double min,max;

        for(int i=0; i<MAX_DIMENSIONS; i++){
            min = rectangles.get(0).getVector1()[i];
            max = rectangles.get(0).getVector2()[i];
            for(int k=1; k<rectangles.size(); k++){
                if (rectangles.get(k).getVector1()[i] < min){
                    min = rectangles.get(k).getVector1()[i];
                }
                if (rectangles.get(k).getVector2()[i] > max){
                    max = rectangles.get(k).getVector2()[i];
                }
            }
            vec1[i] = min;
            vec2[i] = max;
        }

        mbr = new Rectangle(vec1, vec2);

        return mbr;
    }

    /**
     * This function takes points and returns the
     * minimum bounding rectangle that includes all
     * the points given.
     * @param points A list of points
     * @return Minimum bounding rectangle
     */
    public static Rectangle mbrPoints(List<double[]> points){
        int MAX_DIMENSIONS = points.get(0).length;
        Rectangle mbr;
        double[] vec1 = new double[MAX_DIMENSIONS];
        double[] vec2 = new double[MAX_DIMENSIONS];
        double min,max;

        for(int i=0; i<MAX_DIMENSIONS; i++){
            min = points.get(0)[i];
            max = points.get(0)[i];
            for(int k=1; k<points.size(); k++){
                if (points.get(k)[i] < min){
                    min = points.get(k)[i];
                }
                if (points.get(k)[i] > max){
                    max = points.get(k)[i];
                }
            }
            vec1[i] = min;
            vec2[i] = max;
        }

        mbr = new Rectangle(vec1, vec2);

        return mbr;
    }
}
