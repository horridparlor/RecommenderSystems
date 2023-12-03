package fi.tuni;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Exercise4 extends MyScene {
    private static TextField userSelection1;
    private static TextField userSelection2;
    private static TextField userSelection3;
    private static Text recommendations;

    public Exercise4(Stage primaryStage) {
        super(getContainer(), primaryStage);
    }

    private static VBox getContainer() {
        VBox container = new VBox();
        container.getChildren().add(new Text(Messages.TODO));
        addWhyNotMethodToggle(container);
        addGroupSelection(container);
        return container;
    }

    private static void addGroupSelection(VBox container) {
        userSelection1 = new TextField();
        userSelection2 = new TextField();
        userSelection3 = new TextField();
        recommendations = new Text(Messages.ARRAY);
        ArrayList<TextField> userSelections = getUserSelections();
        container.getChildren().addAll(getUserSelector(userSelections), recommendations);
        makeUserbound(userSelections);
    }

    private static ArrayList<TextField> getUserSelections() {
        return new ArrayList<>(Arrays.asList(userSelection1, userSelection2, userSelection3));
    }

    private static void makeUserbound(ArrayList<TextField> userSelections) {
        for (TextField userInput : userSelections) {
            userInput.textProperty().addListener((observable, old, value) -> {
                updatePredictions(old, userInput);
            });
        }
    }

    public static void updatePredictions(String old, TextField userInput) {
        HashSet<Integer> userIds = forceUserIds(old, userInput, getUserSelections());
        if (userIds.isEmpty()) {
            recommendations.setText(Messages.ARRAY);
            return;
        }
        ArrayList<User> users = Algorithm.findUsers(userIds);

        recommendations.setText(idsToString(getMovies(users)));
    }

    public static ArrayList<Integer> getMovies(ArrayList<User> users) {
        Enums.WhyNotMethod method = Main.getWhyNotMethod();
        switch (method) {
            case Atomic -> {
                return Calculator.whyNotAtomic(users, Constants.MAX_ITEMS);
            }
            case Group -> {
                return Calculator.whyNotGroup(users, Constants.MAX_ITEMS);
            }
            case Position -> {
                return Calculator.whyNotPosition(users, Constants.MAX_ITEMS);
            }
        }
        return null;
    }

    public static void updatePredictions() {
        updatePredictions(Messages.EMPTY, userSelection1);
    }
}
