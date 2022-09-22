import java.util.List;
import java.util.Queue;

public class Search {

    private List<Entry> finalEntries;
    private List<TreeNode> TreeNodes;

    public Search(TreeNode root, Entry e){

    }

    public List<Entry> searchArea(TreeNode root, Rectangle rect){



        searchAreaHelper(finalEntries,TreeNodes,rect);


        return null;
    }


    private boolean isOverlaping(Rectangle r1,Rectangle r2){
        boolean overlaps = true;
        for (int i=0;i<r1.getSize();i++){
            if(r1.getVector1()[i]<=r2.getVector1()[i]){

            }
        }
        return true;
    }






    /////////////////////---------------- search entry in the tree ----------------/////////////////////
    private Queue<TreeNode> tQueue;
    private boolean exists;
    public boolean searchEntry(TreeNode root, Entry e){
        exists = false;

        tQueue.add(root);
        searchEntryHelper(e);
        if(exists==true) {
            return true;
        }else {
            return false;
        }

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
    /////////////////////---------------- search entry in the tree ----------------/////////////////////
}
