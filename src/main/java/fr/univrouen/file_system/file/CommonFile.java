package fr.univrouen.file_system.file;

public abstract class CommonFile implements File {

    protected String name;
    protected int size;

    /**
     * Constructor for under-class extends CommunFile
     * @param name Name of file
     * @param size Size of file
     */
    public CommonFile(String name, Integer size){
        this.name = name;
        this.size = size;
    }

    /**
     * Get name of file
     * @return {@link String}
     */
    public String getName() {
        return name;
    }

    /**
     * Set name of file
     * @param name  Name of file
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get size of file
     * @return {@link String}
     */
    public int getSize() {
        return size;
    }

    /**
     * Set Size of file
     * @param size
     * @return void
     */
    public void setSize(int size) {
        this.size = size;
    }
}
