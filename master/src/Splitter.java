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

    @SuppressWarnings("unchecked") // Cast, <? extends TreeNode> to <LeafNode>/<NotLeafNode> : If conditions are in place
    public static void main(String[] args) {
        //TODO Finishing this part and the testing the split to see if it works in future
        //TODO Beginning of the report and the tidying up the warnings and the comments in the finished parts of the algorithm.

        double[] v = new double[2];
        double[] v2 = new double[2];
        v[0] = 1.2;
        v[1] = 2.3;
        v2[0] = 3.3;
        v2[1] = 5.5;
        List<Entry> entries = new ArrayList<>();
        Rectangle rect = new Rectangle(v.clone(), v2.clone());
        entries.add(new Entry(v.clone(), 1, 1));
        entries.add(new Entry(v2.clone(), 1, 2));
        List<LeafNode> leafNodes = new ArrayList<>();

        leafNodes.add(new LeafNode(entries, rect,new Context()));

        List<NotLeafNode> notLeafNodes = new ArrayList<>();
        notLeafNodes.add(new NotLeafNode(leafNodes, rect, new Context()));

        List<LeafNode> list =(List<LeafNode>) notLeafNodes.get(0).entries();
        System.out.println(list.get(0).getRectangle().getVector1()[0]);
        NotLeafNode node = new NotLeafNode(notLeafNodes, rect, new Context());

        List<? extends TreeNode> tmpList = node.entries();
        System.out.println(tmpList.get(0).getClass());
        List<NotLeafNode> notlist =(List<NotLeafNode>) node.entries();
        System.out.println(notlist.get(0).getRectangle().area());

        System.out.println(LeafNode.class.equals(notlist.get(0).getClass()));

        //List<NotLeafNode> notlist =(List<NotLeafNode>) node.entries();

        double[] S = new double[10];
        for (double d: S){
            System.out.println(d);
        }

        // Testing split... Sometime in the future
        Random r = new Random();
        List<Rectangle> rectangles = new ArrayList<>();
        double rangeMin = -180.0;
        double rangeMax = 180.0;
        double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();

    }


    /**
     * This method splits the node entries and addUp (TreeNodes) to two NotLeafNodes
     * @param node the Non Leaf node with the entries
     * @param addUp the extra entry
     * @param context the context of the node
     * @param <G> the class of the Entries (LeafNode/NotLeafNode)
     * @return a List of two NotLeafNodes
     */
    @SuppressWarnings("unchecked") // Cast, <? extends TreeNode> to <LeafNode>/<NotLeafNode> : If conditions are in place
    public <G extends TreeNode> List<NotLeafNode> split(NotLeafNode node, G addUp, Context context){
        int axis;
        List<NotLeafNode> splitNodes = new ArrayList<>();       // 2 in number
        List<Rectangle> rectangles = new ArrayList<>();

        // Just for mapping the results with the nodes later


        // Taking the different cases (NotLeaf and leaf node)
        if (NotLeafNode.class.equals(addUp.getClass())){
            HashMap<Rectangle, NotLeafNode> map = new HashMap<>();
            List<NotLeafNode> list1 = new ArrayList<>();
            List<NotLeafNode> list2 = new ArrayList<>();
            List<NotLeafNode> objects = (List<NotLeafNode>) node.entries();
            objects.add((NotLeafNode)addUp);

            for(NotLeafNode obj: objects){
                rectangles.add(obj.getRectangle());
                map.put(obj.getRectangle(), obj);
            }

            //
            axis = chooseSplitAxesRect(rectangles, context);
            List<List<Rectangle>> listPair = chooseSplitIndexRect(axis, rectangles, context);


            for (int i=0; i<listPair.get(0).size(); i++) {
                list1.add(map.get(listPair.get(0).get(i) ));
            }
            for (int i=0; i<listPair.get(1).size(); i++) {
                list2.add(map.get(listPair.get(1).get(i) ));
            }

            splitNodes.add(new NotLeafNode(list1, Utils.mbrRect(listPair.get(0)), context));
            splitNodes.add(new NotLeafNode(list2, Utils.mbrRect(listPair.get(1)), context));

        } else if (LeafNode.class.equals(addUp.getClass())){
            HashMap<Rectangle, LeafNode> map = new HashMap<>();
            List<LeafNode> list1 = new ArrayList<>();
            List<LeafNode> list2 = new ArrayList<>();
            List<LeafNode> objects = (List<LeafNode>) node.entries();
            objects.add((LeafNode)addUp);

            for(LeafNode obj: objects){
                rectangles.add(obj.getRectangle());
                map.put(obj.getRectangle(), obj);
            }

            // Find the axis and the best group split
            axis = chooseSplitAxesRect(rectangles, context);
            List<List<Rectangle>> listPair = chooseSplitIndexRect(axis, rectangles, context);


            for (int i=0; i<listPair.get(0).size(); i++) {
                list1.add(map.get(listPair.get(0).get(i) ));
            }
            for (int i=0; i<listPair.get(1).size(); i++) {
                list2.add(map.get(listPair.get(1).get(i) ));
            }

            splitNodes.add(new NotLeafNode(list1, Utils.mbrRect(listPair.get(0)), context));
            splitNodes.add(new NotLeafNode(list2, Utils.mbrRect(listPair.get(1)), context));


        } else {    // just in case :P
            System.out.println("Miscalculation in the split of NotLeafNode.");
        }

        return splitNodes;
    }

    /**
     * This method splits the node entries and addUp (Entries) to two LeafNodes
     * @param node the Leaf node with the entries
     * @param addUp the extra entry
     * @param context the context of the node
     * @return a List of two LeafNodes
     */
    public List<LeafNode> split(LeafNode node, Entry addUp, Context context){
        int axis;
        List<LeafNode> splitNodes = new ArrayList<>();       // 2 in number

        List<Entry> list1 = new ArrayList<>();
        List<Entry> list2 = new ArrayList<>();
        List<Entry> objects = node.entries();
        objects.add(addUp);


        // Two algorithms
        axis = chooseSplitAxesPoint(objects, context);
        List<List<Entry>> listPair = chooseSplitIndexPoint(axis, objects, context);

        splitNodes.add(new LeafNode(list1, Utils.mbrPoints(listPair.get(0)), context));
        splitNodes.add(new LeafNode(list2, Utils.mbrPoints(listPair.get(1)), context));

        return splitNodes;
    }


