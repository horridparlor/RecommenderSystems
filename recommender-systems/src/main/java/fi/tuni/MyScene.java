package fi.tuni;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
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
        Separator separator = new Separator();
        separator.setPadding(new Insets(5, 0, 5, 0));
        container.getChildren().add(separator);
    }

}
