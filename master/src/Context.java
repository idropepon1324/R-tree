/**
 * Configures an RTree prior to instantiation of an {@link RTree}.
 */
public final class Context{

    private final int maxChildren;
    private final int minChildren;
    private final int reInsertRate;
    private final Splitter splitter;
//    private final Selector selector;
//    ///**
//     //     * Constructor.
//     //     *
//     //     * @param minChildren
//     //     *            minimum number of children per node (at least 1)
//     //     * @param maxChildren
//     //     *            max number of children per node (minimum is 3)
//     //     * @param selector
//     //     *            algorithm to select search path
//     //     * @param splitter
//     //     *            algorithm to split the children across two new nodes
//     //     * @param factory
//     //     *            node creation factory
//     //     */
//   // private final Factory<T> factory;
//
////
////    public Context(int minChildren, int maxChildren, Selector selector, Splitter splitter,
////                   Factory<T, S> factory) {
////        Preconditions.checkNotNull(splitter);
////        Preconditions.checkNotNull(selector);
////        Preconditions.checkArgument(maxChildren > 2);
////        Preconditions.checkArgument(minChildren >= 1);
////        Preconditions.checkArgument(minChildren < maxChildren);
////        Preconditions.checkNotNull(factory);
////        this.selector = selector;
////        this.maxChildren = maxChildren;
////        this.minChildren = minChildren;
////        this.splitter = splitter;
////        this.factory = factory;
////    }

    // Default values are m=2 and M=5
    // Research has shown that m=40% gives the best results
    // reInsertRate(p) shoes best result at 30%.
    public Context(){
        maxChildren = 5;
        minChildren = 2;
        reInsertRate = 2;
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

//    public Selector selector() {
//        return selector;
//    }
//
////    public Factory<T, S> factory() {
////        return factory;
////    }

}
