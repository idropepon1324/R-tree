import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.exit;

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
//            List<List<Integer>> dictionary = new ArrayList<>();
//            dictionary.add(new ArrayList<>());  // Block 0
//            dictionary.get(0).add(0);   // Block 0 in dictionary
//            dictionary.add(new ArrayList<>());  // Block 1
            Dictionary dictionary = new Dictionary();
            List<Integer> listDict = new ArrayList<>();
            listDict.add(0);
            dictionary.addEntry(listDict);  // Block 0
            listDict = new ArrayList<>(); // Prep it again for block 1

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            OutputStream os = new FileOutputStream(super.getFile());
            DataInfo block0 = new DataInfo();

            int recordCounter = 0;
            int counterOld;
            int blockCounter = 1; // for dictionary of records
            int byteCounter = 0;
            byte[] blockList = new byte[0];

            for(int i=0;i<nodes.size();i++) {

                oos.writeObject(nodes.get(i));  // Writing object to bos stream
                oos.flush();                    // writing the unsaved of oos
                byte[] myByteArray = bos.toByteArray();
//                for(int j=0;j<myByteArray.length;j++){
//                   System.out.print(myByteArray[j]);
//                }
//                System.out.println();
                oos.reset();                // resetting the oss and bos to New state
                bos.reset();
//                System.out.println(myByteArray.length);
                //System.out.println(blockList.length);

                /* We add the new PosNode to the block if it has space,
                *  or else we create the next block and we save it there.
                *  We save the blocks as entity and the last one with the
                *  remaining records.
                *  */
                if(byteCounter + myByteArray.length  < BLOCK_SIZE){
                    listDict.add(byteCounter);
                    recordCounter++;
                    counterOld = byteCounter;
                    byteCounter += myByteArray.length;
                    byte[] tmp = new byte[counterOld];
                    System.arraycopy(blockList, 0, tmp, 0, counterOld);
//                    for (int j=0; j<counterOld; j++){
//                        tmp[j] = blockList[j];
//                    }

                    blockList = new byte[byteCounter];  //Initialize the BlockList
                    System.arraycopy(tmp, 0, blockList, 0, counterOld);
//                    for(int j=0; j<counterOld; j++){
//                        blockList[j] = tmp[j];
//                    }
                    if (byteCounter - counterOld >= 0)
                        System.arraycopy(myByteArray, 0, blockList, counterOld, byteCounter - counterOld);
//                    for (int j=counterOld;j<byteCounter;j++){
//                        blockList[j] = myByteArray[j-counterOld];
//                    }


                } else {
                    //System.out.println(blockList.length);
                    //System.out.println(blocksCounter);
                    dictionary.addEntry(listDict);  // Add entry to the dictionary
                    listDict = new ArrayList<>();   // Prep for the next block
                    os.write(blockList, 0,blockList.length);
                    byte[] extra = new byte[Options.BLOCK_SIZE - blockList.length];
                    for(int j=0;j<extra.length;j++){
                        extra[j] = 0;
                    }

                    //System.out.println("------------------");
                    os.write(extra,0,extra.length);
                    //System.out.println("------------------");

                    //write blockList
                    Block block = new Block(recordCounter);
                    block0.addBlock(block);
                    //Initialize
                    recordCounter = 0;
                    byteCounter = 0;
                    blockList = new byte[0];
                    i--;    // not eating the last save
                }
            } // End of while loop
            // Write the remaining to the file
            if(recordCounter!=0){
                dictionary.addEntry(listDict);  // Save last entries of dictionary
                os.write(blockList, 0,blockList.length);
                Block block = new Block(recordCounter);
                block0.addBlock(block);
            }
            // Save Dictionary to a file
            if(!saveDictionary(dictionary)){
                System.out.println("Error occurred during saving the Dictionary!!!");
            }
            //dictionary.printDictionary();   // Printing the Dictionary

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean saveDictionary(Dictionary dictionary){
        try{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            OutputStream os = new FileOutputStream(Options.DICTIONARY_PATH);

            oos.writeObject(dictionary);    // Writing object to bos stream
            oos.flush();                    // writing the unsaved of oos
            byte[] myByteArray = bos.toByteArray();

            os.write(myByteArray);
            return true;        // Successfully saved the Dictionary


        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;   // Failed

    }

}
