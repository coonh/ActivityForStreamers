package webcam;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.Base64;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class ServerConnection {


    private static ServerConnection instance;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private boolean isReady;

    private HashMap<String, ObjectProperty<Image>> images;

    private String name;

    private int count;



    private ServerConnection(){
        isReady = false;
        images = new HashMap<>();
        count = 0;
    }

    public void connect(String name, String ipAddress, int port){
        this.name = name;

        try {
            socket = new Socket(ipAddress, port);

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            writer = new PrintWriter(socket.getOutputStream());

            startConnection();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void startConnection(){

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("event", "webcam");
        jsonObject.put("name", name);

        writer.println(jsonObject.toString());
        writer.flush();

        String answer;

        try {
            if ((answer = reader.readLine())!= null){
                jsonObject = new JSONObject(answer);
                if (jsonObject.getString("status").equals("ok")){
                    outputStream = new ObjectOutputStream(socket.getOutputStream());
                    inputStream = new ObjectInputStream(socket.getInputStream());
                    startListening();

                } else {
                    socket.close();
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }


    private void startListening(){
        isReady = true;

        Runnable listening = () -> {
            ImageData input;
            synchronized (inputStream) {
                try {
                    while (true) {

                        if ((input = (ImageData) inputStream.readObject()) != null) {
                            receivePicture(input);


                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread listener = new Thread(listening);
        listener.setDaemon(true);
        listener.start();
    }

    private void receivePicture(ImageData input){
        String name = input.getName();
        if (!images.containsKey(name)){
            images.put(name,new SimpleObjectProperty<>());
        }

        Task<Void> receiveImage = new Task<Void>() {
            final AtomicReference<WritableImage> ref = new AtomicReference<>();
            @Override
            protected Void call() throws Exception {
                ref.set(SwingFXUtils.toFXImage(input.getMyImg(),ref.get()));
                input.getMyImg().flush();
                Platform.runLater(() -> images.get(name).set(ref.get()));
                return null;
            }
        } ;
        Thread receive = new Thread(receiveImage);
        receive.setDaemon(true);
        receive.start();
    }

    public synchronized void sendImage(BufferedImage img){
        if (!isReady) return;
        //synchronized (outputStream) {
            try {


                ImageData imageData = new ImageData(img, name);

                outputStream.writeObject(imageData);
                outputStream.reset();
                outputStream.flush();
                imageData = null;
                img.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        //}

    }

    public void updateOwnImage(BufferedImage img){
        if (!images.containsKey("myself")){
            images.put("myself",new SimpleObjectProperty<>());
        }
        final AtomicReference<WritableImage> ref = new AtomicReference<>();
        ref.set(SwingFXUtils.toFXImage(img,ref.get()));
        Platform.runLater(() -> images.get("myself").set(ref.get()));
        img.flush();
    }

    public void closeConnection(){
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] getWebcamUserList(){
        return images.keySet().toArray(new String[0]);
    }

    public ObjectProperty<Image> getImagePropertyWithName(String name){
        return images.get(name);
    }

    public static ServerConnection getInstance(){
        if (ServerConnection.instance == null){
            ServerConnection.instance = new ServerConnection();
        }
        return ServerConnection.instance;
    }

}
