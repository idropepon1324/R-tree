import java.util.*;

public class Splitter {

    /*
    * splitter: Split Algorithm for R* trees
    * 1- ChooseSplitAxes
    * 2- ChooseSplitIndex
    * 3- Distribute the 2 groups
    *
    * chooseSplitAxes: Select the best axis to do the split.
    * For Every axis sort first by the lower value of their rectangle and then their upper.
    * Then determine all the M-2m+2 distributions of the M+1 entries.
    * Calculate the S which is the sum value of all margin-values (Perimeter) of
    * the different distributions.
    * Choose the axis with the minimum S.
    *
    * ChooseSplitIndex: Determine the best distribution between two groups.
    *
    * bb: bounding box
    * margin-value: margin[bb(first group)] + margin[bb(second group)]
    * overlap-value: area[bb(first group) n bb(second group)]   Intersection
    * area-value: area[bb(first group)] + area[bb(second group)]
    *
     */
    public Splitter(){
        //
    }

    public static void main(String[] args) {
//        Splitter s = new Splitter();
//        List<NotLeafNode> list = new ArrayList<>();
//        list.add(new NotLeafNode(null,null,null));
        //s.split(list,3);

        HashMap<Double, String> ex = new HashMap<>();
        ex.put(70.2,"Rec1");
        ex.put(30.1,"Rec2");
        ex.put(59.9,"Rec3");
        ex.put(121.2,"Rec4");
        ex.put(11.3,"Rec5");
        TreeMap<Double, String> sorted = new TreeMap<>(ex);
        Collection<String> str = sorted.values();
        System.out.println(str);
        List<String> stringList = new ArrayList<>();
        for(String s:str){
            stringList.add(s);
        }

        System.out.println(stringList.subList(2,4));



    }

    public <T extends TreeNode, G extends HasGeometry> List<T> split(T node, G addUp, Context context){

        int axis;
        List<T> listPair = new ArrayList<>();
        List<? extends TreeNode> entries;
        List<Entry> pointsEntries;
        List<double[]> points = new ArrayList<>();
        List<G> objects = new ArrayList<>();


        if(node.getClass().equals(NotLeafNode.class)){  // Here we have T = NotLeafNode & G = Rectangle

            entries = ((NotLeafNode) node).entries();

            for(int i=0; i<entries.size(); i++){
                objects.add((G)entries.get(i).getRectangle());
            }
            objects.add(addUp);

            //
            axis = chooseSplitAxesRect((List<Rectangle>)objects, context);


        } else if (node.getClass().equals(LeafNode.class)){ // Here we have T = LeafNode & G = Entry

            pointsEntries = ((LeafNode) node).entries();
            for(int i=0; i<pointsEntries.size(); i++){
                points.add(pointsEntries.get(i).getPoint());
            }
            points.add(((Entry)addUp).getPoint());


        }

        return new ArrayList<>();
    }

    private int chooseSplitAxesRect(List<Rectangle> objects, Context context){
        int m = context.minChildren();
        int M = context.maxChildren();
        int MAX_DIMENSIONS = objects.get(0).getSize();

        double[] S = new double[MAX_DIMENSIONS];  // an S for each dimension
        for(int i=0; i<S.length;i++){   // Initialization of the Sums
            S[i] = 0;
        }

        // For each axis/dimension
        for (int i=0; i<MAX_DIMENSIONS; i++){

            // Creating a Hashmap with the lower point of the dimension, in order to sort
            HashMap<Double, Rectangle> lower = new HashMap<>();
            for (Rectangle r: objects){
                double[] lowerPoint = r.getVector1();
                lower.put(lowerPoint[i], r);
            }
            TreeMap<Double, Rectangle> sorted = new TreeMap<>(lower);
            List<Rectangle> sortedRect = new ArrayList<>(sorted.values());
            for(int k=0; k<(M-2*m+2); k++){
                S[i] += Utils.mbrRect(sortedRect.subList(0,m+k)).perimeter() + Utils.mbrRect(sortedRect.subList(m+k,M)).perimeter();
            }

            // Upper point Hashmap
            HashMap<Double, Rectangle> upper = new HashMap<>();
            for (Rectangle r: objects){
                double[] upperPoint = r.getVector2();
                upper.put(upperPoint[i], r);
            }
            sorted = new TreeMap<>(upper);
            sortedRect = new ArrayList<>(sorted.values());
            for(int k=0; k<(M-2*m+2); k++){
                S[i] += Utils.mbrRect(sortedRect.subList(0,m+k)).perimeter() + Utils.mbrRect(sortedRect.subList(m+k,M)).perimeter();
            }

        }

        // Find the axis with the Min Sum and return that axis
        return minPosition(S);
    }

