import javax.swing.text.AbstractDocument;
import java.util.*;

public class Queries {


    private Optional<? extends TreeNode> root;



    public Queries(){
        root = null;
    }

    /////////////////////--------------------skyline bbs---------------------////////////////////////////


    private List<TreeNode> heapList; //list that i use like a min heap

    private int indexOfMinTreeNodeList(List<TreeNode> treeNodes){
        List<TreeNode> newTns;

        for(int i=0;i<treeNodes.size();i++){
            for(int j=0;j<treeNodes.get(0).getRectangle().getVector1().length;j++){
                treeNodes.get(i).getRectangle().getVector1()[j] += 180;
                treeNodes.get(i).getRectangle().getVector2()[j] += 180;
            }
        }

        int indexMin = 0;
        TreeNode tnMin = treeNodes.get(0);
        double distMin = 0;
        for(int j=0;j<tnMin.getRectangle().getVector1().length;j++){
            distMin += tnMin.getRectangle().getVector1().length;
        }
        double dist = 0;
        for(int i=1;i<treeNodes.size();i++){
            for(int j=0;j<tnMin.getRectangle().getVector1().length;j++){
                dist += treeNodes.get(i).getRectangle().getVector1().length;
            }
            if(distMin>dist){
                distMin = dist;
                indexMin = i;
                tnMin = treeNodes.get(i);

            }
        }

        for(int i=0;i<treeNodes.size();i++){
            for(int j=0;j<treeNodes.get(0).getRectangle().getVector1().length;j++){
                treeNodes.get(i).getRectangle().getVector1()[j] -= 180;
                treeNodes.get(i).getRectangle().getVector2()[j] -= 180;
            }
        }

        return indexMin;
    }


    //list for skyline bbs
    private List<Entry> skyLineList;

    public List<Entry> skyLineBBS(TreeNode root){

        //minHeap = new PriorityQueue<>();

        heapList = new ArrayList<>();
        skyLineList = new ArrayList<>();
        for(int i=0;i<root.childrenSize();i++){
            heapList.add(root.child(i));
        }

        System.out.println("first item of heap " + heapList.get(indexOfMinTreeNodeList(heapList)).getRectangle().getVector1()[0] + " " + heapList.get(indexOfMinTreeNodeList(heapList)).getRectangle().getVector1()[1]);
        skyLineBBSHelper(heapList,skyLineList);

        return skyLineList;

    }

