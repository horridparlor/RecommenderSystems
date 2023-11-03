package fi.tuni;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.HashSet;

public class Main extends Application {
    private static HashMap<Integer, User> users;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(Messages.APP_NAME);
        primaryStage.setScene(new MenuScene(primaryStage));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static HashMap<Integer, User> getUsers() {
        if (users == null) {
            users = DataParser.getUsers();
        }
        return users;
    }
}