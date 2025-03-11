package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.*;
import com.opencsv.*;

/**
 * Evaluate Single Variable Binary Classification
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

        System.out.println("y_true\t y_predicted");
        
        int tp = 0, tn = 0, fp = 0, fn = 0;
        double bce = 0;
        int count = 0;
        
        List<double[]> rocPoints = new ArrayList<>();
        
        for (String[] row : allData) {
            int y_true = Integer.parseInt(row[0]);
            double y_predicted = Double.parseDouble(row[1]);
            
            System.out.println(y_true + "\t" + y_predicted);
            
            // Binary Cross-Entropy Loss
            bce += -(y_true * Math.log(y_predicted + 1e-10) + (1 - y_true) * Math.log(1 - y_predicted + 1e-10));
            
            // Convert probability to binary prediction (threshold = 0.5)
            int y_pred_binary = (y_predicted >= 0.5) ? 1 : 0;
            
            // Confusion Matrix Calculation
            if (y_true == 1 && y_pred_binary == 1) tp++;
            else if (y_true == 0 && y_pred_binary == 0) tn++;
            else if (y_true == 0 && y_pred_binary == 1) fp++;
            else if (y_true == 1 && y_pred_binary == 0) fn++;
            
            rocPoints.add(new double[]{y_predicted, y_true});
            count++;
        }
        
        if (count > 0) {
            bce /= count;
        }
        
        double accuracy = (double) (tp + tn) / count;
        double precision = tp + fp == 0 ? 0 : (double) tp / (tp + fp);
        double recall = tp + fn == 0 ? 0 : (double) tp / (tp + fn);
        double f1_score = (precision + recall) == 0 ? 0 : 2 * (precision * recall) / (precision + recall);
        
        // AUC-ROC Calculation
        rocPoints.sort((a, b) -> Double.compare(b[0], a[0]));
        double auc = 0, prev_x = 0, prev_y = 0;
        int tpr_count = 0, fpr_count = 0;
        
        for (double[] point : rocPoints) {
            if (point[1] == 1) {
                tpr_count++;
            } else {
                fpr_count++;
            }
            double x = (double) fpr_count / (fp + tn);
            double y = (double) tpr_count / (tp + fn);
            auc += (y + prev_y) * (x - prev_x) / 2;
            prev_x = x;
            prev_y = y;
        }
        
        System.out.println("\nEvaluation Metrics for " + filePath + ":");
        System.out.println("BCE: " + bce);
        System.out.println("Confusion Matrix:");
        System.out.println("TP: " + tp + "  FP: " + fp);
        System.out.println("FN: " + fn + "  TN: " + tn);
        System.out.println("Accuracy: " + accuracy);
        System.out.println("Precision: " + precision);
        System.out.println("Recall: " + recall);
        System.out.println("F1-Score: " + f1_score);
        System.out.println("AUC-ROC: " + auc);
    }
}
