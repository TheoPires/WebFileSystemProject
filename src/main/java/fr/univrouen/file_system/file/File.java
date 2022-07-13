package fr.univrouen.file_system.file;

public interface File {

    /**
     * Regex use for name of {@link File}
     */
    String REGEX_PATTERN = "[a-zA-Z0-9._]+";

    /**
     * Get name of file
     * @return {@link String}
     */
    String getName();
    /**
     * Get size of file
     * @return {@link int}
     */
    int getSize();
}
