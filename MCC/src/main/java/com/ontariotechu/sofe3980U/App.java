package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.*;
import com.opencsv.*;

/**
 * Evaluate Multiclass Classification
 */
public class App {
    public static void main(String[] args) {
        String filePath = "model.csv";
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
        
        int numClasses = 5;
        int[][] confusionMatrix = new int[numClasses][numClasses];
        double ce = 0;
        int count = 0;
        
        for (String[] row : allData) {
            int y_true = Integer.parseInt(row[0]) - 1;
            double[] y_predicted = new double[numClasses];
            
            int predictedClass = 0;
            double maxProb = Double.NEGATIVE_INFINITY;
            
            for (int i = 0; i < numClasses; i++) {
                y_predicted[i] = Double.parseDouble(row[i + 1]);
                if (y_predicted[i] > maxProb) {
                    maxProb = y_predicted[i];
                    predictedClass = i;
                }
            }
            
            ce += -Math.log(y_predicted[y_true] + 1e-10);
            confusionMatrix[y_true][predictedClass]++;
            count++;
        }
        
        if (count > 0) {
            ce /= count;
        }
        
        System.out.println("Cross-Entropy Loss: " + ce);
        System.out.println("Confusion Matrix:");
        
        for (int i = 0; i < numClasses; i++) {
            for (int j = 0; j < numClasses; j++) {
                System.out.print(confusionMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}
