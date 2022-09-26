import java.util.List;

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

    @SuppressWarnings("unchecked") // Cast, <? extends TreeNode> to <LeafNode>/<NotLeafNode> : If conditions are in place
    public static <T extends HasGeometry> List<? extends TreeNode> add(T entry, NotLeafNode node){


        // The space availability check occurs at the Insert Class
        if (node.child(0) instanceof LeafNode && entry instanceof LeafNode){
            List<LeafNode> children = (List<LeafNode>)node.entries();
            LeafNode newData = (LeafNode) entry;
            children.add(newData);

            return children;

        } else if (node.child(0) instanceof NotLeafNode && entry instanceof NotLeafNode) {
            List<NotLeafNode> children = (List<NotLeafNode>)node.entries();
            NotLeafNode newData = (NotLeafNode) entry;
            children.add(newData);

            return children;

        }
        // if the data are not correct
        return null;

    }




}
