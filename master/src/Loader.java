import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.lang.reflect.Array;
import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static java.lang.System.exit;

public class Loader extends FileManagement{
    // String file

    public Loader(){
        //
    }

    public Loader(String file){
        super(file);
    }

    public <T extends Geometry> List<PosNode<T>> loadOsmNodes(){
        List<PosNode<T>> nodes = new ArrayList<PosNode<T>>();
        NodeList nList = null;
        try {
            DocumentBuilderFactory dbFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuild = dbFact.newDocumentBuilder();
            Document doc = dBuild.parse(super.getFile());
            nList = doc.getElementsByTagName("node");

            //nodes = new PosNode[nList.getLength()];

            for(int i=0;i<nList.getLength();i++){
                Node nNode = nList.item(i);
                if(nNode.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) nNode;
                    String id = eElement.getAttribute("id");
                    double lan = Float.parseFloat(eElement.getAttribute("lat"));
                    double lon = Float.parseFloat(eElement.getAttribute("lon"));
                    String name = nNode.getNodeName() + "_" + (i);

                    double[] vec = new double[2];
                    vec[0] = lan;
                    vec[1] = lon;
                    PosNode node = new PosNode(id,name,vec,Options.DIMENSIONS);
                    //node.printInfo();

                    //nodes[i] = node;
                    nodes.add(node);
                }
            }
        } catch (Exception e){
            System.out.println(e);
        }

