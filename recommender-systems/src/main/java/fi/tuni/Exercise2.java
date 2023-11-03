package fi.tuni;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Exercise2 extends MyScene {
    public Exercise2(Stage primaryStage) {
        super(getContainer(), primaryStage);
    }

    private static VBox getContainer() {
        VBox container = new VBox();
        container.getChildren().add(new Text(Messages.TODO));
        return container;
    }
}
