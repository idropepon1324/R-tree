import java.io.Serializable;

/**
 * This class represent the Positional Node.
 * @param <T>
 */
public class PosNode<T extends Geometry> implements Serializable {
    private String id; //the id
    private String name; //the name
    private double[] featVec; //the feature vector
    private int dim; //the dimension of the feature vector

    public PosNode(String id, String name, double[] features, int dim){
        this.id = id;
        this.name = name;
        this.dim = dim;
        featVec = new double[dim];
        featVec = features;
    }

    public PosNode(){
        //
    }

    public PosNode(String id, String name, double[] features){
        this(id, name, features, features.length);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDim() {
        return dim;
    }

    public double[] getFeatVec() {
        return featVec;
    }

    public void printInfo() {
        System.out.println("-------------------------------------------------");
        System.out.println("Node name: " + name);
        System.out.println("point id: " + id);
        System.out.println("point lat: " + featVec[0]);
        System.out.println("point lon: " + featVec[1]);
        if (dim > 2) {
            for(int i=2; i<dim; i++){
                System.out.println("Point "+ i +"th: "+ featVec[i]);
            }
        }
        System.out.println("-------------------------------------------------");
    }

    public boolean equals(PosNode<T> node){
//        System.out.println(node.getName()+" "+name);
//        System.out.println(node.getId()+" "+id);
        boolean flag = true;
        for(int i=0; i<featVec.length; i++){
            //System.out.println(featVec[i]+" : "+ node.getFeatVec()[i]);
            if(featVec[i] != node.getFeatVec()[i]){
                flag = false;
                break;
            }
        }
        if (node.getName().equals(name) && node.getId().equals(id) && flag){
            return true;
        }
        return false;




    }
}
