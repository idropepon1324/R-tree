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
    private final Context context;
    private TreeNode parent;

    /**
     * The constructor
     * @param children the children of the node
     * @param rectangle the rectangle(mbr) of the node
     * @param context the context of the r*-tree
     * @param parent the node's parent
     */
    public NotLeafNode(List<? extends TreeNode> children, Rectangle rectangle, Context context, TreeNode parent){
        this.children = children;
        this.rectangle = rectangle;
        this.context = context;
        this.parent = parent;
    }

    /**
     * The constructor
     * @param children the children of the node
     * @param rectangle the rectangle(mbr) of the node
     * @param context the context of the r*-tree
     */
    public NotLeafNode(List<? extends TreeNode> children, Rectangle rectangle, Context context){
        this.children = children;
        this.rectangle = rectangle;
        this.context = context;
        this.parent = null;
    }

    /**
     *
     * @param children the children of the node
     * @param context the rectangle(mbr) of the node
     * @param parent the context of the r*-tree
     */
    public NotLeafNode(List<? extends TreeNode> children, Context context, TreeNode parent){
        this.children = children;
        this.context = context;
        this. parent = parent;
        fixMbr();
    }

    /**
     *
     * @param children the children of the node
     * @param context the rectangle(mbr) of the node
     */
    public NotLeafNode(List<? extends TreeNode> children, Context context){
        this.children = children;
        this.context = context;
        this. parent = null;
        fixMbr();
    }

    /**
     *
     * @param entry the entry we want to add to this leaf node
     * @param <T> an object that Has Geometry attributes
     */
    // Possible error in the future, but it is okay now
    public <T extends HasGeometry> void add(T entry) {
        List<? extends TreeNode> tmp = NonLeafFunctions.add(entry, this);
        if (tmp != null){
            children = new ArrayList<>(tmp);
        } else {
            System.out.println("Children wasn't added in the NonLeafFunction.add().");
        }
    }

    /**
     *
     * @return how many children this node has
     */
    public int childrenSize() {
        return children.size();
    }

    /**
     *
     * @return the Context of this r*-tree
     */
    public Context context() {
        return context;
    }

    /**
     *
     * @param i the index of the child
     * @return a child
     */
    public TreeNode child(int i) {
        return children.get(i);
    }

    /**
     *
     * @return the entries of this node
     */
    public List<? extends TreeNode> entries() {
        return children;
    }

    /**
     *
     * @return the rectangle of this node
     */
    public Rectangle getRectangle(){
        return rectangle;
    }

    /**
     *
     * @param i index of an entry
     * @return the entry(only for leaf node)
     */
    public Entry entryChild(int i){return null;} // Out of use

    /**
     *
     * @return list of the entry's children
     */
    @SuppressWarnings("unchecked")
    public List<TreeNode> children(){
        return (List<TreeNode>) children;
    }

    /**
     *
     * @return the parent of this node
     */
    public TreeNode getParent(){ return parent; }

    /**
     * sets new parent for this node
     * @param parent the new parent
     */
    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    /**
     * Fixes the Mbr(minimum bounding rectangle) of this node
     */
    public void fixMbr(){
        rectangle = Utils.mbr(this);
    }

    /**
     * deletes an entry child(for leaf nodes)
     * @param e the child we want to delete
     */
    public void deleteChild(Entry e){
        //return;
    }

    /**
     * deletes an tree node child(for non leaf nodes)
     * @param tn the tree node child we want to delete
     */
    public void deleteChild(TreeNode tn){
        children.remove(tn);
    }

}
