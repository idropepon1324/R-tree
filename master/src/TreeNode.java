import java.util.List;

/**
 * This is the basic interface that connects all the Tree nodes.
 */
public interface TreeNode {
    public Rectangle getRectangle();
    public int childrenSize();
    public TreeNode child(int i);
    public Entry entryChild(int i);

}
