package com.crimedetect.utils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class ChartUtils {

    // Bar Chart
    public static ChartPanel createBarChart(String title, String xLabel, String yLabel) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        // Add sample data (replace with database data)
        dataset.addValue(15, "Crimes", "Jan");
        dataset.addValue(20, "Crimes", "Feb");
        dataset.addValue(12, "Crimes", "Mar");

        JFreeChart chart = ChartFactory.createBarChart(
            title, xLabel, yLabel, dataset
        );
        return new ChartPanel(chart);
    }

    // Pie Chart
    public static ChartPanel createPieChart(String title) {
        
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        // Add sample data (replace with database data)
        dataset.setValue("Theft", 45);
        dataset.setValue("Assault", 30);
        dataset.setValue("Fraud", 25);

        JFreeChart chart = ChartFactory.createPieChart(
            title, dataset, true, true, false
        );
        return new ChartPanel(chart);
    }
}