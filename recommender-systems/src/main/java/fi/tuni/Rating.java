package fi.tuni;

public class Rating {
    private Integer userId;
    private Integer movieId;
    private Double score;
    private Long timestamp;

    public Rating(Integer userId, Integer movieId, Double score, Long timestamp) {
        this.userId = userId;
        this.movieId = movieId;
        this.score = score;
        this.timestamp = timestamp;
    }

    public Double getScore() {
        return this.score;
    }

    @Override
    public String toString() {
        return "Rating{ userId: " + userId + ", movieId: " + movieId +
                ", score: " + score + ", timestamp: " + timestamp + " }";
    }
}
