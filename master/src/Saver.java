import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

public class Saver extends FileManagement {
    public static int BLOCK_SIZE = 32768;
    // String file

    public Saver(){
        //
    }

    public Saver(String file){
        super(file);
    }

    public <T extends Geometry> boolean saveNodes(List<PosNode<T>> nodes){
        try{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            OutputStream os = new FileOutputStream(super.getFile());
            int blocksCounter = 0;


            byte[] blockList = new byte[0];
            int byteCounter = 0;
            for(int i=0;i<nodes.size();i++) {

                oos.writeObject(nodes.get(i));
                oos.flush();
                byte[] myByteArray = bos.toByteArray();
//                for(int j=0;j<myByteArray.length;j++){
//                   System.out.print(myByteArray[j]);
//                }
//                System.out.println();
                oos.reset();
                bos.reset();
//                System.out.println(myByteArray.length);
                //System.out.println(blockList.length);
                if(byteCounter + myByteArray.length  < BLOCK_SIZE){
                    int counterOld = byteCounter;
                    byteCounter += myByteArray.length;
                    byte[] tmp = new byte[counterOld];
                    for (int j=0; j<counterOld; j++){
                        tmp[j] = blockList[j];
                    }

                    blockList = new byte[byteCounter];
                    for(int j=0; j<counterOld; j++){
                        blockList[j] = tmp[j];
                    }
                    for (int j=counterOld;j<byteCounter;j++){
                        blockList[j] = myByteArray[j-counterOld];
                    }


                }else{
                    System.out.println(blockList.length);
                    System.out.println(blocksCounter);
                    os.write(blockList, 0,blockList.length);
                    byte[] extra = new byte[BLOCK_SIZE - blockList.length];
                    for(int j=0;j<extra.length;j++){
                        extra[j] = 1;
                    }

                    System.out.println("------------------");
                    os.write(extra,0,extra.length);
                    System.out.println("------------------");
                    //write blockList
                    blocksCounter += 1;
                    byteCounter = 0;
                    blockList = new byte[0];
                    i--;
                }
            }
            //Write the remaining to the file
            os.write(blockList, 0,blockList.length);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
