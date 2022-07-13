package fr.univrouen.file_system.file.link;


import fr.univrouen.file_system.file.CommonFile;
import fr.univrouen.file_system.file.regular_file.StdRegularFile;

public class StdLinkFile extends CommonFile implements LinkFile {
    private String path;

    /**
     * Constructor for {@link StdLinkFile}
     * @param name Name of link
     * @param path Path of link file
     * @return {@link LinkFile}
     */
    public StdLinkFile(String name, String path) {
        super(name,0);
        this.path = path;
    }
    /**
     * Get path of file linked
     * @return {@link String}
     */
    @Override
    public String getPath() {
        return path;
    }
    /**
     * Set path of file linked
     * @param path Path of file to link
     * @return void
     */
    @Override
    public void setPath(String path) {
        this.path = path;
    }


}
