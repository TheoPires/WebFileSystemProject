package fr.univrouen.file_system.file.folder;

import fr.univrouen.file_system.file.File;

import java.util.List;

public interface Folder extends File{

    /**
     * Method to add file in folder
     * @param file to add
     */
    void addFile(File file);
    /**
     * Method to remove file in folder
     * @param file
     */
    void removeFile(File file);
    /**
     * Method to remove all file in files param
     * @param files
     * @return void
     */
    void removeFiles(List<File> files);

    /**
     * Get all file of folder
     * @return {@link List<File>}
     */
    List<File> getFiles();


}
