import java.io.Serializable;

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
