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
    private static final String CHARTS_DIR = PerformanceTestMatrix.CHARTS_DIR + "GenericOfReflectionVsObject/charts/";
    private static final int RUNS = PerformanceTestMatrix.RUNS;
    private static final int[] DIMENSIONS = PerformanceTestMatrix.DIMENSIONS;

    public PerformanceTestMatrixGenericofReflectionVsObject() {
        super();
        createDirectoriesIfNotExists(OUTPUT_DIR + "statistic/");

    }
    public void runTest() throws IOException {
        aggregatedResults.clear();
        performTestGeneric();

        for (KindOfMatrix kind : KindOfMatrix.values()) {
            createDirectoriesIfNotExists(CHARTS_DIR + "RatioBarChart/");
            Vizualization.showOrSaveBarChartForRatioWithKind(aggregatedResults, kind, CHARTS_DIR);
        }

        String temp = CHARTS_DIR + "linearChart/byStat/";
        String temp2 = CHARTS_DIR + "barChart/byStat/" ;
        createDirectoriesIfNotExists(temp + "add/");
        createDirectoriesIfNotExists(temp + "subtract/");
        createDirectoriesIfNotExists(temp + "multiply/");


        createDirectoriesIfNotExists(temp2 + "add/");
        createDirectoriesIfNotExists(temp2 + "subtract/");
        createDirectoriesIfNotExists(temp2 + "multiply/");


        Field[] fields = StatisticsResult.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.getName();
            if (value.equals("kindOfBubbleSort") || value.equals("kindOfVector") || value.equals("kindOfMatrix") || value.equals("dimension") || value.equals("operation")) {
            } else {


                Vizualization.showOrSaveChartRatioVsDimWithOperationStat(aggregatedResults, "add", temp +"add/" , field.getName());
                Vizualization.showOrSaveChartRatioVsDimWithOperationStat(aggregatedResults, "subtract", temp +  "subtract/", field.getName());
                Vizualization.showOrSaveChartRatioVsDimWithOperationStat(aggregatedResults, "multiply", temp +"multiply/", field.getName());

                Vizualization.showOrSaveBarChartForOperation(aggregatedResults,"add" , temp2 +"add/" , field.getName() );
                Vizualization.showOrSaveBarChartForOperation(aggregatedResults,"multiply" , temp2 +  "subtract/", field.getName()   );
                Vizualization.showOrSaveBarChartForOperation(aggregatedResults,"subtract" , temp2 +"multiply/" , field.getName() );

            }

        }

    }



    public void performTestGeneric() {
        createDirectoriesIfNotExists(INPUT_DIR);
        createDirectoriesIfNotExists(OUTPUT_DIR);
        createDirectoriesIfNotExists(CHARTS_DIR);

        MatrixGenerator<Numberxx> generator = new MatrixGenerator<>(new NumberxxOperations());
        for (KindOfMatrix kind : KindOfMatrix.values()) {
           for (int dim : DIMENSIONS) {
               System.out.println("kind = " + kind + ", dim = " + dim);
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
           CalculationStatistic.saveStatisticsByOperation(OUTPUT_DIR, aggregatedResults, "matrix");
        }

        CalculationStatistic.writeDetailedStatisticsByMatrixKindToFile(aggregatedResults,OUTPUT_DIR + "statistic/" + "DetailedStatisticsByMatrixKind.txt");
        CalculationStatistic.writeDetailedStatisticsByOperationToFile(aggregatedResults , "add" , "matrix" , OUTPUT_DIR + "statistic/" + "addDetailedStatisticsByOperation.txt");
        CalculationStatistic.writeDetailedStatisticsByOperationToFile(aggregatedResults , "subtract" , "matrix", OUTPUT_DIR + "statistic/" + "subtractDetailedStatisticsByOperation.txt");
        CalculationStatistic.writeDetailedStatisticsByOperationToFile(aggregatedResults , "multiply" , "matrix" , OUTPUT_DIR + "statistic/" + "MultiplyDetailedStatisticsByOperation.txt");


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
        long startReflection;
        long endReflection;
        long startObject;
        long endObject;

        //AK RUNS
        for (int i = 0; i < RUNS; i++) {
            // Pomiar operacji refleksyjnej
             startReflection = System.nanoTime();
            MatrixReflectionUtil.performOperation(matrix1, matrix2, whatOperation);
             endReflection = System.nanoTime();
            reflectionTimes.add(endReflection - startReflection);

            // Pomiar operacji obiektowej
             startObject = System.nanoTime();
            matrix1.subtract(matrix2);
             endObject = System.nanoTime();
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

        createDirectoriesIfNotExists(OUTPUT_DIR  + "statistic/" + whatOperation + "/");

        String resultsFilename = OUTPUT_DIR + "statistic/" + whatOperation + "/" + "matrix_performance_" + whatOperation + "_of_reflection_generic" + dim + ".txt";
        saveResultsToFile(resultsFilename, reflectionTimes, objectTimes);

        String statsFilename = OUTPUT_DIR + "statistic/" + whatOperation + "/" + "matrix_statistics_" + whatOperation + "_of_reflection_generic" + dim + ".txt";
        StatisticsResult stats = CalculationStatistic.calculateAndSaveStatistics(
                reflectionTimes, objectTimes, statsFilename, whatOperation, dim, kind);
        return stats;
    }


    private void createDirectoriesIfNotExists(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
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
