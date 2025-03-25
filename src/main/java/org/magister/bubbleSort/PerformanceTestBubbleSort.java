package org.magister.bubbleSort;


import org.magister.helper.Numberxx;
import org.magister.helper.StatisticsResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class PerformanceTestBubbleSort {

    // Katalogi dla danych testowych i wyników
    protected static final String INPUT_DIR = "test_dataSem2/input/bubble/";
    protected static final String OUTPUT_DIR = "test_dataSem2/output/bubble/";
    protected static final String CHARTS_DIR = "test_dataSem2/output/bubble/";
    // Liczba pomiarów dla danego przypadku
    protected static final int RUNS = 1000;
    // Rozmiary tablic testowych
    protected static final int[] DIMENSIONS = {10, 20, 30, 40, 80};

    // Lista zagregowanych wyników pomiarów
    protected final List<StatisticsResult> aggregatedResults = new ArrayList<>();



    public PerformanceTestBubbleSort() {
    }


    public void performTests() {
        createDirectories();
        for (int dim : DIMENSIONS) {
        }


    }



    public void saveResultsToFile(String filename, List<Long> objectTimes, List<Long> reflectionTimes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Nagłówki
            writer.write(String.format("%-15s %-15s%n", "Direct Time (ns)", "Reflection Time (ns)"));
            writer.write(String.format("%-15s %-15s%n", "---------------", "-------------------"));

            // Dane
            for (int i = 0; i < reflectionTimes.size(); i++) {
                writer.write(String.format("%-15d %-15d%n", objectTimes.get(i), reflectionTimes.get(i)));
            }
        } catch (IOException e) {
            System.err.println("Error saving results to file: " + e.getMessage());
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





    // --- Metoda pomocnicza do tworzenia katalogów ---
    protected void createDirectoriesIfNotExists(String path) {
        File directory = new File(path);
        if (!directory.exists() && !directory.mkdirs()) {
            System.err.println("Nie udało się utworzyć katalogu: " + path);
        }
    }


}

