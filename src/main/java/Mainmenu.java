import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDevice;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import webcam.WebcamHandler;


public class Mainmenu {
    Scene scene;
    Stage stage;

    private ComboBox<String> webcamBox;

    public Mainmenu(Stage stage){
        this.stage = stage;


        Label l1 = new Label("Server-IP: ");
        Label l2 = new Label("Port: ");
        Label l3 = new Label("Streamresolution: ");
        Label l0 = new Label("Webcam: ");
        Label l4 = new Label("Name: ");

        TextField ip_adress = new TextField("127.0.0.1");
        TextField port = new TextField("11000");
        TextField name = new TextField();

        port.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) port.setText((newValue.replaceAll("[^\\d]", "")));
            }
        });

        ComboBox<String> comboBox = new ComboBox<String>();
        comboBox.getItems().addAll(
                "848 x 480",
                "960 x 540",
                "1360 x 768",
                "1280 x 720",
                "1600 x 900",
                "1920 x 1080"
        );
        comboBox.setVisibleRowCount(6);
        comboBox.setValue("1280 x 720");

        HBox h0 = new HBox(5);
        HBox h1 = new HBox(5);
        HBox h2 = new HBox(5);
        HBox h3 = new HBox(5);
        HBox h4 = new HBox(5);

        h0.getChildren().addAll(l0,webcamsSelection());
        h1.getChildren().addAll(l1,ip_adress);
        h2.getChildren().addAll(l2,port);
        h3.getChildren().addAll(l3,comboBox);
        h4.getChildren().addAll(l4,name);

        h0.setAlignment(Pos.BASELINE_RIGHT);
        h4.setAlignment(Pos.BASELINE_RIGHT);
        h2.setAlignment(Pos.BASELINE_RIGHT);
        h1.setAlignment(Pos.BASELINE_RIGHT);
        h3.setAlignment(Pos.BASELINE_RIGHT);

        Button send = new Button("Connect and start!");
        VBox v = new VBox(20);

        v.setAlignment(Pos.CENTER);

        v.getChildren().addAll(h4,h0,h1,h2,h3,send);
        v.setAlignment(Pos.CENTER);



        send.setOnAction(event -> {

            int prt = Integer.parseInt(port.getText());
            String ip = ip_adress.getText();
            String tempName = name.getText();

            if (!ServerConnector.getInstance().connectToServer(ip,prt)) return;
            String [] selectedResolution = comboBox.getValue().toString().split(" x ");
            Gameboard g = new Gameboard(stage,Double.parseDouble(selectedResolution[0]),Double.parseDouble(selectedResolution[1]));

            ServerConnector.getInstance().setGameboard(g);

            webcam.ServerConnection.getInstance().connect(tempName,ip,prt+1);

            if (webcamBox.getSelectionModel().getSelectedItem()!="Keine Kamera") WebcamHandler.getInstance().setWebcamByName(webcamBox.getSelectionModel().getSelectedItem());

            stage.setScene(g.scene);
            stage.centerOnScreen();
            ServerConnector.getInstance().startReceiving();
            g.show(stage);
        });


        v.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                System.out.println("Wir drücken den Knopf");
                send.fire();
            }
        });
        scene = new Scene(v);
    }

    private ComboBox<String> webcamsSelection() {
        webcamBox = new ComboBox<String>();
        for(int i=0;i<Webcam.getWebcams().size();i++){
            webcamBox.getItems().add(Webcam.getWebcams().get(i).getName());
        }
        webcamBox.getItems().add("Keine Kamera");
        webcamBox.getSelectionModel().select(0);
        return webcamBox;
    }

    public void show(Stage stage){
        stage.setScene(scene);
        stage.show();
    }
}
