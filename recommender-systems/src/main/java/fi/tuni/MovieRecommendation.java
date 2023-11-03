package fi.tuni;

public class MovieRecommendation implements Identifiable {
    private int id;
    private double relevancy;
    public MovieRecommendation(int id, double relevancy) {
        this.id = id;
        this.relevancy = relevancy;
    }
    @Override
    public int getId() {
        return this.id;
    }
    public double getRelevancy() {
        return this.relevancy;
    }
}
