import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
     */
    public static void main(String[] args) {
        Context contextRoot = new Context(2,5,2);
        Insert insert = new Insert();
        RTree rTree = new RTree(null, contextRoot);
        double[] vector = new double[2];
        Random r = new Random();
        double randomValue;
        List<Entry> entries = new ArrayList<>();

        for (int i=0; i<30; i++){
            vector[0] = -180 + (180 + 180) * r.nextDouble();
            vector[1] = -180 + (180 + 180) * r.nextDouble();
            entries.add(new Entry( vector.clone(), 1, i));
        }

        for (int i=0; i<entries.size(); i++){
            //System.out.println("Entering the : " +i+" st entry.");
            insert.insertData(rTree, rTree.calculateDepth(), entries.get(i));
        }

        //System.out.println("Depth: "+rTree.calculateDepth());
        System.out.println(rTree.getRoot().child(0).child(2).childrenSize());

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

}
