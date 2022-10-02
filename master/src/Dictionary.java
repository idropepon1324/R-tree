import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** This Class is used to represent a map of the Entries saved in the organised file.
 *  The file is separated in blocks of 32KB.
 *  The dictionary is used to return the offset where the entries are. (Bytes/Chars)
 *  The Saver Class has a private method that saves/creates a dictionary file for future reference
 *  located/saved in "files\dictionary.file".
 */
public class Dictionary implements Serializable {
    private List<List<Integer>> dictionary;
    private String filePointing;

    public Dictionary(){
        dictionary = new ArrayList<>();
    }

    public Dictionary(List<List<Integer>> dictionary, String fileName) {
        this.dictionary = dictionary;
        this.filePointing = fileName;
    }

    public List<List<Integer>> getDictionary(){
        return dictionary;
    }

    public void setDictionary(List<List<Integer>> dictionary){
        this.dictionary = dictionary;
    }

    public String getFilePointing(){
        return filePointing;
    }

    public void setFilePointing(String fileName){
        filePointing = fileName;
    }

    /**  Add a new entry/List of integer to the Dictionary.
        This represent the records of the new added block.  */
    public void addEntry(List<Integer> entry){
        dictionary.add(entry);
    }

    private int getValue(int i, int j){
        // Checking the validity of the dimensions given
        if (i>=0 && i<dictionary.size()) {
            if (j>=0 && j<dictionary.get(i).size()) {
                return dictionary.get(i).get(j);
            }
        }
        return -1;
    }

    public boolean setValue(int i, int j, int value){
        // Checking the validity of the dimensions given
        if (i>=0 && i<dictionary.size()){
            if(j>=0 && j<dictionary.get(i).size()){
                dictionary.get(i).set(j,value);
                return true;
            }
        }
        return false;
    }

    /** Returns the offset index in the file that the 'record' is saved
     *  in that specific 'block'.
     *
     *  @param block the number of the block. record - the number of record
     *  @return int Index */
    public int getIndex(int block, int record){
        if (block>=0 && block<dictionary.size()) {
            if (record>=0 && record<dictionary.get(block).size()) {
                return block * Options.BLOCK_SIZE + getValue(block, record);
            }
        }
        return -1;
    }

    /** Returns the number of records that are saved in that block.
     *  If the block doesn't exist, it returns -1.
     *
     * @param  block: the selected block
     * @return int Size */
    public int getEntrySize(int block){
        if(block>=0 && block<dictionary.size()){
            return dictionary.get(block).size();
        }
        return -1;
    }

    // Number of total blocks
    public int getSize(){
        return dictionary.size();
    }

    public int getTotalEntries(){
        int count = 0;
        for (List<Integer> l: dictionary){
            count += l.size();
        }
        return count;
    }

//    public void printDictionary(){
//        int count =0 ;
//        for (List<Integer> list : dictionary){
//            System.out.println("======="+count++);
//            for (int n: list){
//                System.out.println("   "+n);
//            }
//        }
//    }
}
