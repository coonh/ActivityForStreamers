import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONStringer;


public class WordInterface {
    private Scene wordInputScene;

    public WordInterface(Stage stage, Scene scene){

        TextField input = new TextField();
        Button enter = new Button("Eingabe");
        ChoiceBox<Integer> value = new ChoiceBox<>();
        value.getItems().addAll(3,4,5);
        ChoiceBox<String> activity = new ChoiceBox<String>();
        activity.getItems().addAll("erklären", "zeichnen", "darstellen");
        value.setValue(3);
        activity.setValue("erklären");

        HBox line1 = new HBox(30);
        line1.getChildren().addAll(input,enter);
        HBox line2 = new HBox(15);
        line2.getChildren().addAll(new Label("Aktivität: "),activity,new Label("Wert: "),value);

        enter.setOnAction(e ->{
            System.out.println("Button pressed");
            if (input.getText()=="")return;
            byThePushOfTheButton(activity.getValue().toString(), value.getValue(), input.getText());
            input.setText("");
        });
        line1.setOnKeyPressed(event -> {
            if(event.getCode()== KeyCode.ENTER){
                enter.fire();
            }
        });
        Button exit = new Button("Exit");
        exit.setOnAction(event -> {
            stage.setScene(scene);
        });
        line2.getChildren().add(exit);
        VBox center = new VBox(30);
        center.getChildren().addAll(line1,line2);
        BorderPane layout = new BorderPane();
        layout.setCenter(center);

       wordInputScene = new Scene(layout);
    }

    private void byThePushOfTheButton(String activity, int value, String text) {
        switch (activity){
            case "erklären":
                addWord("e",value,text);
                break;
            case "zeichnen":
                addWord("d",value,text);
                break;
            case "darstellen":
                addWord("p",value,text);
                break;
        }
    }

    public Scene getWordInputScene() {
        return wordInputScene;
    }

    private void addWord(String activity, int value, String word){
        String message =  new JSONStringer().object()
                .key("event").value("addword")
                .key("activity").value(activity)
                .key("value").value(value).key("word").value(word)
                .endObject().toString();

        System.out.println("Sending message: " +message);

        ServerConnector.getInstance().sendMessage(message);
    }
}
