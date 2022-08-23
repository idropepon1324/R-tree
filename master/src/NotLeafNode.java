import java.util.List;

/**
 * This class represent a R* node of the tree that is not a leaf.
 * The attribute of such a node are the children of it, the rectangle
 * which associates the children and the context and settings that the
 * tree is allowed to work (e.g. maxChildrenSize).
 */
public class NotLeafNode implements TreeNode {
    private List<? extends TreeNode> children;
    private Rectangle rectangle;
    private Context context;

    public NotLeafNode(List<? extends TreeNode> children, Rectangle rectangle, Context context){
        this.children = children;
        this.rectangle = rectangle;
        this.context = context;
    }


    public List<TreeNode> add(Entry entry) {
        return NonLeafFunctions.add(entry, this);
    }

    public NodeAndEntries delete(Entry entry, boolean all) {
        return NonLeafFunctions.delete(entry, all, this);
    }

    public int childrenSize() {
        return children.size();
    }

    public Context context() {
        return context;
    }

    public TreeNode child(int i) {
        return children.get(i);
    }

    public List<? extends TreeNode> entry(int i) {
        return children;
    }

}
