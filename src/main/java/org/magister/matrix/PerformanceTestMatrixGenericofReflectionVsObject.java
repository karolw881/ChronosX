package org.magister.matrix;

import lombok.Getter;
import lombok.Setter;
import org.magister.helper.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PerformanceTestMatrixGenericofReflectionVsObject extends PerformanceTestMatrix {

    private static final String INPUT_DIR = PerformanceTestMatrix.INPUT_DIR + "GenericOfReflectionVsObject/";
    private static final String OUTPUT_DIR = PerformanceTestMatrix.OUTPUT_DIR + "GenericOfReflectionVsObject/";
    private static final String CHARTS_DIR = PerformanceTestMatrix.CHARTS_DIR + "GenericOfReflectionVsObject/";
    private static final int RUNS = PerformanceTestMatrix.RUNS;
    private static final int[] DIMENSIONS = PerformanceTestMatrix.DIMENSIONS;

    public PerformanceTestMatrixGenericofReflectionVsObject() {
        createDirectories();
    }
    public void runTest() throws IOException {
        aggregatedResults.clear();
        performTestGeneric();

        for (KindOfMatrix kind : KindOfMatrix.values())
            Vizualization.showOrSaveBarChartForRatioWithKind(aggregatedResults, kind, CHARTS_DIR);

        Field[] fields = StatisticsResult.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.getName();
            if (value.equals("kindOfBubbleSort") || value.equals("kindOfVector") || value.equals("kindOfMatrix") || value.equals("dimension") || value.equals("operation")) {
            } else {

               // System.out.println("Nazwa pola: " + field.getName() + " -> wartość: " + value);
                Vizualization.showOrSaveChartRatioVsDimWithOperationStat(aggregatedResults, "add", CHARTS_DIR, field.getName());
                Vizualization.showOrSaveChartRatioVsDimWithOperationStat(aggregatedResults, "subtract", CHARTS_DIR, field.getName());
                Vizualization.showOrSaveChartRatioVsDimWithOperationStat(aggregatedResults, "multiply", CHARTS_DIR, field.getName());

                Vizualization.showOrSaveBarChartForOperation(aggregatedResults,"add" , CHARTS_DIR , field.getName() );
                Vizualization.showOrSaveBarChartForOperation(aggregatedResults,"multiply" , CHARTS_DIR, field.getName()   );
                Vizualization.showOrSaveBarChartForOperation(aggregatedResults,"subtract" , CHARTS_DIR  , field.getName() );
                //System.out.println(field.getName());
            }

        }

    }



    public void performTestGeneric() {
        createDirectoriesIfNotExists(INPUT_DIR);
        createDirectoriesIfNotExists(OUTPUT_DIR);
        createDirectoriesIfNotExists(CHARTS_DIR);
        MatrixGenerator<Numberxx> generator = new MatrixGenerator<>(new NumberxxOperations());
        for (KindOfMatrix kind : KindOfMatrix.values()) {
            System.out.println("Test dla typu macierzy: " + kind);
            for (int dim : DIMENSIONS) {
                System.out.println("Testujemy macierz " + kind + " o wymiarze " + dim + "x" + dim);

                Matrix<Numberxx> matrixGenericFirst = generator.createMatrix(kind, dim, 0L);
                Matrix<Numberxx> matrixGenericSecond = generator.createMatrix(kind, dim, 1L);

                String fileNameFirst = INPUT_DIR + "matrix1_" + kind.toString().toLowerCase() + "_" + dim + ".txt";
                String fileNameSecond = INPUT_DIR + "matrix2_" + kind.toString().toLowerCase() + "_" + dim + ".txt";

                saveMatrixToFile(matrixGenericFirst, fileNameFirst);
                saveMatrixToFile(matrixGenericSecond, fileNameSecond);
                aggregatedResults.add(testGenericObjectVsReflect(matrixGenericFirst, matrixGenericSecond, dim, kind , "multiply"));
                aggregatedResults.add(testGenericObjectVsReflect(matrixGenericFirst, matrixGenericSecond, dim,kind , "add"));
                aggregatedResults.add(testGenericObjectVsReflect(matrixGenericFirst, matrixGenericSecond, dim,kind , "subtract"));

            }
            // Wyświetlamy i zapisujemy zagregowane statystyki
            CalculationStatistic.saveStatisticsByOperation(OUTPUT_DIR, aggregatedResults, "matrix");
        }
        CalculationStatistic.displayDetailedStatisticsByOperation(aggregatedResults , "add" , "matrix");
        CalculationStatistic.displayDetailedStatisticsByOperation(aggregatedResults , "subtract" , "matrix");
        CalculationStatistic.displayDetailedStatisticsByOperation(aggregatedResults , "multiply" , "matrix" );
        // Wyświetlamy i zapisujemy zagregowane statystyki
        CalculationStatistic.displayDetailedStatisticsByMatrixKind(aggregatedResults);



    }






        private StatisticsResult testGenericObjectVsReflect(
            Matrix<Numberxx> matrix1,
            Matrix<Numberxx> matrix2,
            int dim, KindOfMatrix kind, String whatOperation) {

        List<Long> reflectionTimes = new ArrayList<>();
        List<Long> objectTimes = new ArrayList<>();

        // Wykonaj rozgrzewkę dla odpowiedniej operacji
        performWarmup(matrix1, matrix2, whatOperation);

        // Wykonaj pomiary dla odpowiedniej operacji
        performMeasurements(matrix1, matrix2, whatOperation, reflectionTimes, objectTimes);

        // Zapisz wyniki i statystyki
        return saveResults(reflectionTimes, objectTimes, whatOperation, dim, kind);
    }

    /**
     * Wykonuje operacje rozgrzewające przed właściwymi pomiarami
     */
    private void performWarmup(Matrix<Numberxx> matrix1, Matrix<Numberxx> matrix2, String whatOperation) {
        for (int i = 0; i < 3; i++) {
            // Wykonaj operację obiektową
            performObjectOperation(matrix1, matrix2, whatOperation);
            // Wykonaj operację refleksyjną
            MatrixReflectionUtil.performOperation(matrix1, matrix2, whatOperation);
        }
    }

    /**
     * Wykonuje właściwe pomiary dla danej operacji
     */
    private void performMeasurements(
            Matrix<Numberxx> matrix1,
            Matrix<Numberxx> matrix2,
            String whatOperation,
            List<Long> reflectionTimes,
            List<Long> objectTimes) {

        if (whatOperation.equalsIgnoreCase("multiply")) {
            measureMultiplyOperation(matrix1, matrix2, whatOperation, reflectionTimes, objectTimes);
        } else if (whatOperation.equalsIgnoreCase("subtract")) {
            measureSubtractOperation(matrix1, matrix2, whatOperation, reflectionTimes, objectTimes);
        } else if (whatOperation.equalsIgnoreCase("add")) {
            measureAddOperation(matrix1, matrix2, whatOperation, reflectionTimes, objectTimes);
        }
    }

    /**
     * Wykonuje pomiary dla operacji mnożenia
     */
    private void measureMultiplyOperation(
            Matrix<Numberxx> matrix1,
            Matrix<Numberxx> matrix2,
            String whatOperation,
            List<Long> reflectionTimes,
            List<Long> objectTimes) {

        for (int i = 0; i < RUNS; i++) {
            // Pomiar operacji refleksyjnej
            long startReflection = System.nanoTime();
            MatrixReflectionUtil.performOperation(matrix1, matrix2, whatOperation);
            long endReflection = System.nanoTime();
            reflectionTimes.add(endReflection - startReflection);

            // Pomiar operacji obiektowej
            long startObject = System.nanoTime();
            matrix1.multiply(matrix2);
            long endObject = System.nanoTime();
            objectTimes.add(endObject - startObject);
        }
    }

    /**
     * Wykonuje pomiary dla operacji odejmowania
     */
    private void measureSubtractOperation(
            Matrix<Numberxx> matrix1,
            Matrix<Numberxx> matrix2,
            String whatOperation,
            List<Long> reflectionTimes,
            List<Long> objectTimes) {

        for (int i = 0; i < RUNS; i++) {
            // Pomiar operacji refleksyjnej
            long startReflection = System.nanoTime();
            MatrixReflectionUtil.performOperation(matrix1, matrix2, whatOperation);
            long endReflection = System.nanoTime();
            reflectionTimes.add(endReflection - startReflection);

            // Pomiar operacji obiektowej
            long startObject = System.nanoTime();
            matrix1.subtract(matrix2);
            long endObject = System.nanoTime();
            objectTimes.add(endObject - startObject);
        }
    }

    /**
     * Wykonuje pomiary dla operacji dodawania
     */
    private void measureAddOperation(
            Matrix<Numberxx> matrix1,
            Matrix<Numberxx> matrix2,
            String whatOperation,
            List<Long> reflectionTimes,
            List<Long> objectTimes) {

        for (int i = 0; i < RUNS; i++) {
            // Pomiar operacji refleksyjnej
            long startReflection = System.nanoTime();
            MatrixReflectionUtil.performOperation(matrix1, matrix2, whatOperation);
            long endReflection = System.nanoTime();
            reflectionTimes.add(endReflection - startReflection);

            // Pomiar operacji obiektowej
            long startObject = System.nanoTime();
            matrix1.add(matrix2);
            long endObject = System.nanoTime();
            objectTimes.add(endObject - startObject);
        }
    }

    /**
     * Wykonuje operację obiektową na podstawie nazwy operacji
     */
    private void performObjectOperation(Matrix<Numberxx> matrix1, Matrix<Numberxx> matrix2, String whatOperation) {
        if (whatOperation.equalsIgnoreCase("multiply")) {
            matrix1.multiply(matrix2);
        } else if (whatOperation.equalsIgnoreCase("subtract")) {
            matrix1.subtract(matrix2);
        } else if (whatOperation.equalsIgnoreCase("add")) {
            matrix1.add(matrix2);
        }
    }

    /**
     * Zapisuje wyniki pomiarów i oblicza statystyki
     */
    private StatisticsResult saveResults(
            List<Long> reflectionTimes,
            List<Long> objectTimes,
            String whatOperation,
            int dim,
            KindOfMatrix kind) {

        String resultsFilename = OUTPUT_DIR + "matrix_performance_" + whatOperation + "_of_reflection_generic" + dim + ".txt";
        saveResultsToFile(resultsFilename, reflectionTimes, objectTimes);

        String statsFilename = OUTPUT_DIR + "matrix_statistics_" + whatOperation + "_of_reflection_generic" + dim + ".txt";
        StatisticsResult stats = CalculationStatistic.calculateAndSaveStatistics(
                reflectionTimes, objectTimes, statsFilename, whatOperation, dim, kind);

        System.out.println(whatOperation + " results saved to " + resultsFilename + " and " + statsFilename);
        return stats;
    }



    @Override
    void displayDetailedStatistics() {
        System.out.println("\n===== AGREGOWANE STATYSTYKI Generyki obiektowe versus Generyki refleksyjne =====");

        // Nagłówek tabeli rozszerzony o nowe statystyki
        System.out.printf("%-10s %-5s %-15s %-15s %-15s %-15s %-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s " +
                        "%-15s %-15s %-15s %-15s %-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s\n",
                "Operacja", "Dim",
                "Ref_Mean(ns)", "Ref_Median(ns)", "Ref_Mode(ns)", "Ref_StdDev(ns)",
                "Ref_Q1(ns)", "Ref_Q3(ns)", "Ref_IQR(ns)", "Ref_CV(%)", "Ref_Skew", "Ref_Kurt",
                "Ref_Mom3", "Ref_Mom4",
                "Obj_Mean(ns)", "Obj_Median(ns)", "Obj_Mode(ns)", "Obj_StdDev(ns)",
                "Obj_Q1(ns)", "Obj_Q3(ns)", "Obj_IQR(ns)", "Obj_CV(%)", "Obj_Skew", "Obj_Kurt",
                "Obj_Mom3", "Obj_Mom4",
                "Ratio","Kind of matrix");

        // Iteracja po wynikach i wypisanie wierszy tabeli
        for (StatisticsResult sr : aggregatedResults) {
            System.out.printf("%-10s %-5d %15.2f %15.2f %15d %15.2f %10.2f %10.2f %10.2f %10.2f %10.4f %10.4f %10.4f %10.4f " +
                            "%15.2f  %15.2f %10.2f %10.2f %10.2f %10.2f %10.4f %10.4f %10.4f %10.4f %10.2f %s  \n",
                    sr.operation, sr.dimension,
                    sr.reflectMean, sr.reflectMedian, sr.reflectStdDev,
                    sr.reflectQ1, sr.reflectQ3, sr.reflectIQR, sr.reflectCV, sr.reflectSkewness, sr.reflectKurtosis,
                    sr.objectMean, sr.objectMedian, sr.objectStdDev,
                    sr.objectQ1, sr.objectQ3, sr.objectIQR, sr.objectCV, sr.objectSkewness, sr.objectKurtosis,
                    sr.ratio , sr.kindOfMatrix.toString() );
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
     * @param filename        Ścieżka do pliku.
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















}
