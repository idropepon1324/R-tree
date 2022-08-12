import java.io.File;

/** This is a abstract class for file management.
 *  It contains the general methods for a file
 *  and it is used as a super class for others to inherit.
 *
 */
public abstract class FileManagement {
    private File file;
    public FileManagement(){
        //
    }

    public FileManagement(String file){
        this.file = new File(file);
    }

    public File getFile(){
        return file;
    }

    public String getFileName(){
        return file.getName();
    }

    public String getFilePath(){
        return file.getPath();
    }

    public void setFile(String file){
        this.file = new File(file);
    }

}
