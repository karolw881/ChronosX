package org.magister.bubbleSort;

import org.magister.helper.*;
import org.magister.vector.PerformanceTestVector;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class PerformanceBubbleSortConcreteReflectVsObject extends  PerformanceTestBubbleSort {
    private static final String INPUT_DIR = PerformanceTestBubbleSort.INPUT_DIR + "/ConcreteOfReflectionVsObject/";
    private static final String OUTPUT_DIR = PerformanceTestBubbleSort.OUTPUT_DIR + "/ConcreteOfReflectionVsObject/";
    private static final String CHARTS_DIR = PerformanceTestBubbleSort.CHARTS_DIR + "/ConcreteOfReflectionVsObject/charts/";
    private static final int RUNS = PerformanceTestBubbleSort.RUNS;
    private static final int[] DIMENSIONS = PerformanceTestBubbleSort.DIMENSIONS;

    public PerformanceBubbleSortConcreteReflectVsObject(){
        super();
        createDirectoriesIfNotExists(INPUT_DIR);
        createDirectoriesIfNotExists(OUTPUT_DIR);
        createDirectoriesIfNotExists(CHARTS_DIR);
        createDirectoriesIfNotExists(OUTPUT_DIR + "statistic/");

    }


    /**
     * Główna metoda testowa.
     */
    public void runTest() throws IOException {
        aggregatedResults.clear();
        performTestConcreteBubbleSort();
        // Dla każdego rodzaju uporządkowania (DataOrder) tworzymy wykres słupkowy
        for (KindOfBubbleSort kind : KindOfBubbleSort.values()) {
            createDirectoriesIfNotExists(CHARTS_DIR + "/RatioBarChart/");
            Vizualization.showOrSaveBarChartForRatioWithKindForBubble(aggregatedResults,kind,CHARTS_DIR+ "/RatioBarChart/");
        }

        String temp = CHARTS_DIR + "/linearChart/byStat/";
        String temp2 = CHARTS_DIR + "/barChart/byStat/" ;
        createDirectoriesIfNotExists(temp + "sort/");
        createDirectoriesIfNotExists(temp2 + "sort/");



       CalculationStatistic.saveStatisticsByOperation(OUTPUT_DIR, aggregatedResults, "bubble");

        Field[] fields = StatisticsResult.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.getName();
            if (value.equals("kindOfBubbleSort") || value.equals("kindOfVector") || value.equals("kindOfMatrix") || value.equals("dimension") || value.equals("operation")) {
            } else {
                Vizualization.showOrSaveChartDiffrenStatistictVsDimWithOperationForBubble(aggregatedResults, "sort", temp + "sort/", field.getName());
                Vizualization.showOrSaveBarChartForOperationBubble(aggregatedResults,"sort" , temp2 + "sort/" , field.getName() );

            }

        }





    }

    private void performTestConcreteBubbleSort() {
        BubbleSort1Generator bubbleSort1Generator = new BubbleSort1Generator();
        for (KindOfBubbleSort k : KindOfBubbleSort.values()) {
            for (int dim : DIMENSIONS) {

                BubbleSort1 bubbleSortFirst = bubbleSort1Generator.createArray(k, dim, 0L);
                BubbleSort1 bubbleSortSecond = bubbleSort1Generator.createArray(k, dim, 0L);

                String fileNameFirst = INPUT_DIR + "bubble_1_generic" + k.toString().toLowerCase() + "_" + dim + ".txt";
                String fileNameSecond = INPUT_DIR + "bubble_2_generic" + k.toString().toLowerCase() + "_" + dim + ".txt";

                saveBubble1ToFile(bubbleSortFirst,fileNameFirst);
                saveBubble1ToFile(bubbleSortSecond,fileNameSecond);

                aggregatedResults.add(testConcreteObjectVsReflect(bubbleSortFirst,  dim, k, "sort"));

            }
       CalculationStatistic.saveStatisticsByOperation(OUTPUT_DIR, aggregatedResults, "bubble");
        }
        CalculationStatistic.writeDetailedStatisticsByOperationToFile(aggregatedResults,"sort" , "bubble" ,OUTPUT_DIR + "statistic/" + "sortStatisticsByOperation.txt" );
        CalculationStatistic.writeDetailedStatisticsByBubbleKindToFile(aggregatedResults,OUTPUT_DIR + "statistic/" + "DetailedStatisticsByBubbleKind.txt");

    }

    private StatisticsResult testConcreteObjectVsReflect(BubbleSort1 bubble1, int dim, KindOfBubbleSort k, String sort) {

        List<Long> reflectionTimes = new ArrayList<>();
        List<Long> objectTimes = new ArrayList<>();

        // Wykonaj rozgrzewkę dla odpowiedniej operacji
        performWarmup(bubble1, sort);

        // Wykonaj pomiary dla odpowiedniej operacji
        performMeasurements(bubble1, sort, reflectionTimes, objectTimes);

        // Zapisz wyniki i statystyki
        return saveResults(reflectionTimes, objectTimes, sort, dim, k);



    }

    private void performMeasurements(BubbleSort1 bubble1, String sort, List<Long> reflectionTimes, List<Long> objectTimes) {

        for (int i = 0; i < RUNS; i++) {
            // Pomiar operacji refleksyjnej
            long startReflection = System.nanoTime();
            performReflectionOperation(bubble1, sort);
            long endReflection = System.nanoTime();
            reflectionTimes.add(endReflection - startReflection);

            // Pomiar operacji obiektowej
            long startObject = System.nanoTime();
            performObjectOperation(bubble1);
            long endObject = System.nanoTime();
            objectTimes.add(endObject - startObject);
        }


    }

    /***
     * Here to complete !!!!!1
     * @param bubble1
     */

    private void performObjectOperation(BubbleSort1 bubble1) {
    bubble1.sort();
    }

    private void performReflectionOperation(BubbleSort1 bubble1, String sort) {
   BubbleSortReflectionUtil.performOperationReflectBubbleConcrete(bubble1,sort);
    }

    private void performWarmup(BubbleSort1 bubble1, String sort) {

        // Here add a Object opertion in warm up
        performObjectOperation(bubble1);

        //  Hear add a Reflect warm up
        performReflectionOperation(bubble1,sort);


    }

    private StatisticsResult saveResults(List<Long> reflectionTimes, List<Long> objectTimes, String operation, int dim, KindOfBubbleSort kind) {
        createDirectoriesIfNotExists(OUTPUT_DIR  + "statistic/" + operation + "/");
        String resultsFilename = OUTPUT_DIR + "statistic/"+ operation + "/"  + "bubble_performance_" + operation +   kind + "_of_reflection_concrete" + dim + ".txt";
        saveResultsToFile(resultsFilename, objectTimes, reflectionTimes);
        String statsFilename = OUTPUT_DIR + "statistic/"+ operation + "/" +  "bubble_statistics_" + operation  + kind + "_of_reflection_concrete" + dim + ".txt";
        StatisticsResult stats = CalculationStatistic.calculateAndSaveStatistics(reflectionTimes, objectTimes, statsFilename, operation, dim, kind);
        return stats;
    }




    /**
     * Zapisuje wektor do pliku tekstowego.
     */

    public static void saveBubble1ToFile(BubbleSort1 bubbleSort, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
    writer.newLine();
            writer.newLine();
            for (int coordinate : bubbleSort.getData()) {
                writer.write(coordinate + "\t");
            }
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving vector to file: " + e.getMessage());
        }
    }


}