    private void skyLineBBSHelper(List<TreeNode> heapList,List<Entry> skyLineList){
        TreeNode tn;
        System.out.println("--------------------new round-----------------------");

        if(heapList.isEmpty()){
            System.out.println("heap goy empty");
            return;
        }

        int index = indexOfMinTreeNodeList(heapList);
        tn = heapList.get(index);
        System.out.println("pirame to proto element " + heapList.get(indexOfMinTreeNodeList(heapList)).getRectangle().getVector1()[0] + " " + heapList.get(indexOfMinTreeNodeList(heapList)).getRectangle().getVector1()[1]);
        heapList.remove(index);

        if(tn instanceof LeafNode){
            System.out.println("ftasame se leaf node");

            for (int i=0;i<heapList.size();i++){
                System.out.println("sto heap exoume " + i + " " + heapList.get(i).getRectangle().getVector1()[0] + " " + heapList.get(i).getRectangle().getVector1()[1]);
            }

            for (int i=0;i<skyLineList.size();i++){
                System.out.println("sto skiline exoume " + i + " " + skyLineList.get(i).getRectangle().getVector1()[0] + " " + skyLineList.get(i).getRectangle().getVector1()[1]);
            }


            Entry minEnt = tn.entryChild(0);
            List<Entry> safeEntries = new ArrayList<Entry>();



            for(int i=0;i<tn.childrenSize();i++){
                boolean putit = true;
                for(int j=0;j<tn.childrenSize();j++){
                    if (j == i){
                        continue;
                    }else{
                        int counter1 = 0;
                        int counter2 = 0;
                        boolean continuetrying = true;
                        for(int k = 0; k < tn.entryChild(i).getFeatVec().length; k++){

                            //aftos o kodikas apodiknii oti kapio allo entryChild katakta i oxi to entryChild(i).
                            //den apodiknii oti to entryChild(i) ine kataktitis
                            if(tn.entryChild(i).getFeatVec()[k] + 180 > tn.entryChild(j).getFeatVec()[k] + 180){
                                counter1 += 1;
                            }else if(tn.entryChild(i).getFeatVec()[k] + 180 == tn.entryChild(j).getFeatVec()[k] + 180){
                                counter2 += 1;
                            }

                            if(counter1>0 && counter1+counter2==tn.entryChild(i).getFeatVec().length){
                                continuetrying = false;
                                break;
                            }

                        }
                        if(continuetrying==false){
                            System.out.println("afto to antikimeno kataktite");
                            putit=false;
                            break;
                        }
                    }
                }
                if(putit==true){
                    safeEntries.add(tn.entryChild(i));
                }
            }

            for (int i=0;i<safeEntries.size();i++){
                System.out.println("sto safeEntries exoume " + i + " " + safeEntries.get(i).getRectangle().getVector1()[0] + " " + safeEntries.get(i).getRectangle().getVector1()[1]);
            }

            for(int i=0;i<safeEntries.size();i++){
                boolean putit = true;
                for (int j=0;j<skyLineList.size();j++){
                    int counter1 = 0;
                    int counter2 = 0;
                    boolean continuetrying = true;
                    for(int k = 0; k < tn.entryChild(i).getFeatVec().length; k++){


                        //aftos o kodikas apodiknii oti kapio allo child.rectangle katakta i oxi to child.rectangle(i).
                        //den apodiknii oti to child.rectangle(i) ine kataktitis
                        if(skyLineList.get(j).getFeatVec()[k] + 180 < safeEntries.get(i).getFeatVec()[k] + 180){
                            counter1 += 1;
                        }else if(skyLineList.get(j).getFeatVec()[k] + 180 == safeEntries.get(i).getFeatVec()[k] + 180){
                            counter2 += 1;
                        }

                        if(counter1>0 && counter1+counter2==tn.entryChild(i).getFeatVec().length){
                            continuetrying = false;
                            break;
                        }

                    }
                    if(continuetrying==false){
                        System.out.println("afto to antikimeno kataktite");
                        putit=false;
                        break;
                    }
                }
                if(putit==true) {
                    skyLineList.add(safeEntries.get(i));
                }
            }

        }else {

            System.out.println("den imaste se leafnode");

            for (int i=0;i<heapList.size();i++){
                System.out.println("sto heap exoume " + i + " " + heapList.get(i).getRectangle().getVector1()[0] + " " + heapList.get(i).getRectangle().getVector1()[1]);
            }

            for (int i=0;i<skyLineList.size();i++){
                System.out.println("sto skiline exoume " + i + " " + skyLineList.get(i).getRectangle().getVector1()[0] + " " + skyLineList.get(i).getRectangle().getVector1()[1]);
            }

            for(int i=0;i<tn.childrenSize();i++){     //trexa ta pedia
                boolean putit = true;
                for(int j=0;j<skyLineList.size();j++){ //tsekare an to sigekrimeno pedi bori na bi sto heap i an iparxi kapio simio pou to kiriarxi
                    int counter1 = 0;
                    int counter2 = 0;
                    boolean continuetrying = true;
                    for(int k=0;k<skyLineList.get(j).getFeatVec().length;k++){ //kane to tsek gia ola ta vectors



                        //aftos o kodikas apodiknii oti kapio allo child.rectangle katakta i oxi to child.rectangle(i).
                        //den apodiknii oti to child.rectangle(i) ine kataktitis
                        if(skyLineList.get(j).getFeatVec()[k] + 180 < tn.child(i).getRectangle().getVector1()[k] + 180){
                            counter1 += 1;
                        }else if(skyLineList.get(j).getFeatVec()[k] + 180 == tn.child(i).getRectangle().getVector1()[k] + 180){
                            counter2 += 1;
                        }

                        if(counter1>0 && counter1+counter2==tn.entryChild(i).getFeatVec().length){
                            continuetrying = false;
                            break;
                        }

                    }

                    if(continuetrying==false){
                        putit=false;
                        break;
                    }
                }

                //ean to put it kataliksi true tote valto sto heap(den iparxi simio entry pou na to kiriarxi)
                if(putit==true) {
                    heapList.add(tn.child(i));
                }
            }

        }

        skyLineBBSHelper(heapList,skyLineList);

        return;
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

    private int getIndexOfMaxDistOfEntryFromEntriesList(Entry p, List<Entry> ens){
        int indexMax = 0;
        for(int i=1;i<ens.size();i++){
            if(pointDist(p,ens.get(indexMax)) > pointDist(p,ens.get(i))){
                indexMax = i;
            }
        }
        return indexMax;
    }


    public Entry nnSearch(TreeNode node, Entry point){
        double[] fv = new double[point.getFeatVec().length];
        Entry e = new Entry(fv,1,1);
        for(int i=0;i<point.getFeatVec().length;i++){
            e.getFeatVec()[i] = Integer.MAX_VALUE;
        }
        return nnSearchHelper(node,point,e);
    }

    //initialize nearest = inf
    private Entry nnSearchHelper(TreeNode node, Entry point, Entry nearest){

        System.out.println("-------------new round---------------------");

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

                nnSearchHelper(newNode,point,nearest);

                pruneBranchListUpward(node,point,nearest,branchList);
            }
        }
        return nearest;
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

            /*
            for(int j=0;j<Nearest.leangth){
                if()
            }
            */
        }
    }

    private void pruneBranchListUpward(TreeNode node, Entry point, Entry nearest, List<TreeNode> branchList){
        for(int i=0;i<branchList.size();i++){
            double minDist = minDist(point,branchList.get(i).getRectangle());
            if(pointDist(point,nearest)<minDist){
                branchList.remove(branchList.get(i));
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
