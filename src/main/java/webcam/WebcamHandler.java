package webcam;

import com.github.sarxos.webcam.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebcamHandler {


    private static WebcamHandler instance;

    private Webcam myWebcam;
    private double scalefactor;

    private WebcamHandler(){
        //Webcam.setDriver(new JavaCvDriver());
        scalefactor = 1;
    }


    public void setWebcamByName(String webcam){
        System.out.println(webcam);
        if (myWebcam != null && myWebcam.isOpen()) myWebcam.close();

        myWebcam = Webcam.getWebcamByName(webcam);
        myWebcam.getDevice().setResolution(WebcamResolution.HD.getSize());
        myWebcam.open(true);
        listenToPictures();

    }


    private void listenToPictures(){

        Runnable runnable = new Runnable() {
            BufferedImage img;
            Thread th;
            ExecutorService executorService = Executors.newFixedThreadPool(2);
            @Override
            public void run() {
                while (myWebcam.isOpen()){
                    //System.out.println("Looking for Pictures");
                    if((img = myWebcam.getImage())!= null){
                        img = MainCam.resizeImage(img,img.getType());
                        Callable<Void> c1 = (Callable<Void>) () -> {ServerConnection.getInstance().updateOwnImage(img); return null;};
                        Callable<Void> c2 = (Callable<Void>) () -> {ServerConnection.getInstance().sendImage(img); return null;};
                        try {
                            executorService.invokeAll(Arrays.asList(c1,c2));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        img.flush();
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


        if (type == 0) type = BufferedImage.TYPE_INT_RGB;
        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
        g = resizedImage.createGraphics();
        if (scalefactor!= 1) g.scale(scalefactor,scalefactor);
        int x = (int) Math.round(originalImage.getWidth()*scalefactor-originalImage.getWidth())/4;
        int y = (int) Math.round(originalImage.getHeight()*scalefactor-originalImage.getHeight())/4;
        g.drawImage(originalImage, -x, -y, IMG_WIDTH, IMG_HEIGHT, null);
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
