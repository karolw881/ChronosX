package org.magister.bubbleSort;

import org.magister.helper.CalculationStatistic;
import org.magister.helper.StatisticsResult;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.style.Styler;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PerformanceBubbleSortGenericReflectVsObject {

    // Katalogi dla danych testowych i wyników
    private static final String INPUT_DIR = "test_data/input/bubble/Generic/";
    private static final String OUTPUT_DIR = "test_data/output/bubble/Generic/";
    private static final String CHARTS_DIR = "test_dataGG/output/bubble/charts/Generic/";
    // Liczba pomiarów dla danego przypadku
    private static final int RUNS = 1000;
    // Rozmiary tablic testowych
    private static final int[] DIMENSIONS = {10, 100, 1000};

    // Lista zagregowanych wyników pomiarów
    protected final List<StatisticsResult> aggregatedResults = new ArrayList<>();

    public PerformanceBubbleSortGenericReflectVsObject() {
        createDirectoriesIfNotExists(INPUT_DIR);
        createDirectoriesIfNotExists(OUTPUT_DIR);
        createDirectoriesIfNotExists(CHARTS_DIR);
    }

    /**
     * Główna metoda testowa.
     */
    public void runTest() throws IOException {
        aggregatedResults.clear();
        performTestGenericBubbleSort();
        // Dla każdego rodzaju uporządkowania (DataOrder) tworzymy wykres słupkowy
        for (KindOfBubbleSort  kind : KindOfBubbleSort.values()) {
            showOrSaveBarChartForRatioWithOrder(aggregatedResults, kind);
        }
        CalculationStatistic.saveStatisticsByOperation(CHARTS_DIR , aggregatedResults , "bubble");
        CalculationStatistic.saveStatisticsByOperation(OUTPUT_DIR,aggregatedResults , "bubble");
    }


    public void performTestGenericBubbleSort() {
        for (KindOfBubbleSort k : KindOfBubbleSort.values()) {
            System.out.println("Testy dla uporządkowania: " + k);

            for (int dim : DIMENSIONS) {
                System.out.println("Testujemy tablicę o rozmiarze: " + dim);


                List<Long> genericTimes = new ArrayList<>();
                List<Long> nonGenericTimes = new ArrayList<>();

                // Warm-up
                for (int i = 0; i < 3; i++) {

                }

                // Główne pomiary
                for (int run = 0; run < RUNS; run++) {


                }


            }
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
    private void createDirectoriesIfNotExists(String path) {
        File directory = new File(path);
        if (!directory.exists() && !directory.mkdirs()) {
            System.err.println("Nie udało się utworzyć katalogu: " + path);
        }
    }
}
