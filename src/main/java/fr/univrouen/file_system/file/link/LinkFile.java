package fr.univrouen.file_system.file.link;

import fr.univrouen.file_system.file.File;

public interface LinkFile extends File {

    /**
     * Get path of file linked
     * @return {@link String}
     */
    String getPath();

    /**
     * Set path of file linked
     * @param path Path of file to link
     * @return void
     */
    void setPath(String path);
}
