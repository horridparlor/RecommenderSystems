package fi.tuni;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import java.io.*;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;



public class Main {
    public static void main(String[] args) {
        InputStream ratings = Main.class.getResourceAsStream("/ml-latest-small/ratings.csv");
        BufferedReader reader = null;
        String line = "";
        int rowCount = 0;
        try {
            reader = new BufferedReader(new InputStreamReader(ratings));
            while ((line = reader.readLine()) != null) {
                rowCount++;
            }
            System.out.println(rowCount);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private double pearsonCorrelation(Map<Integer, Double> user1, Map<Integer, Double> user2) {
        // Find common movies
        Set<Integer> commonMovies = new HashSet<>(user1.keySet());
        commonMovies.retainAll(user2.keySet());

        if (commonMovies.size() == 0) {
            return 0.0;  
        }


        double[] user1Ratings = new double[commonMovies.size()];
        double[] user2Ratings = new double[commonMovies.size()];
        int i = 0;
        for (int movieId : commonMovies) {
            user1Ratings[i] = user1.get(movieId);
            user2Ratings[i] = user2.get(movieId);
            i++;
        }

        // Calculate Pearson correlation
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
        double correlation = pearsonsCorrelation.correlation(user1Ratings, user2Ratings);

        return correlation;
    }



    private double adjustedCosineSimilarity(Map<Integer, Double> user1, Map<Integer, Double> user2) {
        Set<Integer> commonMovies = new HashSet<>(user1.keySet());
        commonMovies.retainAll(user2.keySet());

        if (commonMovies.isEmpty()) {
            return 0.0; 
        }

        double meanUser1 = calculateMeanRating(user1, commonMovies);
        double meanUser2 = calculateMeanRating(user2, commonMovies);

        double numerator = 0;
        double denominatorUser1 = 0;
        double denominatorUser2 = 0;

        for (int movieId : commonMovies) {
            double ratingUser1 = user1.get(movieId);
            double ratingUser2 = user2.get(movieId);

            numerator += (ratingUser1 - meanUser1) * (ratingUser2 - meanUser2);
            denominatorUser1 += Math.pow(ratingUser1 - meanUser1, 2);
            denominatorUser2 += Math.pow(ratingUser2 - meanUser2, 2);
        }

        if (denominatorUser1 == 0 || denominatorUser2 == 0) {
            return 0.0;
        }

        double similarity = numerator / (Math.sqrt(denominatorUser1) * Math.sqrt(denominatorUser2));
        return similarity;
    }

    private double calculateMeanRating(Map<Integer, Double> userRatings, Set<Integer> commonMovies) {
        double sum = 0;
        for (int movieId : commonMovies) {
            sum += userRatings.get(movieId);
        }
        return sum / commonMovies.size();
    }
}