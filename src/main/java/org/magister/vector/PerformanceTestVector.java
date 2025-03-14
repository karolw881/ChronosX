package org.magister.vector;


import org.magister.helper.IntegerOperations;
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
    protected static final String INPUT_DIR = "test_dataGG/input/vector/input";
    protected static final String OUTPUT_DIR = "test_dataGG/output/vector/output";

    protected static final String CHARTS_DIR = "test_dataGG/output/vector/output/charts";

    // Number of test runs for each case
    protected static final int RUNS = 100;
    protected static final Random random = new Random(42);
    // Vector dimensions (wymiar wektora)
    protected static final int[] DIMENSIONS = {10, 50, 100, 200};

    // Lista zagregowanych wyników (dla każdego typu operacji i rozmiaru)
    protected final List<StatisticsResult> aggregatedResults = new ArrayList<>();
    /**
     * Metoda główna wykonująca testy wydajności dla wektorów.
     */
    public void performTests() {
        createDirectories();

        for (int dim : DIMENSIONS) {
            System.out.println("Testing vector of dimension " + dim);

            // Tworzymy dwa losowe wektory
            Vector<Integer> vector1 = createRandomVector(dim);
            Vector<Integer> vector2 = createRandomVector(dim);

            // Zapisujemy wektory do pliku (opcjonalnie)
            saveVectorToFile(vector1, INPUT_DIR + "vector1_" + dim + ".txt");
            saveVectorToFile(vector2, INPUT_DIR + "vector2_" + dim + ".txt");

            testAdd(vector1, vector2, dim);
            testSubtract(vector1, vector2, dim);
            // Dodatkowe operacje można dodać analogicznie
        }
    }

    /**
     * Testuje operację dodawania wektorów.
     */
    private void testAdd(Vector<Integer> vector1, Vector<Integer> vector2, int dim) {
        List<Long> directTimes = new ArrayList<>();
        List<Long> reflectionTimes = new ArrayList<>();

        // Warm-up (3 razy, aby wyrzucić wpływ cold startu)
        for (int i = 0; i < 3; i++) {
            vector1.add(vector2);
            VectorReflectionUtil.performOperationReflectVector(vector1, vector2, "addVector");
        }

        // Pomiary RUNS razy
        for (int i = 0; i < RUNS; i++) {
            long startDirect = System.nanoTime();
            Vector<Integer> resultDirect = vector1.add(vector2);
            long endDirect = System.nanoTime();
            directTimes.add(endDirect - startDirect);

            long startReflection = System.nanoTime();
            Vector<Integer> resultReflection = VectorReflectionUtil.performOperationReflectVector(vector1, vector2, "addVector");
            long endReflection = System.nanoTime();
            reflectionTimes.add(endReflection - startReflection);
        }

        String resultsFilename = OUTPUT_DIR + "vector_performance_add_" + dim + ".txt";
        saveResultsToFile(resultsFilename, directTimes, reflectionTimes);

        String statsFilename = OUTPUT_DIR + "vector_statistics_add_" + dim + ".txt";
        calculateAndSaveStatistics(directTimes, reflectionTimes, statsFilename);

        System.out.println("Vector add results for dimension " + dim + " saved to "
                + resultsFilename + " and " + statsFilename);
    }

    /**
     * Testuje operację odejmowania wektorów (bezpośrednie odejmowanie – subtractVector2).
     */
    private void testSubtract(Vector<Integer> vector1, Vector<Integer> vector2, int dim) {
        List<Long> directTimes = new ArrayList<>();
        List<Long> reflectionTimes = new ArrayList<>();

        // Warm-up
        for (int i = 0; i < 3; i++) {
            vector1.subtruct(vector2);
            VectorReflectionUtil.performOperationReflectVector(vector1, vector2, "subtractVector2");
        }

        // Pomiary
        for (int i = 0; i < RUNS; i++) {
            long startDirect = System.nanoTime();
            Vector<Integer> resultDirect = vector1.subtruct(vector2);
            long endDirect = System.nanoTime();
            directTimes.add(endDirect - startDirect);

            long startReflection = System.nanoTime();
            Vector<Integer> resultReflection = VectorReflectionUtil.performOperationReflectVector(vector1, vector2, "subtractVector2");
            long endReflection = System.nanoTime();
            reflectionTimes.add(endReflection - startReflection);
        }

        String resultsFilename = OUTPUT_DIR + "vector_performance_subtract_" + dim + ".txt";
        saveResultsToFile(resultsFilename, directTimes, reflectionTimes);

        String statsFilename = OUTPUT_DIR + "vector_statistics_subtract_" + dim + ".txt";
        calculateAndSaveStatistics(directTimes, reflectionTimes, statsFilename);

        System.out.println("Vector subtract results for dimension " + dim + " saved to "
                + resultsFilename + " and " + statsFilename);
    }

    /**
     * Tworzy losowy wektor o zadanym wymiarze z wartościami z zakresu 0-10.
     */
    private Vector<Integer> createRandomVector(int dimension) {
        Integer[] data = new Integer[dimension];
        for (int i = 0; i < dimension; i++) {
            data[i] = random.nextInt(11);
        }
        return new Vector<>(data, new IntegerOperations());
    }

    /**
     * Zapisuje wektor do pliku tekstowego.
     */
    <T extends Number> void saveVectorToFile(Vector<T> vector, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Vector Dimension: " + vector.getCoordinates().length + "\n\n");
            for (T coordinate : vector.getCoordinates()) {
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
        } catch (IOException e) {
            System.err.println("Error creating directories: " + e.getMessage());
        }
    }

    /**
     * Zapisuje surowe wyniki czasowe do pliku.
     */
    void saveResultsToFile(String filename, List<Long> directTimes, List<Long> reflectionTimes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Direct Time (ns)\tReflection Time (ns)\n");
            writer.write("----------------\t-------------------\n");
            for (int i = 0; i < directTimes.size(); i++) {
                writer.write(directTimes.get(i) + "\t\t" + reflectionTimes.get(i) + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving results to file: " + e.getMessage());
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
