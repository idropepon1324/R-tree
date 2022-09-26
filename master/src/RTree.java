import java.io.Serializable;

/**
 * This is the R* tree representative class.
 */
public final class RTree implements Serializable {
    private TreeNode root;  // Root is in the family of Tree Nodes
    private Context context;

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

    private static int calculateDepth(TreeNode root) {  // Fix this later //TODO
        //return root.map(node -> calculateDepth(node, 0)).orElse(0);
        return 0;
    }

    private static int calculateDepth(TreeNode node, int depth) {
        if (node instanceof LeafNode) {
            return depth + 1;
        } else {
            return calculateDepth(( node).child(0), depth + 1);
        }
    }

}
