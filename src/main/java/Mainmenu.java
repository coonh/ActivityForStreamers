import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Mainmenu {
    Scene scene;
    Stage stage;

    public Mainmenu(Stage stage){
        this.stage = stage;


        Label l1 = new Label("Server-IP: ");
        Label l2 = new Label("Port: ");
        Label l3 = new Label("Streamresolution: ");
        Label x = new Label("x");

        TextField ip_adress = new TextField("Enter Server-IP");
        TextField port = new TextField("11000");
        TextField w = new TextField("1680");
        TextField h = new TextField("942");

        HBox h1 = new HBox(5);
        HBox h2 = new HBox(5);
        HBox h3 = new HBox(5);


        h1.getChildren().addAll(l1,ip_adress);
        h2.getChildren().addAll(l2,port);
        h3.getChildren().addAll(l3,w,x,h);

        h2.setAlignment(Pos.BASELINE_RIGHT);
        h1.setAlignment(Pos.BASELINE_RIGHT);
        h3.setAlignment(Pos.BASELINE_RIGHT);

        Button send = new Button("Connect and start!");
        VBox v = new VBox(20);

        v.setAlignment(Pos.CENTER);

        v.getChildren().addAll(h1,h2,h3,send);
        v.setAlignment(Pos.CENTER);

        send.setOnAction(event -> {
            //TODO connecToServer()
            String prt = port.getText();
            String ip = ip_adress.getText();

            Gameboard g = new Gameboard(stage,Double.parseDouble(w.getText()),Double.parseDouble(h.getText()));
            stage.setScene(g.scene);
            g.show(stage);
        });

        scene = new Scene(v);
    }

    public void show(Stage stage){
        stage.setScene(scene);
        stage.show();
    }
}