    private int chooseSplitAxesPoint(List<double[]> objects, Context context){
        int m = context.minChildren();
        int M = context.maxChildren();
        int MAX_DIMENSIONS = objects.get(0).length;

        double[] S = new double[MAX_DIMENSIONS];  // an S for each dimension
        for(int i=0; i<S.length;i++){   // Initialization of the Sums
            S[i] = 0;
        }

        for (int i=0; i<MAX_DIMENSIONS; i++){

            // Creating a Hashmap with the lower point of the dimension, in order to sort
            HashMap<Double, double[]> pointsMap = new HashMap<>();
            for (double[] p: objects){
                pointsMap.put(p[i], p);
            }
            TreeMap<Double, double[]> sorted = new TreeMap<>(pointsMap);
            List<double[]> sortedPoints = new ArrayList<>(sorted.values());
            for(int k=0; k<(M-2*m+2); k++){
                S[i] += Utils.mbrPoints(sortedPoints.subList(0,m+k)).perimeter() + Utils.mbrPoints(sortedPoints.subList(m+k,M)).perimeter();
            }

        }

        // Find the axis with the Min Sum and return that axis
        return minPosition(S);
    }

    private List<List<Rectangle>> chooseSplitIndexRect(int axis, List<Rectangle> objects, Context context){
        int m = context.minChildren();
        int M = context.maxChildren();
        int MAX_DIMENSIONS = objects.get(0).getSize();
        double minOverlapArea, overlapArea, minArea, Area;

        List<List<Rectangle>> listPair = new ArrayList<>();

        // Creating a Hashmap with the lower point of the dimension, in order to sort
        HashMap<Double, Rectangle> rectangleMap = new HashMap<>();
        for (Rectangle r: objects){
            double[] lowerPoint = r.getVector1();
            rectangleMap.put(lowerPoint[axis], r);
        }
        TreeMap<Double, Rectangle> sorted = new TreeMap<>(rectangleMap);
        List<Rectangle> sortedRect = new ArrayList<>(sorted.values());

        // Taking the first as the min
        minOverlapArea = Utils.mbrRect(sortedRect.subList(0,m)).perimeter() + Utils.mbrRect(sortedRect.subList(m,M)).perimeter();
        listPair.add(sortedRect.subList(0,m));
        listPair.add(sortedRect.subList(m,M));

        for(int k=1; k<(M-2*m+2); k++){         // Continue for the rest distributions
            overlapArea = Utils.mbrRect(sortedRect.subList(0,m+k)).intersectionArea(Utils.mbrRect(sortedRect.subList(m+k,M)));
            if (minOverlapArea > overlapArea ){
                minOverlapArea = overlapArea;
                listPair.clear();
                listPair.add(sortedRect.subList(0,m+k));
                listPair.add(sortedRect.subList(m+k,M));

            } else if (minOverlapArea == overlapArea ) {    // a rare occasion
                minArea = Utils.mbrRect(listPair.get(0)).area() + Utils.mbrRect(listPair.get(1)).area();
                Area = Utils.mbrRect(sortedRect.subList(0,m)).area() + Utils.mbrRect(sortedRect.subList(m,M)).area();
                if (minArea > Area){
                    listPair.clear();
                    listPair.add(sortedRect.subList(0,m+k));
                    listPair.add(sortedRect.subList(m+k,M));
                }
            }
        }

        // Creating a Hashmap with the upper point of the rectangle, in order to sort
        rectangleMap = new HashMap<>();
        for (Rectangle r: objects){
            double[] lowerPoint = r.getVector2();
            rectangleMap.put(lowerPoint[axis], r);
        }
        sorted = new TreeMap<>(rectangleMap);
        sortedRect = new ArrayList<>(sorted.values());

        for (int k=0; k<(M-2*m+2); k++){
            overlapArea = Utils.mbrRect(sortedRect.subList(0,m+k)).intersectionArea(Utils.mbrRect(sortedRect.subList(m+k,M)));
            if (minOverlapArea > overlapArea ){
                minOverlapArea = overlapArea;
                listPair.clear();
                listPair.add(sortedRect.subList(0,m+k));
                listPair.add(sortedRect.subList(m+k,M));

            } else if (minOverlapArea == overlapArea ) {    // a rare occasion
                minArea = Utils.mbrRect(listPair.get(0)).area() + Utils.mbrRect(listPair.get(1)).area();
                Area = Utils.mbrRect(sortedRect.subList(0,m)).area() + Utils.mbrRect(sortedRect.subList(m,M)).area();
                if (minArea > Area){
                    listPair.clear();
                    listPair.add(sortedRect.subList(0,m+k));
                    listPair.add(sortedRect.subList(m+k,M));
                }
            }
        }

        return listPair;
    }

    private void chooseSplitIndexPoint(){

    }

    private int minPosition(double[] S){
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
}
