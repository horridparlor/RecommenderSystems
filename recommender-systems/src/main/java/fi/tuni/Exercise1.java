package fi.tuni;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Exercise1 extends MyScene {
    private static TextField userInput;
    private static Text usersResult;
    private static Text moviesResult;
    public Exercise1(Stage primaryStage) {
        super(getContainer(), primaryStage);
    }

    private static VBox getContainer() {
        VBox container = new VBox();
        getCoupleRows(container);
        addCountOfRows(container);
        addSeparator(container);
        addUserRecommender(container);
        return container;
    }

    public static void addCountOfRows(VBox container) {
        Text countOfRows = new Text(Messages.COUNT_OF_ROWS + DataParser.countRows()
            + Messages.PERIOD);
        container.getChildren().add(countOfRows);
    }

    public static void getCoupleRows(VBox container) {
        int ratings_left = 3;
        final int LAST_ROW = 1;
        for (Map.Entry<Integer, Rating> rating : Main.getUsers().get(1).getRatings().entrySet()) {
            container.getChildren().add(new Text(rating.getValue().toString() +
                    (ratings_left == LAST_ROW ? Messages.TO_CONTINUE : Messages.EMPTY)));
            ratings_left--;
            if (ratings_left == 0) {break;}
        }
    }

    public static void addUserRecommender(VBox container) {
        usersResult = new Text(Messages.SIMILAR_USERS + Messages.ARRAY);
        moviesResult = new Text(Messages.RELEVANT_MOVIES + Messages.ARRAY);
        userInput = new TextField();
        makeUserbound(userInput);
        addSimilarityFunctionToggle(container);
        container.getChildren().addAll(getUserSelector(userInput), usersResult, moviesResult);
    }

    private static void makeUserbound(TextField userInput) {
        userInput.textProperty().addListener((observable, old, value) -> {
            updatePredictions(old);
        });
    }
    public static void updatePredictions(String old) {
        int userId = forceUserId(old, userInput);
        if (userId == Constants.NO_ID) {
            return;
        }
        ArrayList<Similarity> similarUsers = Algorithm.getSimilarUsers(userId);
        usersResult.setText(Messages.SIMILAR_USERS + onlyIds(similarUsers));
        moviesResult.setText(Messages.RELEVANT_MOVIES + onlyIds(Algorithm.getRelevantMovies(userId, similarUsers)));
    }

    private static ArrayList<Integer> onlyIds(ArrayList<? extends Identifiable> items) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (Identifiable item : items) {
            ids.add(item.getId());
        }
        return ids;
    }
}
