package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.List;
import com.opencsv.*;

/**
 * Evaluate Single Variable Continuous Regression
 */
public class App {
    public static void main(String[] args) {
        String filePath = "model_3.csv";
        FileReader filereader;
        List<String[]> allData;
        
        try {
            filereader = new FileReader(filePath);
            CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
            allData = csvReader.readAll();
        } catch (Exception e) {
            System.out.println("Error reading the CSV file");
            return;
        }

		
        //Start of updated program to calculate MSE, MAE, MARE
        double mse = 0, mae = 0, mare = 0;
        int count = 0;
		// Small value to avoid division by zero in MARE
        double epsilon = 1e-10; 

        for (String[] row : allData) {
            double y_true = Double.parseDouble(row[0]);
            double y_predicted = Double.parseDouble(row[1]);
            
            System.out.println(y_true + "\t" + y_predicted);
            
            double error = y_true - y_predicted;
            mse += error * error;
            mae += Math.abs(error);
            mare += (Math.abs(error) / (Math.abs(y_true) + epsilon)) * 100;
            
            count++;
        }
        
        if (count > 0) {
            mse /= count;
            mae /= count;
            mare /= count;
        }
        
        System.out.println("\nEvaluation Metrics for " + filePath + ":");
        System.out.println("MSE: " + mse);
        System.out.println("MAE: " + mae);
        System.out.println("MARE (%): " + mare);
    }
}
