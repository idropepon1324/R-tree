import java.io.Serializable;

/**
 * This class represents the general information for its block that is saved.
 * It is saved in the block 0, with the help of the DataInfo Class.
 */
public class Block implements Serializable {
    private int records;

    public Block(){
        records = 0;
    }

    public Block(int rec){
        records = rec;
    }

    public int getRecords(){
        return records;
    }

    public void setRecords(int records){
        this.records = records;
    }

}
