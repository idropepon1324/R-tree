import java.io.File;

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
