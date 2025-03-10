package org.magister.matrix;

import lombok.Getter;
import lombok.Setter;
import org.magister.helper.StatisticsResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;

@Getter
@Setter
public class PerformanceTestMatrixGenericofReflectionVsObject extends PerformanceTestMatrix {

    private static final String INPUT_DIR = PerformanceTestMatrix.INPUT_DIR + "ConcreteOfReflectionVsObject/";
    private static final String OUTPUT_DIR = PerformanceTestMatrix.OUTPUT_DIR + "ConcreteOfReflectionVsObject/";
    private static final String CHARTS_DIR = PerformanceTestMatrix.CHARTS_DIR + "ConcreteOfReflectionVsObject/";
    private static final int RUNS = PerformanceTestMatrix.RUNS;
    private static final int[] DIMENSIONS = PerformanceTestMatrix.DIMENSIONS;
  //  private final List<StatisticsResult> aggregatedResults = new ArrayList<>();

    public PerformanceTestMatrixGenericofReflectionVsObject() {
        // Używamy metod odziedziczonych do tworzenia katalogów
        createDirectories();
     //   performTest();
    }



    public void runTest() {
        // Czyszczenie poprzednich wyników
        aggregatedResults.clear();
        performTestGeneric();

    }




    public void performTestGeneric() {
        // Testujemy dla każdego wymiaru macierzy
        for (int dim : DIMENSIONS) {
            System.out.println("Testing matrix of dimension " + dim + "x" + dim);

            // Tworzymy macierze z ustalonymi ziarnami: seed 0 dla pierwszej i seed 1 dla drugiej
            Matrix<Integer> matrixGenericFirst = createRandomMatrix(dim, 0L);
            Matrix<Integer> matrixGenericSecond = createRandomMatrix(dim, 1L);

            // Tworzenie katalogów wejściowych, wyjściowych, wykresów oraz dodatkowego katalogu "wyniki"
            createDirectoriesIfNotExists(INPUT_DIR);
            createDirectoriesIfNotExists(OUTPUT_DIR);
            createDirectoriesIfNotExists(CHARTS_DIR);
            createDirectoriesIfNotExists("wyniki");

            // Zapisujemy macierze wejściowe do plików
            saveMatrixToFile(matrixGenericFirst, INPUT_DIR + "matrix1_generic" + dim + ".txt");
            saveMatrixToFile(matrixGenericSecond, INPUT_DIR + "matrix2_generic" + dim + ".txt");

            // Porównanie i zapis macierzy
          //  saveMatrixComparerToFile(dim, matrixGenericReflectFirst, matrixGenericReflectSecond);



            aggregatedResults.add(testAddGenericObjectVsReflect(matrixGenericFirst, matrixGenericSecond, dim));
            aggregatedResults.add(testSubstractGenericObjectVsReflect(matrixGenericFirst, matrixGenericSecond, dim));
            aggregatedResults.add(testMultiplyGenericObjectVsReflect(matrixGenericFirst, matrixGenericSecond, dim));
            aggregatedResults.add(testDivideGenericObjectVsReflect(matrixGenericFirst, matrixGenericSecond, dim));

        }

        // Wyświetlamy i zapisujemy zagregowane statystyki
        displayDetailedStatistics();
        saveAggregatedStatisticsToFile();

        // Generowanie wykresów (jeśli wymagane)
        // generateCharts();
    }

