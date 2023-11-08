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

    public static List<Integer> aggregateAverage(List<Integer> groupUsers, int numRecommendations) {
        // Create a map to store the sum of ratings for each movie
        Map<Integer, Double> movieScores = new HashMap<>();
    
        // Count the number of users who have rated each movie
        Map<Integer, Integer> movieCounts = new HashMap<>();
    
        for (int userId : groupUsers) {
            // Get individual recommendations for the user
            ArrayList<Similarity> similarUsers = Algorithm.getSimilarUsers(userId);
            ArrayList<MovieRecommendation> recommendations = Algorithm.getRelevantMovies(userId, similarUsers);
    
            for (MovieRecommendation recommendation : recommendations) {
                int movieId = recommendation.getId(); // Extract the movie ID from MovieRecommendation
                double rating = recommendation.getRelevancy(); // Get the predicted rating from MovieRecommendation
    
                // Update the sum of ratings for the movie
                movieScores.put(movieId, movieScores.getOrDefault(movieId, 0.0) + rating);
                // Increment the count for the movie
                movieCounts.put(movieId, movieCounts.getOrDefault(movieId, 0) + 1);
            }
        }
    
        // Calculate the average rating for each movie
        Map<Integer, Double> movieAverages = new HashMap<>();
        for (int movieId : movieScores.keySet()) {
            double sum = movieScores.get(movieId);
            int count = movieCounts.get(movieId);
            double average = sum / count;
            movieAverages.put(movieId, average);
        }
    
        // Sort the aggregated recommendations by average rating in descending order
        List<Integer> sortedRecommendations = movieAverages.entrySet().stream()
                .sorted((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    
        return sortedRecommendations.subList(0, Math.min(numRecommendations, sortedRecommendations.size()));
    }
    
    public static List<Integer> aggregateLeastMisery(List<Integer> groupUsers, int numRecommendations) {
        // Create a map to store the minimum ratings for each movie
        Map<Integer, Double> movieScores = new HashMap<>();
    
        for (int userId : groupUsers) {
            // Get individual recommendations for the user
            ArrayList<Similarity> similarUsers = Algorithm.getSimilarUsers(userId);
            ArrayList<MovieRecommendation> recommendations = Algorithm.getRelevantMovies(userId, similarUsers);
    
            for (MovieRecommendation recommendation : recommendations) {
                int movieId = recommendation.getId(); // Extract the movie ID from MovieRecommendation
                double rating = recommendation.getRelevancy(); // Get the predicted rating from MovieRecommendation
    
                // Update the minimum rating for the movie
                double currentMinimum = movieScores.getOrDefault(movieId, Double.MAX_VALUE);
                if (rating < currentMinimum) {
                    movieScores.put(movieId, rating);
                }
            }
        }
    
        // Sort the aggregated recommendations by the minimum rating in ascending order
        List<Integer> sortedRecommendations = movieScores.entrySet().stream()
                .sorted((entry1, entry2) -> Double.compare(entry1.getValue(), entry2.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    
        return sortedRecommendations.subList(0, Math.min(numRecommendations, sortedRecommendations.size()));
    }
    
    



    private static double calculateMeanRating(Map<Integer, Rating> userRatings, Set<Integer> commonMovies) {
        double sum = 0;
        for (int movieId : commonMovies) {
            sum += userRatings.get(movieId).getScore();
        }
        return sum / commonMovies.size();
    }
}
