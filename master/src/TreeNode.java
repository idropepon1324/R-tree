import java.io.Serializable;

/**
 * This is the basic interface that connects all the Tree nodes.
 */
public interface TreeNode extends HasGeometry, Serializable {
    public Rectangle getRectangle();
    public int childrenSize();
    public TreeNode child(int i);
    public Entry entryChild(int i);
    public <T extends HasGeometry> void add(T entry);
    public void fixMbr();
    public TreeNode getParent();
    public void setParent(TreeNode parent);
    public Context context();
    public void deleteChild(Entry e);
    public void deleteChild(TreeNode e);

}
