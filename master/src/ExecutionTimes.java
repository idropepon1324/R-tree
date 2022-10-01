import java.util.List;

/**
 * This method is used to compare the times of its algorithm that the R* tree uses,
 * like comparing the range queries, knn and skyline with the linear approaches.
 * The creation one by one to the bottom up, too.
 */
public class ExecutionTimes {

    private ExecutionTimes(){
        // Prevent initializations
    }

    public static void areaSearchWithIndex(RTree rTree, Rectangle area){
        Search search = new Search();
        long timeStart, timeFinish, result;
        timeStart = System.currentTimeMillis();
        search.searchArea(rTree.getRoot(), area);
        timeFinish = System.currentTimeMillis();
        result = timeFinish - timeStart;

        System.out.println("Time took to search with an R* Tree Index: "+ result+ " milliseconds.");

    }

    public static void areaSearchWithLinear(List<Entry> entries, Rectangle area){
        Search search = new Search();
        long timeStart, timeFinish, result;
        timeStart = System.currentTimeMillis();
        search.searchAreaLinear(entries, area);
        timeFinish = System.currentTimeMillis();
        result = timeFinish - timeStart;

        System.out.println("Time took to search with the Linear algorithm: "+ result+ " milliseconds.");
    }

    public static void knnSearchWithIndex(RTree rTree, double[] point, int k){
        Queries query = new Queries();
        long timeStart, timeFinish, result;
        timeStart = System.currentTimeMillis();
        query.knnSearch(rTree.getRoot(), point, k);
        timeFinish = System.currentTimeMillis();
        result = timeFinish - timeStart;

        System.out.println("Time took to search the K="+k+" nearest neighbours with an R* Tree Index: "+ result+" milliseconds.");

    }

    public static void knnSearchWithLinear(List<Entry> entries, double[] point, int k){
        Queries query = new Queries();
        long timeStart, timeFinish, result;
        timeStart = System.currentTimeMillis();
        query.knnSearchLinear(entries, point, k);
        timeFinish = System.currentTimeMillis();
        result = timeFinish - timeStart;

        System.out.println("Time took to search the K="+k+" nearest neighbours with the Linear Algorithm: "+ result+" milliseconds.");

    }

    public static void skylineWithIndex(RTree rTree){
        Queries query = new Queries();
        long timeStart, timeFinish, result;
        timeStart = System.currentTimeMillis();
        List<Entry> list = query.skyLineBBS(rTree.getRoot());
        timeFinish = System.currentTimeMillis();
        result = timeFinish - timeStart;

        System.out.println("Time took to search the Skyline with the R* Tree Index: "+ result+" milliseconds.");
        //System.out.println("Size of the result: "+list.size());

    }

    public static void skylineWithLinear(List<Entry> entries){
        Queries query = new Queries();
        long timeStart, timeFinish, result;
        timeStart = System.currentTimeMillis();
        List<Entry> list = query.skyLineLinear(entries);
        timeFinish = System.currentTimeMillis();
        result = timeFinish - timeStart;

        System.out.println("Time took to search the Skyline with the Linear Algorithm: "+ result+" milliseconds.");
        //System.out.println("Size of the result: "+list.size());
    }

    public static void insertionOneByOne(List<Entry> entries){
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

    public static void insertionBottomUp(List<Entry> entries, Context context){
        RTree rTree = new RTree();
        Insert insert = new Insert();
        long timeStart, timeFinish, result;
        timeStart = System.currentTimeMillis();
        insert.bottomUpInsertion(entries, context);
        timeFinish = System.currentTimeMillis();
        result = timeFinish - timeStart;

        System.out.println("Time took to insert data BottomUp: "+ result+ " milliseconds.");

    }


}
