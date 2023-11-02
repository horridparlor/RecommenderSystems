package fi.tuni;

import java.util.HashMap;
import java.util.Map;

public class User {
    private int id;
    private Map<Integer, Double> ratings;

    public User(int id) {
        this.id = id;
        this.ratings = new HashMap<>();
    }

    public void addRating(Integer movieId, Double rating) {
        this.ratings.put(movieId, rating);
    }

    public Map<Integer, Double> getRatings() {
        return this.ratings;
    }
}
