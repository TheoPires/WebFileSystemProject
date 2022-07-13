package fr.univrouen.file_system.file.factory;

import fr.univrouen.file_system.file.File;
import fr.univrouen.file_system.file.folder.Folder;
import fr.univrouen.file_system.file.folder.StdFolder;
import fr.univrouen.file_system.file.link.LinkFile;
import fr.univrouen.file_system.file.link.StdLinkFile;
import fr.univrouen.file_system.file.regular_file.RegularFile;
import fr.univrouen.file_system.file.regular_file.StdRegularFile;

import java.util.List;

import static fr.univrouen.file_system.file.File.REGEX_PATTERN;

public class StdFileFactory implements FileFactory{

    /**
     * Method to create a RegularFile
     * @param name Name of RegularFile
     * @param size Size of RegularFile
     * @param content content of RegularFile
     * @return {@link RegularFile}
     */
    @Override
    public RegularFile createRegularFile(String name, int size,byte[] content) {
        if(isValid(name))
            return new StdRegularFile(name, size, content);
        return null;
    }

    /**
     * Method to create a LinkFile
     * @param name Name of link
     * @param path Path of link file
     * @return {@link LinkFile}
     */

    @Override
    public LinkFile createLinkFile(String name, String path) {
        if(isValid(name)) {
            return new StdLinkFile(name, path);
        }
        return null;
    }

    /**
     * Method to create a Folder
     * @param name Name of folder
     * @param files List of files to add
     * @return {@link Folder}
     */
    @Override
    public Folder createFolder(String name, List<File> files) {
        StdFolder folder;
        if(isValid(name)) {
            folder = (files == null) ? new StdFolder(name) : new StdFolder(name, files);
            return folder;
        }
        return null;
    }
    /**
     * Method to check if name of file respect REGEX_PATTERN
     * @param name Name of new folder
     * @return {@link boolean}
     */
    private boolean isValid(String name){
        return name.matches(REGEX_PATTERN) && name.length() >= 1 && name.length()<= 20;
    }
}
