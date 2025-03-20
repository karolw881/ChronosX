package org.magister.bubbleSort;


import org.magister.helper.StatisticsResult;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class PerformanceTestBubbleSort {

    // Katalogi dla danych testowych
    protected static final String INPUT_DIR = "test_data/input/bubble";
    protected static final String OUTPUT_DIR = "test_data/output/bubble";
    // Liczba wykonanych pomiarów dla danego przypadku
    protected static final int RUNS = 1000;
    protected static final Random random = new Random(42);
    // Rozmiary tablic testowych (ilość elementów)
    protected static final int[] DIMENSIONS = {10, 100, 1000};

    protected static final String CHARTS_DIR = "test_dataGG/output/vector/charts";
    protected final List<StatisticsResult> aggregatedResults = new ArrayList<>();


    public PerformanceTestBubbleSort() {
    }


    public void performTests() {
        createDirectories();
        for (int dim : DIMENSIONS) {
            System.out.println("Testing vector of dimension " + dim);
        }


    }





    public void createDirectories() {
        try {
            Files.createDirectories(Paths.get(INPUT_DIR));
            Files.createDirectories(Paths.get(OUTPUT_DIR));
        } catch (IOException e) {
            System.err.println("Error creating directories: " + e.getMessage());
        }
    }



    /**
     * Oblicza statystyki (średnia, mediana, min, max, odchylenie standardowe) i zapisuje do pliku.
     */
    private void calculateAndSaveStatistics(List<Long> directTimes, List<Long> reflectionTimes, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            DoubleSummaryStatistics directStats = directTimes.stream()
                    .mapToDouble(Long::doubleValue)
                    .summaryStatistics();
            DoubleSummaryStatistics reflectionStats = reflectionTimes.stream()
                    .mapToDouble(Long::doubleValue)
                    .summaryStatistics();
            double directMedian = calculateMedian(directTimes);
            double reflectionMedian = calculateMedian(reflectionTimes);

            writer.write("Statistics for direct method calls (ns):\n");
            writer.write(String.format("Mean: %.2f\n", directStats.getAverage()));
            writer.write(String.format("Median: %.2f\n", directMedian));
            writer.write(String.format("Min: %d\n", (long) directStats.getMin()));
            writer.write(String.format("Max: %d\n", (long) directStats.getMax()));
            writer.write(String.format("Std Dev: %.2f\n\n", calculateStandardDeviation(directTimes)));

            writer.write("Statistics for reflection method calls (ns):\n");
            writer.write(String.format("Mean: %.2f\n", reflectionStats.getAverage()));
            writer.write(String.format("Median: %.2f\n", reflectionMedian));
            writer.write(String.format("Min: %d\n", (long) reflectionStats.getMin()));
            writer.write(String.format("Max: %d\n", (long) reflectionStats.getMax()));
            writer.write(String.format("Std Dev: %.2f\n\n", calculateStandardDeviation(reflectionTimes)));

            double ratio = reflectionStats.getAverage() / directStats.getAverage();
            writer.write(String.format("Reflection/Direct mean ratio: %.2f\n", ratio));
        } catch (IOException e) {
            System.err.println("Error saving statistics to file: " + e.getMessage());
        }
    }

    /**
     * Oblicza medianę z listy wartości.
     */
    double calculateMedian(List<Long> values) {
        List<Long> sorted = values.stream().sorted().collect(Collectors.toList());
        int size = sorted.size();
        if (size % 2 == 0) {
            return (sorted.get(size / 2 - 1) + sorted.get(size / 2)) / 2.0;
        } else {
            return sorted.get(size / 2);
        }
    }

    /**
     * Oblicza odchylenie standardowe z listy wartości. wersja populacyjna
     */
    double calculateStandardDeviation(List<Long> values) {
        double mean = values.stream().mapToDouble(Long::doubleValue).average().orElse(0.0);
        double variance = values.stream()
                .mapToDouble(v -> Math.pow(v - mean, 2))
                .average().orElse(0.0);
        return Math.sqrt(variance);
    }

    long calculateMode(List<Long> values) {
        Map<Long, Long> freq = values.stream()
                .collect(Collectors.groupingBy(v -> v, Collectors.counting()));
        return freq.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0L);
    }


}

