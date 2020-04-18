package webcam;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicReference;

public class MainCam extends Application {


    Webcam webcam;
    ImageView showImage;
    private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        showImage = new ImageView();




        webcam  = Webcam.getDefault();


        webcam.getDevice().setResolution(WebcamResolution.NHD.getSize());

        for (Dimension dimension:webcam.getViewSizes()) {
            System.out.println(dimension.toString());
        }


        System.out.println();

        //webcam.setViewSize(WebcamResolution.VGA.getSize());

        webcam.open(true);


        System.out.println(webcam.getName());

        primaryStage.setScene(new Scene(new BorderPane(showImage)));
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                final AtomicReference<WritableImage> ref = new AtomicReference<>();
                BufferedImage img = null;
                BufferedImage resized;

                Boolean isRunning = true;

                while(isRunning){
                    try{
                        if ((img = webcam.getImage()) != null){

                            img = resizeImage(img,img.getType());

                            ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);

                            ImageIO.write(img,"JPG", baos);
                            baos.flush();

                            String input = new String(Base64.getEncoder().encode(baos.toByteArray()), "UTF8");
                            baos.close();
                            //System.out.println(input);
                            //byte[] bytes = Base64.getDecoder().decode(input);
                            //img = ImageIO.read(new ByteArrayInputStream(bytes));
                            ref.set(SwingFXUtils.toFXImage(img,ref.get()));
                            img.flush();

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    imageProperty.set(ref.get());
                                }
                            });
                        }
                    } catch (Exception e){
                        isRunning = false;
                        e.printStackTrace();
                    }
                } return null;
            }
        };

        Thread th = new Thread(task);

        th.setDaemon(true);
        th.start();
        showImage.imageProperty().bind((imageProperty));
        showImage.setFitHeight(360);
        showImage.setFitWidth(640);

        primaryStage.show();
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int type){
        int IMG_WIDTH = 640;
        int IMG_HEIGHT = 360;
        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
        g.dispose();

        return resizedImage;
    }

    @Override
    public void stop() {
        webcam.close();
        System.exit(0);

    }
}
