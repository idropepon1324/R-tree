import java.util.ArrayList;
import java.util.List;

public class LeafNode implements TreeNode {

    private List<Entry> entries;
    private Rectangle rectangle;
    private final Context context;
    private TreeNode parent;

    /**
     * The constructor
     * @param entries the Entries of this leaf node
     * @param rectangle the mbr of this leaf node
     * @param context the context of this particular r*-tree
     * @param parent this leaf's parent
     */
    public LeafNode (List<Entry> entries, Rectangle rectangle, Context context, TreeNode parent){
        this.entries = entries;
        this.rectangle = rectangle;
        this.context = context;
        this.parent = parent;
    }


    /**
     * The constructor
     * @param entries the Entries of this leaf node
     * @param rectangle the mbr of this leaf node
     * @param context the context of this particular r*-tree
     */
    public LeafNode (List<Entry> entries, Rectangle rectangle, Context context){
        this.entries = entries;
        this.rectangle = rectangle;
        this.context = context;
        this.parent = null;
    }

    /**
     * The constructor
     * @param entries the Entries of this leaf node
     * @param context the context of this particular r*-tree
     * @param parent this leaf's parent
     */
    public LeafNode(List<Entry> entries, Context context, TreeNode parent){
        this.entries = entries;
        this.context = context;
        this.parent = parent;
        fixMbr();
    }

    /**
     * The constructor
     * @param entries the Entries of this leaf node
     * @param context the context of this particular r*-tree
     */
    public LeafNode(List<Entry> entries, Context context){
        this.entries = entries;
        this.context = context;
        this.parent = null;
        fixMbr();
    }


    /**
     *
     * @return how many children this leaf has
     */
    public int childrenSize(){
        return entries.size();
    }

    /**
     *
     * @return the Context object of this tree
     */
    public Context context() {
        return context;
    }

    /**
     *
     * @param i index of the particular child
     * @return this particular child
     */
    public Entry entryChild(int i) {
        return entries.get(i);
    }

    /**
     *
     * @return list of the entries
     */
    public List<Entry> entries(){
        return entries;
    }

    /**
     *
     * @return the rectangle of this leaf node
     */
    public Rectangle getRectangle(){
        return rectangle;
    }

    /**
     *
     * @param i index of non leaf tree node child
     * @return this particular child
     */
    public TreeNode child(int i) {
        return null;
    }

    /**
     *
     * @param entry the entry we want to add to this leaf node
     * @param <T> an object that Has Geometry attributes
     */
    public <T extends HasGeometry> void add(T entry){

        if (entry instanceof Entry){
            // Changing the add of the entry to this 3 line code and fixing for some reason the ConcurrentModificationException
            // After 4 hour the problem was solved
            List<Entry> tmp = new ArrayList<>(entries);
            tmp.add((Entry)entry);
            entries = new ArrayList<>(tmp);
            //entries.add((Entry)entry);            // This was the big problem

        } else {
            System.out.println("Error occurred during saving a not entry object in a leaf");
            System.out.println("_____________________________________________________________");
        }
    }

    /**
     * Fixes the Mbr(minimum bounding rectangle) of this node
     */
    public void fixMbr(){
        rectangle = Utils.mbr(this);
    }

    /**
     * Returns the parent
     * @return the parent of this node
     */
    public TreeNode getParent(){
        return parent;
    }

    /**
     * Sets a new parent
     * @param parent the new parent we want to set
     */
    public void setParent(TreeNode parent){
        this.parent = parent;
    }

    /**
     * Deletes an entry(for leaf nodes)
     * @param e the entry we want to delete
     */
    public void deleteChild(Entry e){
        entries.remove(e);
    }

    /**
     * Deletes a child node(for non leaf node)
     * @param tn the node we want to delete
     */
    public void deleteChild(TreeNode tn){
        //return;
    }


}
