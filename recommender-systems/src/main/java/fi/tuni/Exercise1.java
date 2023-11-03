package fi.tuni;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Exercise1 extends MyScene {
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
        Text prompt = new Text(Messages.CHOOSE_USER);
        TextField userInput = new TextField();
        Text users = new Text(Messages.SIMILAR_USERS + Messages.ARRAY);
        Text movies = new Text(Messages.RELEVANT_MOVIES + Messages.ARRAY);
        makeUserbound(userInput, users, movies);
        userInput.setMaxWidth(Constants.NUMBER_INPUT_WIDTH);
        container.getChildren().addAll(prompt, userInput, users, movies);
    }

    private static void makeUserbound(TextField userInput, Text users, Text movies) {
        userInput.textProperty().addListener((observable, old, value) -> {
            if (value.equals(old)) {
                return;
            } else if (value.trim().isEmpty()) {
                userInput.setText(Messages.EMPTY);
                return;
            }
            try {
                int number = Math.max(Constants.USER_FIRST, (Math.min(Constants.USER_LAST, Integer.parseInt(value))));
                System.out.println(number);
                users.setText(Messages.SIMILAR_USERS + onlyIds(Algorithm.getSimilarUsers(number)));
                userInput.setText(String.valueOf(number));
            } catch (NumberFormatException e) {
                userInput.setText(Messages.EMPTY);
            }
        });
    }

    private static ArrayList<Integer> onlyIds(ArrayList<User> users) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (User user : users) {
            ids.add(user.getId());
        }
        return ids;
    }
}
