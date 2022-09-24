import java.util.*;

public class Queries {


    private Optional<? extends TreeNode> root;



    public Queries(){
        root = null;
    }

    /////////////////////--------------------skyline bbs---------------------////////////////////////////
    //min heap for bbs
    private PriorityQueue<TreeNode> minHeap = new PriorityQueue<TreeNode>(new Comparator<TreeNode>() {
        @Override
        public int compare(TreeNode o1, TreeNode o2) {
            double distO1 = 0;
            double distO2 = 0;
            for(int i=0;i<o1.getRectangle().getVector1().length;i++){
                distO1 += o1.getRectangle().getVector1()[i];
                distO2 += o2.getRectangle().getVector1()[i];
            }
            return - Double.compare(distO1,distO2);
        }
    });
    //list for skyline bbs
    private List<Entry> skyLineList;

    private List<Entry> skyLineBBS(TreeNode root){

        skyLineList = new ArrayList<>();
        for(int i=0;i<root.childrenSize();i++){
            minHeap.add(root.child(i));
        }

        skyLineBBSHelper(minHeap,skyLineList);

        return skyLineList;

    }

    private List<Entry> skyLineBBSHelper(PriorityQueue<TreeNode> minHeap,List<Entry> skyLineList){
        TreeNode tn;

        if(minHeap.isEmpty()){
            return skyLineList;
        }

        tn = minHeap.poll();
        if(tn instanceof LeafNode){
            Entry minEnt = tn.entryChild(0);
            List<Entry> safeEntries = new ArrayList<Entry>();



            for(int i=0;i<tn.childrenSize();i++){
                boolean putit = true;
                for(int j=0;j<=tn.childrenSize();j++){
                    if (j == i){
                        continue;
                    }else{
                        int counter1 = 0;
                        int counter2 = 0;
                        boolean continuetrying = true;
                        for(int k = 0; k < tn.entryChild(i).getFeatVec().length; k++){

                            if(counter1>0 && counter1+counter2==skyLineList.get(j).getFeatVec().length){
                                continuetrying = false;
                                break;
                            }

                            if(tn.entryChild(i).getFeatVec()[k]<tn.entryChild(i).getFeatVec()[k]){
                                counter1 += 1;
                            }else if(tn.entryChild(i).getFeatVec()[k]==tn.entryChild(i).getFeatVec()[k]){
                                counter2 += 1;
                            }
                        }
                    }
                }
                if(putit==true){
                    safeEntries.add(tn.entryChild(i));
                }
            }


            for(int i=0;i<safeEntries.size();i++){
                boolean putit = true;
                for (int j=0;j<=skyLineList.size();j++){
                    int counter1 = 0;
                    int counter2 = 0;
                    boolean continuetrying = true;
                    for(int k = 0; k < tn.entryChild(i).getFeatVec().length; k++){
                        if(counter1>0 && counter1+counter2==skyLineList.get(j).getFeatVec().length){
                            continuetrying = false;
                            break;
                        }

                        if(skyLineList.get(j).getFeatVec()[k]<safeEntries.get(i).getFeatVec()[k]){
                            counter1 += 1;
                        }else if(skyLineList.get(j).getFeatVec()[k]==safeEntries.get(i).getFeatVec()[k]){
                            counter2 += 1;
                        }
                    }
                    if(continuetrying==false){
                        putit=false;
                        break;
                    }
                }
                if(putit==true) {
                    skyLineList.add(safeEntries.get(i));
                }
            }

        }else {

            for(int i=0;i<tn.childrenSize();i++){     //trexa ta pedia
                boolean putit = true;
                for(int j=0;j<skyLineList.size();j++){ //tsekare an to sigekrimeno pedi bori na bi sto heap i an iparxi kapio simio pou to kiriarxi
                    int counter1 = 0;
                    int counter2 = 0;
                    boolean continuetrying = true;
                    for(int k=0;k<skyLineList.get(j).getFeatVec().length;k++){ //kane to tsek gia ola ta vectors

                        if(counter1>0 && counter1+counter2==skyLineList.get(j).getFeatVec().length){
                            continuetrying = false;
                            break;
                        }

                        if(skyLineList.get(j).getFeatVec()[k]<tn.child(i).getRectangle().getVector1()[k]){
                            counter1 += 1;
                        }else if(skyLineList.get(j).getFeatVec()[k]==tn.child(i).getRectangle().getVector1()[k]){
                            counter2 += 1;
                        }

                    }

                    if(continuetrying==false){
                        putit=false;
                        break;
                    }
                }

                //ean to put it kataliksi true tote valto sto heap(den iparxi simio entry pou na to kiriarxi)
                if(putit==true) {
                    minHeap.add(tn.child(i));
                }
            }

        }

        skyLineBBSHelper(minHeap,skyLineList);

        return null;
    }
    /////////////////////--------------------skyline bbs---------------------////////////////////////////

    /////////////////////--------------------range queries---------------------////////////////////////////




}
