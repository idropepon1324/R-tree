import java.util.List;

public class ExecutionTimes {

    private ExecutionTimes(){
        // Prevent initializations
    }

    public void areaSearchWithIndex(RTree rTree, Rectangle area){
        Search search = new Search();
        long timeStart, timeFinish, result;
        timeStart = System.currentTimeMillis();
        search.searchArea(rTree.getRoot(), area);
        timeFinish = System.currentTimeMillis();
        result = timeFinish - timeStart;

        System.out.println("Time took to search with an R* Tree Index: "+ result+ " milliseconds.");

    }

    public void areaSearchWithLinear(List<Entry> entries, Rectangle area){
        Search search = new Search();
        long timeStart, timeFinish, result;
        timeStart = System.currentTimeMillis();
        search.searchAreaLinear(entries, area);
        timeFinish = System.currentTimeMillis();
        result = timeFinish - timeStart;

        System.out.println("Time took to search with the Linear algorithm: "+ result+ " milliseconds.");
    }

    public void knnSearchWithIndex(RTree rTree, Entry point, int k){
        Queries query = new Queries();
        long timeStart, timeFinish, result;
        timeStart = System.currentTimeMillis();
        query.nnSearch(rTree.getRoot(), point, k);
        timeFinish = System.currentTimeMillis();
        result = timeFinish - timeStart;

        System.out.println("Time took to search the K="+k+" nearest neighbours with an R* Tree Index: "+ result+" milliseconds.");

    }

    public void knnSearchWithLinear(List<Entry> entries, Entry point, int k){
        Queries query = new Queries();
        long timeStart, timeFinish, result;
        timeStart = System.currentTimeMillis();
        query.knnSearchLinear(entries, point, k);
        timeFinish = System.currentTimeMillis();
        result = timeFinish - timeStart;

        System.out.println("Time took to search the K="+k+" nearest neighbours with the Linear Algorithm: "+ result+" milliseconds.");

    }

    public void skylineWithIndex(RTree rTree){
        Queries query = new Queries();
        long timeStart, timeFinish, result;
        timeStart = System.currentTimeMillis();
        query.skyLineBBS(rTree.getRoot());
        timeFinish = System.currentTimeMillis();
        result = timeFinish - timeStart;

        System.out.println("Time took to search the Skyline with the R* Tree Index: "+ result+" milliseconds.");

    }

    public void skylineWithLinear(List<Entry> entries){
        Queries query = new Queries();
        long timeStart, timeFinish, result;
        timeStart = System.currentTimeMillis();
        query.skyLineLinear(entries);
        timeFinish = System.currentTimeMillis();
        result = timeFinish - timeStart;

        System.out.println("Time took to search the Skyline with the R* Tree Index: "+ result+" milliseconds.");
    }

    public void insertionOneByOne(List<Entry> entries){
        RTree rTree = new RTree();
        Insert insert = new Insert();
        long timeStart, timeFinish, result;
        timeStart = System.currentTimeMillis();
        for(Entry entry: entries){
            insert.insertData(rTree, rTree.calculateDepth(), entry);
        }
        timeFinish = System.currentTimeMillis();
        result = timeFinish - timeStart;

        System.out.println("Time took to insert data One by One: "+ result+ " milliseconds.");

    }

    public void insertionBottomUp(List<Entry> entries){
//        RTree rTree = new RTree();
//        Insert insert = new Insert();
//        long timeStart, timeFinish, result;
//        timeStart = System.currentTimeMillis();
//        for(Entry entry: entries){
//            insert.insertData(rTree, rTree.calculateDepth(), entry);
//        }
//        timeFinish = System.currentTimeMillis();
//        result = timeFinish - timeStart;
//
//        System.out.println("Time took to insert data One by One: "+ result+ " milliseconds.");

    }


}
