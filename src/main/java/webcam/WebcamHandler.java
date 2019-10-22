package webcam;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.ds.javacv.JavaCvDriver;
import javafx.scene.canvas.GraphicsContext;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WebcamHandler {


    private static WebcamHandler instance;

    private Webcam myWebcam;
    private double scalefactor;

    private WebcamHandler(){
        //Webcam.setDriver(new JavaCvDriver());
        scalefactor = 1;
    }


    public void setWebcamByName(Webcam webcam){
        System.out.println(webcam.getName());
        if (myWebcam != null && myWebcam.isOpen()) myWebcam.close();

        myWebcam = webcam;
        myWebcam.getDevice().setResolution(WebcamResolution.NHD.getSize());
        myWebcam.open(true);
        listenToPictures();
    }


    private void listenToPictures(){

        Runnable runnable = new Runnable() {
            BufferedImage img;
            @Override
            public void run() {
                while (myWebcam.isOpen()){
                    //System.out.println("Looking for Pictures");
                    if((img = myWebcam.getImage())!= null){
                        img = resizeImage(img,img.getType());
                        ServerConnection.getInstance().sendImage(img);
                    }
                }
            }
        };
        Thread th = new Thread(runnable);
        th.setDaemon(true);
        th.start();


    }

    public void setScalefactor(double scalefactor) {
        this.scalefactor = scalefactor;
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int type){
        int IMG_WIDTH = 640;
        int IMG_HEIGHT = 360;
        Graphics2D g = originalImage.createGraphics();
        if (scalefactor!= 1){
            g.scale(scalefactor,scalefactor);
            g.drawImage(originalImage,0,0,originalImage.getWidth(),originalImage.getHeight(),null);
            g.dispose();
        }
        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
        g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
        g.dispose();

        return resizedImage;
    }



    public static WebcamHandler getInstance(){
        if (WebcamHandler.instance == null){
            WebcamHandler.instance = new WebcamHandler();
        }
        return WebcamHandler.instance;
    }

    public void closeWebcam(){
        if (myWebcam != null && myWebcam.isOpen()) myWebcam.close();
    }
}
