import java.util.*;

/**
 * This Class is used to Insert data to an R* tree.
 * The method used in this is the insertData(RTree newRtree, int leafLevel, Entry newData)
 * which takes as a parameter the r* tree, the leaf level and the new data Entry.
 */
public class Insert {
    RTree rtree;
    boolean firstCall;
    int currentLevel;

    /*
     * Inserting 2 types of data Entries/Points and TreeNodes/Rectangles.
     *
     * Inserting new data to the R*tree algorithm:
     * insertData starts insert with the level leaf level as a parameter.
     *
     *
     * OverflowTreatment: Reinsert or split, the latter
     * only if it is the root or this is the second call in this level
     * of overflowTreatment.
     */

    /*
     * ReverseOrder in sort works perfectly.
     */

    public Insert(){
        //
        rtree = new RTree();
        firstCall = true;
    }

    public Insert(RTree rtree){
        this.rtree = rtree;
        firstCall = true;
    }

    /**
     * This inserts the new Data to the given R* tree.
     * @param newRtree an Rtree
     * @param leafLevel the leaf level
     * @param newData the new Entry data
     */
    public void insertData(RTree newRtree, int leafLevel, Entry newData){
        currentLevel = leafLevel;
        rtree = newRtree;
        insert(leafLevel, newData);
    }

    /**
     * This private method checks and follow the algorithm of how to insert new Entries.
     * All the main logic is in this method.
     * @param level the level we wish to insert the entry
     * @param newData the entry we wish to insert
     * @param <T> the class of the entry (TreeNode/Entry)
     */
    @SuppressWarnings("unchecked") // Casting of <? extends TreeNode> to <TreeNode>: Obvious
    private <T extends HasGeometry> void insert(int level, T newData){
        TreeNode node = chooseSubtree(level, newData);

        if (node == null) { // First ever root
            List<Entry> entry = new ArrayList<>();
            entry.add((Entry)newData);
            rtree.setRoot(new LeafNode(entry, rtree.getContext()));
            node = rtree.getRoot();
        } else if (node.childrenSize() < rtree.getContext().maxChildren()){    // <M
            if(node instanceof NotLeafNode){
                node.add(newData);
            } else if (node instanceof LeafNode){
                node.add((Entry) newData);
            }
            //node.add(newData);

            if (newData instanceof TreeNode){
                ((TreeNode) newData).setParent(node.getParent());
            }

        } else if (node.childrenSize() == rtree.getContext().maxChildren()){ // ==M
            currentLevel = level;
            List<TreeNode> listPair =(List<TreeNode>) overflowTreatment(level, node, newData);

            // Split occurred, Go up a level to fix the problem
            while (listPair != null && currentLevel != 0){ // NOT THE ROOT
                currentLevel = currentLevel - 1;
                TreeNode parent = node.getParent();
                parent.deleteChild(node);

                // All splits entries fit in the tree node.
                if (parent.childrenSize() + 2 <= parent.context().maxChildren()){
                    listPair.get(0).setParent(parent);
                    listPair.get(1).setParent(parent);
                    parent.add(listPair.get(0));
                    parent.add(listPair.get(1));
                    node = parent; // In order to fix the mbr later
                    listPair = null; // Break;
                } else { // M+1 entries
                    listPair.get(0).setParent(parent);
                    parent.add(listPair.get(0));
                    parent.fixMbr();
                    node = parent;
                    listPair =(List<TreeNode>)  overflowTreatment(currentLevel, node, listPair.get(1));
                }
            }

            // Special Case: when the root has to be split
            if (listPair != null) { //currentLevel == 0
                TreeNode newRoot = new NotLeafNode(listPair, rtree.getContext());
                listPair.get(0).setParent(newRoot);
                listPair.get(1).setParent(newRoot);
                rtree.setRoot(newRoot);
            }
        }

        // Fix all the rectangles mbr of the path
        while (node.getParent() != null){
            node.fixMbr();
            node = node.getParent();
        }
        node.fixMbr();  // Root fix
    }

