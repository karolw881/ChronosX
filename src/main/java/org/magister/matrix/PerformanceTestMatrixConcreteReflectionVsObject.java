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
public class PerformanceTestMatrixConcreteReflectionVsObject extends PerformanceTestMatrix {
    private static final String INPUT_DIR = PerformanceTestMatrix.INPUT_DIR + "ConcreteOfReflectionVsObject/";
    private static final String OUTPUT_DIR = PerformanceTestMatrix.OUTPUT_DIR + "ConcreteOfReflectionVsObject/";
    private static final String CHARTS_DIR = PerformanceTestMatrix.CHARTS_DIR + "ConcreteOfReflectionVsObject/";
    private static final int RUNS = PerformanceTestMatrix.RUNS;
    private static final int[] DIMENSIONS = PerformanceTestMatrix.DIMENSIONS;
    //  private final List<StatisticsResult> aggregatedResults = new ArrayList<>();

    public PerformanceTestMatrixConcreteReflectionVsObject() {
        // Używamy metod odziedziczonych do tworzenia katalogów
        createDirectories();
        //   performTest();
    }



    public void runTest() {

        aggregatedResults.clear();
        performTestConcrete();
    }

    public void performTestConcrete() {
        // Testujemy dla każdego wymiaru macierzy
        for (int dim : DIMENSIONS) {
            System.out.println("Testing matrix of dimension " + dim + "x" + dim);

            // Tworzymy macierze z ustalonymi ziarnami: seed 0 dla pierwszej i seed 1 dla drugiej
         //   Matrix1 matrixConcrete1 = createRandomMatrix1(dim, 0L);
          //  Matrix1 matrixConcrete2 = createRandomMatrix1(dim, 1L);



            Matrix1Generator matrix1Generator = new Matrix1Generator();
            Matrix1 matrixConcrete1 = matrix1Generator.createIdentityMatrix(dim);
            Matrix1 matrixConcrete2 = matrix1Generator.createIdentityMatrix(dim);


            // Tworzenie katalogów wejściowych, wyjściowych, wykresów oraz dodatkowego katalogu "wyniki"
            createDirectoriesIfNotExists(INPUT_DIR);
            createDirectoriesIfNotExists(OUTPUT_DIR);
            createDirectoriesIfNotExists(CHARTS_DIR);
            createDirectoriesIfNotExists("wyniki");

            // Zapisujemy macierze wejściowe do plików
            saveMatrix1ToFile( matrixConcrete1, INPUT_DIR + "matrix1_concrete" + dim + ".txt");
            saveMatrix1ToFile(matrixConcrete2, INPUT_DIR + "matrix2_concrete" + dim + ".txt");

            // Porównanie i zapis macierzy
            //  saveMatrixComparerToFile(dim, matrixGenericReflectFirst, matrixGenericReflectSecond);



             aggregatedResults.add(testAddConcreteObjectVsReflect(matrixConcrete1, matrixConcrete2, dim));
             aggregatedResults.add(testSubstractConcreteObjectVsReflect(matrixConcrete1, matrixConcrete2, dim));
             aggregatedResults.add(testMultiplyConcreteObjectVsReflect(matrixConcrete1, matrixConcrete2, dim));
             // aggregatedResults.add(testDivideGenericObjectVsReflect(matrixConcrete1, matrixConcrete2, dim));

        }

        // Wyświetlamy i zapisujemy zagregowane statystyki
        displayDetailedStatistics();
        saveAggregatedStatisticsToFile();

        // Generowanie wykresów (jeśli wymagane)
        // generateCharts();
    }

    private StatisticsResult testMultiplyConcreteObjectVsReflect(Matrix1 matrixConcrete1,
                                                                 Matrix1 matrixConcrete2,
                                                                 int dim) {
        List<Long> reflectionTimes = new ArrayList<>();
        List<Long> objectTimes = new ArrayList<>();

        // Warm-up – wykonywane kilka razy przed pomiarem
        for (int i = 0; i < 3; i++) {
            matrixConcrete1.multiply(matrixConcrete2);
            MatrixReflectionUtil.performOperation2(matrixConcrete1, matrixConcrete2 , "multiply");
        }

        // Główne pomiary
        for (int i = 0; i < RUNS; i++) {
            // Pomiar operacji refleksyjnej
            long startReflection = System.nanoTime();
            MatrixReflectionUtil.performOperation2(matrixConcrete1, matrixConcrete2, "multiply");
            long endReflection = System.nanoTime();
            reflectionTimes.add(endReflection - startReflection);

            // Pomiar operacji obiektowej
            long startObject = System.nanoTime();
            matrixConcrete1.multiply(matrixConcrete2);
            long endObject = System.nanoTime();
            objectTimes.add(endObject - startObject);
        }

        String resultsFilename = OUTPUT_DIR + "matrix_performance_multiply_of_reflection_and_object_concrete" + dim + ".txt";
        saveResultsToFile(resultsFilename, reflectionTimes, objectTimes);
        String statsFilename = OUTPUT_DIR + "matrix_statistics_multiply_of_reflection_and_object_concrete" + dim + ".txt";
        StatisticsResult stats = calculateAndSaveStatistics(reflectionTimes, objectTimes, statsFilename, "multiply", dim);
        System.out.println("multiply results saved to " + resultsFilename + " and " + statsFilename);
        return stats;

    }