    private StatisticsResult testDivideGenericObjectVsReflect(Matrix<Integer> matrix1,
                                                              Matrix<Integer> matrix2,
                                                              int dim) {
        List<Long> reflectionTimes = new ArrayList<>();
        List<Long> objectTimes = new ArrayList<>();

        // Warm-up – wykonywane kilka razy przed pomiarem
        for (int i = 0; i < 3; i++) {
            matrix1.divide(matrix2);
            MatrixReflectionUtil.performOperation(matrix1, matrix2, "divide");
        }

        // Główne pomiary
        for (int i = 0; i < RUNS; i++) {
            // Pomiar operacji refleksyjnej
            long startReflection = System.nanoTime();
            MatrixReflectionUtil.performOperation(matrix1, matrix2, "divide");
            long endReflection = System.nanoTime();
            reflectionTimes.add(endReflection - startReflection);

            // Pomiar operacji obiektowej
            long startObject = System.nanoTime();
            matrix1.divide(matrix2);
            long endObject = System.nanoTime();
            objectTimes.add(endObject - startObject);
        }

        String resultsFilename = OUTPUT_DIR + "matrix_performance_divide_of_reflection_generic" + dim + ".txt";
        saveResultsToFile(resultsFilename, reflectionTimes, objectTimes);
        String statsFilename = OUTPUT_DIR + "matrix_statistics_divide_of_reflection_generic" + dim + ".txt";
        StatisticsResult stats = calculateAndSaveStatistics(reflectionTimes, objectTimes, statsFilename, "Divide", dim);
        System.out.println("Divide results saved to " + resultsFilename + " and " + statsFilename);
        return stats;

    }

    /**
     *
     * @param matrix1
     * @param matrix2
     * @param dim
     * @return
     */
    private StatisticsResult testMultiplyGenericObjectVsReflect(
            Matrix<Integer> matrix1,
            Matrix<Integer> matrix2,
            int dim) {


        List<Long> reflectionTimes = new ArrayList<>();
        List<Long> objectTimes = new ArrayList<>();

        // Warm-up – wykonywane kilka razy przed pomiarem
        for (int i = 0; i < 3; i++) {
            matrix1.multiply(matrix2);
            MatrixReflectionUtil.performOperation(matrix1, matrix2, "multiply");
        }

        // Główne pomiary
        for (int i = 0; i < RUNS; i++) {
            // Pomiar operacji refleksyjnej
            long startReflection = System.nanoTime();
            MatrixReflectionUtil.performOperation(matrix1, matrix2, "multiply");
            long endReflection = System.nanoTime();
            reflectionTimes.add(endReflection - startReflection);

            // Pomiar operacji obiektowej
            long startObject = System.nanoTime();
            matrix1.multiply(matrix2);
            long endObject = System.nanoTime();
            objectTimes.add(endObject - startObject);
        }

        String resultsFilename = OUTPUT_DIR + "matrix_performance_multiply_of_reflection_generic" + dim + ".txt";
        saveResultsToFile(resultsFilename, reflectionTimes, objectTimes);
        String statsFilename = OUTPUT_DIR + "matrix_statistics_multiply_of_reflection_generic" + dim + ".txt";
        StatisticsResult stats = calculateAndSaveStatistics(reflectionTimes, objectTimes, statsFilename, "Multiply", dim);
        System.out.println("Multiply results saved to " + resultsFilename + " and " + statsFilename);
        return stats;



    }


    @Override
    void displayDetailedStatistics() {
        System.out.println("\n===== AGREGOWANE STATYSTYKI Generyki obiektowe versus Generyki refleksyjne =====");
        System.out.printf("%-10s %-5s %-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s %-10s\n",
                "Operacja", "Dim", "Gen_Ref_Mean(ns)", "Gen_Ref_Median(ns)", "Gen_Ref_Mode(ns)", "Gen_Ref_StdDev(ns)",
                "Gen_Object_Mean(ns)", "Gen_Object_Median(ns)", "Gen_Object_Mode(ns)", "Gen_Object_StdDev(ns)", "Ratio");

        for (StatisticsResult sr : aggregatedResults) {
            System.out.printf("%-10s %-5d %20.2f %20.2f %20d %20.2f %20.2f %20.2f %20d %20.2f %10.2f\n",
                    sr.operation, sr.dimension,
                    sr.genericMean, sr.genericMedian, sr.genericMode, sr.genericStdDev,
                    sr.concreteMean, sr.concreteMedian, sr.concreteMode, sr.concreteStdDev,
                    sr.ratio);
        }
        System.out.println("=========================================================================================");
    }