    /**
     * This private method chooses the most appropriate node in the tree, at the
     * level that has being asked, to accommodate the new Entry.
     * @param level the level we wish to insert the entry
     * @param newData the entry to be inserted
     * @param <T> the class of the entry(TreeNode/Entry)
     * @return a TreeNode (NotLeafNode/LeafNode)
     */
    public <T extends HasGeometry> TreeNode chooseSubtree(int level, T newData){
        int depth = 0;
        TreeNode root = rtree.getRoot();
        TreeNode node;
        node = root;

        while (true) {
            if (node == null){ // first root
                return null;
            } else if (node instanceof LeafNode) {
                //return node;
                break;
            } else if (level == depth) {
                break;
                //return node;
            } else {
                int childrenSize = node.childrenSize();
                double[] S = new double[childrenSize];
                List<Rectangle> rectangles = new ArrayList<>();
                HashMap<Rectangle, TreeNode> map = new HashMap<>();

                /*
                 * We take and calculate the enlargement area that is needed to
                 * accommodate the new Data entry/Rectangle.
                 * In conflicts of equality we get the smaller rectangle.
                 */
                for (int i = 0; i < childrenSize; i++) { // No LeafNodes in here so node.child() works perfectly
                    rectangles.add(node.child(i).getRectangle());
                    map.put(node.child(i).getRectangle(), node.child(i));


                    List<Rectangle> tmp = new ArrayList<>();
                    tmp.add(rectangles.get(i));
                    tmp.add(newData.getRectangle());
                    S[i] = Utils.mbrRect(tmp).area() - rectangles.get(i).area();
                }

                double min = S[0];
                Rectangle minRect = rectangles.get(0);
                for (int i = 1; i < childrenSize; i++) {
                    if (S[i] < min) {
                        min = S[i];
                        minRect = rectangles.get(i);
                    } else if (S[i] == min) {
                        if (rectangles.get(i).area() < minRect.area()) {
                            minRect = rectangles.get(i);
                        }
                    }
                }
                node = map.get(minRect);
            }
            // Up the count of the depth
            depth += 1;

        }

        return node;
    }

    /**
     * Returns null, if it Reinserts(dynamically), or else return two
     * treeNodes that are created from the split of the node with the new data.
     *
     * @param level The level of the node
     * @param node the node to be reInserted or split
     * @param newData the new data to be registered
     * @param <T> a TreeNode or an Entry
     * @return a ListPair of TreeNodes or null for reInsert
     */
    private <T extends HasGeometry> List<? extends TreeNode> overflowTreatment(int level, TreeNode node, T newData){

        if (level != 0 && isFirstCall(level)){
            firstCall = false;      // So if the the OverflowTreatment call itself in the same level, split
            // ReInsert()
            reInsert(level, node, newData);
            firstCall = true; // Reset its function
        } else {
            // Split()
            if(node instanceof NotLeafNode){
                TreeNode addUp = (TreeNode) newData;
                // Return the listPair of NotLeafNodes
                return rtree.getContext().splitter().split((NotLeafNode)node, addUp, node.context());
            } else if (node instanceof LeafNode){
                Entry addUp = (Entry) newData;
                // Return the listPair of LeafNodes
                return rtree.getContext().splitter().split((LeafNode) node, addUp, node.context());
            }
        }
        // Reinsert occurred
        return null;

    }

    /**
     * This method re-inserts the M+1 entries accordingly.
     * The algorithm chooses the "p" furthest distance Entries to be
     * re-Inserted and the M+1-p stay in the node with the new mbr.
     * M entries from node + 1 from the new Data.
     * @param level the level to be reInserted
     * @param node the node with the entries
     * @param newData the new entry
     * @param <T> the class of the Entries
     */
    private <T extends HasGeometry> void reInsert(int level, TreeNode node, T newData){
        List<T> p = new ArrayList<>();

        List<T> sorted = sortNodeReInsert(node, newData);   // Sorted is M+1
        node.add(newData);  // Add the extra add too

        // Remove the first "p" objects, adjust the mbr and insert them again
        for (int i=0; i<node.context().getReInsertRate(); i++){
            p.add(sorted.get(i));

            if (node instanceof LeafNode){
                node.deleteChild((Entry)sorted.get(i));
            } else if (node instanceof NotLeafNode){
                node.deleteChild((TreeNode)sorted.get(i));
            }
        }
        node.fixMbr();

        // Insert the "p" entries again
        for (T t: p){
            insert(level, t);
        }
    }

    // Checks if it is the first call of the reinsert for that level.
    private boolean isFirstCall(int level){
        return currentLevel != level || firstCall;
    }

    // Sorts the Entries in Descendant order of their distances of the main Rect and returns the M+1 in a list.
    @SuppressWarnings("unchecked") // T casting to a node.child(): It is of T Class
    private <T extends HasGeometry> List<T> sortNodeReInsert(TreeNode node, T newData){
        int kids = node.childrenSize();
        Rectangle mainRect = node.getRectangle();
        HashMap<Double, T> map = new HashMap<>();
        double diff;

        for (int i=0; i<kids; i++){
            if(node instanceof LeafNode){
                diff = Utils.distanceRect(mainRect, node.entryChild(i).getRectangle());
                map.put(diff, (T)node.entryChild(i));
            } else if (node instanceof NotLeafNode) {
                diff = Utils.distanceRect(mainRect, node.child(i).getRectangle());
                map.put(diff, (T)node.child(i));
            }
        }
        map.put(Utils.distanceRect(mainRect, newData.getRectangle()), newData);
        TreeMap<Double, T> sorted = new TreeMap<>(Collections.reverseOrder());
        sorted.putAll(map);
        return new ArrayList<>(sorted.values());
    }
}
