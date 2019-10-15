import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Gameboard {
    Scene scene;
    double mainSceneX, mainSceneY;

    public Gameboard(){

        double window_width=1280;
        double window_height=720;
        Pane stack = new Pane();
        GridPane backframe= new GridPane();
        backframe.setMinWidth(window_width);
        backframe.setMinHeight(window_height);
        Image field_graf_1 = new Image("file:img\\field_1.png");
        Image field_graf_2 = new Image("file:img\\field_2.png");
        Image field_graf_3 = new Image("file:img\\field_3.png");
        Image field_graf_4 = new Image("file:img\\field_4.png");
        Image img_player_1 = new Image("file:img\\player_1.png");
        Image img_player_2 = new Image("file:img\\player_2.png");

        ImagePattern field_patt_1 = new ImagePattern(field_graf_1);
        ImagePattern field_patt_2 = new ImagePattern(field_graf_2);
        ImagePattern field_patt_3 = new ImagePattern(field_graf_3);
        ImagePattern field_patt_4 = new ImagePattern(field_graf_4);


        for(int i=0;i<10;i++){

            Rectangle r = new Rectangle(window_width/10,window_width/20);
            if(i%2==0){
                r.setFill(field_patt_1);
            }else{
                r.setFill(field_patt_2);
            }
            r.setStroke(Color.BLACK);
            r.setArcWidth(10);
            r.setArcHeight(10);
            r.setStrokeWidth(5);
            if(i%9==0&&i!=0){
                r.setWidth(window_width/20);
                r.setHeight(window_width/10);
                r.setFill(field_patt_4);
            }
            backframe.add(r,i,0);
        }
       stack.getChildren().add(backframe);

        ImagePattern ip_p_1 = new ImagePattern(img_player_1);

        Rectangle player1 = new Rectangle(window_width/30,window_width/22);
        player1.setFill(ip_p_1);
        player1.setCursor(Cursor.HAND);

        player1.setOnMousePressed((t) -> {
            mainSceneX = t.getSceneX();
            mainSceneY = t.getSceneY();

            Rectangle r = (Rectangle) (t.getSource());
            r.toFront();
        });
        player1.setOnMouseDragged((t) -> {
            double offsetX = t.getSceneX() - mainSceneX;
            double offsetY = t.getSceneY() - mainSceneY;

            Rectangle r = (Rectangle) (t.getSource());

            r.setX(r.getX() + offsetX);
            r.setY(r.getY() + offsetY);

            mainSceneX = t.getSceneX();
            mainSceneY = t.getSceneY();

        });

        stack.getChildren().add(player1);
        scene = new Scene(stack);
    }
    public void show(Stage stage){
        stage.setScene(scene);
        stage.show();
    }

}
