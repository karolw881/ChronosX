package org.magister.bubbleSort;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class PerformanceTestBubbleSort {

    // Katalogi dla danych testowych
    private static final String INPUT_DIR = "test_data/input/bubble";
    private static final String OUTPUT_DIR = "test_data/output/bubble";
    // Liczba wykonanych pomiarów dla danego przypadku
    private static final int RUNS = 1000;
    private static final Random random = new Random(42);
    // Rozmiary tablic testowych (ilość elementów)
    private static final int[] SIZES = {10, 100, 1000};

    /**
     * Metoda główna uruchamiająca testy.
     */
    public void performTests() {
        createDirectories();

        for (int size : SIZES) {
            System.out.println("Testing bubble sort for array of size...... " + size);
            Integer[] data = createRandomArray(size);

            // Opcjonalne zapisywanie danych wejściowych
            saveArrayToFile(data, INPUT_DIR + "array_" + size + ".txt");

            testBubbleSort(data, size);
        }






    }











    /**
     * Testuje sortowanie bąbelkowe – porównuje implementację bezpośrednią (BubbleSort1)
     * i implementację refleksyjną (BubbleSort).
     */
    private void testBubbleSort(Integer[] originalData, int size) {
        List<Long> directTimes = new ArrayList<>();
        List<Long> reflectionTimes = new ArrayList<>();

        // Rozgrzewka (3 wywołania, wyniki nie są mierzone)
        for (int i = 0; i < 3; i++) {
            // Bezpośrednie sortowanie
            new BubbleSort1().sort(originalData.clone(), Comparator.naturalOrder());
            // Sortowanie refleksyjne – metoda sort przyjmuje nazwę metody "compareTo"
            try {
                new BubbleSort<Integer>().sort(originalData.clone(), "compareTo");
            } catch (Exception e) {
                throw new RuntimeException("Błąd w wywołaniu sortowania refleksyjnego: " + e.getMessage(), e);
            }
        }

        // Pomiary RUNS razy
        for (int i = 0; i < RUNS; i++) {
            // Bezpośrednie sortowanie
            long startDirect = System.nanoTime();
            Integer[] sortedDirect = (Integer[]) new BubbleSort1().sort(originalData.clone(), Comparator.naturalOrder());
            long endDirect = System.nanoTime();
            directTimes.add(endDirect - startDirect);

            // Sortowanie refleksyjne
            long startReflection = System.nanoTime();
            try {
                Integer[] sortedReflection = (Integer[]) new BubbleSort<Integer>().sort(originalData.clone(), "compareTo");
            } catch (Exception e) {
                throw new RuntimeException("Błąd podczas sortowania refleksyjnego: " + e.getMessage(), e);
            }
            long endReflection = System.nanoTime();
            reflectionTimes.add(endReflection - startReflection);
        }

        // Zapis wyników
        String resultsFilename = OUTPUT_DIR + "bubble_performance_" + size + ".txt";
        saveResultsToFile(resultsFilename, directTimes, reflectionTimes);

        String statsFilename = OUTPUT_DIR + "bubble_statistics_" + size + ".txt";
        calculateAndSaveStatistics(directTimes, reflectionTimes, statsFilename);

        System.out.println("Results saved to " + resultsFilename + " and " + statsFilename);
    }

    /**
     * Tworzy losową tablicę Integer o zadanym rozmiarze, wartości z zakresu 0-100.
     */
    private Integer[] createRandomArray(int size) {
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(101);
        }
        return array;
    }


    /**
     * Tworzy tablice
     */

    /**
     * Zapisuje tablicę do pliku tekstowego.
     */
    private <T> void saveArrayToFile(T[] array, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Array size: " + array.length + "\n");
            for (T element : array) {
                writer.write(element + " ");
            }
            writer.write("\n");
        } catch (IOException e) {
            System.err.println("Error saving array to file: " + e.getMessage());
        }
    }

    /**
     * Tworzy katalogi wejściowe i wyjściowe istnieją.
     */
    private void createDirectories() {
        try {
            Files.createDirectories(Paths.get(INPUT_DIR));
            Files.createDirectories(Paths.get(OUTPUT_DIR));
        } catch (IOException e) {
            System.err.println("Error creating directories: " + e.getMessage());
        }
    }

    /**
     * Zapisuje wyniki pomiarów do pliku.
     */
    private void saveResultsToFile(String filename, List<Long> directTimes, List<Long> reflectionTimes) {
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
     * Oblicza statystyki (średnia, mediana, min, max, odchylenie standardowe) i zapisuje je do pliku.
     */
    private void calculateAndSaveStatistics(List<Long> directTimes, List<Long> reflectionTimes, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            DoubleSummaryStatistics directStats = directTimes.stream().mapToDouble(Long::doubleValue).summaryStatistics();
            DoubleSummaryStatistics reflectionStats = reflectionTimes.stream().mapToDouble(Long::doubleValue).summaryStatistics();

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
    private double calculateMedian(List<Long> values) {
        List<Long> sorted = values.stream().sorted().collect(Collectors.toList());
        int size = sorted.size();
        if (size % 2 == 0) {
            return (sorted.get(size / 2 - 1) + sorted.get(size / 2)) / 2.0;
        } else {
            return sorted.get(size / 2);
        }
    }

    /**
     * Oblicza odchylenie standardowe z listy wartości.
     */
    private double calculateStandardDeviation(List<Long> values) {
        double mean = values.stream().mapToDouble(Long::doubleValue).average().orElse(0.0);
        double variance = values.stream().mapToDouble(v -> Math.pow(v - mean, 2)).average().orElse(0.0);
        return Math.sqrt(variance);
    }

}
