package fi.tuni;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Calculator {
    public static double getPearsonCorrelation(User user1, User user2) {
        Set<Integer> commonMovies = new HashSet<>(user1.getRatings().keySet());
        commonMovies.retainAll(user2.getRatings().keySet());

        if (commonMovies.size() == 0) {
            return 0.0;
        }


        double[] user1Ratings = new double[commonMovies.size()];
        double[] user2Ratings = new double[commonMovies.size()];
        int i = 0;
        for (int movieId : commonMovies) {
            user1Ratings[i] = user1.getRatings().get(movieId);
            user2Ratings[i] = user2.getRatings().get(movieId);
            i++;
        }

        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double correlation = pearsonsCorrelation.correlation(user1Ratings, user2Ratings);

        return correlation;
    }

    public static double getAdjustedCosineSimilarity(User user1, User user2) {
        Set<Integer> commonMovies = new HashSet<>(user1.getRatings().keySet());
        commonMovies.retainAll(user2.getRatings().keySet());

        if (commonMovies.isEmpty()) {
            return 0.0;
        }

        double meanUser1 = calculateMeanRating(user1.getRatings(), commonMovies);
        double meanUser2 = calculateMeanRating(user2.getRatings(), commonMovies);

        double numerator = 0;
        double denominatorUser1 = 0;
        double denominatorUser2 = 0;

        for (int movieId : commonMovies) {
            double ratingUser1 = user1.getRatings().get(movieId);
            double ratingUser2 = user2.getRatings().get(movieId);

            numerator += (ratingUser1 - meanUser1) * (ratingUser2 - meanUser2);
            denominatorUser1 += Math.pow(ratingUser1 - meanUser1, 2);
            denominatorUser2 += Math.pow(ratingUser2 - meanUser2, 2);
        }

        if (denominatorUser1 == 0 || denominatorUser2 == 0) {
            return 0.0;
        }

        double similarity = numerator / (Math.sqrt(denominatorUser1) * Math.sqrt(denominatorUser2));
        return similarity;
    }

    private static double calculateMeanRating(Map<Integer, Double> userRatings, Set<Integer> commonMovies) {
        double sum = 0;
        for (int movieId : commonMovies) {
            sum += userRatings.get(movieId);
        }
        return sum / commonMovies.size();
    }
}
