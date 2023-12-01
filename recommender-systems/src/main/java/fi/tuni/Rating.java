package fi.tuni;

public class Rating {
    private Integer userId;
    private Integer movieId;
    private Double score;
    private double preference;
    private Long timestamp;

    public Rating(Integer userId, Integer movieId, Double score, double preference, Long timestamp) {
        this.userId = userId;
        this.movieId = movieId;
        this.score = score;
        this.preference = preference;
        this.timestamp = timestamp;
    }

    public Double getScore() {
        return this.score;
    }

    public double getPreference() {
        return preference;
    }

    public void setPreference(double preference) {
        this.preference = preference;
    }

    @Override
    public String toString() {
        return "Rating{ userId: " + userId + ", movieId: " + movieId +
                ", score: " + score + ", timestamp: " + timestamp + " }";
    }
}
