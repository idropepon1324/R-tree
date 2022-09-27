import javax.swing.text.AbstractDocument;
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

        minHeap = new PriorityQueue<>();
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






    /////////////////////--------------------NN-algorithm---------------------////////////////////////////

    private double minDist(Entry e, Rectangle r){
        double dist = 0;
        for(int i=0;i<e.getFeatVec().length;i++){
            if(e.getFeatVec()[i] < r.getVector1()[i]){
                dist += Math.pow(r.getVector1()[i] - e.getFeatVec()[i],2);
            }else if(e.getFeatVec()[i] < r.getVector2()[i]){
                dist += Math.pow(r.getVector2()[i] - e.getFeatVec()[i],2);
            }
        }
        return dist;
    }

    private double minMaxDist(Entry e, Rectangle r){
        double[] dist = new double[e.getFeatVec().length];
        for(int k=0;k<e.getFeatVec().length;k++){
            double rmk;
            if(e.getFeatVec()[k] <= (r.getVector1()[k]+r.getVector2()[k])/2){
                rmk = r.getVector1()[k];
            }else{
                rmk = r.getVector2()[k];
            }


            double rMi = 0;
            for(int i=0;i<e.getFeatVec().length;i++){
                if(k==i){
                    continue;
                }
                if (e.getFeatVec()[i] >= (r.getVector1()[i]+r.getVector2()[i])/2){
                    rMi += Math.pow(e.getFeatVec()[i] - r.getVector1()[i],2);
                }else {
                    rMi += Math.pow(e.getFeatVec()[i] - r.getVector2()[i],2);
                }
            }

            dist[k] = Math.pow(e.getFeatVec()[k]-rmk,2) + rMi;
        }

        double min = dist[0];
        for(int i=1;i<e.getFeatVec().length;i++){
            if(dist[i]<min){
                min = dist[i];
            }
        }
        return min;
    }


    public void nnSearch(TreeNode node, Entry point, Entry nearest){

        TreeNode newNode;
        List<TreeNode> branchList = new ArrayList<>();
        double dist, last;

        if(node instanceof LeafNode){
            for(int i=0;i<node.childrenSize();i++){
                dist = pointDist(point, node.entryChild(i));
                if(dist < pointDist(point,nearest)){
                    nearest = node.entryChild(i);
                }
            }
        }else {
            genABLandSortIt(point,node,branchList);

            pruneBranchListDownward(node,point,nearest,branchList);

            for(int i=0;i<branchList.size();i++){
                newNode = branchList.get(i);

                nnSearch(newNode,point,nearest);

                pruneBranchListUpward(node,point,nearest,branchList);
            }
        }
    }

    private void genABLandSortIt(Entry point,TreeNode node, List<TreeNode> branchList){

        double minDists[] = new double[node.childrenSize()];
        TreeNode[] nodes = new TreeNode[node.childrenSize()];
        for(int i=0;i<node.childrenSize();i++){
            minDists[i] = minDist(point,node.child(i).getRectangle());
            nodes[i] = node.child(i);
        }

        double temp1;
        TreeNode temp2;
        for(int i=0; i < node.childrenSize(); i++){
            for(int j=1; j < (node.childrenSize()-i); j++){
                if(minDists[j-1] > minDists[j]){
                    //swap elements
                    temp1 = minDists[j-1];
                    minDists[j-1] = minDists[j];
                    minDists[j] = temp1;

                    temp2 = nodes[j-1];
                    nodes[j-1] = nodes[j];
                    nodes[j] = temp2;
                }

            }
        }
        for(int i=0;i<node.childrenSize();i++){
            branchList.add(nodes[i]);
        }
    }

    private void pruneBranchListDownward(TreeNode node, Entry point, Entry Nearest, List<TreeNode> branchList){
        for(int i=0;i<node.childrenSize();i++){
            double minDist = minDist(point,node.child(i).getRectangle());
            for(int j=0;j<node.childrenSize();j++){
                if(j==i){
                    continue;
                }
                if(minDist > minMaxDist(point,node.child(j).getRectangle())){
                    branchList.remove(node.child(i));
                }
            }
        }
    }

    private void pruneBranchListUpward(TreeNode node, Entry point, Entry nearest, List<TreeNode> branchList){
        for(int i=0;i<node.childrenSize();i++){
            double minDist = minDist(point,node.child(i).getRectangle());
            if(pointDist(point,nearest)<minDist){
                branchList.remove(node.child(i));
            }
        }

    }

    private double pointDist(Entry p1, Entry p2){
        double dist = 0;
        for(int i=0;i<p1.getFeatVec().length;i++){
            dist += Math.pow(p1.getFeatVec()[i]-p2.getFeatVec()[i],2);
        }
        return Math.sqrt(dist);
    }

    /////////////////////--------------------NN-algorithm---------------------////////////////////////////

}
