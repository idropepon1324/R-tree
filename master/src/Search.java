import java.util.*;

public class Search {



    public Search(){

    }

    /////////////////////---------------- search entries rectangle ----------------/////////////////////
    private List<Entry> finalEntries;
    private Queue<TreeNode> tnQueue;

    /**
     * search an area(rectangle)
     * @param root the root of the r*-tree
     * @param rect the area(rectangle) we want to search
     * @return the list of entries in the area(rectangle)
     */
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

        return counter1 == e.getFeatVec().length && counter2 == e.getFeatVec().length;

    }
    /////////////////////---------------- search entries rectangle ----------------/////////////////////




    /////////////////////---------------- search entry in the tree ----------------/////////////////////
    private Queue<TreeNode> tQueue;
    private boolean exists;

    /**
     * search an entry in the R*TREE
     * @param root the root of the tree
     * @param e the entry we want to search
     * @return true if the entry exists, else false
     */
    public boolean searchEntry(TreeNode root, Entry e){
        tQueue = new LinkedList<>();
        exists = false;
        tnQueue = new LinkedList<>();
        tQueue.add(root);
        searchEntryHelper(e);

        return exists;

    }

    private void searchEntryHelper(Entry e){


        //an oura keni termatise
        if(tQueue.isEmpty() || exists){
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

        return counter1 == e.getFeatVec().length && counter2 == e.getFeatVec().length;

    }
    /////////////////////---------------- search entry in the tree ----------------/////////////////////

    /**
     * This method searches through a list of Entries to find the entries that are
     * included in the selected area/rectangle. Algorithm linear search: O(n)
     * @param entries a List of entries
     * @param area a Rectangle
     * @return a List of entries
     */
    public List<Entry> searchAreaLinear(List<Entry> entries, Rectangle area){
        List<Entry> foundEntries = new ArrayList<>();

        for (Entry entry : entries) {
            if (area.contains(entry.getVector())) {
                foundEntries.add(entry);
            }
        }

        return foundEntries;
    }
}