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

    private boolean isReady;

    private HashMap<String, ObjectProperty<Image>> images;

    private String name;


    private final AtomicReference<WritableImage> ref  = new AtomicReference<>();


    private ServerConnection(){
        isReady = false;
        images = new HashMap<>();
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
            String input;
            synchronized (reader) {
                try {
                    while (true) {

                        if ((input = reader.readLine()) != null) {
                            receivePicture(input);
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread listener = new Thread(listening);
        listener.setDaemon(true);
        listener.start();
    }

    private void receivePicture(String input){
        JSONObject object = new JSONObject(input);

        String name = object.getString("name");
        if (!images.containsKey(name)){
            images.put(name,new SimpleObjectProperty<>());
        }

        try {
            byte[] bytes = Base64.getDecoder().decode(object.getString("image"));
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
            ref.set(SwingFXUtils.toFXImage(img, ref.get()));
            img.flush();
            Platform.runLater(() -> images.get(name).set(ref.get()));
        } catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    public synchronized void sendImage(BufferedImage img){
        if (!isReady) return;
        synchronized (writer) {
            try {


                ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);

                ImageIO.write(img, "JPG", baos);
                baos.flush();
                String input = new String(Base64.getEncoder().encode(baos.toByteArray()), "UTF8");

                JSONObject message = new JSONObject();
                message.put("name", name);
                message.put("image", input);
                writer.println(message.toString());
                writer.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void updateOwnImage(BufferedImage img){
        if (!images.containsKey("myself")){
            images.put("myself",new SimpleObjectProperty<>());
        }
        final AtomicReference<WritableImage> ref = new AtomicReference<>();
        ref.set(SwingFXUtils.toFXImage(img,ref.get()));
        Platform.runLater(() -> images.get("myself").set(ref.get()));

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
