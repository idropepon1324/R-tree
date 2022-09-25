import java.io.Serializable;
import java.util.Optional;

/**
 * This is the R* tree representative class.
 */
public final class RTree implements Serializable {
    private Optional<? extends TreeNode> root;  // Root is in the family of Tree Nodes
    private Context context;

    public RTree(){
        //
        context = new Context();
    }

    public RTree(Optional<? extends TreeNode> root, Context context){
        this.root = root;
        this.context = context;
    }

    public Optional<? extends TreeNode> getRoot(){
        return root;
    }

    public void setRoot(Optional<? extends TreeNode> newRoot){
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

    private static int calculateDepth(Optional<? extends TreeNode> root) {
        return root.map(node -> calculateDepth(node, 0)).orElse(0);
    }

    private static int calculateDepth(TreeNode node, int depth) {
        if (node instanceof LeafNode) {
            return depth + 1;
        } else {
            return calculateDepth(((NotLeafNode) node).child(0), depth + 1);
        }
    }

}
