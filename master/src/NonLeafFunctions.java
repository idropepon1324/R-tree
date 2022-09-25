import java.util.List;
import java.util.Optional;

/**
 * This Class has all the needed functions for the NonLeaf Nodes to operate.
 * It includes the methods search, add, delete and makeNonLeaves.
 *
 */
public final class NonLeafFunctions {

    // search
    // add
    // makeNonLeaves
    // delete

    private NonLeafFunctions(){
        // prevent initialization
    }

    public static <T extends HasGeometry> List<? extends TreeNode> add(T entry, NotLeafNode node){


        if (node.child(0) instanceof LeafNode){
            List<LeafNode> children = (List<LeafNode>)node.entries();
            LeafNode newData = (LeafNode) entry;
            children.add(newData);

            return children;

        } else if (node.child(0) instanceof NotLeafNode) {
            List<NotLeafNode> children = (List<NotLeafNode>)node.entries();
            NotLeafNode newData = (NotLeafNode) entry;
            children.add(newData);

            return children;

        }
        return null;

    }




}
