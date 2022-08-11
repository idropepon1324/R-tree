import java.util.ArrayList;
import java.util.List;

public final class DataInfo {
    private int totalRecords;
    private int totalBlocks;
    private List<Block> blocks;

    public DataInfo(){
        totalRecords = 0;
        totalBlocks = 0;
        blocks = new ArrayList<>();
    }

    public DataInfo(int rec, int bl, List<Block> blocks){
        totalRecords = rec;
        totalBlocks = bl;
        this.blocks = blocks;
    }

    public int getTotalBlocks() {
        return totalBlocks;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setTotalRecords(int records) {
        this.totalRecords = records;
    }

    public void setTotalBlocks(int blocks) {
        this.totalBlocks = blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }

    public void addBlock(Block block) {
        blocks.add(block);
        totalRecords += block.getRecords();
        totalBlocks++;
    }
}
