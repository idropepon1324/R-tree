public class Entry implements HasGeometry {
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

    public double[] getVector() { return featVec; }

    public double[] getPoint() { return featVec; }

    public Rectangle getRectangle(){
        return new Rectangle(featVec, featVec);
    }

    public boolean isEqualTo(Entry e){
        for(int i=0;i<featVec.length;i++){
            if (featVec[i] != e.featVec[i]){
                return false;
            }
        }
        return true;
    }
}