//    public <T extends TreeNode, G extends HasGeometry> List<T> split(T node, G addUp, Context context){
//
//        int axis;
//        //List<T> listPair = new ArrayList<>();
//        List<? extends TreeNode> entries;
//        List<Entry> pointsEntries;
//        List<double[]> points = new ArrayList<>();
//        List<G> objects = new ArrayList<>();
//        List<T> splitNodes = new ArrayList<>();
//
//
//        if(node.getClass().equals(NotLeafNode.class)){  // Here we have T = NotLeafNode & G = Rectangle
//
//            entries = ((NotLeafNode) node).entries();
//
//            for(int i=0; i<entries.size(); i++){
//                objects.add((G)entries.get(i).getRectangle());
//            }
//            objects.add(addUp);
//
//            //
//            axis = chooseSplitAxesRect((List<Rectangle>)objects, context);
//            List<List<Rectangle>> listPair = chooseSplitIndexRect(axis, (List<Rectangle>)objects, context);
//
//            //splitNodes.add(new NotLeafNode(listPair.get(0), Utils.mbrRect(listPair.get(0)), context ));
//
//        } else if (node.getClass().equals(LeafNode.class)){ // Here we have T = LeafNode & G = Entry
//
//            pointsEntries = ((LeafNode) node).entries();
//            for(int i=0; i<pointsEntries.size(); i++){
//                points.add(pointsEntries.get(i).getPoint());
//            }
//            points.add(((Entry)addUp).getPoint());
//
//
//        }
//
//        return new ArrayList<>();
//    }

    private int chooseSplitAxesRect(List<Rectangle> objects, Context context){
        int m = context.minChildren();
        int M = context.maxChildren();
        int MAX_DIMENSIONS = objects.get(0).getSize();

        // Initialization of the Sums to 0.0
        double[] S = new double[MAX_DIMENSIONS];  // an S for each dimension

        // For each axis/dimension
        for (int i=0; i<MAX_DIMENSIONS; i++){

            // Creating a Hashmap with the lower point of the rectangle, in order to sort
        //            HashMap<Double, Rectangle> lower = new HashMap<>();
        //            for (Rectangle r: objects){
        //                double[] lowerPoint = r.getVector1();
        //                lower.put(lowerPoint[i], r);
        //            }
        //            TreeMap<Double, Rectangle> sorted = new TreeMap<>(lower);
        //            //List<Rectangle> sortedRect = new ArrayList<>(sorted.values());
            List<Rectangle> sortedRect = sortRectanglesLower(objects, i);
            for(int k=0; k<(M-2*m+2); k++){
                S[i] += Utils.mbrRect(sortedRect.subList(0,m+k)).perimeter() + Utils.mbrRect(sortedRect.subList(m+k,M)).perimeter();
            }

            // Upper point Hashmap
        //            HashMap<Double, Rectangle> upper = new HashMap<>();
        //            for (Rectangle r: objects){
        //                double[] upperPoint = r.getVector2();
        //                upper.put(upperPoint[i], r);
        //            }
        //            sorted = new TreeMap<>(upper);
        //            sortedRect = new ArrayList<>(sorted.values());
            sortedRect = sortRectanglesUpper(objects, i);
            for(int k=0; k<(M-2*m+2); k++){
                S[i] += Utils.mbrRect(sortedRect.subList(0,m+k)).perimeter() + Utils.mbrRect(sortedRect.subList(m+k,M)).perimeter();
            }

        }

        // Find the axis with the Min Sum and return that axis
        return Utils.minPosition(S);
    }

    private int chooseSplitAxesPoint(List<Entry> objects, Context context){
        int m = context.minChildren();
        int M = context.maxChildren();
        int MAX_DIMENSIONS = objects.get(0).getVector().length;

        // Initialization of the Sums to 0.0
        double[] S = new double[MAX_DIMENSIONS];  // an S for each dimension

        for (int i=0; i<MAX_DIMENSIONS; i++){

            // Creating a Hashmap with the lower point of the rectangle, in order to sort
            HashMap<Double, Entry> pointsMap = new HashMap<>();
            for (Entry e: objects){
                pointsMap.put(e.getVector()[i], e);
            }
            TreeMap<Double, Entry> sorted = new TreeMap<>(pointsMap);
            List<Entry> sortedPoints = new ArrayList<>(sorted.values());
            for(int k=0; k<(M-2*m+2); k++){
                S[i] += Utils.mbrPoints(sortedPoints.subList(0,m+k)).perimeter() + Utils.mbrPoints(sortedPoints.subList(m+k,M)).perimeter();
            }

        }

        // Find the axis with the Min Sum and return that axis
        return Utils.minPosition(S);
    }

    private List<List<Rectangle>> chooseSplitIndexRect(int axis, List<Rectangle> objects, Context context){
        int m = context.minChildren();
        int M = context.maxChildren();
        double minOverlapArea, overlapArea, minArea, Area;

        List<List<Rectangle>> listPair = new ArrayList<>();

        // Creating a Hashmap with the lower point of the rectangle, in order to sort
    //        HashMap<Double, Rectangle> rectangleMap = new HashMap<>();
    //        for (Rectangle r: objects){
    //            double[] lowerPoint = r.getVector1();
    //            rectangleMap.put(lowerPoint[axis], r);
    //        }
    //        TreeMap<Double, Rectangle> sorted = new TreeMap<>(rectangleMap);
    //        List<Rectangle> sortedRect = new ArrayList<>(sorted.values());
        List<Rectangle> sortedRect = sortRectanglesLower(objects, axis);
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
    //        rectangleMap = new HashMap<>();
    //        for (Rectangle r: objects){
    //            double[] lowerPoint = r.getVector2();
    //            rectangleMap.put(lowerPoint[axis], r);
    //        }
    //        sorted = new TreeMap<>(rectangleMap);
    //        sortedRect = new ArrayList<>(sorted.values());
        sortedRect = sortRectanglesUpper(objects, axis);
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

    // Don't know if it is correct... it was late night when I wrote this
    private List<List<Entry>> chooseSplitIndexPoint(int axis, List<Entry> objects, Context context){
        int m = context.minChildren();
        int M = context.maxChildren();
        double minOverlapArea, overlapArea, minArea, Area;

        List<List<Entry>> listPair = new ArrayList<>();

        HashMap<Double, Entry> entryMap = new HashMap<>();
        for (Entry p: objects){
            double[] point = p.getVector();
            entryMap.put(point[axis], p);
        }
        TreeMap<Double, Entry> sorted = new TreeMap<>(entryMap);
        List<Entry> sortedPoints = new ArrayList<>(sorted.values());

        // Taking the first as the min
        minOverlapArea = Utils.mbrPoints(sortedPoints.subList(0,m)).perimeter() + Utils.mbrPoints(sortedPoints.subList(m,M)).perimeter();
        listPair.add(sortedPoints.subList(0,m));
        listPair.add(sortedPoints.subList(m,M));

        for(int k=1; k<(M-2*m+2); k++){         // Continue for the rest distributions
            overlapArea = Utils.mbrPoints(sortedPoints.subList(0,m+k)).intersectionArea(Utils.mbrPoints(sortedPoints.subList(m+k,M)));
            if (minOverlapArea > overlapArea ){
                minOverlapArea = overlapArea;
                listPair.clear();
                listPair.add(sortedPoints.subList(0,m+k));
                listPair.add(sortedPoints.subList(m+k,M));

            } else if (minOverlapArea == overlapArea ) {    // a rare occasion
                minArea = Utils.mbrPoints(listPair.get(0)).area() + Utils.mbrPoints(listPair.get(1)).area();
                Area = Utils.mbrPoints(sortedPoints.subList(0,m)).area() + Utils.mbrPoints(sortedPoints.subList(m,M)).area();
                if (minArea > Area){
                    listPair.clear();
                    listPair.add(sortedPoints.subList(0,m+k));
                    listPair.add(sortedPoints.subList(m+k,M));
                }
            }
        }

        return listPair;
    }

    private List<Rectangle> sortRectanglesLower (List<Rectangle> objects, int axis){
        // Creating a Hashmap with the lower point of the rectangle, in order to sort
        HashMap<Double, Rectangle> rectangleMap = new HashMap<>();
        for (Rectangle r: objects){
            double[] lowerPoint = r.getVector1();
            rectangleMap.put(lowerPoint[axis], r);
        }
        TreeMap<Double, Rectangle> sorted = new TreeMap<>(rectangleMap);
        return new ArrayList<>(sorted.values());
    }

    private List<Rectangle> sortRectanglesUpper (List<Rectangle> objects, int axis){
        // Creating a Hashmap with the upper point of the rectangle, in order to sort
        HashMap<Double, Rectangle> rectangleMap = new HashMap<>();
        for (Rectangle r: objects){
            double[] lowerPoint = r.getVector2();
            rectangleMap.put(lowerPoint[axis], r);
        }
        TreeMap<Double, Rectangle> sorted = new TreeMap<>(rectangleMap);
        return new ArrayList<>(sorted.values());
    }
}
