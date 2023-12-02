package fi.tuni;

import java.util.*;
import java.util.stream.Collectors;

public class Algorithm {
    public static ArrayList<Similarity> getSimilarUsers(Integer userId) {
        HashMap<Integer, User> users = Main.getUsers();
        User chosen = users.get(userId);
        return getMostSimilarIds(chosen, users);
    }

    private static ArrayList<Similarity> getMostSimilarIds(User chosen, HashMap<Integer, User> users) {
        HashMap<Integer, Double> correlations = new HashMap<>();

        for (Map.Entry<Integer, User> user : users.entrySet()) {
            int userId = user.getKey();
            if (userId != chosen.getId()) {
                Double correlation = Main.getSimilarityFunction() == Enums.SimilarityFunction.PEARSON
                        ? Calculator.getPearsonCorrelation(chosen, user.getValue())
                        : Calculator.getAdjustedCosineSimilarity(chosen, user.getValue());
                correlations.put(userId, correlation);
            }
        }
        return correlations.entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .limit(Constants.MAX_ITEMS)
                .map(entry -> new Similarity(users.get(entry.getKey()), entry.getValue()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<MovieRecommendation> getRelevantMovies(Integer userId, ArrayList<Similarity> similarUsers) {
        ArrayList<MovieRecommendation> relevantMovies = new ArrayList<>();
        HashMap<Integer, User> users = Main.getUsers();
        User chosen = users.get(userId);
        ArrayList<Integer> unknownMovies = getUnknownMovies(chosen, similarUsers);
        for (Integer movieId : unknownMovies) {
            relevantMovies.add(getRelevancy(movieId, chosen, similarUsers));
        }
        relevantMovies.sort(new Comparator<MovieRecommendation>() {
            @Override
            public int compare(MovieRecommendation r1, MovieRecommendation r2) {
                return Double.compare(r2.getRelevancy(), r1.getRelevancy());
            }
        });

        return new ArrayList<>(relevantMovies.subList(0, Math.min(relevantMovies.size(), Constants.MAX_ITEMS)));
    }
    private static MovieRecommendation getRelevancy(int movieId, User chosen, ArrayList<Similarity> similarUsers) {
        double numerator = 0.0;
        double demoninator = 0.0;
        for (Similarity similarity : similarUsers) {
            User user = similarity.getUser();
            Double correlation = similarity.getCorrelation();
            if (user.getRatings().containsKey(movieId)) {
                numerator += correlation * (user.getExpectedScore() - user.getRatings().get(movieId).getScore());
                demoninator += correlation;
            }
        }
        double relevancy = chosen.getExpectedScore() + numerator / demoninator;
        return new MovieRecommendation(movieId, relevancy);
    }
    private static ArrayList<Integer> getUnknownMovies(User chosen, ArrayList<Similarity> similarUsers) {
        ArrayList<Integer> unknownMovies = new ArrayList<>();
        for(Similarity similarity : similarUsers) {
            for (Map.Entry<Integer, Rating> rating : similarity.getUser().getRatings().entrySet()) {
                if (!chosen.getRatings().containsKey(rating.getKey())) {
                    unknownMovies.add(rating.getKey());
                }
            }
        }
        return unknownMovies;
    }

    public static ArrayList<User> findUsers(HashSet<Integer> userIds) {
        ArrayList<User> foundUsers = new ArrayList<>();
        HashMap<Integer, User> users = Main.getUsers();
        for (int userId : userIds) {
            foundUsers.add(users.get(userId));
        }
        return foundUsers;
    }


// Assignment 3

}   