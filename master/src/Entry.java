import java.io.Serializable;

/**
 * This class represents an Entry in the R* tree.
 * It contains short information, like the point and the position
 * of that point to the hard disk/file.
 */
public class Entry implements HasGeometry, Serializable {
    private final double[] featVec;
    private final int blockId;
    private final int recordId;

    /**
     * The constructor
     * @param featVec array of the features
     * @param blockId the Id of the block
     * @param recordId the Id of the record
     */
    public Entry(double[] featVec, int blockId, int recordId){
        this.featVec = featVec;
        this.blockId = blockId;
        this.recordId = recordId;
    }

    /**
     *
     * @return the Id of the block
     */
    public int getBlockId(){
        return blockId;
    }

    /**
     *
     * @return the Id of the record
     */
    public int getRecordId(){
        return recordId;
    }

    /**
     *
     * @return the feature vector as a double array
     */
    public double[] getFeatVec(){
        return featVec;
    }

    /**
     *
     * @return the feature vector as a double array
     */
    public double[] getVector() { return featVec; }

    /**
     *
     * @return the feature vector of the point as an array
     */
    public double[] getPoint() { return featVec; }

    /**
     *
     * @return the point as a rectangle
     */
    public Rectangle getRectangle(){
        return new Rectangle(featVec, featVec);
    }

    /**
     * Checks equality between this Entry and the one we give as a parameter
     * @param e an Entry object
     * @return if they are equal returns true, else false
     */
    public boolean isEqualTo(Entry e){
        for(int i=0;i<featVec.length;i++){
            if (featVec[i] != e.featVec[i]){
                return false;
            }
        }
        return true;
    }
}
