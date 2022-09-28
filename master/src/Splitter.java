import java.io.Serializable;
import java.util.*;


public class Splitter implements Serializable {

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

//    @SuppressWarnings("unchecked") // Cast, <? extends TreeNode> to <LeafNode>/<NotLeafNode> : If conditions are in place
//    public static void main(String[] args) {
//
//
//        Random r = new Random();
//        List<Rectangle> rectangles = new ArrayList<>();
//        double rangeMin = -180.0;
//        double rangeMax = 180.0;
//        double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
//        double[] vec1 = new double[2];
//        double[] vec2 = new double[2];
//        vec1[0] = 1.1;
//        vec1[1] = 1.2;
//        vec2[0] = 2.2;
//        vec2[1] = 2.3;
//        rectangles.add(new Rectangle(vec1.clone(), vec2.clone()));
//        vec1[0] = 5.1;
//        vec1[1] = 5.2;
//        vec2[0] = 6.2;
//        vec2[1] = 6.3;
//        rectangles.add(new Rectangle(vec1.clone(), vec2.clone()));
//        vec1[0] = 3.1;
//        vec1[1] = 3.2;
//        vec2[0] = 4.2;
//        vec2[1] = 4.3;
//        rectangles.add(new Rectangle(vec1.clone(), vec2.clone()));
//
//
//
//
//        List<Double> listOne = new ArrayList<>();
//        for (Rectangle rec: rectangles){
//            double[] lowerPoint = rec.getVector2();
//            listOne.add(lowerPoint[0]);
//        }
//        int n = listOne.size();
//
//        Integer[] indices = new Integer[n];
//        for (int i = 0; i < n; ++i) {
//            indices[i] = i;
//        }
//
//        Arrays.sort(
//                indices,
//                new Comparator<Integer>() {
//                    public int compare(Integer a, Integer b) {
//                        return listOne.get(a).compareTo(listOne.get(b));
//                    }
//                });
//        System.out.println("ListOne  and rect before reorder:");
//        System.out.println(listOne);
//        System.out.println(rectangles);
//
//        reorder(indices, listOne);
//        reorder(indices, rectangles);
//
//
//        System.out.println("ListOne  and rect after reorder:");
//        System.out.println(listOne);
//        System.out.println(rectangles);
//
//    }


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
        if (addUp instanceof NotLeafNode){
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

        } else if (addUp instanceof LeafNode){
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

        // Set the new Parents
        for (TreeNode treeNode: splitNodes){
            for(int i=0;i<treeNode.childrenSize();i++){
                treeNode.child(i).setParent(treeNode);
            }
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

        List<Entry> objects = node.entries();
        objects.add(addUp);


        // Two algorithms
        axis = chooseSplitAxesPoint(objects, context);
        List<List<Entry>> listPair = chooseSplitIndexPoint(axis, objects, context);

        splitNodes.add(new LeafNode(listPair.get(0), Utils.mbrPoints(listPair.get(0)), context));
        splitNodes.add(new LeafNode(listPair.get(1), Utils.mbrPoints(listPair.get(1)), context));
        // No need for parents because they are entries

        return splitNodes;
    }

    private int chooseSplitAxesRect(List<Rectangle> objects, Context context){
        int m = context.minChildren();
        int M = context.maxChildren();
        int MAX_DIMENSIONS = objects.get(0).getSize();

        // Initialization of the Sums to 0.0
        double[] S = new double[MAX_DIMENSIONS];  // an S for each dimension

        // For each axis/dimension
        for (int i=0; i<MAX_DIMENSIONS; i++){

            // Creating a Hashmap with the lower point of the rectangle, in order to sort
            List<Rectangle> sortedRect = sortRectanglesLower(objects, i);

            for(int k=0; k<(M-2*m+2); k++){ //Serializable problem
                S[i] += Utils.mbrRect(new ArrayList<>(sortedRect.subList(0,m+k))).perimeter() +
                        Utils.mbrRect(new ArrayList<>(sortedRect.subList(m+k,M+1))).perimeter();
            }

            // Upper point Hashmap
            sortedRect = new ArrayList<>(sortRectanglesUpper(objects, i));
            for(int k=0; k<(M-2*m+2); k++){
                S[i] += Utils.mbrRect(new ArrayList<>(sortedRect.subList(0,m+k))).perimeter() +
                        Utils.mbrRect(new ArrayList<>(sortedRect.subList(m+k,M+1))).perimeter();
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
                S[i] += Utils.mbrPoints(new ArrayList<>(sortedPoints.subList(0,m+k))).perimeter() +
                        Utils.mbrPoints(new ArrayList<>(sortedPoints.subList(m+k,M+1))).perimeter();
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
        List<Rectangle> sortedRect = sortRectanglesLower(objects, axis);

        // Taking the first as the min
        minOverlapArea = Utils.mbrRect(new ArrayList<>(sortedRect.subList(0,m))).perimeter() +
                        Utils.mbrRect(new ArrayList<>(sortedRect.subList(m,M+1))).perimeter();
        listPair.add(new ArrayList<>(sortedRect.subList(0,m)));
        listPair.add(new ArrayList<>(sortedRect.subList(m,M+1)));

        for(int k=1; k<(M-2*m+2); k++){         // Continue for the rest distributions
            overlapArea = Utils.mbrRect(new ArrayList<>(sortedRect.subList(0,m+k))).
                    intersectionArea(Utils.mbrRect(new ArrayList<>(sortedRect.subList(m+k,M+1))));
            if (minOverlapArea > overlapArea ){
                minOverlapArea = overlapArea;
                listPair.clear();
                listPair.add(new ArrayList<>(sortedRect.subList(0,m+k)));
                listPair.add(new ArrayList<>(sortedRect.subList(m+k,M+1)));

            } else if (minOverlapArea == overlapArea ) {    // a rare occasion
                minArea = Utils.mbrRect(listPair.get(0)).area() + Utils.mbrRect(listPair.get(1)).area();
                Area = Utils.mbrRect(new ArrayList<>(sortedRect.subList(0,m))).area() +
                        Utils.mbrRect(new ArrayList<>(sortedRect.subList(m,M+1))).area();
                if (minArea > Area){
                    listPair.clear();
                    listPair.add(new ArrayList<>(sortedRect.subList(0,m+k)));
                    listPair.add(new ArrayList<>(sortedRect.subList(m+k,M+1)));
                }
            }
        }

        // Creating a Hashmap with the upper point of the rectangle, in order to sort
        sortedRect = sortRectanglesUpper(objects, axis);

        for (int k=0; k<(M-2*m+2); k++){
            overlapArea = Utils.mbrRect(new ArrayList<>(sortedRect.subList(0,m+k))).
                    intersectionArea(Utils.mbrRect(new ArrayList<>(sortedRect.subList(m+k,M+1))));
            if (minOverlapArea > overlapArea ){
                minOverlapArea = overlapArea;
                listPair.clear();
                listPair.add(new ArrayList<>(sortedRect.subList(0,m+k)));
                listPair.add(new ArrayList<>(sortedRect.subList(m+k,M+1)));

            } else if (minOverlapArea == overlapArea ) {    // a rare occasion
                minArea = Utils.mbrRect(listPair.get(0)).area() + Utils.mbrRect(listPair.get(1)).area();
                Area = Utils.mbrRect(new ArrayList<>(sortedRect.subList(0,m))).area() +
                        Utils.mbrRect(new ArrayList<>(sortedRect.subList(m,M+1))).area();
                if (minArea > Area){
                    listPair.clear();
                    listPair.add(new ArrayList<>(sortedRect.subList(0,m+k)));
                    listPair.add(new ArrayList<>(sortedRect.subList(m+k,M+1)));
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
        minOverlapArea = Utils.mbrPoints(new ArrayList<>(sortedPoints.subList(0,m))).perimeter() +
                Utils.mbrPoints(new ArrayList<>(sortedPoints.subList(m,M+1))).perimeter();
        listPair.add(new ArrayList<>(sortedPoints.subList(0,m)));
        listPair.add(new ArrayList<>(sortedPoints.subList(m,M+1)));

        for(int k=1; k<(M-2*m+2); k++){         // Continue for the rest distributions
            overlapArea = Utils.mbrPoints(new ArrayList<>(sortedPoints.subList(0,m+k))).
                    intersectionArea(Utils.mbrPoints(new ArrayList<>(sortedPoints.subList(m+k,M+1))));
            if (minOverlapArea > overlapArea ){
                minOverlapArea = overlapArea;
                listPair.clear();
                listPair.add(new ArrayList<>(sortedPoints.subList(0,m+k)));
                listPair.add(new ArrayList<>(sortedPoints.subList(m+k,M+1)));

            } else if (minOverlapArea == overlapArea ) {    // a rare occasion
                minArea = Utils.mbrPoints(listPair.get(0)).area() + Utils.mbrPoints(listPair.get(1)).area();
                Area = Utils.mbrPoints(new ArrayList<>(sortedPoints.subList(0,m))).area() +
                        Utils.mbrPoints(new ArrayList<>(sortedPoints.subList(m,M+1))).area();
                if (minArea > Area){
                    listPair.clear();
                    listPair.add(new ArrayList<>(sortedPoints.subList(0,m+k)));
                    listPair.add(new ArrayList<>(sortedPoints.subList(m+k,M+1)));
                }
            }
        }

        return listPair;
    }

//    private List<Rectangle> sortRectanglesLower (List<Rectangle> objects, int axis){
//        // Creating a Hashmap with the lower point of the rectangle, in order to sort
//        HashMap<Double, Rectangle> rectangleMap = new HashMap<>();
//        for (Rectangle r: objects){
//            double[] lowerPoint = r.getVector1();
//            rectangleMap.put(lowerPoint[axis], r);
//        }
//        TreeMap<Double, Rectangle> sorted = new TreeMap<>(rectangleMap);
//
//        if (objects.size() != sorted.size()){
//            System.out.println("Error in object != sorted size in Splitter.sortRectanglesLower()");
//            System.out.println("Objects:");
//            for(Rectangle r: objects){
//                r.print();
//            }
//            System.out.println("Sorted:");
//            for(Rectangle r: sorted.values()){
//                r.print();
//            }
//        }
//
//
//        return new ArrayList<>(sorted.values());
//    }

    private List<Rectangle> sortRectanglesLower (List<Rectangle> objects, int axis){
        List<Double> listOne = new ArrayList<>();
        for (Rectangle r: objects){
            double[] lowerPoint = r.getVector1();
            listOne.add(lowerPoint[axis]);
        }

        return sorted(objects, listOne);
    }

    private List<Rectangle> sortRectanglesUpper (List<Rectangle> objects, int axis){
        List<Double> listOne = new ArrayList<>();
        for (Rectangle r: objects){
            double[] lowerPoint = r.getVector2();
            listOne.add(lowerPoint[axis]);
        }

        return sorted(objects, listOne);
    }

    private static <T> void reorder(Integer[] indices, List<T> mutatedInPlace) {
        List<T> tempSpace = new ArrayList<>(indices.length);
        for (int index : indices) {
            tempSpace.add(mutatedInPlace.get(index));
        }
        mutatedInPlace.clear();
        mutatedInPlace.addAll(tempSpace);
    }

    // Sort the ListOne of double in Ascending and the the objects accordingly
    // Same point of reference
    private static List<Rectangle> sorted(List<Rectangle> objects, List<Double> listOne){
        int n = listOne.size();

        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; ++i) {
            indices[i] = i;
        }

        Arrays.sort(
                indices,
                Comparator.comparing(listOne::get));

        reorder(indices, listOne);
        reorder(indices, objects);

        return new ArrayList<>(objects);
    }
}
