import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This is the R* tree representative class.
 */
public final class RTree implements Serializable {
    private TreeNode root;  // Root is in the family of Tree Nodes
    private Context context;

    /*
     * Testing things... First test... Inserting and creating a small R*tree
     * 1 attempt: Errors in Split... forgot the M+1
     * 2 attempt: Errors in node.child()... there are 2 different methods
     *              one for the Not-leaves and one for the leaves.
     * It works... without knowing what really is being saved.
     * Apparently it does not work correctly... only a depth of 1.
     * First sus: split children disappear. Investigate. Solved.
     * Second sus: Entries got up to M+1 kids for some reason. Reinsert problem: solved.
     * Thirds sus: No split in level 1 and every new entry disappear. Solved. and many more.
     */
    public static void main(String[] args) {
        Context contextRoot = new Context(2,5,2);
        Insert insert = new Insert();
        RTree rTree = new RTree(null, contextRoot);
        double[] vector = new double[2];
        Random r = new Random();
        double randomValue;
        List<Entry> entries = new ArrayList<>();

        for (int i=0; i<10000; i++){
            vector[0] = -180 + (180 + 180) * r.nextDouble();
            vector[1] = -180 + (180 + 180) * r.nextDouble();
            entries.add(new Entry( vector.clone(), 1, i));
        }

        for (int i=0; i<entries.size(); i++){
            //System.out.println("Entering the : " +i+" st entry.");
            insert.insertData(rTree, rTree.calculateDepth(), entries.get(i));
            //rTree.printTree();
        }

        //rTree.printTree();
        System.out.println("Depth: "+rTree.calculateDepth());


        System.out.println("ok");
    }

    public RTree(){
        //
        context = new Context();
        root = null;
    }

    public RTree(TreeNode root, Context context){
        this.root = root;
        this.context = context;
    }

    public TreeNode getRoot(){
        return root;
    }

    public <T extends TreeNode> void setRoot(T newRoot){
        root = newRoot;
    }

    public Context getContext(){
        return context;
    }

    public void setContext(Context context){    // Prefer not to use this
        this.context = context;
    }

    /**
     * This method returns the depth of the tree.
     * It calculates it by traversing all the tree till it
     * reaches a leaf node.
     *
     * @return depth of the R*-tree
     */
    public int calculateDepth() {
        return calculateDepth(root);
    }

    private static int calculateDepth(TreeNode root) {
        if (root == null || root instanceof LeafNode){
            return 0;
        }

        if (root.childrenSize() > 0){
            return calculateDepth(root.child(0), 1);
        }
        return 0;
    }

    private static int calculateDepth(TreeNode node, int depth) {
        if (node instanceof LeafNode) {
            return depth;
        } else {
            return calculateDepth( node.child(0), depth + 1);
        }
    }

    /*
     * Printing a rough blueprint of the R*tree
     */
    private void printTree(){
        System.out.println("  -------------------------------------------------------------------------------");
        System.out.println("Root rectangle:");
        System.out.println("  ----------------");
        TreeNode node = root;
        List<TreeNode> nodes = new ArrayList<>();

        nodes.add(node);

        while (true){
            if (nodes.isEmpty()){
                break;
            }
            node = nodes.get(0);
            if(node instanceof LeafNode){
                node.getRectangle().print();
                System.out.println("Children: "+ node.childrenSize());
                System.out.println("Parent:"+node.getParent());
                System.out.println("Entries Rectangles in leaf:");
                for (int i=0; i<node.childrenSize(); i++){
                    node.entryChild(i).getRectangle().print();
                }
                System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\");
            } else if (node instanceof NotLeafNode){
                node.getRectangle().print();
                System.out.println("Children of Non Leaf Node: "+ node.childrenSize());
                System.out.println("Parent:"+node.getParent());
                for (int i=0; i<node.childrenSize(); i++){
                    nodes.add(node.child(i));
                }
            }
            nodes.remove(node);
        }
    }

}
