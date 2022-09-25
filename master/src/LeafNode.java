import java.util.List;

public class LeafNode implements TreeNode {

    private List<Entry> entries;
    private Rectangle rectangle;
    private Context context;
    private TreeNode parent;
    private int index;

    public LeafNode (List<Entry> entries, Rectangle rectangle, Context context){
        this.entries = entries;
        this.rectangle = rectangle;
        this.context = context;
    }
    
//    public List<TreeNode> add(Entry entry) {
//        return NonLeafHelper.add(entry, this);
//    }
//
//    public NodeAndEntries<T, S> delete(Entry<? extends T, ? extends S> entry, boolean all) {
//        return LeafHelper.delete(entry, all, this);
//    }

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
            if (entries().size() < context.maxChildren()){
                entries.add((Entry)entry);
            }
        }

    }

    public void fixMbr(){
        rectangle = Utils.mbr(this);
    }

    public TreeNode getParent(){
        return parent;
    }

//    public void fixRectangle(){
//        rectangle = Utils.mbrPoints(entries);
//    }

    public void deleteChild(Entry e){
        entries.remove(e);
    }

    public void deleteChild(TreeNode tn){
        return;
    }
}
