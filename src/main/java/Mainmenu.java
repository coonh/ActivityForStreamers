import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Mainmenu {
    Scene scene;

    public Mainmenu(){
        TextField ip_adress = new TextField("Enter Server-IP");
        Button send = new Button("Connect");
        VBox v = new VBox(20);
        v.getChildren().addAll(ip_adress,send);
        v.setAlignment(Pos.CENTER);

        send.setOnAction(event -> {});

        scene = new Scene(v);
    }

    public void show(Stage stage){
        stage.setScene(scene);
        stage.show();
    }
}