    private void createDirectoriesIfNotExists(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("Created directory: " + path);
            } else {
                System.err.println("Failed to create directory: " + path);
            }
        }
    }




    /**
     * Zapis wyników testów do pliku.
     *
     * @param filename       Ścieżka do pliku.
     * @param reflectionTimes Lista czasów operacji refleksyjnych.
     * @param objectTimes     Lista czasów operacji obiektowych.
     */
    @Override
    protected void saveResultsToFile(String filename,
                                     List<Long> reflectionTimes,
                                     List<Long> objectTimes) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Generic Reflection Time\tGeneric Object Time\n");
            writer.write("-----------\t-------------\n");
            for (int i = 0; i < reflectionTimes.size(); i++) {
                writer.write(reflectionTimes.get(i) + "\t\t" + objectTimes.get(i) + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving results to file: " + e.getMessage());
        }
    }


    /**
     *
      * @param matrix1
     * @param matrix2
     * @param dim
     * @return
     */
    StatisticsResult testSubstractGenericObjectVsReflect(
            Matrix<Integer> matrix1,
            Matrix<Integer> matrix2,
            int dim
    ){
        List<Long> reflectionTimes = new ArrayList<>();
        List<Long> objectTimes = new ArrayList<>();

        // Warm-up – wykonywane kilka razy przed pomiarem
        for (int i = 0; i < 3; i++) {
          //  matrix1.subtract(matrix2);
            MatrixReflectionUtil.performOperation(matrix1, matrix2, "subtract");
        }


        // Główne pomiary
        for (int i = 0; i < RUNS; i++) {
            // Pomiar operacji refleksyjnej
            long startReflection = System.nanoTime();
            MatrixReflectionUtil.performOperation(matrix1, matrix2, "subtract");
            long endReflection = System.nanoTime();
            reflectionTimes.add(endReflection - startReflection);

            // Pomiar operacji obiektowej
            long startObject = System.nanoTime();
            matrix1.add(matrix2);
            long endObject = System.nanoTime();
            objectTimes.add(endObject - startObject);
        }

        String resultsFilename = OUTPUT_DIR + "matrix_performance_subtrac_of_reflection_generic" + dim + ".txt";
        saveResultsToFile(resultsFilename, reflectionTimes, objectTimes);
        String statsFilename = OUTPUT_DIR + "matrix_statistics_subtrac_of_reflection_generic" + dim + ".txt";
        StatisticsResult stats = calculateAndSaveStatistics(reflectionTimes, objectTimes, statsFilename, "Subtract", dim);
        System.out.println("subtrac results saved to " + resultsFilename + " and " + statsFilename);
        return stats;


    }


    @Override
    StatisticsResult testAddGenericObjectVsReflect(Matrix<Integer> matrix1,
                                                   Matrix<Integer> matrix2,
                                                   int dim) {
        List<Long> reflectionTimes = new ArrayList<>();
        List<Long> objectTimes = new ArrayList<>();

        // Warm-up – wykonywane kilka razy przed pomiarem
        for (int i = 0; i < 3; i++) {
            matrix1.add(matrix2);
            MatrixReflectionUtil.performOperation(matrix1, matrix2, "add");
        }

        // Główne pomiary
        for (int i = 0; i < RUNS; i++) {
            // Pomiar operacji refleksyjnej
            long startReflection = System.nanoTime();
            MatrixReflectionUtil.performOperation(matrix1, matrix2, "add");
            long endReflection = System.nanoTime();
            reflectionTimes.add(endReflection - startReflection);

            // Pomiar operacji obiektowej
            long startObject = System.nanoTime();
            matrix1.add(matrix2);
            long endObject = System.nanoTime();
            objectTimes.add(endObject - startObject);
        }

        String resultsFilename = OUTPUT_DIR + "matrix_performance_add_of_reflection_generic" + dim + ".txt";
        saveResultsToFile(resultsFilename, reflectionTimes, objectTimes);
        String statsFilename = OUTPUT_DIR + "matrix_statistics_add_of_reflection_generic" + dim + ".txt";
        StatisticsResult stats = calculateAndSaveStatistics(reflectionTimes, objectTimes, statsFilename, "Add", dim);
        System.out.println("Addition results saved to " + resultsFilename + " and " + statsFilename);
        return stats;
    }

    public void saveMatrixComparerToFile(int dimension, Matrix<Integer> matrixGeneric, Matrix<Integer> matrixOther) {
        File dir = new File("wyniki");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = "wyniki/porownanie_macierzy_" + dimension + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Porównanie macierzy dla wymiaru " + dimension + "x" + dimension + ":\n");
            writer.write("-------------------------------------------------\n");
            writer.write("Generic implementation (Matrix<Integer>) - seed 0L:\n");
            writer.write(matrixGeneric.toString() + "\n\n");
            writer.write("Object implementation (Matrix<Integer>) - seed 1L:\n");
            writer.write(matrixOther.toString() + "\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
        StatisticsResult calculateAndSaveStatistics(List<Long> genericReflectTimes, List<Long> GenericObjectTimes,
                                                String filename, String operation, int dimension) {
        DoubleSummaryStatistics genericStats = genericReflectTimes.stream().mapToDouble(Long::doubleValue).summaryStatistics();
        DoubleSummaryStatistics concreteStats = GenericObjectTimes.stream().mapToDouble(Long::doubleValue).summaryStatistics();

        double genericMean = genericStats.getAverage();
        double concreteMean = concreteStats.getAverage();
        double genericMedian = calculateMedian(genericReflectTimes);
        double concreteMedian = calculateMedian(GenericObjectTimes);
        long genericMode = calculateMode(genericReflectTimes);
        long concreteMode = calculateMode(GenericObjectTimes);
        double genericStd = calculateStandardDeviation(genericReflectTimes);
        double concreteStd = calculateStandardDeviation(GenericObjectTimes);
        double ratio = genericMean / concreteMean;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Statistics for generic Reflect implementation (Matrix<Integer>):\n");
            writer.write(String.format("Mean: %.2f ns\n", genericMean));
            writer.write(String.format("Median: %.2f ns\n", genericMedian));
            writer.write(String.format("Mode: %d ns\n", genericMode));
            writer.write(String.format("Min: %d ns\n", (long) genericStats.getMin()));
            writer.write(String.format("Max: %d ns\n", (long) genericStats.getMax()));
            writer.write(String.format("Standard Deviation: %.2f ns\n", genericStd));
            writer.write("\n");
            writer.write("Statistics for generic Reflect implementation (Matrix<Integer>:\n");
            writer.write(String.format("Mean: %.2f ns\n", concreteMean));
            writer.write(String.format("Median: %.2f ns\n", concreteMedian));
            writer.write(String.format("Mode: %d ns\n", concreteMode));
            writer.write(String.format("Min: %d ns\n", (long) concreteStats.getMin()));
            writer.write(String.format("Max: %d ns\n", (long) concreteStats.getMax()));
            writer.write(String.format("Standard Deviation: %.2f ns\n", concreteStd));
            writer.write("\n");
            writer.write(String.format("Ratio of mean times (generic reflect /generic object): %.2f\n", ratio));
            writer.write(String.format("Performance difference: Concrete implementation is %.2f times faster than generic\n",
                    (ratio > 1) ? ratio : 1 / ratio));
        } catch (IOException e) {
            System.err.println("Error saving statistics to file: " + e.getMessage());
        }
        return new StatisticsResult(operation, dimension, genericMean, genericMedian, genericMode, genericStd,
                concreteMean, concreteMedian, concreteMode, concreteStd, ratio);
    }

    void saveAggregatedStatisticsToFile() {
        String filename = CHARTS_DIR + "/aggregated_statistics_Generic.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Operacja\tDim\tGen_Mean(ns)\tGen_Median(ns)\tGen_Mode(ns)\tGen_StdDev(ns)\t" +
                    "Con_Mean(ns)\tCon_Median(ns)\tCon_Mode(ns)\tCon_StdDev(ns)\tRatio\n");
            for (StatisticsResult sr : aggregatedResults) {
                writer.write(String.format(
                        "%s\t%d\t%.2f\t%.2f\t%d\t%.2f\t%.2f\t%.2f\t%d\t%.2f\t%.2f\n",
                        sr.operation, sr.dimension, sr.genericMean, sr.genericMedian, sr.genericMode, sr.genericStdDev,
                        sr.concreteMean, sr.concreteMedian, sr.concreteMode, sr.concreteStdDev, sr.ratio
                ));
            }
        } catch (IOException e) {
            System.err.println("Error saving aggregated statistics: " + e.getMessage());
        }
    }
}
