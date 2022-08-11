import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.lang.reflect.Array;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
                    PosNode node = new PosNode(id,name,2,vec);
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

    public <T extends Geometry> List<PosNode<T>> loadStorageNodes(){
        List<PosNode<T>> nodeList = new ArrayList<>();
        try {
            InputStream is = new FileInputStream(super.getFile());
            byte[] b = null;
//            System.out.println("yooooooo");
//            System.out.println("yooooooo");
            b = is.readNBytes(Options.BLOCK_SIZE);
            System.out.println(b.length);

            ByteArrayInputStream in = new ByteArrayInputStream(b);
            ObjectInputStream iss = new ObjectInputStream(in);
            int counter =0;
            while (true){
                PosNode<T> m = (PosNode) iss.readObject();
                System.out.println(m.getId() + " " + m.getDim() + " " + m.getFeatVec()[0] + " " + m.getFeatVec()[1]);
                System.out.println(counter++);
                nodeList.add(m);
            }
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

}
