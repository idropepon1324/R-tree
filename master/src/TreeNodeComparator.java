import java.util.Comparator;

public class TreeNodeComparator implements Comparator<TreeNode> {
    @Override
    public int compare(TreeNode o1, TreeNode o2) {
        double distO1 = 0;
        double distO2 = 0;
        for(int i=0;i<o1.getRectangle().getVector1().length;i++){
            distO1 += o1.getRectangle().getVector1()[i];
            distO2 += o2.getRectangle().getVector1()[i];
        }
        return Double.compare(distO1,distO2);
    }
}
