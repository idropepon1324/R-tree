import java.io.Serializable;

/**
 * This is the basic interface that connects all the Tree nodes.
 */
public interface TreeNode extends HasGeometry, Serializable {
    Rectangle getRectangle();
    int childrenSize();
    TreeNode child(int i);
    Entry entryChild(int i);
    <T extends HasGeometry> void add(T entry);
    void fixMbr();
    TreeNode getParent();
    void setParent(TreeNode parent);
    Context context();
    void deleteChild(Entry e);
    void deleteChild(TreeNode e);

}
