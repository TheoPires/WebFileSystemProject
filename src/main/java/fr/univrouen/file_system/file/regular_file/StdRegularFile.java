package fr.univrouen.file_system.file.regular_file;

import fr.univrouen.file_system.file.CommonFile;

public class StdRegularFile extends CommonFile implements RegularFile {

    /**
     * Content attributes
     */
    private byte[] content;

    /**
     * Constructor for {@link StdRegularFile}
     * @param name Name of RegularFile
     * @param size Size of RegularFile
     * @param content content of RegularFile
     * @return {@link RegularFile}
     */
    public StdRegularFile(String name, int size, byte[] content){
        super(name,size);
        this.content = content;
    }

    /**
     * Get content of file
     * @return {@link byte[]}
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * Set content of file
     * @param content Bytes array with content
     * @return void
     */
    public void setContent(byte[] content) {
        this.content = content;
    }

}
