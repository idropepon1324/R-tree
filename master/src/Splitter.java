import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Splitter s = new Splitter();
        List<NotLeafNode> list = new ArrayList<>();
        list.add(new NotLeafNode(null,null,null));
        //s.split(list,3);
    }

    public <T extends TreeNode, G extends HasGeometry> List<T> split(T node, G addUp, Context context){

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
            chooseSplitAxesRect((List<Rectangle>)objects, context);

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
        int dim = 0;
        int m = context.minChildren();
        int M = context.maxChildren();

        // For each axis/dimension
        for (int i=0; i<objects.size(); i++){

        }


        return dim;
    }

    private int chooseSplitAxesPoint(){
        int dim = 0;



        return dim;
    }

    private void chooseSplitIndex(){

    }
}
