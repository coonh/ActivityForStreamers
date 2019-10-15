import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Gameboard {
    Scene scene;

    public Gameboard(){

        StackPane backframe= new StackPane();

        Image field_graf_1 = new Image("img/field_1.png");
        ImagePattern field_patt_1 = new ImagePattern(field_graf_1);
        Rectangle r1 = new Rectangle(500,500);
        r1.setFill(field_patt_1);

        backframe.getChildren().add(r1);

        scene = new Scene(backframe);
    }
    public void show(Stage stage){
        stage.setScene(scene);
        stage.show();
    }

}
