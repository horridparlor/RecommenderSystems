package fi.tuni;

import org.apache.commons.csv.CSVParser;

import java.io.*;

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
}