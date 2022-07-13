package fr.univrouen.file_system.file.factory;

import fr.univrouen.file_system.file.File;
import fr.univrouen.file_system.file.folder.Folder;
import fr.univrouen.file_system.file.link.LinkFile;
import fr.univrouen.file_system.file.regular_file.RegularFile;

import java.util.List;

public interface FileFactory {
    /**
     * Method to create a RegularFile
     * @param name Name of RegularFile
     * @param size Size of RegularFile
     * @param content content of RegularFile
     * @return {@link RegularFile}
     */
    RegularFile createRegularFile(String name, int size,byte[] content);
    /**
     * Method to create a LinkFile
     * @param name Name of link
     * @param path Path of link file
     * @return {@link LinkFile}
     */
    LinkFile createLinkFile(String name, String path);
    /**
     * Method to create a Folder
     * @param name Name of folder
     * @param files List of files to add
     * @return {@link Folder}
     */
    Folder createFolder(String name, List<File> files);

}
