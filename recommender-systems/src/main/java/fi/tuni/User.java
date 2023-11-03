package fi.tuni;

import java.util.HashMap;
import java.util.Map;

public class User implements Identifiable {
    private int id;
    private Map<Integer, Rating> ratings;

    public User(int id) {
        this.id = id;
        this.ratings = new HashMap<>();
    }

    public void addRating(Integer movieId, Rating rating) {
        this.ratings.put(movieId, rating);
    }

    public int getId() {
        return this.id;
    }
    public Map<Integer, Rating> getRatings() {
        return this.ratings;
    }

    @Override
    public String toString() {
        return "User{ id: " + String.valueOf(id) + ", ratings: " + ratings.toString() + " }";
    }

    public Double getExpectedScore() {
        Double total = 0.0;
        for(Map.Entry<Integer, Rating> rating : ratings.entrySet()) {
            total += rating.getValue().getScore();
        }
        return total / ratings.size();
    }
}
