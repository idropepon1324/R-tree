import java.util.*;

public class Search {



    public Search(){

    }

    /////////////////////---------------- search entries rectangle ----------------/////////////////////
    private List<Entry> finalEntries;
    private Queue<TreeNode> tnQueue;
    public List<Entry> searchArea(TreeNode root, Rectangle rect){

        finalEntries = new ArrayList<>();
        tnQueue = new LinkedList<>();
        tnQueue.add(root);
        //System.out.println("edo vlepo an ine empty" + tnQueue.peek().getRectangle().getVector1()[0]);
        searchAreaHelper(rect);
        return finalEntries;
    }
    private void searchAreaHelper(Rectangle rect){

        if(tnQueue.isEmpty()){
            return;
        }

        TreeNode tn = tnQueue.poll();

        if(tn instanceof LeafNode){
            for(int i=0;i<tn.childrenSize();i++){
                if(entryInRect(tn.entryChild(i),rect)){
                    finalEntries.add(tn.entryChild(i));
                }
            }
        }else {
            if(isOverlapping(rect,tn.getRectangle())){
                for(int i=0;i<tn.childrenSize();i++){
                    tnQueue.add(tn.child(i));
                }
            }
        }

        searchAreaHelper(rect);

    }

    private boolean isOverlapping(Rectangle r1,Rectangle r2){
        for (int i=0;i<r1.getSize();i++){
            if(r1.getVector1()[i]>r2.getVector2()[i] || r2.getVector1()[i]>r1.getVector2()[i]){
                return false;
            }
        }
        return true;
    }

    private boolean entryInRect(Entry e,Rectangle rect){
        int counter1 = 0;
        int counter2 = 0;
        for(int i=0;i<e.getFeatVec().length;i++){
            if(e.getFeatVec()[i]>=rect.getVector1()[i]){
                counter1 += 1;
            }else{
                break;
            }
            if(e.getFeatVec()[i]<=rect.getVector2()[i]){
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
    /////////////////////---------------- search entries rectangle ----------------/////////////////////




    /////////////////////---------------- search entry in the tree ----------------/////////////////////
    private Queue<TreeNode> tQueue;
    private boolean exists;
    public boolean searchEntry(TreeNode root, Entry e){
        tQueue = new LinkedList<>();
        exists = false;
        tnQueue = new LinkedList<>();
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