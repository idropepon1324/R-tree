import java.util.List;

public class LeafNode implements TreeNode {

    private List<Entry> entries;
    private Rectangle rectangle;
    private Context context;
    private int index;

    public LeafNode (List<Entry> entries, Rectangle rectangle, Context context){
        this.entries = entries;
        this.rectangle = rectangle;
        this.context = context;
    }
    
    public List<TreeNode> add(Entry entry) {
        return NonLeafHelper.add(entry, this);
    }

    public NodeAndEntries<T, S> delete(Entry<? extends T, ? extends S> entry, boolean all) {
        return LeafHelper.delete(entry, all, this);
    }

    public int childrenSize(){
        return entries.size();
    }

    public Context context() {
        return context;
    }

    public Entry entry(int i) {
        return entries.get(i);
    }

    public int getIndex(){
        return index;
    }

}
