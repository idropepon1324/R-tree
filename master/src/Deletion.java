import java.util.*;

public class Deletion {


    public Deletion(){

    }



    private List<TreeNode> listOfTn;
    public boolean deleteEntry(TreeNode root,Entry e){
        TreeNode tn = returnTnOfSearchedEntry(root,e);
       listOfTn = new ArrayList<>();
        List<Entry> listOfEn = new ArrayList<>();

        if(tn == null){
            return false;
        }

        if(tn.getParent().getRectangle().isEqualTo(root.getRectangle())){

            for(int i=0;i<tn.childrenSize();i++){
                if(tn.entryChild(i).isEqualTo(e) == false) {
                    listOfEn.add(tn.entryChild(i));
                }
            }

            for(int i=0;i<listOfEn.size();i++){
                //insert listOfEn.get(i);
            }

        }else{
            TreeNode p = tn.getParent();
            if(tn.childrenSize()-1<tn.context().minChildren()){
                p.deleteChild(tn);
                listOfTn.add(tn);
                deleteEntryHelper(root, tn);
            }else {
                fixRects(tn,root);
            }
        }
        return true;
    }

    private List<Entry> entriesHelper;
    private void deleteEntryHelper(TreeNode root, TreeNode tn){

        if(tn.getParent().getRectangle().isEqualTo(root.getRectangle())){

            List<Entry> listOfEn = new ArrayList<>();

            for(int i=0;i<listOfTn.size();i++){
                getEntriesFromTreeNodeThatIsNotLeaf(listOfTn.get(i)); //ta vazoume aftomata stin entriesHelper
            }


            for(int i=0;i<entriesHelper.size();i++){
                //insert entriesHelper.get(i);
            }

        }else{
            TreeNode p = tn.getParent();
            if(tn.childrenSize()-1<tn.context().minChildren()){
                p.deleteChild(tn);
                listOfTn.add(tn);
                deleteEntryHelper(root, tn);
            }else {
                List<Entry> listOfEn = new ArrayList<>();

                for(int i=0;i<listOfTn.size();i++){
                    getEntriesFromTreeNodeThatIsNotLeaf(listOfTn.get(i)); //ta vazoume aftomata stin entriesHelper
                }


                for(int i=0;i<entriesHelper.size();i++){
                    //insert entriesHelper.get(i);
                }
            }
        }
    }


    private void getEntriesFromTreeNodeThatIsNotLeaf(TreeNode tn){

        if(tn instanceof NotLeafNode){
            for(int j=0;j<tn.childrenSize();j++){
                entriesHelper.add(tn.entryChild(j));
            }

        }else {
            for(int i=0;i<tn.childrenSize();i++) {
                getEntriesFromTreeNodeThatIsNotLeaf(tn.child(i));
            }
        }

    }

    //diorthoni ta rectangles anadromika pros ta piso epipeda
    private void fixRects(TreeNode tn,TreeNode root){
        tn.fixRectangle();
        if(tn.getRectangle().isEqualTo(root.getRectangle())){
            return;
        }
        fixRects(tn.getParent(),root);
    }


    /////////////////////---------------- search entry in the tree and return leaf tree node of that entry ----------------/////////////////////
    private Queue<TreeNode> tQueue;
    private boolean exists;
    private TreeNode tnOfDeletion;
    private TreeNode returnTnOfSearchedEntry(TreeNode root, Entry e){
        tQueue = new LinkedList<>();
        exists = false;
        tnOfDeletion = null;
        tQueue.add(root);
        searchEntryHelper(e);
        return tnOfDeletion;

    }

    private void searchEntryHelper(Entry e){


        //an oura keni termatise
        if(tQueue.isEmpty() || exists == true){
            return;
        }

        TreeNode tn = tQueue.poll();

        //an ine leaf tsekare an ine mesa to e
        if(tn instanceof LeafNode){
            for(int i=0;i<tn.childrenSize();i++){
                int counterSimilarities = 0;
                for(int j=0;j<e.getFeatVec().length;j++){
                    if(e.getFeatVec()[j]==tn.entryChild(i).getFeatVec()[j]){
                        counterSimilarities += 1;
                    }else {
                        break;
                    }
                }

                if(counterSimilarities == e.getFeatVec().length){
                    exists = true;
                    tnOfDeletion = tn;
                }
            }
        }else { //an den ine leaf tsekare an tha borouse na ine mesa kai an nai tote valto stin oura
            if(couldBeIn(e,tn)){
                for(int i=0;i<tn.childrenSize();i++){
                    tQueue.add(tn.child(i));
                }
            }
        }
        searchEntryHelper(e);
        return;

    }

    private boolean couldBeIn(Entry e,TreeNode tn){
        int counter1 = 0;
        int counter2 = 0;
        for(int i=0;i<e.getFeatVec().length;i++){
            if(e.getFeatVec()[i]>=tn.getRectangle().getVector1()[i]){
                counter1 += 1;
            }else{
                break;
            }
            if(e.getFeatVec()[i]<=tn.getRectangle().getVector2()[i]){
                counter2 += 1;
            }else {
                break;
            }

        }

        if (counter1==e.getFeatVec().length && counter2==e.getFeatVec().length){
            return true;
        }else {
            return false;
        }

    }
    /////////////////////---------------- search entry in the tree and return leaf tree node of that entry ----------------/////////////////////

}
