import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class Main  {
    public static void  main(String[] args)  throws IOException {
        Saver save = new Saver("files\\allData.txt");
//        save.setFile("files/map.osm");
        //System.out.println(save.getFile());

        Loader load = new Loader("files\\map.osm");
        //Loader load = new Loader("files\\tmp.osm");
        List<PosNode<Rectangle>> nodes = load.loadOsmNodes();


        System.out.println(nodes.size());

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


//        Dictionary dictionary = load.loadDictionary();      // Dictionary works
//        dictionary.printDictionary();

        //storageLoader.testing();

        // Block 0 Printing Data
//        DataInfo info = storageLoader.loadStorageInfo();
//        info.printInfo();


        // Testing the printing of the first saved entry after the changes to the code
//        PosNode<Rectangle> node = storageLoader.getStorageNode(2, 0);
//        node.printInfo();

    } // End of main
}
