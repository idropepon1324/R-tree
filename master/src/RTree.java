import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This is the R* tree representative class.
 */
public final class RTree implements Serializable {
    private TreeNode root;  // Root is in the family of Tree Nodes
    private Context context;

    /*
     * Testing things... First test... Inserting and creating a small R*tree
     * 1 attempt: Errors in Split... forgot the M+1
     * 2 attempt: Errors in node.child()... there are 2 different methods
     *              one for the Not-leaves and one for the leaves.
     * It works... without knowing what really is being saved.
     * Apparently it does not work correctly... only a depth of 1.
     * First sus: split children disappear. Investigate. Solved.
     * Second sus: Entries got up to M+1 kids for some reason. Reinsert problem: solved.
     * Thirds sus: No split in level 1 and every new entry disappear. Solved. and many more.
     */
    public static void main(String[] args) {
        Context contextRoot = new Context(2,3,2);
        Insert insert = new Insert();
        RTree rTree = new RTree(null, contextRoot);
        double[] vector = new double[2];
        Random r = new Random();
        double randomValue;
        List<Entry> entries = new ArrayList<>();


        for (int i=0; i<10; i++){
            vector[0] = -180 + (180 + 180) * r.nextDouble();
            vector[1] = -180 + (180 + 180) * r.nextDouble();
            System.out.println(vector);
            entries.add(new Entry( vector.clone(), 1, i));
        }

        for (int i=0; i<entries.size(); i++){
            System.out.println("Entering the : " +i+" st entry.");
            insert.insertData(rTree, rTree.calculateDepth(), entries.get(i));
            rTree.printTree();
        }

        //rTree.printTree();
        System.out.println("Depth: "+rTree.calculateDepth());


        System.out.println("ok");

        Queries q = new Queries();

        Search s = new Search();




        //-------------------------------------testing deletion-------------------------------------
        /*
        Deletion d = new Deletion();
        if(d.deleteEntry(rTree, rTree.root, entries.get(7))){
            System.out.println("true");
        }else {
            System.out.println("false");
        }

        rTree.printTree();
        */
        //-------------------------------------testing deletion-------------------------------------



        //-------------------------------------testing aplo search-------------------------------------
        /*
        vector[0]=3;
        vector[1]=4;
        if(s.searchEntry(rTree.root,entries.get(3))){
            System.out.println("true");
        }else{
            System.out.println("false");
        }

        if(s.searchEntry(rTree.root,new Entry(vector.clone(),1,1))){
            System.out.println("true");
        }else {
            System.out.println("false");
        }
        */

        //-------------------------------------testing area search-------------------------------------
        /*
        double[] vector1 = new double[2];
        double[] vector2 = new double[2];
        vector1[0]=-50;
        vector1[1]=-100;
        vector2[0]=150;
        vector2[1]=50;
        List<Entry> el = s.searchArea(rTree.root,new Rectangle(vector1,vector2));

        if(el.size()==0){
            System.out.println("den ixe tpt mesa");;
        }else {
            System.out.println("ta stixia mesa sto rectangle ine ta eksis: ");
            for (int i=0;i<el.size();i++){
                System.out.println(el.get(i).getFeatVec()[0] + " " + el.get(i).getFeatVec()[1]);
            }
        }
        */



        //-------------------------------------testing skyline-------------------------------------


        List<Entry> skyline = q.skyLineBBS(rTree.root);
        System.out.println("BBS skyline:");

        for(int i=0;i<skyline.size();i++){
            System.out.println(skyline.get(i).getFeatVec()[0] + " " + skyline.get(i).getFeatVec()[1]);
        }

        skyline = q.skyLineLinear(entries);

        System.out.println("Linear skyline:");

        for(int i=0;i<skyline.size();i++){
            System.out.println(skyline.get(i).getFeatVec()[0] + " " + skyline.get(i).getFeatVec()[1]);
        }



        //-------------------------------------testing NN-------------------------------------

//        Entry E = q.nnSearch(rTree.root,entries.get(3), 1);
//
//        System.out.println(E.getFeatVec()[0]+" "+E.getFeatVec()[1]);

        /*
        vector[0]=1;
        vector[1]=6;
        entries.add(new Entry(vector.clone(),1,0));

        vector[0]=1;
        vector[1]=4;
        entries.add(new Entry(vector.clone(),1,1));

        vector[0]=3;
        vector[1]=4;
        entries.add(new Entry(vector.clone(),1,2));

        vector[0]=2;
        vector[1]=2;
        entries.add(new Entry(vector.clone(),1,3));

        vector[0]=4;
        vector[1]=3;
        entries.add(new Entry(vector.clone(),1,4));



        for (int i=0; i<entries.size(); i++){
            System.out.println("Entering the : " +i+" st entry.");
            insert.insertData(rTree, rTree.calculateDepth(), entries.get(i));
            rTree.printTree();
        }

        //rTree.printTree();
        System.out.println("Depth: "+rTree.calculateDepth());


        System.out.println("ok");
    */

    }

    public RTree(){
        //
        context = new Context();
        root = null;
    }

    public RTree(TreeNode root, Context context){
        this.root = root;
        this.context = context;
    }

    public TreeNode getRoot(){
        return root;
    }

    public <T extends TreeNode> void setRoot(T newRoot){
        root = newRoot;
    }

    public Context getContext(){
        return context;
    }

    public void setContext(Context context){    // Prefer not to use this
        this.context = context;
    }

    /**
     * This method returns the depth of the tree.
     * It calculates it by traversing all the tree till it
     * reaches a leaf node.
     *
     * @return depth of the R*-tree
     */
    public int calculateDepth() {
        return calculateDepth(root);
    }

    private static int calculateDepth(TreeNode root) {
        if (root == null || root instanceof LeafNode){
            return 0;
        }

        if (root.childrenSize() > 0){
            return calculateDepth(root.child(0), 1);
        }
        return 0;
    }

    private static int calculateDepth(TreeNode node, int depth) {
        if (node instanceof LeafNode) {
            return depth;
        } else {
            return calculateDepth( node.child(0), depth + 1);
        }
    }

    public void save(){
        Saver saver = new Saver();
        if(!saver.saveRtree(this)){
            System.out.println("Save of the Rtree failed.");
        }
    }

    /*
     * Printing a rough blueprint of the R*tree
     */
    private void printTree(){
        System.out.println("  -------------------------------------------------------------------------------");
        System.out.println("Root rectangle:");
        System.out.println("  ----------------");
        TreeNode node = root;
        List<TreeNode> nodes = new ArrayList<>();

        nodes.add(node);

        while (!nodes.isEmpty()){
            node = nodes.get(0);
            if(node instanceof LeafNode){
                node.getRectangle().print();
                System.out.println("Children: "+ node.childrenSize());
                System.out.println("Parent:"+node.getParent());
                System.out.println("Entries Rectangles in leaf:");
                for (int i=0; i<node.childrenSize(); i++){
                    node.entryChild(i).getRectangle().print();
                }
                System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\");
            } else if (node instanceof NotLeafNode){
                node.getRectangle().print();
                System.out.println("Children of Non Leaf Node: "+ node.childrenSize());
                System.out.println("Parent:"+node.getParent());
                for (int i=0; i<node.childrenSize(); i++){
                    nodes.add(node.child(i));
                }
            }
            nodes.remove(node);
        }
    }

}
