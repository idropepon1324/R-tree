public class Splitter {

    /*
    * splitter: Split Algorithm for R* trees
    * 1- ChooseSplitAxes
    * 2- ChooseSplitIndex
    * 3- Distribute the 2 groups
    *
    * chooseSplitAxes: Select the best axis to do the split.
    * For Every axis sort first by the lower value of their rectangle and then their upper.
    * Then determine all the M-2m+2 distributions of the M+1 entries.
    * Calculate the S which is the sum value of all margin-values (Perimeter) of
    * the different distributions.
    * Choose the axis with the minimum S.
    *
    * ChooseSplitIndex: Determine the best distribution between two groups.
    *
    * bb: bounding box
    * margin-value: margin[bb(first group)] + margin[bb(second group)]
    * overlap-value: area[bb(first group) n bb(second group)]   Intersection
    * area-value: area[bb(first group)] + area[bb(second group)]
    *
     */

    private int chooseSplitAxes(){
        int dim = 0;



        return dim;
    }
}
