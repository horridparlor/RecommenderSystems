package fi.tuni;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.HashSet;

public class Main extends Application {
    private static HashMap<Integer, User> users;
    private static Enums.SimilarityFunction simFunc = Enums.SimilarityFunction.PEARSON;

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
    public static Enums.SimilarityFunction getSimilarityFunction() {
        if (simFunc == null) {
            simFunc = Enums.SimilarityFunction.PEARSON;
        }
        return simFunc;
    }
    public static String toggleSimilarityFunction() {
        switch (simFunc) {
            case COSINE -> {
                simFunc = Enums.SimilarityFunction.PEARSON;
            }
            case PEARSON -> {
                simFunc = Enums.SimilarityFunction.COSINE;
            }
        }
        return getSimilarityFunctionName();
    }
    public static String getSimilarityFunctionName() {
        switch (simFunc) {
            case COSINE -> {
                return Messages.SIMILARITY_FUNCTION_COSINE;
            }
            case PEARSON -> {
                return Messages.SIMILARITY_FUNCTION_PEARSON;
            }
        }
        return Messages.SIMILARITY_FUNCTION_PEARSON;
    }
}