package fi.tuni;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public abstract class MyScene extends Scene {
    private static final int X = 1280;
    private static final int Y = 720;
    private static final int SPACING = 10;
    public MyScene(VBox container, Stage primaryStage) {
        this(container, primaryStage, Enums.BackOption.BACK);
    }
    public MyScene(VBox container, Stage primaryStage, Enums.BackOption backOption) {
        super(container, X, Y);
        container.setSpacing(SPACING);
        if (backOption == Enums.BackOption.QUIT) {
            addQuitButton(container);
        } else {
            addBackButton(container, primaryStage);
        }
        container.setAlignment(Pos.CENTER);
        this.getStylesheets().add("styles.css");
    }
    protected static void addQuitButton(VBox container) {
        Button quit = new Button(Messages.QUIT);
        quit.setOnAction(event -> Platform.exit());
        container.getChildren().add(quit);
    }
    protected static void addBackButton(VBox container, Stage primaryStage) {
        Button quit = new Button(Messages.BACK);
        quit.setOnAction(event -> primaryStage.setScene(new MenuScene(primaryStage)));
        container.getChildren().add(quit);
    }

    protected static void addSeparator(VBox container) {
        container.getChildren().add(newSeparator());
    }

    protected static Separator newSeparator() {
        Separator separator = new Separator();
        separator.setPadding(new Insets(5, 0, 5, 0));
        return separator;
    }

    protected static void addSimilarityFunctionToggle(VBox container) {
        HBox row = new HBox(Constants.ROW_MARGIN);
        row.setAlignment(Pos.CENTER);
        Text prompt = new Text(Messages.CHOOSE_SIMILARITY_FUNCTION);
        Button toggle = new Button(Main.getSimilarityFunctionName());
        toggle.setOnAction(event -> {
            toggle.setText(Main.toggleSimilarityFunction());
            Exercise1.updatePredictions(Messages.EMPTY);
        });
        row.getChildren().addAll(prompt, toggle);
        container.getChildren().add(row);
    }

}
