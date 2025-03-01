package org.magister.matrix;


import org.magister.helper.IntegerOperations;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.DoubleSummaryStatistics;
import java.util.stream.Collectors;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PerformanceTestMatrix {
    // Directory constants
    private static final String INPUT_DIR = "test_data/input/matrix/";
    private static final String OUTPUT_DIR = "test_data/output/matrix";
    // Number of test runs for each case
    private static final int RUNS = 100;
    private static final Random random = new Random(42);

    // Matrix dimensions to test
    private static final int[] DIMENSIONS = {10, 50, 100, 200};

    /**
     * Main method to run performance tests
     */
    public void performTests() {
        // Ensure directories exist
        createDirectories();

        // Test each matrix dimension
        for (int dim : DIMENSIONS) {
            System.out.println("Testing matrix of dimension " + dim + "x" + dim);

            // Create test matrices
            Matrix<Integer> matrix1 = createRandomMatrix(dim);
            Matrix<Integer> matrix2 = createRandomMatrix(dim);

            // Save matrices to input directory
            saveMatrixToFile(matrix1, INPUT_DIR + "matrix1_" + dim + ".txt");
            saveMatrixToFile(matrix2, INPUT_DIR + "matrix2_" + dim + ".txt");

            testAdd(matrix1 , matrix2 , dim);
            testSubstract(matrix1 , matrix2 , dim);
            //  testDivide(matrix1 , matrix2 , dim);
            testMultiply(matrix1 , matrix2 , dim);


        }
    }

    private void testAdd(Matrix matrix1 , Matrix matrix2 , int dim  ){
        // Perform operation tests
        List<Long> directTimes = new ArrayList<>();
        List<Long> reflectionTimes = new ArrayList<>();
        // Warm-up runs (not counted in statistics)
        for (int i = 0; i < 3; i++) {
            matrix1.multiply(matrix2);
            MatrixReflectionUtil.performOperation(matrix1, matrix2, "add");
        }

        // Measured runs
        for (int i = 0; i < RUNS; i++) {
            // Test direct method call
            long startDirect = System.nanoTime();
            Matrix<Integer> resultDirect = matrix1.add(matrix2);
            long endDirect = System.nanoTime();
            directTimes.add(endDirect - startDirect);

            // Test reflection method call
            long startReflection = System.nanoTime();
            Matrix<Integer> resultReflection = MatrixReflectionUtil.performOperation(matrix1, matrix2, "add");
            long endReflection = System.nanoTime();
            reflectionTimes.add(endReflection - startReflection);
        }

        // Save raw timing results
        String resultsFilename = OUTPUT_DIR + "matrix_performance_multiply" + dim + ".txt";
        saveResultsToFile(resultsFilename, directTimes, reflectionTimes);

        // Calculate and save statistics
        String statsFilename = OUTPUT_DIR + "matrix_statistics_multiply" + dim + ".txt";
        calculateAndSaveStatistics(directTimes, reflectionTimes, statsFilename);

        System.out.println("Results saved to " + resultsFilename + " and " + statsFilename);
    }










    private void testSubstract(Matrix matrix1 , Matrix matrix2 , int dim ){
        // Perform operation tests
        List<Long> directTimes = new ArrayList<>();
        List<Long> reflectionTimes = new ArrayList<>();
        // Warm-up runs (not counted in statistics)
        for (int i = 0; i < 3; i++) {
            matrix1.subtract(matrix2);
            MatrixReflectionUtil.performOperation(matrix1, matrix2, "subtract");
        }

        // Measured runs
        for (int i = 0; i < RUNS; i++) {
            // Test direct method call
            long startDirect = System.nanoTime();
            Matrix<Integer> resultDirect = matrix1.subtract(matrix2);
            long endDirect = System.nanoTime();
            directTimes.add(endDirect - startDirect);

            // Test reflection method call
            long startReflection = System.nanoTime();
            Matrix<Integer> resultReflection = MatrixReflectionUtil.performOperation(matrix1, matrix2, "subtract");
            long endReflection = System.nanoTime();
            reflectionTimes.add(endReflection - startReflection);
        }

        // Save raw timing results
        String resultsFilename = OUTPUT_DIR + "matrix_performance_subtract" + dim + ".txt";
        saveResultsToFile(resultsFilename, directTimes, reflectionTimes);

        // Calculate and save statistics
        String statsFilename = OUTPUT_DIR + "matrix_statistics_subtract" + dim + ".txt";
        calculateAndSaveStatistics(directTimes, reflectionTimes, statsFilename);

        System.out.println("Results saved to " + resultsFilename + " and " + statsFilename);
    }


    private void testMultiply(Matrix matrix1 , Matrix matrix2 , int dim ){
        // Perform operation tests
        List<Long> directTimes = new ArrayList<>();
        List<Long> reflectionTimes = new ArrayList<>();
        // Warm-up runs (not counted in statistics)
        for (int i = 0; i < 3; i++) {
            matrix1.multiply(matrix2);
            MatrixReflectionUtil.performOperation(matrix1, matrix2, "multiply");
        }

        // Measured runs
        for (int i = 0; i < RUNS; i++) {
            // Test direct method call
            long startDirect = System.nanoTime();
            Matrix<Integer> resultDirect = matrix1.multiply(matrix2);
            long endDirect = System.nanoTime();
            directTimes.add(endDirect - startDirect);

            // Test reflection method call
            long startReflection = System.nanoTime();
            Matrix<Integer> resultReflection = MatrixReflectionUtil.performOperation(matrix1, matrix2, "multiply");
            long endReflection = System.nanoTime();
            reflectionTimes.add(endReflection - startReflection);
        }

        // Save raw timing results
        String resultsFilename = OUTPUT_DIR + "matrix_performance_multiply" + dim + ".txt";
        saveResultsToFile(resultsFilename, directTimes, reflectionTimes);

        // Calculate and save statistics
        String statsFilename = OUTPUT_DIR + "matrix_statistics_multiply" + dim + ".txt";
        calculateAndSaveStatistics(directTimes, reflectionTimes, statsFilename);

        System.out.println("Results saved to " + resultsFilename + " and " + statsFilename);
    }
/*
    private void testDivide(Matrix matrix1 , Matrix matrix2 , int dim ){
        // Perform operation tests
        List<Long> directTimes = new ArrayList<>();
        List<Long> reflectionTimes = new ArrayList<>();
        // Warm-up runs (not counted in statistics)
        for (int i = 0; i < 3; i++) {
            matrix1.add(matrix2);
            MatrixReflectionUtil.performOperation(matrix1, matrix2, "add");
        }

        // Measured runs
        for (int i = 0; i < RUNS; i++) {
            // Test direct method call
            long startDirect = System.nanoTime();
            Matrix<Integer> resultDirect = matrix1.add(matrix2);
            long endDirect = System.nanoTime();
            directTimes.add(endDirect - startDirect);

            // Test reflection method call
            long startReflection = System.nanoTime();
            Matrix<Integer> resultReflection = MatrixReflectionUtil.performOperation(matrix1, matrix2, "add");
            long endReflection = System.nanoTime();
            reflectionTimes.add(endReflection - startReflection);
        }

        // Save raw timing results
        String resultsFilename = OUTPUT_DIR + "matrix_performance_" + dim + ".txt";
        saveResultsToFile(resultsFilename, directTimes, reflectionTimes);

        // Calculate and save statistics
        String statsFilename = OUTPUT_DIR + "matrix_statistics_" + dim + ".txt";
        calculateAndSaveStatistics(directTimes, reflectionTimes, statsFilename);

        System.out.println("Results saved to " + resultsFilename + " and " + statsFilename);
    }


 */





    /**
     * Create a random matrix of specified dimension
     */
    private Matrix<Integer> createRandomMatrix(int dimension) {
        Integer[][] data = new Integer[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                data[i][j] = random.nextInt(10);
            }
        }

        return new Matrix<>(data, new IntegerOperations());
    }

    /**
     * Save matrix to a text file
     */
    private <T extends Number> void saveMatrixToFile(Matrix<T> matrix, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Get matrix dimensions
            int rows = matrix.getRows();
            int cols = matrix.getCols();

            writer.write("Matrix Dimension: " + rows + "x" + cols + "\n\n");

            // Write matrix elements
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    writer.write(matrix.getElement(i, j) + "\t");
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving matrix to file: " + e.getMessage());
        }
    }

    /**
     * Ensure directories exist
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
     * Save raw performance results to text file
     */
    private void saveResultsToFile(String filename, List<Long> directTimes, List<Long> reflectionTimes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Direct Time\tReflection Time\n");
            writer.write("-----------\t---------------\n");
            for (int i = 0; i < directTimes.size(); i++) {
                writer.write(directTimes.get(i) + "\t\t" + reflectionTimes.get(i) + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving results to file: " + e.getMessage());
        }
    }

    /**
     * Calculate statistics using Java's stream API and save to file
     */
    private void calculateAndSaveStatistics(List<Long> directTimes, List<Long> reflectionTimes, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Calculate statistics for direct method calls
            DoubleSummaryStatistics directStats = directTimes.stream()
                    .mapToDouble(Long::doubleValue)
                    .summaryStatistics();

            // Calculate statistics for reflection method calls
            DoubleSummaryStatistics reflectionStats = reflectionTimes.stream()
                    .mapToDouble(Long::doubleValue)
                    .summaryStatistics();

            // Calculate medians
            double directMedian = calculateMedian(directTimes);
            double reflectionMedian = calculateMedian(reflectionTimes);

            // Write statistics to file
            writer.write("Statistics for direct method calls:\n");
            writer.write(String.format("Mean: %.2f ns\n", directStats.getAverage()));
            writer.write(String.format("Median: %.2f ns\n", directMedian));
            writer.write(String.format("Min: %d ns\n", (long)directStats.getMin()));
            writer.write(String.format("Max: %d ns\n", (long)directStats.getMax()));
            writer.write(String.format("Standard Deviation: %.2f ns\n", calculateStandardDeviation(directTimes)));
            writer.write("\n");

            writer.write("Statistics for reflection method calls:\n");
            writer.write(String.format("Mean: %.2f ns\n", reflectionStats.getAverage()));
            writer.write(String.format("Median: %.2f ns\n", reflectionMedian));
            writer.write(String.format("Min: %d ns\n", (long)reflectionStats.getMin()));
            writer.write(String.format("Max: %d ns\n", (long)reflectionStats.getMax()));
            writer.write(String.format("Standard Deviation: %.2f ns\n", calculateStandardDeviation(reflectionTimes)));
            writer.write("\n");

            // Calculate and write ratio
            double ratio = reflectionStats.getAverage() / directStats.getAverage();
            writer.write(String.format("Ratio of mean times (reflection/direct): %.2f\n", ratio));

        } catch (IOException e) {
            System.err.println("Error saving statistics to file: " + e.getMessage());
        }
    }

    /**
     * Calculate median from a list of values
     */
    private double calculateMedian(List<Long> values) {
        List<Long> sortedValues = values.stream()
                .sorted()
                .collect(Collectors.toList());

        int size = sortedValues.size();
        if (size % 2 == 0) {
            return (sortedValues.get(size / 2 - 1) + sortedValues.get(size / 2)) / 2.0;
        } else {
            return sortedValues.get(size / 2);
        }
    }

    /**
     * Calculate standard deviation from a list of values
     */
    private double calculateStandardDeviation(List<Long> values) {
        double mean = values.stream().mapToLong(Long::longValue).average().orElse(0.0);
        double variance = values.stream()
                .mapToDouble(value -> Math.pow(value - mean, 2))
                .average()
                .orElse(0.0);

        return Math.sqrt(variance);
    }


}