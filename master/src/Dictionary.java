import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Dictionary implements Serializable {
    private List<List<Integer>> dictionary;

    public Dictionary(){
        dictionary = new ArrayList<>();
    }

    public Dictionary(List<List<Integer>> dictionary) {
        this.dictionary = dictionary;
    }

    public List<List<Integer>> getDictionary(){
        return dictionary;
    }

    public void setDictionary(List<List<Integer>> dictionary){
        this.dictionary = dictionary;
    }

    public void addEntry(List<Integer> entry){
        dictionary.add(entry);
    }

    public int getValue(int i, int j){
        if (i>=0 && i<dictionary.size()) {
            if (j>=0 && j<dictionary.get(i).size()) {
                return dictionary.get(i).get(j);
            }
        }
        return -1;
    }

    public int getIndex(int i, int j){
        return i * Options.BLOCK_SIZE + getValue(i, j);
    }

    public int getEntrySize(int i){
        if(i>=0 && i<dictionary.size()){
            return dictionary.get(i).size();
        }
        return -1;
    }

    public void printDictionary(){
        int count =0 ;
        for (List<Integer> list : dictionary){
            System.out.println("======="+count++);
            for (int n: list){
                System.out.println("   "+n);
            }
        }
    }
}
