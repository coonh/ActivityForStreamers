import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.json.JSONStringer;

import java.util.regex.Pattern;


public class Popup_card extends HBox {


    private String word;
    private double size;
    private ImageView check;
    private ImageView cross;
    private Gameboard parent;

    public Popup_card(String word, double size, Gameboard parent,int value){
        this.word = word;
        this.setSpacing(50);
        this.parent = parent;

        Image background = new Image(this.getClass().getResourceAsStream("/img/popup_card.png"));
        Image img_check = new Image(this.getClass().getResourceAsStream("/img/check.png"));
        Image img_cross = new Image(this.getClass().getResourceAsStream("/img/cross.png"));

        ImagePattern popup_card = new ImagePattern(background);
        check = new ImageView(img_check);
        cross = new ImageView(img_cross);

        check.setFitHeight(size);
        cross.setFitHeight(size);
        check.setPreserveRatio(true);
        cross.setPreserveRatio(true);



        Text term = new Text(word);

        term.setFont(new Font("Berlin Sans FB",20));

        Rectangle bound = new Rectangle(3*size, size);
        bound.setFill(popup_card);
        bound.setEffect(new DropShadow(10,10,10, Color.BLACK));

        check.setOnMouseClicked(event -> {
            parent.increaseCounter();
            String message =  new JSONStringer().object()
                    .key("event").value("drawCard")
                    .key("type").value(value)
                    .endObject().toString();

            System.out.println("Sending message: " +message);

            ServerConnector.getInstance().sendMessage(message);
        });
        cross.setCursor(Cursor.HAND);
        check.setCursor(Cursor.HAND);

        cross.setOnMouseClicked(event -> {
            parent.finishRound();
        });

        StackPane stack = new StackPane();

        stack.getChildren().addAll(bound,term);

        System.out.println("WORT: "+ term.getText());

        this.getChildren().addAll(cross,stack,check);




    }

    public ImageView getCheck() {
        return check;
    }

    public ImageView getCross() {
        return cross;
    }
}
