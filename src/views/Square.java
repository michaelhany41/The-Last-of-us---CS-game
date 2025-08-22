package views;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Square extends StackPane {

    private static final int SQUARE_SIZE = 30;

    public Square() {
        Rectangle border = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
        border.setFill(Color.WHITE);
        border.setStroke(Color.BLACK);

        getChildren().add(border);
    }
}