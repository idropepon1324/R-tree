import java.util.ArrayList;
import java.util.List;

public class LeafNode implements TreeNode {

    private List<Entry> entries;
    private Rectangle rectangle;
    private Context context;
    private TreeNode parent;

    public LeafNode (List<Entry> entries, Rectangle rectangle, Context context, TreeNode parent){
        this.entries = entries;
        this.rectangle = rectangle;
        this.context = context;
        this.parent = parent;
    }


    public LeafNode (List<Entry> entries, Rectangle rectangle, Context context){
        this.entries = entries;
        this.rectangle = rectangle;
        this.context = context;
        this.parent = null;
    }

    public LeafNode(List<Entry> entries, Context context, TreeNode parent){
        this.entries = entries;
        this.context = context;
        this.parent = parent;
        fixMbr();
    }

    public LeafNode(List<Entry> entries, Context context){
        this.entries = entries;
        this.context = context;
        this.parent = null;
        fixMbr();
    }



    public int childrenSize(){
        return entries.size();
    }

    public Context context() {
        return context;
    }

    public Entry entryChild(int i) {
        return entries.get(i);
    }

    public List<Entry> entries(){
        return entries;
    }

    public Rectangle getRectangle(){
        return rectangle;
    }

    public TreeNode child(int i) {
        return null;
    }

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

    public void fixMbr(){
        rectangle = Utils.mbr(this);
    }

    public TreeNode getParent(){
        return parent;
    }

    public void setParent(TreeNode parent){
        this.parent = parent;
    }


    public void deleteChild(Entry e){
        entries.remove(e);
    }

    public void deleteChild(TreeNode tn){
        //return;
    }


}
