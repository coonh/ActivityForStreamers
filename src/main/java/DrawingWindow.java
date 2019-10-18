import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class DrawingWindow extends Canvas {
    
    GraphicsContext myGC;

    public DrawingWindow(){
        super(700,700);
        
        
        myGC = this.getGraphicsContext2D();
        

        myGC.setFill(Color.BLACK);
        myGC.setLineWidth(8);
        activateEventHandler();
    }
    
    
    public void activateEventHandler(){
        this.setOnMousePressed(event -> {
            if (event.getButton().equals(MouseButton.SECONDARY)){
                myGC.moveTo(event.getX(), event.getY());
                myGC.setStroke(Color.WHITE);
                myGC.setLineWidth(100);

            }else {
                myGC.setLineWidth(8);
                myGC.moveTo(event.getX(), event.getY());
                myGC.setStroke(Color.BLACK);
            }
        });

        this.setOnMouseReleased(event -> {
            myGC.beginPath();
        });

        this.setOnMouseDragged(event -> {

            //this.getGraphicsContext2D().moveTo(event.getX()-1,event.getY()-1);
            myGC.lineTo(event.getX(), event.getY());
            myGC.stroke();

        });
    }


}
