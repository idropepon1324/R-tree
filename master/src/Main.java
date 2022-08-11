import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.List;

public class Main  {
    public static void  main(String[] args)  throws IOException {
        Saver save = new Saver("files\\allData.txt");
//        save.setFile("files/map.osm");
        //System.out.println(save.getFile());

        //Loader load = new Loader("files\\map.osm");
        Loader load = new Loader("files\\tmp.osm");
        List<PosNode<Rectangle>> nodes = load.loadOsmNodes();
        System.out.println(nodes.size());
        for(PosNode<Rectangle> n: nodes){
            n.printInfo();
        }

        if (save.saveNodes(nodes)){
            System.out.println("Successfully Saved!\n");
        } else {
            System.out.println("Error Occurred during saving!\n");
        }

        Loader storageLoader = new Loader("files\\allData.txt");
        List<PosNode<Rectangle>> storageNodes = storageLoader.loadStorageNodes();

        for(PosNode<Rectangle> n: storageNodes){
            n.printInfo();
        }

        for (int i=0; i<nodes.size(); i++){
            if (!nodes.get(i).equals(storageNodes.get(i)))
            {
                System.out.println("Equality "+i+" false");
            }

        }
    }
}
