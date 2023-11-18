package fi.tuni;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;
import java.util.function.Consumer;

public abstract class MyScene extends Scene {
    private static final int X = 1280;
    private static final int Y = 720;
    private static final int SPACING = 10;
    private static boolean isOperating = false;
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
        Button toggle = new Button(Main.getSimilarityFunctionName());
        Consumer<ActionEvent> update = event -> {
            toggle.setText(Main.toggleSimilarityFunction());
            Exercise1.updatePredictions(Messages.EMPTY);
        };
        addToggle(container, Messages.CHOOSE_SIMILARITY_FUNCTION, toggle, update);
    }
    protected static void addGroupAggregationMethodToggle(VBox container) {
        Button toggle = new Button(Main.getGroupAggregationMethodName());
        Consumer<ActionEvent> update = event -> {
            toggle.setText(Main.toggleGroupAggregationMethod());
            Exercise2.updatePredictions();
        };
        addToggle(container, Messages.CHOOSE_GROUP_AGGREGATION_METHOD, toggle, update);
    }
    protected static void addToggle(VBox container, String promptMessage, Button toggle, Consumer<ActionEvent> update) {
        HBox row = new HBox(Constants.ROW_MARGIN);
        row.setAlignment(Pos.CENTER);
        Text prompt = new Text(promptMessage);
        toggle.setOnAction(update::accept);
        row.getChildren().addAll(prompt, toggle);
        container.getChildren().add(row);
    }

    protected static HBox getUserSelector(TextField userInput) {
        return getUserSelector(new ArrayList<>(Collections.singletonList(userInput)));
    }

    protected static HBox getUserSelector(ArrayList<TextField> userInputs) {
        HBox row = new HBox(Constants.ROW_MARGIN);
        row.setAlignment(Pos.CENTER);
        Text prompt = new Text(userInputs.size() == 1 ? Messages.CHOOSE_USER : Messages.CHOOSE_USERS);
        row.getChildren().add(prompt);
        for (TextField userInput : userInputs) {
            userInput.setMaxWidth(Constants.NUMBER_INPUT_WIDTH);
            row.getChildren().add(userInput);
        }
        return row;
    }

    protected static Boolean doIgnoreTextField(String old, TextField input) {
        String value = input.getText();
        if (value.equals(old)) {
            return true;
        } else if (value.trim().isEmpty()) {
            input.setText(Messages.EMPTY);
            return true;
        }
        return false;
    }

    protected static int forceUserId(String old, TextField input) {
        if (doIgnoreTextField(old, input)) {
            return Constants.NO_ID;
        }
        String value = input.getText();
        try {
            int userId = Math.max(Constants.USER_FIRST, (Math.min(Constants.USER_LAST, Integer.parseInt(value))));
            input.setText(String.valueOf(userId));
            return userId;
        } catch (NumberFormatException e) {
            input.setText(Messages.EMPTY);
            return Constants.NO_ID;
        }
    }

    protected static HashSet<Integer> forceUserIds(String old, TextField input, ArrayList<TextField> allInputs) {
        HashSet<Integer> userIds = new HashSet<>();
        if (isOperating || doIgnoreTextField(old, input)) {
            return userIds;
        }
        isOperating = true;
        for(TextField userInput : allInputs) {
            int userId = forceUserId(userInput == input ? old : Messages.EMPTY, userInput);
            if (userId != Constants.NO_ID) {
                userIds.add(userId);
            }
        }
        System.out.println(idsToString(userIds) + Messages.SEPARATOR + inputsToString(allInputs));
        Iterator<Integer> userIdIterator = userIds.iterator();
        for(TextField userInput : allInputs) {
            userInput.setText(userIdIterator.hasNext() ? userIdIterator.next().toString() : Messages.EMPTY);
        }
        isOperating = false;
        return userIds;
    }

    protected static <T> String idsToString(Collection<T> ids) {
        StringBuilder result = new StringBuilder(Messages.ARRAY_START);
        boolean isFirst = true;
        for (T id : ids) {
            result.append(isFirst ? Messages.EMPTY : Messages.SEPARATOR).append(id);
            if (isFirst) {
                isFirst = false;
            }
        }
        result.append(Messages.ARRAY_END);
        return result.toString();
    }

    protected static String inputsToString(ArrayList<TextField> allInputs) {
        StringBuilder result = new StringBuilder(Messages.ARRAY_START);
        boolean isFirst = true;
        for (TextField input : allInputs) {
            result.append(isFirst ? Messages.EMPTY : Messages.SEPARATOR).append(StringTricks.toSerializeString(input.getText()));
            if (isFirst) {
                isFirst = false;
            }
        }
        result.append(Messages.ARRAY_END);
        return result.toString();
    }
}
