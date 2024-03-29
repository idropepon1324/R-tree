import java.util.ArrayList;
import java.util.List;


/**
 * This class contains useful methods.
 * Methods: mbr(), mbrRect(), mbrPoints().
 */
public class Utils {

    /*
     * Testing main for the Util functions.
     * mbrRect() and mbrPoints() work successfully. Maybe use try() to catch exceptions in the future
     * mbr works but had/have small problems with nulls.
     */

    private Utils(){
        // Prevent initializations
    }

    /**
     * This function takes Tree nodes and returns the mbr that
     * their children create.
     * @param node a TreeNode
     * @param <T> NotLeafNode or LeafNode
     * @return Minimum bounding rectangle
     */
    public static <T extends TreeNode> Rectangle mbr(T node){
        try{
            List<Rectangle> rectangles = new ArrayList<>();
            for (int i=0; i<node.childrenSize(); i++){
                if (node instanceof LeafNode){
                    rectangles.add(node.entryChild(i).getRectangle());
                } else if (node instanceof NotLeafNode){
                    rectangles.add(node.child(i).getRectangle());
                }

            }
            return mbrRect(rectangles);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Location: Utils.mbr()");
        }
        return new Rectangle();


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
     * @param objects A list of Entry Points
     * @return Minimum bounding rectangle
     */
    public static Rectangle mbrPoints(List<Entry> objects){
        List<double []> points = new ArrayList<>();

        for(Entry e: objects){
            points.add(e.getVector());
        }

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

    /**
     * Return the Position of the list in which the double[] list
     * has its minimum element.
     * @param S a double[]
     * @return the Position of the minimum.
     */
    public static int minPosition(double[] S){
        int dim = 0;
        double min = S[0];
        for (int i=1; i<S.length; i++){
            if (S[i] < min){
                min = S[i];
                dim = i;
            }
        }

        return dim;
    }

    /**
     * Return the distance between the centers of two Rectangles.
     * @param rect1 Rectangle one
     * @param rect2 Rectangle two
     * @return (double) distance
     */
    public static double distanceRect(Rectangle rect1, Rectangle rect2){
        double sum = 0;
        double[] tmp1 = rect1.getVector1();
        double[] tmp2 = rect1.getVector2();
        double[] center1 = new double[tmp1.length];
        double[] center2 = new double[tmp1.length];
        for (int i=0; i<center1.length; i++){
            center1[i] = (tmp1[i] + tmp2[i]) / 2;
        }

        tmp1 = rect2.getVector1();
        tmp2 = rect2.getVector2();
        for (int i=0; i<center2.length; i++){
            center2[i] = (tmp1[i] + tmp2[i]) / 2;
        }

        for (int i=0; i<center1.length; i++){
            sum += Math.pow(center1[i] - center2[i], 2);
        }

        return Math.sqrt(sum);
    }
}
