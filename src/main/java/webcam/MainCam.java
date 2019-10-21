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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicReference;

public class MainCam extends Application {


    ImageView showImage;
    private ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        showImage = new ImageView();

        Webcam webcam = Webcam.getDefault();




        webcam.getDevice().setResolution(WebcamResolution.NHD.getSize());

        for (Dimension dimension:webcam.getViewSizes()) {
            System.out.println(dimension.toString());
        }


        System.out.println();

        //webcam.setViewSize(WebcamResolution.VGA.getSize());

        webcam.open();


        System.out.println(webcam.getName());

        primaryStage.setScene(new Scene(new BorderPane(showImage)));
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                final AtomicReference<WritableImage> ref = new AtomicReference<>();
                BufferedImage img = null;

                while(true){
                    try{
                        if ((img = webcam.getImage()) != null){

                            ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);

                            ImageIO.write(img,"JPG", baos);
                            baos.flush();

                            String input = Base64.getEncoder().encodeToString(baos.toByteArray());
                            baos.close();
                            //System.out.println(input);
                            byte[] bytes = Base64.getDecoder().decode(input);
                            img = ImageIO.read(new ByteArrayInputStream(bytes));
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
                        e.printStackTrace();
                    }
                }
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
}
