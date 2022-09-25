import jdk.jshell.execution.Util;

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

    public TreeNode getParent(){
        return parent;
    }

    public void fixRectangle(){
        /*
        for(int i=0;i<rectangle.getVector1().length;i++){
            double max = entryChild(0).getFeatVec()[i];
            double min = entryChild(0).getFeatVec()[i];
            for(int j=1;j<childrenSize();j++){
                if(entryChild(j).getFeatVec()[i]< min){
                    min = entryChild(j).getFeatVec()[i];
                }else if(entryChild(j).getFeatVec()[i] > min)

            }
        }
        */

        Utils u = new Utils();
        rectangle = u.mbrPoints(entries);
    }
}
