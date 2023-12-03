package fi.tuni;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuScene extends MyScene {

    public MenuScene(Stage primaryStage) {
        super(getContainer(primaryStage), primaryStage, Enums.BackOption.QUIT);
    }

    protected static VBox getContainer(Stage primaryStage) {
        Text counterText = new Text(Messages.CHOOSE_EXERCISE);

        VBox container = new VBox(counterText);

        for (Enums.Exercise exercise : Enums.Exercise.values()) {
            getExerciseButton(exercise, container, primaryStage);
        }
        return container;
    }

    private static void getExerciseButton(Enums.Exercise exercise, VBox container, Stage primaryStage) {
        Button button = new Button();
        String name = StringTricks.serialize(exercise.name());
        button.setText(name);

        button.setOnAction(event -> {
            primaryStage.setScene(routeExercise(exercise, primaryStage));
        });
        container.getChildren().add(button);
    }

    private static MyScene routeExercise(Enums.Exercise exercise, Stage primaryStage) {
        return switch (exercise) {
            case EXERCISE_1 -> new Exercise1(primaryStage);
            case EXERCISE_2 -> new Exercise2(primaryStage);
            case EXERCISE_3 -> new Exercise3(primaryStage);
            case EXERCISE_4 -> new Exercise4(primaryStage);
            default -> new Exercise1(primaryStage);
        };
    }
}
