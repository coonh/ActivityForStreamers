import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;


public class Field extends Rectangle {

    int type;

    public Field(int type){
        this.type=type;
        Image img = new Image("img/field_1.png");
        this.setFill(new ImagePattern(img));

    }

}
