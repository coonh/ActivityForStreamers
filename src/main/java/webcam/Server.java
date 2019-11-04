package webcam;

import org.json.JSONObject;
import server.ServerMain;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    ServerSocket serverSocket;

    HashMap<Socket, ClientData> sockets;


    public Server(){

        System.out.println("Starting webcam server");

        try {
            serverSocket = new ServerSocket(ServerMain.getPort()+1);
            sockets = new HashMap<>();
            startServer();
            System.out.println("Webcam server started");
        } catch (IOException e) {
            System.err.println("Error starting Server!");
        }

    }


    private void startServer(){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                while (!serverSocket.isClosed()){
                    try {
                        Socket socket = serverSocket.accept();
                        acceptingClient(socket);
                    } catch (IOException e) {
                        System.err.println("Error listening to new sockets");
                    }
                }
            }
        };
        Thread th = new Thread(r);
        th.setDaemon(true);
        th.start();
    }

    private void acceptingClient(Socket socket){
        System.out.println("New Socket: " +socket.getInetAddress().toString());
        System.out.println("Waiting for message");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            String answer;
            String name = "";

            JSONObject jsonObject;

            if ((answer = reader.readLine()) != null) {
                jsonObject = new JSONObject(answer);
                if (!jsonObject.getString("event").equals("webcam")) {
                    socket.close();
                    return;
                }
                name = jsonObject.getString("name");
            }

            jsonObject = new JSONObject();
            jsonObject.put("event", "webcam");
            jsonObject.put("status", "ok");

            writer.println(jsonObject.toString());
            writer.flush();

            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

            sockets.put(socket,new ClientData(name,reader,writer,input,output));
            startListening(socket,input);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void startListening(Socket socket,ObjectInputStream inputStream){
        Runnable listening = new Runnable() {
            @Override
            public void run() {
                //BufferedImage img;
                while (!socket.isClosed()){
                    try{
                        Object img;
                        if ((img = inputStream.readObject())!= null) {
                            System.out.println("New Image");
                            for (Map.Entry<Socket, ClientData> e : sockets.entrySet()) {
                                //if (e.getKey().equals(socket)) continue;

                                e.getValue().getOutputStream().writeObject(img);
                                e.getValue().getOutputStream().flush();
                                e.getValue().getOutputStream().reset();

                            }
                        }
                    } catch (Exception e){
                        //e.printStackTrace();
                        System.err.println("Error listening to socket from: " + sockets.get(socket).getName());
                        sockets.remove(socket,sockets.get(socket));
                        return;
                    }
                }
                System.err.println("Error listening to socket from: " + sockets.get(socket).getName());
                sockets.remove(socket,sockets.get(socket));
                return;
            }
        };
        Thread thread = new Thread(listening);
        thread.setDaemon(true);
        thread.start();
    }


}