        return nodes;
    }

    // Out of use
    public <T extends Geometry> List<PosNode<T>> loadStorageNodes2(){
        List<PosNode<T>> nodeList = new ArrayList<>();
        Dictionary dictionary = loadDictionary();
        int blockCounter = 1;       // Change this to 0 when i develop the block0

        try {
            InputStream is = new FileInputStream(super.getFile());


            //System.out.println(b.length);

            int nodesPerBlock = dictionary.getEntrySize(blockCounter);
            while (nodesPerBlock>0){    // getEntrySize() returns -1 when the index is out of bound
                System.out.println("Times ins here+==============================+");   // 2 times
                System.out.println("nodes per Block: "+nodesPerBlock);

                byte[] b = is.readNBytes(Options.BLOCK_SIZE);           // Read the Block               Problem read wrong second block... Saving problem or loading one
                //byte[] c= is.readNBytes(Options.BLOCK_SIZE);
                for(int i=0; i<b.length;i++){
                    System.out.print(b[i]);
                }
                System.out.println();
                System.out.println(b.length+" Length");

                ByteArrayInputStream in = new ByteArrayInputStream(b);
                ObjectInputStream iss = new ObjectInputStream(in);
                //System.out.println("========================+++++++++++++++++===");
//                for(int i=0; i<c.length;i++){
//                    System.out.println("C "+c[i]);
//                }
                //System.out.println("=======================================");

                for(int i=0; i<nodesPerBlock; i++){
                    PosNode<T> m = (PosNode) iss.readObject();
                    //m.printInfo();
                    nodeList.add(m);
                }
                blockCounter++;
                nodesPerBlock = dictionary.getEntrySize(blockCounter);


            }
//            int counter =0;
//            while (true){
//                PosNode<T> m = (PosNode) iss.readObject();
////                System.out.println(m.getId() + " " + m.getDim() + " " + m.getFeatVec()[0] + " " + m.getFeatVec()[1]);
////                System.out.println(counter++);
//                nodeList.add(m);
//            }
            //MyNodeClass m = (MyNodeClass) iss.readObject();
            //System.out.println(m.getId() + " " + m.getDim() + " " + m.getFeatVec()[0] + " " + m.getFeatVec()[1]);
            // MyNodeClass m1 = (MyNodeClass) iss.readObject();
            //System.out.println(m1.getId() + " " + m1.getDim() + " " + m1.getFeatVec()[0] + " " + m1.getFeatVec()[1]);
        } catch (EOFException e){
            // Eat the EOF exception
        } catch (Exception e){
            System.out.println(e);
        }

        return nodeList;
    }

    public <T extends Geometry> List<PosNode<T>> loadStorageNodes(){
        List<PosNode<T>> nodeList = new ArrayList<>();
        Dictionary dictionary = loadDictionary();
        int blockCounter = 1;       // Change this to 0 when i develop the block0

        try {
            InputStream is = new FileInputStream(super.getFile());

            //System.out.println(b.length);

            int byteLength = 400;
            int HEADER = 4;
            int blockN = dictionary.getSize();
            byte[] b = is.readAllBytes();
            byte[] tmp=new byte[byteLength];

            // Skip block 0 and this is block 1 only
            int i = 1;
            int entries = dictionary.getEntrySize(i);
            int value =  dictionary.getIndex(i,0) ;          // Header is the solution... 4 bytes
            // of the beginning of the file
            for(int k=0; k<byteLength;k++){
                tmp[k] = b[k + value];
            }

            ByteArrayInputStream in = new ByteArrayInputStream(tmp);
            ObjectInputStream iss = new ObjectInputStream(in);
            PosNode<T> m = (PosNode) iss.readObject();
            //m.printInfo();
            nodeList.add(m);

            // The rest of the block 1
            for (int j=1; j<entries; j++){  //Skip the first one
                value =  dictionary.getIndex(i,j) ;          // Header is the solution... 4 bytes
                // of the beginning of the file
                for(int k=0; k<HEADER;k++){
                    tmp[k] = b[k];
                }
                for (int k=HEADER; k<byteLength; k++){
                    tmp[k] = b[k + value - HEADER];
                }

                in = new ByteArrayInputStream(tmp);
                iss = new ObjectInputStream(in);
                m = (PosNode) iss.readObject();
                //m.printInfo();
                nodeList.add(m);
            }

            // the block 2 and afterwards
            for(i=2; i<blockN; i++){        // Skip block 0 & 1
                entries = dictionary.getEntrySize(i);
                for (int j=0; j<entries; j++){
                    value =  dictionary.getIndex(i,j);          // Header is the solution... 4 bytes
                                                                                     // of the beginning of the file
                    //System.out.println("Value: "+(value-Options.BLOCK_SIZE*i+Options.BLOCK_SIZE));
                    for(int k=0; k<HEADER;k++){
                        tmp[k] = b[k];
                    }
                    // The second condition secures the Out of bound at the end of the file
                    for (int k=HEADER; k<byteLength && (k + value - HEADER)<b.length; k++){
                        tmp[k] = b[k + value - HEADER];
                    }

                    in = new ByteArrayInputStream(tmp);
                    iss = new ObjectInputStream(in);
                    m = (PosNode) iss.readObject();
                    //m.printInfo();
                    nodeList.add(m);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e){
            // Eat it up... It always drops at the end
        }
        catch (EOFException e){
            // Eat the EOF exception
            System.out.println("EOF");
        } catch (Exception e){
            System.out.println(e);
        }

        return nodeList;
    }

    public DataInfo loadStorageInfo(){
        try{
            InputStream is = new FileInputStream(super.getFile());
            byte[] blockByte = is.readNBytes(Options.BLOCK_SIZE);

            ByteArrayInputStream in = new ByteArrayInputStream(blockByte);
            ObjectInputStream oss = new ObjectInputStream(in);

            DataInfo info = (DataInfo) oss.readObject();

            return info;
        } catch (Exception e){
            System.out.println("Load Storage Info Error!");
            e.printStackTrace();
        }
        return (new DataInfo()); // Fail to retrieve the saved one or it doesn't exist
    }

    public Dictionary loadDictionary(){
        try{
            InputStream is = new FileInputStream(Options.DICTIONARY_PATH);
            byte[] b = is.readAllBytes();

            ByteArrayInputStream in = new ByteArrayInputStream(b);
            ObjectInputStream iss = new ObjectInputStream(in);
            Dictionary dictionary = (Dictionary) iss.readObject();
            return dictionary;
        } catch (Exception e){
            e.printStackTrace();
        }
        return new Dictionary();    // In error modes it sends an empty dictionary
    }

    public <T extends Geometry>  void testing(){
        List<PosNode<T>> nodeList = new ArrayList<>();
        try {
            InputStream is = new FileInputStream(super.getFile());
            byte[] b = null;
            b = is.readNBytes(Options.BLOCK_SIZE);
            //System.out.println(b.length);

            ByteArrayInputStream in = new ByteArrayInputStream(b);
            ObjectInputStream iss = new ObjectInputStream(in);
            int counter =0;
            byte bit;
            for(int i=0; i<b.length; i++){
                System.out.println(b[i]);
            }
            //MyNodeClass m = (MyNodeClass) iss.readObject();
            //System.out.println(m.getId() + " " + m.getDim() + " " + m.getFeatVec()[0] + " " + m.getFeatVec()[1]);
            // MyNodeClass m1 = (MyNodeClass) iss.readObject();
            //System.out.println(m1.getId() + " " + m1.getDim() + " " + m1.getFeatVec()[0] + " " + m1.getFeatVec()[1]);
        } catch (EOFException e){
            // Eat the EOF exception
            System.out.println("EOF Exception\n");
        } catch (Exception e){
            System.out.println(e);
        }
    }

}
