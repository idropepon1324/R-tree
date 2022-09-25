import java.util.List;
import java.util.Optional;

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
    private TreeNode parent;
    private int index;

    public NotLeafNode(List<? extends TreeNode> children, Rectangle rectangle, Context context){
        this.children = children;
        this.rectangle = rectangle;
        this.context = context;
    }


    public <T extends HasGeometry> void add(T entry) {
        List<? extends TreeNode> tmp = NonLeafFunctions.add(entry, this);
        if (tmp != null){
            children = tmp;
        }
    }
//
//    public NodeAndEntries delete(Entry entry, boolean all) {
//        return NonLeafFunctions.delete(entry, all, this);
//    }

    public int childrenSize() {
        return children.size();
    }

    public Context context() {
        return context;
    }

    public TreeNode child(int i) {
        return children.get(i);
    }

    public List<? extends TreeNode> entries() {
        return children;
    }

    public Rectangle getRectangle(){
        return rectangle;
    }

    public Entry entryChild(int i){return null;};

    public List<TreeNode> children(){
        return (List<TreeNode>) children;
    }

    public void fixMbr(){
        rectangle = Utils.mbr(this);
    }


    public TreeNode getParent(){ return parent; }

}
