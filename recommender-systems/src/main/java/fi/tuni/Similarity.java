package fi.tuni;

public class Similarity implements Identifiable {
    private User user;
    private Double correlation;

    public Similarity(User user, Double correlation) {
        this.user = user;
        this.correlation = correlation;
    }
    public User getUser() {
        return this.user;
    }
    public Double getCorrelation() {
        return this.correlation;
    }

    @Override
    public int getId() {
        return this.user.getId();
    }
}
