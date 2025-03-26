package org.magister.matrix;

import lombok.Getter;
import lombok.Setter;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.magister.helper.CalculationStatistic;
import org.magister.helper.StatisticsResult;
import org.magister.helper.Vizualization;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



@Getter
@Setter
public class PerformanceTestMatrixConcreteReflectionVsObject extends PerformanceTestMatrix {
    private static final String INPUT_DIR = PerformanceTestMatrix.INPUT_DIR + "ConcreteOfReflectionVsObject/";
    private static final String OUTPUT_DIR = PerformanceTestMatrix.OUTPUT_DIR + "ConcreteOfReflectionVsObject/";
    private static final String CHARTS_DIR = PerformanceTestMatrix.CHARTS_DIR + "ConcreteOfReflectionVsObject/charts/";
    private static final int RUNS = PerformanceTestMatrix.RUNS;
    private static final int[] DIMENSIONS = PerformanceTestMatrix.DIMENSIONS;


    public PerformanceTestMatrixConcreteReflectionVsObject() {
        createDirectories();
    }

    public void runTest() throws IOException {
        aggregatedResults.clear();
        performTestConcrete();

        for (KindOfMatrix kind : KindOfMatrix.values()) {
            createDirectoriesIfNotExists(CHARTS_DIR + "RatioBarChart/");
            Vizualization.showOrSaveBarChartForRatioWithKind(aggregatedResults, kind, CHARTS_DIR);
        }
        Field[] fields = StatisticsResult.class.getDeclaredFields();
        String temp = CHARTS_DIR + "linearChart/byStat/";
        String temp2 = CHARTS_DIR + "barChart/byStat/" ;
        createDirectoriesIfNotExists(temp + "add/");
        createDirectoriesIfNotExists(temp + "subtract/");
        createDirectoriesIfNotExists(temp + "multiply/");


        createDirectoriesIfNotExists(temp2 + "add/");
        createDirectoriesIfNotExists(temp2 + "subtract/");
        createDirectoriesIfNotExists(temp2 + "multiply/");

        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.getName();
            if (value.equals("kindOfBubbleSort") || value.equals("kindOfVector") || value.equals("kindOfMatrix") || value.equals("dimension") || value.equals("operation")) {
            } else {
              //  System.out.println("Nazwa pola: " + field.getName() + " -> wartość: " + value);
                Vizualization.showOrSaveChartRatioVsDimWithOperationStat(aggregatedResults, "add", temp + "add/", field.getName());
                Vizualization.showOrSaveChartRatioVsDimWithOperationStat(aggregatedResults, "subtract", temp + "subtract/", field.getName());
                Vizualization.showOrSaveChartRatioVsDimWithOperationStat(aggregatedResults, "multiply", temp + "multiply/", field.getName());
                Vizualization.showOrSaveBarChartForOperation(aggregatedResults,"add" , temp2 + "add/" , field.getName() );
                Vizualization.showOrSaveBarChartForOperation(aggregatedResults,"multiply" , temp2 + "subtract/", field.getName()   );
                Vizualization.showOrSaveBarChartForOperation(aggregatedResults,"subtract" , temp2 + "multiply/" , field.getName() );
            }

        }

    }





    public void performTestConcrete() {
        createDirectoriesIfNotExists(INPUT_DIR);
        createDirectoriesIfNotExists(OUTPUT_DIR);
        createDirectoriesIfNotExists(CHARTS_DIR);



        // Utworz instancje
        Matrix1Generator generator = new Matrix1Generator();



        // Iterujemy po wszystkich typach macierzy
        for (KindOfMatrix kind : KindOfMatrix.values()) {
            for (int dim : DIMENSIONS) {
                Matrix1 matrixConcreteFirst = generator.createMatrix1(kind,dim,0L);
                Matrix1 matrixConcreteSecond = generator.createMatrix1(kind,dim,1L);

                saveMatrix1ToFile(matrixConcreteFirst, INPUT_DIR + "matrix1_concrete_" + kind.toString().toLowerCase() + "_" + dim + ".txt");
                saveMatrix1ToFile(matrixConcreteSecond, INPUT_DIR + "matrix2_concrete_" +  kind.toString().toLowerCase() + "_" +dim + ".txt");

                aggregatedResults.add(testAddConcreteObjectVsReflect(matrixConcreteFirst, matrixConcreteSecond, dim,kind));
                aggregatedResults.add(testSubstractConcreteObjectVsReflect(matrixConcreteFirst, matrixConcreteSecond, dim,kind));
                aggregatedResults.add(testMultiplyConcreteObjectVsReflect(matrixConcreteFirst, matrixConcreteSecond, dim,kind));

            }

            CalculationStatistic.saveStatisticsByOperation(OUTPUT_DIR,aggregatedResults,"matrix");
        }

        CalculationStatistic.writeDetailedStatisticsByMatrixKindToFile(aggregatedResults,OUTPUT_DIR + "statistic/" + "DetailedStatisticsByMatrixKind.txt");
        CalculationStatistic.writeDetailedStatisticsByOperationToFile(aggregatedResults , "add" , "matrix" , OUTPUT_DIR + "statistic/" + "addDetailedStatisticsByOperation.txt");
        CalculationStatistic.writeDetailedStatisticsByOperationToFile(aggregatedResults , "subtract" , "matrix", OUTPUT_DIR + "statistic/" + "subtractDetailedStatisticsByOperation.txt");
        CalculationStatistic.writeDetailedStatisticsByOperationToFile(aggregatedResults , "multiply" , "matrix" , OUTPUT_DIR + "statistic/" + "MultiplyDetailedStatisticsByOperation.txt");

        ;
        // Wyświetlamy i zapisujemy zagregowane statystyki
       /// CalculationStatistic.displayDetailedStatisticsByOperation(aggregatedResults , "add" , "matrix");
       // CalculationStatistic.displayDetailedStatisticsByOperation(aggregatedResults , "subtract" ,"matrix");
       // CalculationStatistic.displayDetailedStatisticsByOperation(aggregatedResults , "multiply" , "matrix");
        // Wyświetlamy i zapisujemy zagregowane statystyki
       /// CalculationStatistic.displayDetailedStatisticsByMatrixKind(aggregatedResults);


    }







    private StatisticsResult testMultiplyConcreteObjectVsReflect(Matrix1 matrixConcrete1,
                                                                 Matrix1 matrixConcrete2,
                                                                 int dim ,
                                                                 KindOfMatrix kindOfMatrix) {
        List<Long> reflectionTimes = new ArrayList<>();
        List<Long> objectTimes = new ArrayList<>();

        //   Warm-up – wykonywane kilka razy przed pomiarem
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
       // saveResultsToFile(resultsFilename, reflectionTimes, objectTimes);
        String statsFilename = OUTPUT_DIR + "matrix_statistics_multiply_of_reflection_and_object_concrete" + dim + ".txt";
      //  StatisticsResult stats = CalculationStatistic.calculateAndSaveStatistics(reflectionTimes, objectTimes, statsFilename, "multiply", dim,kindOfMatrix);
      //  System.out.println("multiply results saved to " + resultsFilename + " and " + statsFilename);
        return saveResults(reflectionTimes,objectTimes,"multiply" , dim , kindOfMatrix);

    }

    private StatisticsResult testSubstractConcreteObjectVsReflect(Matrix1 matrixConcrete1,
                                                                  Matrix1 matrixConcrete2,
                                                                  int dim,
                                                                  KindOfMatrix kindOfMatrix) {
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
        // saveResultsToFile(resultsFilename, reflectionTimes, objectTimes);
        String statsFilename = OUTPUT_DIR + "matrix_statistics_subtract_of_reflection_and_object_concrete" + dim + ".txt";
       // StatisticsResult stats = CalculationStatistic.calculateAndSaveStatistics(reflectionTimes, objectTimes, statsFilename, "subtract", dim,kindOfMatrix);
        //System.out.println("subtract results saved to " + resultsFilename + " and " + statsFilename);
        return  saveResults(reflectionTimes,objectTimes,"subtract" , dim, kindOfMatrix);

    }

    private StatisticsResult testAddConcreteObjectVsReflect(Matrix1 matrixConcrete1,
                                                            Matrix1 matrixConcrete2,
                                                            int dim,
                                                            KindOfMatrix kindOfMatrix) {
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
       // saveResultsToFile(resultsFilename, reflectionTimes, objectTimes);
        String statsFilename = OUTPUT_DIR + "matrix_statistics_add_of_reflection_and_object_concrete" + dim + ".txt";
      //  StatisticsResult stats = CalculationStatistic.calculateAndSaveStatistics(reflectionTimes, objectTimes, statsFilename, "Add", dim,kindOfMatrix);
       return saveResults(reflectionTimes,objectTimes,"add" , dim , kindOfMatrix);

    }

    private StatisticsResult saveResults(
            List<Long> reflectionTimes,
            List<Long> objectTimes,
            String whatOperation,
            int dim,
            KindOfMatrix kind) {

        createDirectoriesIfNotExists(OUTPUT_DIR  + "statistic/" + whatOperation + "/");

        String resultsFilename = OUTPUT_DIR + "statistic/" + whatOperation + "/" + "matrix_performance_" + whatOperation + "_of_reflection_concrete" + dim + ".txt";
        saveResultsToFile(resultsFilename, reflectionTimes, objectTimes);

        String statsFilename = OUTPUT_DIR + "statistic/" + whatOperation + "/" + "matrix_statistics_" + whatOperation + "_of_reflection_concrete" + dim + ".txt";
        StatisticsResult stats = CalculationStatistic.calculateAndSaveStatistics(
                reflectionTimes, objectTimes, statsFilename, whatOperation, dim, kind);

        return stats;
    }






    private void createDirectoriesIfNotExists(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
               // System.out.println("Created directory: " + path);
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
    void saveAggregatedStatisticsToFile() {
        String filename = CHARTS_DIR + "/aggregated_concrete_statistics.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Operacja\tDim\tGen_Mean(ns)\tGen_Median(ns)\tGen_Mode(ns)\tGen_StdDev(ns)\t" +
                    "Con_Mean(ns)\tCon_Median(ns)\tCon_Mode(ns)\tCon_StdDev(ns)\tRatio\n");
            for (StatisticsResult sr : aggregatedResults) {
                writer.write(String.format(
                        "%s\t%d\t%.2f\t%.2f\t%d\t%.2f\t%.2f\t%.2f\t%d\t%.2f\t%.2f\n",
                        sr.operation, sr.dimension, sr.reflectMean, sr.reflectMedian,  sr.reflectStdDev,
                        sr.objectMean, sr.objectMedian, sr.objectStdDev, sr.ratio
                ));
            }
        } catch (IOException e) {
            System.err.println("Error saving aggregated statistics: " + e.getMessage());
        }
    }
















}
