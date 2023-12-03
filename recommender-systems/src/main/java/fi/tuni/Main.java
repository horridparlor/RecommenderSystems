package fi.tuni;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.HashSet;

public class Main extends Application {
    private static HashMap<Integer, User> users;
    private static Enums.SimilarityFunction simFunc = Enums.SimilarityFunction.PEARSON;
    private static Enums.GroupAggregationMethod groupAggMeth = Enums.GroupAggregationMethod.AVERAGE;
    private static Enums.WhyNotMethod whyNotMeth = Enums.WhyNotMethod.Atomic;

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
    public static Enums.GroupAggregationMethod getGroupAggregationMethod() {
        if (groupAggMeth == null) {
            groupAggMeth = Enums.GroupAggregationMethod.AVERAGE;
        }
        return groupAggMeth;
    }

    public static String toggleGroupAggregationMethod() {
        switch (groupAggMeth) {
            case AVERAGE -> {
                groupAggMeth = Enums.GroupAggregationMethod.LEAST_MISERY;
            }
            case LEAST_MISERY -> {
                groupAggMeth = Enums.GroupAggregationMethod.BALANCED;
            }
            case BALANCED -> {
                groupAggMeth = Enums.GroupAggregationMethod.AVERAGE;
            }
        }
        return getGroupAggregationMethodName();
    }
    public static String getGroupAggregationMethodName() {
        switch (groupAggMeth) {
            case AVERAGE -> {
                return Messages.GROUP_AGGREGATION_METHOD_AVERAGE;
            }
            case BALANCED -> {
                return Messages.GROUP_AGGREGATION_METHOD_BALANCED;
            }
            case LEAST_MISERY -> {
                return Messages.GROUP_AGGREGATION_METHOD_LEAST_MISERY;
            }
        }
        return Messages.GROUP_AGGREGATION_METHOD_AVERAGE;
    }

    public static Enums.WhyNotMethod getWhyNotMethod() {
        if (whyNotMeth == null) {
            whyNotMeth = Enums.WhyNotMethod.Atomic;
        }
        return whyNotMeth;
    }

    public static String toggleWhyNotMethod() {
        switch (whyNotMeth) {
            case Atomic -> {
                whyNotMeth = Enums.WhyNotMethod.Atomic;
            }
            case Group -> {
                whyNotMeth = Enums.WhyNotMethod.Group;
            }
            case Position -> {
                whyNotMeth = Enums.WhyNotMethod.Position;
            }
        }
        return getGroupWhyNotMethodName();
    }
    public static String getGroupWhyNotMethodName() {
        switch (whyNotMeth) {
            case Atomic -> {
                return Messages.WHY_NOT_METHOD_ATOMIC;
            }
            case Group -> {
                return Messages.WHY_NOT_METHOD_GROUP;
            }
            case Position -> {
                return Messages.WHY_NOT_METHOD_POSITION;
            }
        }
        return Messages.WHY_NOT_METHOD_ATOMIC;
    }
}