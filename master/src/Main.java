import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This is the main that creates the Pos Nodes from the osm file
 * and then creates the R*Tree index.
 */
public class Main  {
    public static void  main(String[] args)  throws IOException {
        Saver save = new Saver("files\\allData.txt");
//        save.setFile("files/map.osm");
        //System.out.println(save.getFile());

        Loader load = new Loader("files\\map.osm");
        //Loader load = new Loader("files\\tmp.osm");
        List<PosNode<Rectangle>> nodes = load.loadOsmNodes();


        //System.out.println(nodes.size());

        // Prints the nodes to be saved
//        for(PosNode<Rectangle> n: nodes){
//            n.printInfo();
//        }

        // Save the nodes to the Organised(in blocks of 32KB) file
        if (save.saveNodes(nodes)){
            System.out.println("Successfully Saved!\n");
        } else {
            System.out.println("Error Occurred during saving!\n");
        }

        // Load saved nodes
        Loader storageLoader = new Loader("files\\allData.txt");
        List<PosNode<Rectangle>> storageNodes = storageLoader.loadStorageNodes();

        // Print the stored Nodes
//        for(PosNode<Rectangle> n: storageNodes){
//            n.printInfo();
//        }

        // Check for saved nodes that are different from the initial batch
//        for (int i=0; i<nodes.size(); i++){
//            if (!nodes.get(i).equals(storageNodes.get(i)))
//            {
//                System.out.println("Equality "+i+" false");
//            }
//
//        }


        Dictionary dictionary = load.loadDictionary();      // Dictionary works
//        dictionary.printDictionary();

        //storageLoader.testing();

        // Block 0 Printing Data
//        DataInfo info = storageLoader.loadStorageInfo();
//        info.printInfo();


        // Testing the printing of the first saved entry after the changes to the code
//        PosNode<Rectangle> node = storageLoader.getStorageNode(2, 0);
//        node.printInfo();

        /*
         * Initialization of all the needed variables to do the Comparing of the times
         * with the osm file found in e-learning.
         * PosNode are saved in the disk. We create the entries representing each positional
         * node in the R* tree.
         */
        Context contextRoot = new Context(2,5,2);
        Insert insert = new Insert();
        RTree rTree = new RTree(null, contextRoot);
        List<Entry> entries = new ArrayList<>();
        Rectangle area;
        int blockCounter, counter = 0;

        for (blockCounter=1; blockCounter<storageNodes.size(); blockCounter++){
            for (int k=0; k<dictionary.getEntrySize(blockCounter); k++){
                entries.add(new Entry(storageNodes.get(counter).getFeatVec().clone(), blockCounter, k));
                counter += 1;
            }
        }

        for (Entry entry : entries) {
            //System.out.println(i);
            //entries.get(i).getRectangle().print();
            insert.insertData(rTree, rTree.calculateDepth(), entry);
        }

        // Save the rTree to a file
        rTree.save();

        double[] vec1 = new double[2];
        double[] vec2 = new double[2];
        vec1[0] = -40.0;
        vec1[1] = -40.0;
        vec2[0] = 40.0;
        vec2[1] = 40.0;
        area = new Rectangle(vec1.clone(), vec2.clone());
        double[] point = vec2.clone();
        int k = 10;

        // Compare execution times
        System.out.println("Entries size: "+entries.size());
        System.out.println("Depth of the R* tree: "+ rTree.calculateDepth());
        ExecutionTimes.insertionOneByOne(entries);
        ExecutionTimes.insertionBottomUp(entries, contextRoot);
        ExecutionTimes.areaSearchWithIndex(rTree, area);
        ExecutionTimes.areaSearchWithLinear(entries, area);
        ExecutionTimes.knnSearchWithIndex(rTree, point, k);
        ExecutionTimes.knnSearchWithLinear(entries, point, k);
        ExecutionTimes.skylineWithIndex(rTree);
        ExecutionTimes.skylineWithLinear(entries);

        // Testing times with Random entries.
        //testingRandom(10000, 2);


    } // End of main

    /**
     * This method is used to test time for various amounts of entries and
     * dimensions too.
     * Random entries in the range of [-180, 180]
     * @param amount number of entries
     * @param dimensions the dimensions of each entry
     */
    private static void testingRandom(int amount, int dimensions ){
        Context contextRoot = new Context(2,5,2);
        Insert insert = new Insert();
        RTree rTree = new RTree(null, contextRoot);
        double[] vector = new double[dimensions];
        double[] vector2 = new double[dimensions];
        Random r = new Random();
        List<Entry> entries = new ArrayList<>();
        int k = 10;
        double[] point = new double[dimensions];
        Rectangle rangeQuery;

        for (int j=0; j<dimensions; j++){
            vector[j] = -40.0;
            vector2[j] = 40.0;
        }
        rangeQuery = new Rectangle(vector.clone(), vector2.clone());

        for (int i=0; i<amount; i++){
            for (int j=0; j<dimensions; j++){
                vector[j] = -180 + (180 + 180) * r.nextDouble();
            }
            entries.add(new Entry( vector.clone(), 1, i));
        }

        for (Entry entry : entries) {
            insert.insertData(rTree, rTree.calculateDepth(), entry);
        }

        // Compare execution times
        System.out.println("Entries size: "+entries.size());
        System.out.println("Depth of the R* tree: "+ rTree.calculateDepth());
        ExecutionTimes.insertionOneByOne(entries);
        ExecutionTimes.insertionBottomUp(entries, contextRoot);
        ExecutionTimes.areaSearchWithIndex(rTree, rangeQuery);
        ExecutionTimes.areaSearchWithLinear(entries, rangeQuery);

        ExecutionTimes.knnSearchWithIndex(rTree, point, k);
        ExecutionTimes.knnSearchWithLinear(entries, point, k);


    }
}
