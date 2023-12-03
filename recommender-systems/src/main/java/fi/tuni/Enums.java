package fi.tuni;

public class Enums {
    public enum Exercise {
        EXERCISE_1,
        EXERCISE_2,
        EXERCISE_3,
        EXERCISE_4
    }

    public enum BackOption {
        BACK,
        QUIT
    }

    public enum SimilarityFunction {
        COSINE,
        PEARSON
    }
    public enum GroupAggregationMethod {
        AVERAGE,
        BALANCED,
        LEAST_MISERY
    }

    public enum WhyNotMethod {
        Atomic,
        Group,
        Position
    }
}
