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


    public Queue<TreeNode> tQueue;
    public boolean entryExists;
    public boolean searchEntry(TreeNode root, Entry e){
        entryExists = false;




    }

    private void searchEntry(Entry e){
        TreeNode tn = tQueue.poll();

        //an oura keni termatise
        if(tQueue.isEmpty()){
            return;
        }

        //an ine leaf tsekare an ine mesa to e
        if(tn instanceof LeafNode){

        }else { //an den ine leaf tsekare an tha borouse na ine mesa kai an nai tote valto stin oura
            if(couldBeIn(tn,e)){
                for(int i=0;i<tn.childrenSize();i++){
                    tQueue.add(tn.child(i));
                }
            }else {
                searchEntry(e);
            }
        }
    }

    private boolean couldBeIn(TreeNode tn, Entry e){
        int counter1 = 0;
        int counter2 = 0;
        for(int i=0;i<e.getFeatVec().length;i++){
            if(e.getFeatVec()[i]>=tn.getRectangle().getVector1()[i]){
                counter1 += 1;
            }else{
                break;
            }
            if(e.getFeatVec()[i]<=tn.getRectangle().getVector2()[i]){
                counter1 += 1;
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
}
