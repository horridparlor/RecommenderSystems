package fi.tuni;

import org.apache.commons.csv.CSVParser;

import java.io.*;

public class Main {
    public static void main(String[] args) {
        double[] shoeSizes = {39, 40, 41, 42, 43, 44, 45, 46, 47};
        double[] heights = {160, 163, 169, 172, 176, 176, 178, 179, 180};

        double correlation = PearsonCorrelation.computePearsonCorrelation(shoeSizes, heights);
        System.out.printf("Pearson correlation between shoe size and height: %.2f%n", correlation);

        countRows();
    }

    private static void countRows() {
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