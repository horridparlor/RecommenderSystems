package fi.tuni;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;



public class Main {
    static final String PATH_TO_RATINGS = "/ml-latest-small/ratings.csv";
    public static void main(String[] args) {
        double[] shoeSizes = {39, 40, 41, 42, 43, 44, 45, 46, 47};
        double[] heights = {160, 163, 169, 172, 176, 176, 178, 179, 180};

        double correlation = PearsonCorrelation.computePearsonCorrelation(shoeSizes, heights);
        System.out.printf("Pearson correlation between shoe size and height: %.2f%n", correlation);

        countRows();
        HashMap<Integer, User> users = getUsers();
        System.out.println(Calculator.getPearsonCorrelation(users.get(1), users.get(2)));
        System.out.println("End");
    }

    private static void countRows() {
        InputStream ratings = Main.class.getResourceAsStream(PATH_TO_RATINGS);
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

    private static HashMap<Integer, User> getUsers() {
        HashMap<Integer, User> users = new HashMap<>();
        InputStream ratings = Main.class.getResourceAsStream(PATH_TO_RATINGS);
        BufferedReader reader = null;
        String line = "";
        try {
            reader = new BufferedReader(new InputStreamReader(ratings));
            while ((line = reader.readLine()) != null) {
                processRatingLine(line, users);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    private static void processRatingLine(String line, HashMap<Integer, User> users) {
        try {
            String[] values = line.split(",");
            Integer userId = Integer.parseInt(values[0]);
            Integer movieId = Integer.parseInt(values[1]);
            Double rating = Double.parseDouble(values[2]);
            Long timestamp = Long.parseLong(values[3]);
            if (!users.containsKey(userId)) {
                users.put(userId, new User(userId));
            }
            User user = users.get(userId);
            user.addRating(movieId, rating);
        }  catch (NumberFormatException ignored) {
        }
    }
}