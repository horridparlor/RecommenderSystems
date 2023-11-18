package fi.tuni;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;



public class Calculator {
    public static double getPearsonCorrelation(User user1, User user2) {
        Set<Integer> commonMovies = new HashSet<>(user1.getRatings().keySet());
        commonMovies.retainAll(user2.getRatings().keySet());

        if (commonMovies.size() < 2) {
            return 0.0;
        }

        double[] user1Ratings = new double[commonMovies.size()];
        double[] user2Ratings = new double[commonMovies.size()];
        int i = 0;
        for (int movieId : commonMovies) {
            user1Ratings[i] = user1.getRatings().get(movieId).getScore();
            user2Ratings[i] = user2.getRatings().get(movieId).getScore();
            i++;
        }

        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        System.out.println(Arrays.toString(user1Ratings) + " " + Arrays.toString(user2Ratings));
        double correlation = pearsonsCorrelation.correlation(user1Ratings, user2Ratings);
        return Double.isNaN(correlation) ? 0.0 : correlation;
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
            double ratingUser1 = user1.getRatings().get(movieId).getScore();
            double ratingUser2 = user2.getRatings().get(movieId).getScore();

            numerator += (ratingUser1 - meanUser1) * (ratingUser2 - meanUser2);
            denominatorUser1 += Math.pow(ratingUser1 - meanUser1, 2);
            denominatorUser2 += Math.pow(ratingUser2 - meanUser2, 2);
        }

        if (denominatorUser1 == 0 || denominatorUser2 == 0) {
            return 0.0;
        }

        return numerator / (Math.sqrt(denominatorUser1) * Math.sqrt(denominatorUser2));
    }

    public static ArrayList<Integer> aggregateAverage(List<User> groupUsers, int numRecommendations) {
        Map<Integer, Double> movieScores = new HashMap<>();

        Map<Integer, Integer> movieCounts = new HashMap<>();
    
        for (User user : groupUsers) {
            ArrayList<Similarity> similarUsers = Algorithm.getSimilarUsers(user.getId());
            ArrayList<MovieRecommendation> recommendations = Algorithm.getRelevantMovies(user.getId(), similarUsers);
    
            for (MovieRecommendation recommendation : recommendations) {
                int movieId = recommendation.getId();
                double rating = recommendation.getRelevancy();

                movieScores.put(movieId, movieScores.getOrDefault(movieId, 0.0) + rating);
                movieCounts.put(movieId, movieCounts.getOrDefault(movieId, 0) + 1);
            }
        }

        Map<Integer, Double> movieAverages = new HashMap<>();
        for (int movieId : movieScores.keySet()) {
            double sum = movieScores.get(movieId);
            int count = movieCounts.get(movieId);
            double average = sum / count;
            movieAverages.put(movieId, average);
        }

        List<Integer> sortedRecommendations = movieAverages.entrySet().stream()
                .sorted((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    
        return new ArrayList<>(sortedRecommendations.subList(0, Math.min(numRecommendations, sortedRecommendations.size())));
    }

    
    public static ArrayList<Integer> aggregateLeastMisery(List<User> groupUsers, int numRecommendations) {
        Map<Integer, Double> movieScores = new HashMap<>();
    
        for (User user : groupUsers) {
            ArrayList<Similarity> similarUsers = Algorithm.getSimilarUsers(user.getId());
            ArrayList<MovieRecommendation> recommendations = Algorithm.getRelevantMovies(user.getId(), similarUsers);
    
            for (MovieRecommendation recommendation : recommendations) {
                int movieId = recommendation.getId();
                double rating = recommendation.getRelevancy();

                double currentMinimum = movieScores.getOrDefault(movieId, Double.MAX_VALUE);
                if (rating < currentMinimum) {
                    movieScores.put(movieId, rating);
                }
            }
        }

        List<Integer> sortedRecommendations = movieScores.entrySet().stream()
                .sorted((entry1, entry2) -> Double.compare(entry1.getValue(), entry2.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    
        return new ArrayList<>(sortedRecommendations.subList(0, Math.min(numRecommendations, sortedRecommendations.size())));
    }
    
    



    private static double calculateMeanRating(Map<Integer, Rating> userRatings, Set<Integer> commonMovies) {
        double sum = 0;
        for (int movieId : commonMovies) {
            sum += userRatings.get(movieId).getScore();
        }
        return sum / commonMovies.size();
    }
}
