import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;


/** This class inherits the attributes of the FileManager Class and
 *  creates an object used to save into.
 *  Main methods are: saveNodes.
 *
 */
public class Saver extends FileManagement {
    // String file

    public Saver(){
        //
    }

    public Saver(String file){
        super(file);
    }

    /** This method saves the List of nodes to this.file
       It creates it, if it doesn't exist.
       T class is the Geometry class used to store Positional  nodes.
     */
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

            //Block 0
            byte[] zeroBlockByte = new byte[Options.BLOCK_SIZE];
            os.write(zeroBlockByte,0,zeroBlockByte.length);

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
                *  We save the blocks as a whole entity and the last one with the
                *  remaining records too.
                *  */
                if(byteCounter + myByteArray.length  < Options.BLOCK_SIZE){
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
            if(!dictionary.setValue(1,0, 4)){   // Header Security of 4 bytes
                System.out.println("Error during changing the first entry to preserve the Header!");
            }
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

            os.close();
            // Register the DataInfo in the block 0
            blockZeroDataRegister(block0);
            //dictionary.printDictionary();   // Printing the Dictionary

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean saveRtree(RTree rTree){
        try{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            OutputStream os = new FileOutputStream(Options.RTREE_INDEX_PATH);

            oos.writeObject(rTree);    // Writing object to bos stream
            oos.flush();                    // writing the unsaved of oos
            byte[] myByteArray = bos.toByteArray();

            os.write(myByteArray);
            return true;        // Successfully saved the Dictionary

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;   // Failed

    }

    private void blockZeroDataRegister(DataInfo block0){
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            InputStream is = new FileInputStream(super.getFile());

            oos.writeObject(block0);        // Writing object to bos stream
            oos.flush();                    // writing the unsaved of oos
            byte[] myByteArray = bos.toByteArray();
            byte[] allData = is.readAllBytes();

            if (myByteArray.length > Options.BLOCK_SIZE){
                System.out.println("MAJOR BAG ALERT!!!\nBlock 0 is bigger than 32KB.");
                exit(1);                                                                                // A hard exit of the program
            }

            // Replace the first block
            System.arraycopy(myByteArray, 0, allData, 0, myByteArray.length);
            OutputStream os = new FileOutputStream(super.getFile());

            os.write(allData,0,allData.length);
        } catch (Exception e){
            System.out.println("BlockZeroDataRegister Error");
            e.printStackTrace();
        }
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
