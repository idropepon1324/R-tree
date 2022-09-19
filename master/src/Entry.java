public class Entry {
    private double[] featVec;
    private int blockId;
    private int recordId;

    public Entry(double[] featVec, int blockId, int recordId){
        this.featVec = featVec;
        this.blockId = blockId;
        this.recordId = recordId;
    }

    public int getBlockId(){
        return blockId;
    }

    public int getRecordId(){
        return recordId;
    }

    public double[] getFeatVec(){
        return featVec;
    }
}
