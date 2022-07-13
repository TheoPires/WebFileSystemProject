package fr.univrouen.file_system.file.regular_file;


import fr.univrouen.file_system.file.File;

public interface RegularFile extends File {

    /**
     * Get content of file
     * @return {@link byte[]}
     */
    public byte[] getContent();

    /**
     * Set content of file
     * @param content Bytes array with content
     * @return void
     */
    public void setContent(byte[] content);
}
