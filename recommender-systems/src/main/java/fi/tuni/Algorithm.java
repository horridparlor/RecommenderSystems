package fi.tuni;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Algorithm {
    public static ArrayList<User> getSimilarUsers(Integer chosenId) {
        ArrayList<User> similarUsers = new ArrayList<>();
        HashMap<Integer, User> users = Main.getUsers();
        User chosen = users.get(chosenId);
        ArrayList<Integer> userIds = getMostSimilarIds(chosen, users);

        for (int userId : userIds) {
            similarUsers.add(users.get(userId));
        }
        return similarUsers;
    }

    private static ArrayList<Integer> getMostSimilarIds(User chosen, HashMap<Integer, User> users) {
        HashMap<Integer, Double> correlations = new HashMap<>();
        for (Map.Entry<Integer, User> user : users.entrySet()) {
            int userId = user.getKey();
            if (userId != chosen.getId()) {
                correlations.put(userId, Calculator.getPearsonCorrelation(chosen, user.getValue()));
            }
        }
        return correlations.entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .limit(Constants.MAX_ITEMS)
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}