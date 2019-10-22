package webcam;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class ClientData {

    String name;
    BufferedReader reader;
    PrintWriter writer;


    public ClientData(String name, BufferedReader reader, PrintWriter writer) {
        this.name = name;
        this.reader = reader;
        this.writer = writer;
    }

    public String getName() {
        return name;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public PrintWriter getWriter() {
        return writer;
    }
}
