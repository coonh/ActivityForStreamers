package webcam;

import java.io.BufferedReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

public class ClientData {

    private String name;
    private BufferedReader reader;
    private PrintWriter writer;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;


    public ClientData(String name, BufferedReader reader, PrintWriter writer, ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        this.name = name;
        this.writer = writer;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public String getName() {
        return name;
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public PrintWriter getWriter() {
        return writer;
    }
}
