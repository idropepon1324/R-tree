import java.util.*;

public class Queries {

    public Queries(){
        //
    }

    /////////////////////--------------------skyline bbs---------------------////////////////////////////


    private List<TreeNode> heapList; //list that i use like a min heap

    private int indexOfMinTreeNodeList(List<TreeNode> treeNodes){

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
            distMin += tnMin.getRectangle().getVector1()[j];
        }
        double dist = 0;
        for(int i=1;i<treeNodes.size();i++){
            for(int j=0;j<tnMin.getRectangle().getVector1().length;j++){
                dist += treeNodes.get(i).getRectangle().getVector1()[j];
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

    /**
     * The skyLineBBS algorithm returns the skyline of the R*-TREE
     *
     * @param root the root node of the tree
     * @return a List of entries which belong to the skyline
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public List<Entry> skyLineBBS(TreeNode root){

        //minHeap = new PriorityQueue<>();

        heapList = new ArrayList<>();
        skyLineList = new ArrayList<>();
        for(int i=0;i<root.childrenSize();i++){
            heapList.add(root.child(i));
        }

        skyLineBBSHelper(heapList,skyLineList);

        return skyLineList;

    }

    private void skyLineBBSHelper(List<TreeNode> heapList,List<Entry> skyLineList){
        TreeNode tn;

        if(heapList.isEmpty()){
            return;
        }

        int index = indexOfMinTreeNodeList(heapList);
        tn = heapList.get(index);
        heapList.remove(index);
        if(tn instanceof LeafNode){
            List<Entry> safeEntries = new ArrayList<>();



            for(int i=0;i<tn.childrenSize();i++){
                boolean putit = true;
                for(int j=0;j<tn.childrenSize();j++){
                    if (j != i){
                        int counter1 = 0;
                        int counter2 = 0;
                        boolean continuetrying = true;
                        for(int k = 0; k < tn.entryChild(i).getFeatVec().length; k++){

                            //aftos o kodikas apodiknii oti kapio allo entryChild katakta i oxi to entryChild(i).
                            //den apodiknii oti to entryChild(i) ine kataktitis
                            if(tn.entryChild(i).getFeatVec()[k] > tn.entryChild(j).getFeatVec()[k]){
                                counter1 += 1;
                            }else if(tn.entryChild(i).getFeatVec()[k] == tn.entryChild(j).getFeatVec()[k]){
                                counter2 += 1;
                            }

                            if(counter1>0 && counter1+counter2==tn.entryChild(i).getFeatVec().length){
                                continuetrying = false;
                                break;
                            }

                        }
                        if(!continuetrying){
                            putit=false;
                            break;
                        }
                    }
                }
                if(putit){
                    safeEntries.add(tn.entryChild(i));
                }
            }

            // Comparing the safe Entries with the Skyline entries
            for(int i=0;i<safeEntries.size();i++){
                boolean putit = true;
                for (int j=0;j<skyLineList.size();j++){
                    int counter1 = 0;
                    int counter2 = 0;
                    boolean continuetrying = true;
                    for(int k = 0; k < tn.entryChild(i).getFeatVec().length; k++){


                        //aftos o kodikas apodiknii oti kapio allo child.rectangle katakta i oxi to child.rectangle(i).
                        //den apodiknii oti to child.rectangle(i) ine kataktitis
                        if(skyLineList.get(j).getFeatVec()[k] < safeEntries.get(i).getFeatVec()[k]){
                            counter1 += 1;
                        }else if(skyLineList.get(j).getFeatVec()[k] == safeEntries.get(i).getFeatVec()[k]){
                            counter2 += 1;
                        }

                        if(counter1>0 && counter1+counter2==tn.entryChild(i).getFeatVec().length){
                            continuetrying = false;
                            break;
                        }

                    }
                    if(!continuetrying){
                        putit=false;
                        break;
                    }
                }
                if(putit) {
                    skyLineList.add(safeEntries.get(i));
                }
            }


            // Compare the skyline entries between themselves
            for(int i=0;i<skyLineList.size();i++){
                boolean putit = true;
                for(int j=0;j<skyLineList.size();j++){
                    if(i==j){
                        continue;
                    }
                    int counter1 = 0;
                    int counter2 = 0;
                    boolean continuetrying = true;
                    for(int k=0;k<skyLineList.get(j).getFeatVec().length;k++){
                        //aftos o kodikas apodiknii oti kapio allo child.rectangle katakta i oxi to child.rectangle(i).
                        //den apodiknii oti to child.rectangle(i) ine kataktitis
                        if(skyLineList.get(j).getFeatVec()[k] < skyLineList.get(i).getFeatVec()[k]){
                            counter1 += 1;
                        }else if(skyLineList.get(j).getFeatVec()[k]  == skyLineList.get(i).getFeatVec()[k]){
                            counter2 += 1;
                        }

                        if(counter1>0 && counter1+counter2==skyLineList.get(i).getFeatVec().length){
                            continuetrying = false;
                            break;
                        }
                    }
                    if(!continuetrying){
                        putit=false;
                        break;
                    }
                }
                if(!putit) {
                    skyLineList.remove(i);
                    i--;
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



                        //aftos o kodikas apodiknii oti kapio allo child.rectangle katakta i oxi to child.rectangle(i).
                        //den apodiknii oti to child.rectangle(i) ine kataktitis
                        if(skyLineList.get(j).getFeatVec()[k] < tn.child(i).getRectangle().getVector1()[k]){
                            counter1 += 1;
                        }else if(skyLineList.get(j).getFeatVec()[k] == tn.child(i).getRectangle().getVector1()[k]){
                            counter2 += 1;
                        }

                        if(counter1>0 && counter1+counter2==tn.child(i).getRectangle().getVector1().length){
                            continuetrying = false;
                            break;
                        }

                    }

                    if(!continuetrying){
                        putit=false;
                        break;
                    }
                }

                //ean to put it kataliksi true tote valto sto heap(den iparxi simio entry pou na to kiriarxi)
                if(putit) {
                    heapList.add(tn.child(i));
                }
            }


        }

        skyLineBBSHelper(heapList,skyLineList);

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
            if(pointDist(p,ens.get(indexMax)) < pointDist(p,ens.get(i))){
                indexMax = i;
            }
        }
        return indexMax;
    }


    Entry e;
    /**
     * Returns a List of K nearest Entries to Entry point
     * @param node node(the root),
     * @param point Entry point
     * @return the nearest entry
     */
    public Entry nnSearch(TreeNode node, Entry point){
        double[] fv = new double[point.getFeatVec().length];
        e = new Entry(fv,1,1);
        for(int i=0;i<point.getFeatVec().length;i++){
            e.getFeatVec()[i] = 9999999;
        }
        nnSearchHelper(node,point);
        return e;
    }

    //initialize nearest = inf
    private void nnSearchHelper(TreeNode node, Entry point){


        TreeNode newNode;
        List<TreeNode> branchList = new ArrayList<>();
        double dist;

        if(node instanceof LeafNode){
            //System.out.println("ine leaf");
            for(int i=0;i<node.childrenSize();i++){
                //System.out.println("tsek nearest with leaf entries" + " " + i);
                if(node.entryChild(i)==point){
                    continue;
                }
                dist = pointDist(point, node.entryChild(i));
                if(dist < pointDist(point,e)){

                    //System.out.println("***vrikame kontinotero***");
                    //System.out.println(node.entryChild(i).getFeatVec()[0]+ " " + node.entryChild(i).getFeatVec()[1]);
                    e = node.entryChild(i);
                }
            }
        }else {
            //System.out.println("den ine leaf");
            genABLandSortIt(point,node,branchList);


            pruneBranchListDownward(node,point,e,branchList);
            //System.out.println("meta to downward emine");
            //for(int i=0;i<branchList.size();i++){
                //System.out.println(branchList.get(i).getRectangle().getVector1()[0] + " " + branchList.get(i).getRectangle().getVector1()[1]);
            //}

            for(int i=0;i<branchList.size();i++){
                //System.out.println("beni edo??");
                newNode = branchList.get(i);

                nnSearchHelper(newNode,point);

                pruneBranchListUpward(node,point,e,branchList);
            }
        }

    }

    private void genABLandSortIt(Entry point,TreeNode node, List<TreeNode> branchList){

        double[] minDists = new double[node.childrenSize()];
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
        branchList.addAll(Arrays.asList(nodes).subList(0, node.childrenSize()));

        //System.out.println("mesa sto bracnh list exoume");
        //for(int i=0;i<branchList.size();i++){
            //System.out.println(branchList.get(i).getRectangle().getVector1()[0]+ " " + branchList.get(i).getRectangle().getVector1()[1]);
        //}
    }

    private void pruneBranchListDownward(TreeNode node, Entry point, Entry Nearest, List<TreeNode> branchList){
        for(int i=0;i<branchList.size();i++){
            double minDist = minDist(point,branchList.get(i).getRectangle());
            for(int j=0;j<branchList.size();j++){
                if(j==i){
                    continue;
                }
                if(minDist > minMaxDist(point,branchList.get(j).getRectangle())){
                    branchList.remove(branchList.get(i));
                    i--;
                }
            }

        }
    }

    private void pruneBranchListUpward(TreeNode node, Entry point, Entry nearest, List<TreeNode> branchList){
        for(int i=0;i<branchList.size();i++){
            double minDist = minDist(point,branchList.get(i).getRectangle());
            if(pointDist(point,nearest)<minDist){
                branchList.remove(branchList.get(i));
                i--;
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






    /////////////////////--------------------K-NN-algorithm---------------------////////////////////////////




    List<Entry> ens;
    Entry point;
    /**
     * Returns a List of K nearest Entries to Entry point
     * @param node the root
     * @param k how many points we search
     * @return a list of k nearest entries
     */
    public List<Entry> knnSearch(TreeNode node, double[] po, int k){
        point = new Entry(po,1,1);
        double[] fv = new double[point.getFeatVec().length];
        ens = new ArrayList<>();
        for(int j=0;j<k;j++) {
            ens.add(new Entry(fv,1,j));
            for (int i = 0; i < point.getFeatVec().length; i++) {
                ens.get(j).getFeatVec()[i] = 9999999;
            }
        }
        knnSearchHelper(node,point,k);
        return ens;
    }

    private void knnSearchHelper(TreeNode node, Entry point,int k){


        TreeNode newNode;
        List<TreeNode> branchList = new ArrayList<>();
        double dist;

        if(node instanceof LeafNode){
            for(int i=0;i<node.childrenSize();i++){
                if(node.entryChild(i).isEqualTo(point)){
                    continue;
                }


                boolean isFull = true;
                for(int j=0;j<ens.size();j++){
                    if(ens.get(j).getFeatVec()[0] == 9999999){
                        isFull = false;
                        break;
                    }
                }


                dist = pointDist(point, node.entryChild(i));
                if(dist < pointDist(point,ens.get(getIndexOfMaxDistOfEntryFromEntriesList(point,ens)))){

                    ens.remove(getIndexOfMaxDistOfEntryFromEntriesList(point,ens));
                    ens.add(node.entryChild(i));

                }else if (!(isFull)){

                    ens.remove(getIndexOfMaxDistOfEntryFromEntriesList(point,ens));
                    ens.add(node.entryChild(i));
                }


            }
        }else {

            //kGenABLandSortIt(point,node,branchList);
            genABLandSortIt(point,node,branchList);

            kPruneBranchListDownward(node,point,ens,branchList);

            shortBranchList(point,branchList);


            for(int i=0;i<branchList.size();i++){

                newNode = branchList.get(i);

                knnSearchHelper(newNode,point,k);

                kPruneBranchListUpward(node,point,ens,branchList,k);
            }
        }

    }


    private void kPruneBranchListUpward(TreeNode node, Entry point, List<Entry> nearest, List<TreeNode> branchList,int k){
        for(int i=0;i<branchList.size();i++){

            double minDist = minDist(point,branchList.get(i).getRectangle());


            //CHECK IF Entries full
            boolean isFull = true;
            for(int j=0;j<nearest.size();j++){
                if(nearest.get(j).getFeatVec()[0] == 9999999){
                    isFull = false;
                }
            }

            if(isFull){

                for(int j=0;j<nearest.size();j++){
                    if(pointDist(point,nearest.get(getIndexOfMaxDistOfEntryFromEntriesList(point,nearest)))<minDist){

                        branchList.remove(i);
                        i--;
                        break;
                    }
                }
            }
        }

    }

    private void kGenABLandSortIt(Entry point,TreeNode node, List<TreeNode> branchList){

        double[] minDists = new double[node.childrenSize()];
        TreeNode[] nodes = new TreeNode[node.childrenSize()];
        for(int i=0;i<node.childrenSize();i++){
            minDists[i] = minDist(point,node.child(i).getRectangle());
            branchList.add(node.child(i));
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

                    temp2 = branchList.get(j-1);
                    branchList.set(j-1,nodes[j]);
                    branchList.set(j,temp2);
                }

            }
        }

        /*
        for(int i=0;i<node.childrenSize();i++){
            branchList.add(nodes[i]);
        }
        */

        //System.out.println("mesa sto bracnh list exoume");
        //for(int i=0;i<branchList.size();i++){
            //System.out.println(branchList.get(i).getRectangle().getVector1()[0]+ " " + branchList.get(i).getRectangle().getVector1()[1]);
       // }
    }


    private void kPruneBranchListDownward(TreeNode node, Entry point, List<Entry> nearest, List<TreeNode> branchList){

        boolean isFull = true;
        for(int j=0;j<nearest.size();j++){
            if(nearest.get(j).getFeatVec()[0] == 9999999){
                isFull = false;
            }
        }
        if(isFull) {
            for (int i = 0; i < branchList.size(); i++) {
                double minDist = minDist(point, branchList.get(i).getRectangle());
                for (int j = 0; j < branchList.size(); j++) {
                    if (j == i) {
                        continue;
                    }
                    if (minDist > minMaxDist(point, branchList.get(j).getRectangle())) {
                        branchList.remove(branchList.get(i));
                        i--;
                        break;
                    }
                }

            /*
            for(int j=0;j<Nearest.leangth){
                if()
            }
            */
            }
        }
    }
    private void shortBranchList(Entry p,List<TreeNode> branchList){


        TreeNode temp1;

        if(branchList.size()<=1){
            return;
        }
        for(int i=0; i < branchList.size(); i++){
            for(int j=1; j < (branchList.size()-i); j++){

                if(minMaxDist(p,branchList.get(j-1).getRectangle()) > minMaxDist(p,branchList.get(j).getRectangle())){
                    //swap elements
                    temp1 = branchList.get(j-1);
                    branchList.set(j-1,branchList.get(j));
                    branchList.set(j,temp1);

                }

            }
        }



    }

    /////////////////////--------------------K-NN-algorithm---------------------////////////////////////////


    /**
     * Linear search of the k nearest neighbour
     * @param entriesOrig the entries
     * @param point the entry we want to find the k nearest
     * @param k how many nearest points we want
     * @return returns the List of the Entries
     */
    public List<Entry> knnSearchLinear(List<Entry> entriesOrig, double[] point, int k){
        List<Entry> entries = new ArrayList<>(entriesOrig);
        List<Entry> nearestEntries = new ArrayList<>();
        Rectangle rect = new Rectangle(point.clone(), point.clone());
        double min;
        Entry minEntry;

        // Search for k points
        for (int i=0; i<k && !entries.isEmpty(); i++){
            min = Utils.distanceRect(entries.get(0).getRectangle(), rect);
            minEntry = entries.get(0);
            for (int j=1; j<entries.size(); j++){
                if (min > Utils.distanceRect(entries.get(j).getRectangle(), rect)){
                    min = Utils.distanceRect(entries.get(j).getRectangle(), rect);
                    minEntry = entries.get(j);
                }
            }

            nearestEntries.add(minEntry);
            entries.remove(minEntry);
        }

        return nearestEntries;
    }


    /**
     * Linear search to find the skyline
     * @param entriesOrig list of entries
     * @return a List of entries
     */
    public List<Entry> skyLineLinear(List<Entry> entriesOrig){
        List<Entry> entries = new ArrayList<>(entriesOrig);
        List<Entry> skyLineEntries = new ArrayList<>();
        Entry entry;
        boolean putit;

        for (int i=0; i<entries.size(); i++){
            entry = entries.get(i);
            putit = true;
            for (int j=0; j<entries.size(); j++){
                // Check for the skyline theory
                int counter1 = 0;
                int counter2 = 0;
                boolean continuetrying = true;
                for(int k=0;k<entries.get(j).getFeatVec().length;k++){
                    if(entries.get(j).getFeatVec()[k] + 180 < entry.getRectangle().getVector1()[k] + 180){
                        counter1 += 1;
                    }else if(entries.get(j).getFeatVec()[k] + 180 == entry.getRectangle().getVector1()[k] + 180){
                        counter2 += 1;
                    }

                    if(counter1>0 && counter1+counter2==entry.getFeatVec().length){
                        continuetrying = false;
                        break;
                    }
                }

                if(!continuetrying){
                    putit=false;
                    //break;
                }
            }

            if(putit) {
                skyLineEntries.add(entry);
            }
        }

        return skyLineEntries;

    }






}
