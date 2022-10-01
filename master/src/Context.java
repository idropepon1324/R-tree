import java.io.Serializable;

/**
 * Configures an RTree prior to instantiation of an {@link RTree}.
 */
public final class Context implements Serializable {

    private final int maxChildren;
    private final int minChildren;
    private final int reInsertRate;
    private final Splitter splitter;


    // Default values are m=2 and M=5
    // Research has shown that m=40% gives the best results
    // reInsertRate(p) shoes best result at 30%.
    public Context(){
        maxChildren = 5;
        minChildren = 2;
        reInsertRate = 2;
        splitter = new Splitter();
    }

    public Context(int minChildren, int maxChildren, int reInsertRate){
        this.minChildren = minChildren;
        this.maxChildren = maxChildren;
        this.reInsertRate = reInsertRate;
        splitter = new Splitter();
    }

    public int maxChildren() {
        return maxChildren;
    }

    public int minChildren() {
        return minChildren;
    }

    public Splitter splitter() {
        return splitter;
    }

    public int getReInsertRate(){
        return reInsertRate;
    }


}
