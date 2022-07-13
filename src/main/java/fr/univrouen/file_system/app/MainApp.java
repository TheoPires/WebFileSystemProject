package fr.univrouen.file_system.app;

import java.io.IOException;

public class MainApp {

    /**
     * Start of application
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        if(args.length == 1){
            new Server(args[0], "127.0.0.1",80);
        }else if(args.length == 0)
            new Server("127.0.0.1",80);
        else{
            System.out.println("USAGE: java MainApp <folder>");
        }
    }
}
