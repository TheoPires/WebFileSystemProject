package fr.univrouen.file_system.file.folder;

import fr.univrouen.file_system.file.File;
import fr.univrouen.file_system.file.link.StdLinkFile;

import java.util.ArrayList;
import java.util.List;

public class StdFolder implements Folder {

    private String name;
    private List<File> files;

    /**
     * Constructor for {@link StdFolder} with a folder already exists
     * @param name Name of folder
     * @param files List of files to add
     * @return {@link Folder}
     */
    public StdFolder(String name, List<File> files){
        this.name = name;
        this.files = files;
    }
    /**
     * Constructor for new {@link StdFolder}
     * @param name Name of folder
     * @return {@link Folder}
     */
    public StdFolder(String name){
        this.name = name;
        this.files = new ArrayList<>();
    }

    /**
     * Method to add file in folder
     * @param file
     */
    public void addFile(File file){
        if(file == null)
            throw new IllegalArgumentException();
        this.files.add(file);
    }
    /**
     * Method to remove file in folder
     * @param file
     */
    public void removeFile(File file){
        if(file == null)
            throw new IllegalArgumentException();
        this.files.remove(file);
    }
    /**
     * Method to remove all file in files param
     * @param files
     * @return void
     */
    public void removeFiles(List<File> files){
        if(files == null)
            throw new IllegalArgumentException();
        this.files.removeAll(files);
    }

    /**
     * Get size of folder
     * @return int
     */
    @Override
    public int getSize(){
        int sum = 0;
        for(File f : getFiles()){
            sum += f.getSize();
        }
        return sum;
    }

    /**
     * Get all file of folder
     * @return {@link List<File>}
     */
    public List<File> getFiles(){
        return files;
    }

    /**
     * Get name of folder
     * @return {@link String}
     */
    @Override
    public String getName() {
        return name;
    }
}
