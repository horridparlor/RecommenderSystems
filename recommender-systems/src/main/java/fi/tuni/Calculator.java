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
        getMovieScores(groupUsers, movieScores, movieCounts);

        Map<Integer, Double> movieAverages = new HashMap<>();
        for (int movieId : movieScores.keySet()) {
            double sum = movieScores.get(movieId);
            int count = movieCounts.get(movieId);
            double average = sum / count;
            movieAverages.put(movieId, average);
        }

        return sortRecommendations(movieAverages, numRecommendations);
    }
    private static void getMovieScores(List<User> groupUsers, Map<Integer, Double> movieScores, Map<Integer, Integer> movieCounts) {
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
    }

    private static ArrayList<Integer> sortRecommendations(Map<Integer, Double> scores, int numRecommendations) {
        List<Integer> sortedRecommendations = scores.entrySet().stream()
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

        return sortRecommendations(movieScores, numRecommendations);
    }

    public static ArrayList<Integer> aggregateBalanced(List<User> groupUsers, int numRecommendations) {
        Map<Integer, Double> movieScores = new HashMap<>();
        Map<Integer, Integer> movieCounts = new HashMap<>();
        Map<Integer, Double> movieScoreVariance = new HashMap<>();

        getMovieScores(groupUsers, movieScores, movieCounts);

        for (User user : groupUsers) {
            for (MovieRecommendation recommendation : Algorithm.getRelevantMovies(user.getId(), Algorithm.getSimilarUsers(user.getId()))) {
                int movieId = recommendation.getId();
                double rating = recommendation.getRelevancy();
                double average = movieScores.get(movieId) / movieCounts.get(movieId);
                movieScoreVariance.put(movieId, movieScoreVariance.getOrDefault(movieId, 0.0) + Math.pow(rating - average, 2));
            }
        }

        Map<Integer, Double> movieBalancedScores = new HashMap<>();
        for (int movieId : movieScores.keySet()) {
            double average = movieScores.get(movieId) / movieCounts.get(movieId);
            double variance = movieScoreVariance.get(movieId) / movieCounts.get(movieId);
            double balancedScore = average - variance;
            movieBalancedScores.put(movieId, balancedScore);
        }

        return sortRecommendations(movieBalancedScores, numRecommendations);
    }



// vaihtoehto 2

    public static ArrayList<Integer> aggregateLeastMiseryWithDisagreements(List<User> groupUsers, int numRecommendations) {

        Map<Integer, Double> movieScores = new HashMap<>();

        for (User user : groupUsers) {
            ArrayList<Similarity> similarUsers = Algorithm.getSimilarUsers(user.getId());
            ArrayList<MovieRecommendation> recommendations = Algorithm.getRelevantMovies(user.getId(), similarUsers);

            for (MovieRecommendation recommendation : recommendations) {
                int movieId = recommendation.getId();
                double rating = recommendation.getRelevancy();

                // Calculate the disagreement factor based on balancing rule
                double disagreementFactor = calculateDisagreementFactor(user, groupUsers, movieId);

                // Update the movie score by considering disagreements
                double currentScore = movieScores.getOrDefault(movieId, Double.MAX_VALUE);
                movieScores.put(movieId, Math.min(currentScore, rating * disagreementFactor));
            }
        }   

        // Sort and return the recommendations
        return sortRecommendations(movieScores, numRecommendations);
    }


    // Helper method to calculate the disagreement factor based on balancing rule
    private static double calculateDisagreementFactor(User currentUser, List<User> groupUsers, int movieId) {
        Set<Integer> commonUsers = new HashSet<>();
        for (User user : groupUsers) {
            if (user.getRatings().containsKey(movieId)) {
                commonUsers.add(user.getId());
            }
        }

        double numerator = 0.0;
        double denominator = 0.0;

        for (int u : commonUsers) {
            double satU = calculateSatisfaction(currentUser, u, movieId);
            double satV = calculateSatisfaction(groupUsers.get(0), u, movieId); // Choosing a representative user for group

            numerator += Math.abs(satU - satV);
            denominator += satU + satV;
        }

        return numerator / denominator;
    }


    // Helper method to calculate satisfaction for a user and movie
    private static double calculateSatisfaction(User currentUser, int userId, int movieId) {
        double sum = 0.0;
        Map<Integer, Rating> userRatings = currentUser.getRatings();
        Rating currentRating = userRatings.get(movieId);

        if (currentRating != null) {
            double currentRatingScore = currentRating.getScore();
            ArrayList<MovieRecommendation> movieRecommendations = Algorithm.getRelevantMovies(userId, Algorithm.getSimilarUsers(userId));
            for (MovieRecommendation recommendation : movieRecommendations) {
                sum += recommendation.getRelevancy();
            }
            return sum / currentRatingScore;
        } else {
            return 0.0; // User hasn't rated the movie
        }
    }




    private static double calculateMeanRating(Map<Integer, Rating> userRatings, Set<Integer> commonMovies) {
        double sum = 0;
        for (int movieId : commonMovies) {
            sum += userRatings.get(movieId).getScore();
        }
        return sum / commonMovies.size();
    }
}
