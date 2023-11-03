package fi.tuni;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class DataParser {
    static final String PATH_TO_RATINGS = "/ml-latest-small/ratings.csv";
    public static String countRows() {
        InputStream ratings = Main.class.getResourceAsStream(PATH_TO_RATINGS);
        BufferedReader reader = null;
        String line = "";
        int rowCount = 0;
        try {
            reader = new BufferedReader(new InputStreamReader(ratings));
            while ((line = reader.readLine()) != null) {
                rowCount++;
            }
            return String.valueOf(rowCount);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "0";
    }

    public static HashMap<Integer, User> getUsers() {
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
            Double score = Double.parseDouble(values[2]);
            Long timestamp = Long.parseLong(values[3]);
            Rating rating = new Rating(userId, movieId, score, timestamp);
            if (!users.containsKey(userId)) {
                users.put(userId, new User(userId));
            }
            User user = users.get(userId);
            user.addRating(movieId, rating);
        }  catch (NumberFormatException ignored) {
        }
    }
}
