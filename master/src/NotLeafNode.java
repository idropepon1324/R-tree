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

    public NotLeafNode(List<? extends TreeNode> children, Rectangle rectangle, Context context){
        this.children = children;
        this.rectangle = rectangle;
        this.context = context;
    }


//    public List<TreeNode> add(Entry entry) {
//        return NonLeafFunctions.add(entry, this);
//    }
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

    public TreeNode getParent(){ return parent; }

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
        List<Rectangle> rects = new ArrayList<>();
        for(int i=0;i<childrenSize();i++){
            rects.add(child(i).getRectangle());
        }
        rectangle = u.mbrRect(rects);
    }

    public void deleteChild(Entry e){
        return;
    }

    public void deleteChild(TreeNode tn){
        children.remove(tn);
    }
}
