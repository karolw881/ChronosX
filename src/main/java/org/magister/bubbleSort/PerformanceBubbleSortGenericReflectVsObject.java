package org.magister.bubbleSort;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.style.Styler;
import org.magister.helper.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PerformanceBubbleSortGenericReflectVsObject extends PerformanceTestBubbleSort {

    // Katalogi dla danych testowych i wyników
    private static final String INPUT_DIR = PerformanceTestBubbleSort.INPUT_DIR + "/GenericOfReflectionVsObject/";
    private static final String OUTPUT_DIR = PerformanceTestBubbleSort.OUTPUT_DIR + "/GenericOfReflectionVsObject/";
    private static final String CHARTS_DIR = PerformanceTestBubbleSort.CHARTS_DIR + "/GenericOfReflectionVsObject/charts/";
    private static final int RUNS = PerformanceTestBubbleSort.RUNS;
    private static final int[] DIMENSIONS = PerformanceTestBubbleSort.DIMENSIONS;

    public PerformanceBubbleSortGenericReflectVsObject() {
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
        performTestGenericBubbleSort();
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


    public void performTestGenericBubbleSort() {
        createDirectoriesIfNotExists(INPUT_DIR);
        createDirectoriesIfNotExists(OUTPUT_DIR);
        createDirectoriesIfNotExists(CHARTS_DIR);

        BubbleSortGenerator bubbleSortGenerator = new BubbleSortGenerator(new NumberxxOperations());


        for (KindOfBubbleSort k : KindOfBubbleSort.values()) {
            for (int dim : DIMENSIONS) {

                BubbleSort<Numberxx> bubbleSortFirst = bubbleSortGenerator.createArray(k, dim, 0L);
                BubbleSort<Numberxx> bubbleSortSecond = bubbleSortGenerator.createArray(k, dim, 0L);


                String fileNameFirst = INPUT_DIR + "bubble_1_generic" + k.toString().toLowerCase() + "_" + dim + ".txt";
                String fileNameSecond = INPUT_DIR + "bubble_2_generic" + k.toString().toLowerCase() + "_" + dim + ".txt";

                saveBubbleToFile(bubbleSortFirst,fileNameFirst);
                saveBubbleToFile(bubbleSortSecond,fileNameSecond);

                aggregatedResults.add(testGenericObjectVsReflect(bubbleSortFirst,  dim, k, "sort"));

            }
            CalculationStatistic.saveStatisticsByOperation(OUTPUT_DIR, aggregatedResults, "bubble");
        }
        CalculationStatistic.writeDetailedStatisticsByOperationToFile(aggregatedResults,"sort" , "bubble" ,OUTPUT_DIR + "statistic/" + "sortStatisticsByOperation.txt" );
        CalculationStatistic.writeDetailedStatisticsByBubbleKindToFile(aggregatedResults,OUTPUT_DIR + "statistic/" + "DetailedStatisticsByBubbleKind.txt");

    }

    /**
     * Wykonuje test porównujący operacje obiektowe z refleksyjnymi
     */
    private StatisticsResult testGenericObjectVsReflect(
            BubbleSort<Numberxx> bubble1,
            int dim, KindOfBubbleSort kind, String whatOperation) {

        List<Long> reflectionTimes = new ArrayList<>();
        List<Long> objectTimes = new ArrayList<>();

        // Wykonaj rozgrzewkę dla odpowiedniej operacji
        performWarmup(bubble1, whatOperation);

        // Wykonaj pomiary dla odpowiedniej operacji
        performMeasurements(bubble1, whatOperation, reflectionTimes, objectTimes);

        // Zapisz wyniki i statystyki
        return saveResults(reflectionTimes, objectTimes, whatOperation, dim, kind);
    }



    private StatisticsResult saveResults(List<Long> reflectionTimes, List<Long> objectTimes, String operation, int dim, KindOfBubbleSort kind) {
        createDirectoriesIfNotExists(OUTPUT_DIR  + "statistic/" + operation + "/");
        String resultsFilename = OUTPUT_DIR +  "statistic/"+ operation + "/" + "bubble_performance_" + operation +   kind + "_of_reflection_generic" + dim + ".txt";
        saveResultsToFile(resultsFilename, objectTimes, reflectionTimes);
        String statsFilename = OUTPUT_DIR + "statistic/" +  operation + "/" + "bubble_statistics_" + operation  + kind + "_of_reflection_generic" + dim + ".txt";
        StatisticsResult stats = CalculationStatistic.calculateAndSaveStatistics(reflectionTimes, objectTimes, statsFilename, operation, dim, kind);
        return stats;
    }

    private void performMeasurements(BubbleSort<Numberxx> bubble1,
                                     String whatOperation,
                                     List<Long> reflectionTimes,
                                     List<Long> objectTimes) {


        for (int i = 0; i < RUNS; i++) {
            // Pomiar operacji refleksyjnej
            long startReflection = System.nanoTime();
            performReflectionOperation(bubble1, whatOperation);
            long endReflection = System.nanoTime();
            reflectionTimes.add(endReflection - startReflection);

            // Pomiar operacji obiektowej
            long startObject = System.nanoTime();
            performObjectOperation(bubble1);
            long endObject = System.nanoTime();
            objectTimes.add(endObject - startObject);
        }


    }

    private void performWarmup(BubbleSort<Numberxx> bubble1,
                               String whatOperation) {
        // Wykonaj operację obiektową
        performObjectOperation(bubble1);
        // Wykonaj operację refleksyjną
        performReflectionOperation(bubble1,  whatOperation);

    }

    private void performReflectionOperation(BubbleSort<Numberxx> bubble1,
                                            String whatOperation) {

        BubbleSortReflectionUtil.performOperationReflectBubble(bubble1,whatOperation);
    }

    private void performObjectOperation(BubbleSort<Numberxx> bubble) {

        bubble.sort();




    }


    public static void saveBubbleToFile(BubbleSort<Numberxx> bubbleSort, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Bubble Dimension: " + bubbleSort.getArr().length);
            writer.newLine();
            writer.newLine();
            for (Numberxx coordinate : bubbleSort.getArr()) {
                writer.write(coordinate.toString() + "\t");
            }
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving bubble to file: " + e.getMessage());
        }
    }



    // --- Metody wykresów ---

    /**
     * Tworzy i zapisuje wykres słupkowy przedstawiający stosunek (ratio) czasów
     * dla danej konfiguracji uporządkowania (DataOrder).
     */
    private void showOrSaveBarChartForRatioWithOrder(List<StatisticsResult> results, KindOfBubbleSort kindOfBubbleSort) throws IOException {
        CategoryChart chart = createBarChartForRatioWithOrder(results, kindOfBubbleSort,
                "Ratio (Gen / NonGen) vs. Dimension (" + kindOfBubbleSort.name() + ")",
                "Wymiar", "Ratio");
        String filename = CHARTS_DIR + "ratio_bar_chart_" + kindOfBubbleSort.name().toLowerCase() + ".png";
        BitmapEncoder.saveBitmap(chart, filename, BitmapEncoder.BitmapFormat.PNG);
        System.out.println("Zapisano wykres: " + filename);
    }

    /**
     * Tworzy wykres słupkowy dla danego rodzaju uporządkowania.
     */
    public static CategoryChart createBarChartForRatioWithOrder(
            List<StatisticsResult> results,
            KindOfBubbleSort kindOfBubbleSort,
            String chartTitle,
            String xAxisTitle,
            String yAxisTitle
    ) {
        List<StatisticsResult> filtered = results.stream()
                .filter(r -> r.kindOfBubbleSort.equals(kindOfBubbleSort.name()))
                .collect(Collectors.toList());
        CategoryChart chart = new CategoryChartBuilder()
                .width(1200)
                .height(800)
                .title(chartTitle)
                .xAxisTitle(xAxisTitle)
                .yAxisTitle(yAxisTitle)
                .build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
        chart.getStyler().setXAxisLabelRotation(90);

        List<String> xLabels = filtered.stream()
                .map(r -> "Dim=" + r.dimension)
                .collect(Collectors.toList());
        List<Double> ratioValues = filtered.stream()
                .map(r -> r.ratio)
                .collect(Collectors.toList());

        chart.addSeries("Ratio (" + kindOfBubbleSort.name() + ")", xLabels, ratioValues);
        return chart;
    }

    // --- Metoda pomocnicza do tworzenia katalogów ---
    protected void createDirectoriesIfNotExists(String path) {
        File directory = new File(path);
        if (!directory.exists() && !directory.mkdirs()) {
            System.err.println("Nie udało się utworzyć katalogu: " + path);
        }
    }
}