    private StatisticsResult testSubstractConcreteObjectVsReflect(Matrix1 matrixConcrete1,
                                                                  Matrix1 matrixConcrete2,
                                                                  int dim) {
        List<Long> reflectionTimes = new ArrayList<>();
        List<Long> objectTimes = new ArrayList<>();

        // Warm-up – wykonywane kilka razy przed pomiarem
        for (int i = 0; i < 3; i++) {
            matrixConcrete1.subtract(matrixConcrete2);
            MatrixReflectionUtil.performOperation2(matrixConcrete1, matrixConcrete2 , "subtract");
        }

        // Główne pomiary
        for (int i = 0; i < RUNS; i++) {
            // Pomiar operacji refleksyjnej
            long startReflection = System.nanoTime();
            MatrixReflectionUtil.performOperation2(matrixConcrete1, matrixConcrete2, "subtract");
            long endReflection = System.nanoTime();
            reflectionTimes.add(endReflection - startReflection);

            // Pomiar operacji obiektowej
            long startObject = System.nanoTime();
            matrixConcrete1.subtract(matrixConcrete2);
            long endObject = System.nanoTime();
            objectTimes.add(endObject - startObject);
        }

        String resultsFilename = OUTPUT_DIR + "matrix_performance_subtract_of_reflection_and_object_concrete" + dim + ".txt";
        saveResultsToFile(resultsFilename, reflectionTimes, objectTimes);
        String statsFilename = OUTPUT_DIR + "matrix_statistics_subtract_of_reflection_and_object_concrete" + dim + ".txt";
        StatisticsResult stats = calculateAndSaveStatistics(reflectionTimes, objectTimes, statsFilename, "subtract", dim);
        System.out.println("subtract results saved to " + resultsFilename + " and " + statsFilename);
        return stats;

    }

    private StatisticsResult testAddConcreteObjectVsReflect(Matrix1 matrixConcrete1,
                                                            Matrix1 matrixConcrete2,
                                                            int dim) {
        List<Long> reflectionTimes = new ArrayList<>();
        List<Long> objectTimes = new ArrayList<>();

        // Warm-up – wykonywane kilka razy przed pomiarem
        for (int i = 0; i < 3; i++) {
            matrixConcrete1.add(matrixConcrete2);
            MatrixReflectionUtil.performOperation2(matrixConcrete1, matrixConcrete2 , "add");
        }

        // Główne pomiary
        for (int i = 0; i < RUNS; i++) {
            // Pomiar operacji refleksyjnej
            long startReflection = System.nanoTime();
            MatrixReflectionUtil.performOperation2(matrixConcrete1, matrixConcrete2, "add");
            long endReflection = System.nanoTime();
            reflectionTimes.add(endReflection - startReflection);

            // Pomiar operacji obiektowej
            long startObject = System.nanoTime();
            matrixConcrete1.add(matrixConcrete2);
            long endObject = System.nanoTime();
            objectTimes.add(endObject - startObject);
        }

        String resultsFilename = OUTPUT_DIR + "matrix_performance_add_of_reflection_and_object_concrete" + dim + ".txt";
        saveResultsToFile(resultsFilename, reflectionTimes, objectTimes);
        String statsFilename = OUTPUT_DIR + "matrix_statistics_add_of_reflection_and_object_concrete" + dim + ".txt";
        StatisticsResult stats = calculateAndSaveStatistics(reflectionTimes, objectTimes, statsFilename, "add", dim);
        System.out.println("add results saved to " + resultsFilename + " and " + statsFilename);
        return stats;

    }



    @Override
    void displayDetailedStatistics() {
        System.out.println("\n===== AGREGOWANE STATYSTYKI Concrete obiektowe versus Generyki refleksyjne =====");
        System.out.printf("%-10s %-5s %-20s %-20s %-20s %-20s %-20s %-20s %-20s %-20s %-10s\n",
                "Operacja", "Dim", "Gen_Ref_Mean(ns)", "Gen_Ref_Median(ns)", "Gen_Ref_Mode(ns)", "Gen_Ref_StdDev(ns)",
                "Gen_Object_Mean(ns)", "Gen_Object_Median(ns)", "Gen_Object_Mode(ns)", "Gen_Object_StdDev(ns)", "Ratio");

        for (StatisticsResult sr : aggregatedResults) {
            System.out.printf("%-10s %-5d %20.2f %20.2f %20d %20.2f %20.2f %20.2f %20d %20.2f %10.2f\n",
                    sr.operation, sr.dimension,
                    sr.reflectMean, sr.reflectMedian, sr.reflectMode, sr.reflectStdDev,
                    sr.objectMean, sr.objectMedian, sr.objectMode, sr.objectStdDev,
                    sr.ratio);
        }
        System.out.println("=========================================================================================");
    }

    @Override
    StatisticsResult calculateAndSaveStatistics(List<Long> genericReflectTimes, List<Long> GenericObjectTimes, String filename, String operation, int dimension, KindOfMatrix kind) {
        return null;
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


    @Override
    void saveAggregatedStatisticsToFile() {
        String filename = CHARTS_DIR + "/aggregated_concrete_statistics.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Operacja\tDim\tGen_Mean(ns)\tGen_Median(ns)\tGen_Mode(ns)\tGen_StdDev(ns)\t" +
                    "Con_Mean(ns)\tCon_Median(ns)\tCon_Mode(ns)\tCon_StdDev(ns)\tRatio\n");
            for (StatisticsResult sr : aggregatedResults) {
                writer.write(String.format(
                        "%s\t%d\t%.2f\t%.2f\t%d\t%.2f\t%.2f\t%.2f\t%d\t%.2f\t%.2f\n",
                        sr.operation, sr.dimension, sr.reflectMean, sr.reflectMedian, sr.reflectMode, sr.reflectStdDev,
                        sr.objectMean, sr.objectMedian, sr.objectMode, sr.objectStdDev, sr.ratio
                ));
            }
        } catch (IOException e) {
            System.err.println("Error saving aggregated statistics: " + e.getMessage());
        }
    }
}
