import javax.print.DocFlavor;
import java.util.ArrayList;
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
    private TreeNode parent;
    private int index;

    public NotLeafNode(List<? extends TreeNode> children, Rectangle rectangle, Context context, TreeNode parent){
        this.children = children;
        this.rectangle = rectangle;
        this.context = context;
        this.parent = parent;
    }

    public NotLeafNode(List<? extends TreeNode> children, Rectangle rectangle, Context context){
        this.children = children;
        this.rectangle = rectangle;
        this.context = context;
        this.parent = null;
    }

    public NotLeafNode(List<? extends TreeNode> children, Context context, TreeNode parent){
        this.children = children;
        this.context = context;
        this. parent = parent;
        fixMbr();
    }

    public NotLeafNode(List<? extends TreeNode> children, Context context){
        this.children = children;
        this.context = context;
        this. parent = null;
        fixMbr();
    }

    public <T extends HasGeometry> void add(T entry) {
        List<? extends TreeNode> tmp = NonLeafFunctions.add(entry, this);
        if (tmp != null){
            children = tmp;
        } else {
            System.out.println("Children wasn't added in the NonLeafFunction.add().");
        }
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

    public List<? extends TreeNode> entries() {
        return children;
    }

    public Rectangle getRectangle(){
        return rectangle;
    }

    public Entry entryChild(int i){return null;} // Out of use

    public List<TreeNode> children(){
        return (List<TreeNode>) children;
    }

    public TreeNode getParent(){ return parent; }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public void fixMbr(){
        rectangle = Utils.mbr(this);
    }

    public void deleteChild(Entry e){
        return;
    }

    public void deleteChild(TreeNode tn){
        children.remove(tn);
    }
}
