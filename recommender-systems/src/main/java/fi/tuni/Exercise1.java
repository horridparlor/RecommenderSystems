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
        HBox row = new HBox(Constants.ROW_MARGIN);
        row.setAlignment(Pos.CENTER);
        Text prompt = new Text(Messages.CHOOSE_USER);
        userInput = new TextField();
        row.getChildren().addAll(prompt, userInput);
        usersResult = new Text(Messages.SIMILAR_USERS + Messages.ARRAY);
        moviesResult = new Text(Messages.RELEVANT_MOVIES + Messages.ARRAY);
        makeUserbound(userInput);
        userInput.setMaxWidth(Constants.NUMBER_INPUT_WIDTH);
        addSimilarityFunctionToggle(container);
        container.getChildren().addAll(row, usersResult, moviesResult);
    }

    private static void makeUserbound(TextField userInput) {
        userInput.textProperty().addListener((observable, old, value) -> {
            updatePredictions(old);
        });
    }
    public static void updatePredictions(String old) {
        String value = userInput.getText();
        if (value.equals(old)) {
            return;
        } else if (value.trim().isEmpty()) {
            userInput.setText(Messages.EMPTY);
            return;
        }
        try {
            int userId = Math.max(Constants.USER_FIRST, (Math.min(Constants.USER_LAST, Integer.parseInt(value))));
            ArrayList<Similarity> similarUsers = Algorithm.getSimilarUsers(userId);
            usersResult.setText(Messages.SIMILAR_USERS + onlyIds(similarUsers));
            moviesResult.setText(Messages.RELEVANT_MOVIES + onlyIds(Algorithm.getRelevantMovies(userId, similarUsers)));
            userInput.setText(String.valueOf(userId));
        } catch (NumberFormatException e) {
            userInput.setText(Messages.EMPTY);
        }
    }

    private static ArrayList<Integer> onlyIds(ArrayList<? extends Identifiable> items) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (Identifiable item : items) {
            ids.add(item.getId());
        }
        return ids;
    }
}
