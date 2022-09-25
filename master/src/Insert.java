import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Insert {
    RTree rtree;
    /*
     * Inserting 2 types of data Entries/Points and TreeNodes/Rectangles.
     *
     * Inserting new data to the R*tree algorithm:
     * insertData starts insert with the level leaf level as a parameter.
     *
     *
     * OverflowTreatment: Reinsert or split, the latter
     * only if it is the root or this is the second call in this level
     * of overflowTreatment.
     */

    public static void main(String[] args) {
        List<LeafNode> leafNodes = new ArrayList<>();
        double[] vec = new double[2];
        vec[0] = 1.1;
        vec[1] = 2.2;
        TreeNode treeNode = new NotLeafNode(leafNodes, new Rectangle(vec, vec), new Context());
        NotLeafNode node = new NotLeafNode(leafNodes, new Rectangle(vec, vec), new Context());

        System.out.println(treeNode.getClass());
    }

    public Insert(){
        //
    }

    public Insert(RTree rtree){
        this.rtree = rtree;
    }

    public void insertData(RTree newRtree, int leafLevel, Entry newData){
        rtree = newRtree;
        insert(leafLevel, newData);
    }

    private <T extends HasGeometry> void insert(int level, T newData){
        TreeNode node = chooseSubtree(level, newData);

        if (node.childrenSize() < rtree.getContext().maxChildren()){
            node.add(newData);
        }

        if (newData instanceof Entry){
            Entry data = (Entry) newData;
            chooseSubtree(level, newData);

        } else if (newData instanceof LeafNode){

        } else if (newData instanceof NotLeafNode){

        }
        // Fix all the rectangles mbr of the path
    }

    public <T extends HasGeometry> TreeNode chooseSubtree(int level, T newData){
        int depth = 0;
        Optional<? extends TreeNode> root = rtree.getRoot();
        TreeNode node;
        if (root.isPresent()){
            node = root.get();
        } else {
            return null;
        }

        while (true) {
            if (node instanceof LeafNode) {
                return node;
            } else if (level == depth) {
                return node;
            } else {
                int childrenSize = node.childrenSize();
                double[] S = new double[childrenSize];
                List<Rectangle> rectangles = new ArrayList<>();
                HashMap<Rectangle, TreeNode> map = new HashMap<>();

                /*
                 * We take and calculate the enlargement area that is needed to
                 * accommodate the new Data entry/Rectangle.
                 * In conflicts of equality we get the smaller rectangle.
                 */
                for (int i = 0; i < childrenSize; i++) {
                    rectangles.add(node.child(i).getRectangle());
                    map.put(node.child(i).getRectangle(), node.child(i));
                    List<Rectangle> tmp = new ArrayList<>();
                    tmp.add(rectangles.get(i));
                    tmp.add(newData.getRectangle());
                    S[i] = Utils.mbrRect(tmp).area() - rectangles.get(i).area();
                }

                double min = S[0];
                Rectangle minRect = rectangles.get(0);
                for (int i = 1; i < childrenSize; i++) {
                    if (S[i] < min) {
                        min = S[i];
                        minRect = rectangles.get(i);
                    } else if (S[i] == min) {
                        if (rectangles.get(i).area() < minRect.area()) {
                            minRect = rectangles.get(i);
                        }
                    }
                }
                node = map.get(minRect);
            }
            // Up the count of the depth
            depth += 1;

        }
    }

    private void overflowTreatment(){

    }

    private void reInsert(){

    }
}
