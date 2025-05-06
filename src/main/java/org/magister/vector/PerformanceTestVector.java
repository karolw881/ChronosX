package org.magister.vector;


import org.magister.helper.NumberxxOperations;
import org.magister.helper.Numberxx;
import org.magister.helper.NumberxxOperations;
import org.magister.helper.StatisticsResult;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class PerformanceTestVector implements VtPerformance {

    // Directory constants
    protected static final String INPUT_DIR = "test_dataSem2/input/vector";
    protected static final String OUTPUT_DIR = "test_dataSem2/output/vector";

    protected static final String CHARTS_DIR = "test_dataSem2/output/vector";

    // Number of test runs for each case
    protected static final int RUNS = 100;
    protected static final Random random = new Random(42);
    // Vector dimensions (wymiar wektora)
    protected static final int[] DIMENSIONS = {10, 50, 100, 200, 300, 500, 1000};

    // Lista zagregowanych wyników (dla każdego typu operacji i rozmiaru)
    protected final List<StatisticsResult> aggregatedResults = new ArrayList<>();
    /**
     * Metoda główna wykonująca testy wydajności dla wektorów.
     */
    public void performTests() {
        createDirectories();
    }

    /**
     * Zapisuje wektor do pliku tekstowego.
     */
    <T extends Number> void saveVectorToFile(Vector<Numberxx> vector, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Vector Dimension: " + vector.getCoordinates().length + "\n\n");
            for (Numberxx coordinate : vector.getCoordinates()) {
                writer.write(coordinate + "\t");
            }
        } catch (IOException e) {
            System.err.println("Error saving vector to file: " + e.getMessage());
        }
    }

    /**
     * Upewnia się, że katalogi wejściowe i wyjściowe istnieją.
     */
    public void createDirectories() {
        try {
            Files.createDirectories(Paths.get(INPUT_DIR));
            Files.createDirectories(Paths.get(OUTPUT_DIR));
            Files.createDirectories(Paths.get(CHARTS_DIR));

        } catch (IOException e) {
            System.err.println("Error creating directories: " + e.getMessage());
        }
    }

    /**
     * Zapisuje surowe wyniki czasowe do pliku.
     */
    protected void saveResultsToFile(String filename, List<Long> objectTimes, List<Long> reflectTimes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
          writer.write(String.format("%-15s %-15s%n", "Direct Time (ns)", "Reflection Time (ns)"));
            writer.write(String.format("%-15s %-15s%n", "---------------", "-------------------"));
         for (int i = 0; i < objectTimes.size(); i++) {
             writer.write( objectTimes.get(i).toString() + "        " + reflectTimes.get(i).toString() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving results to file: " + e.getMessage());
        }
    }




}
