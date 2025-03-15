package org.magister.vector;


import lombok.Getter;
import lombok.Setter;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.magister.helper.IntegerOperations;
import org.magister.helper.StatisticsResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class PerformanceTestVectorGenericOfReflectionVsObject extends PerformanceTestVector{
    private static final String INPUT_DIR = PerformanceTestVector.INPUT_DIR + "GenericOfReflectionVsObject/";
    private static final String OUTPUT_DIR = PerformanceTestVector.OUTPUT_DIR + "GenericOfReflectionVsObject/";
    private static final String CHARTS_DIR = PerformanceTestVector.CHARTS_DIR + "GenericOfReflectionVsObject/";
    private static final int RUNS = PerformanceTestVector.RUNS;
    private static final int[] DIMENSIONS = PerformanceTestVector.DIMENSIONS;
    //  private final List<StatisticsResult> aggregatedResults = new ArrayList<>();


    public PerformanceTestVectorGenericOfReflectionVsObject() {
        // Używamy metod odziedziczonych do tworzenia katalogów
        createDirectories();
        //   performTest();
    }


    public void runTest() throws IOException {
        // Czyszczenie poprzednich wyników
        aggregatedResults.clear();
        performTestGeneric();
        // Iterujemy po wszystkich typach wektora
        for (KindOfVector kind : KindOfVector.values()) {

            showOrSaveBarChartForRatioWithKind(aggregatedResults,kind);



        }

        showOrSaveChartRatioVsDim2(aggregatedResults ,"add");
      showOrSaveChartRatioVsDim2(aggregatedResults, "subtruct");


    }



    private void showOrSaveChartRatioVsDim2(List<StatisticsResult> results , String whatoperation) throws IOException {
        XYChart chart = createChartRatioVsDim2ForAdd(
                results,
                "Wykres: Ratio (x) vs. Dim (y) dla operacji " + whatoperation,
                "Wymiar (Dim)" ,
                "Ratio (Gen Ref / Gen Obj)","add"
        );
        // Zapis do pliku:
        BitmapEncoder.saveBitmap(chart, CHARTS_DIR + "ratio_vs_dim_chartlinear"+whatoperation+".png", BitmapEncoder.BitmapFormat.PNG);
        // Lub wyświetlenie w okienku Swing:
        // new SwingWrapper<>(chart).displayChart();
    }

    private void showOrSaveBarChartForRatioWithKind(List<StatisticsResult> results, KindOfVector kind) throws IOException {
        CategoryChart chart = createBarChartForRatioWithKind(
                results,
                kind,
                "Porównanie Mean (Gen Ref vs. Gen Obj)",
                "Wymiar",
                "Czas [ns]"
        );
        // Zapisujemy do pliku z uwzględnieniem rodzaju macierzy w nazwie
        BitmapEncoder.saveBitmap(chart, CHARTS_DIR + "ratio_bar_chart_" + kind + ".png", BitmapEncoder.BitmapFormat.PNG);



    }

    public static CategoryChart createBarChartForRatioWithKind(
            List<StatisticsResult> results,
            KindOfVector kind,
            String chartTitle,
            String xAxisTitle,
            String yAxisTitle
    ) {
        // Filtruj wyniki tylko dla wskazanego rodzaju macierzy
        List<StatisticsResult> filteredResults = results.stream()
                .filter(r -> r.kindOfVector == kind)
                .collect(Collectors.toList());

        CategoryChart chart = new CategoryChartBuilder()
                .width(1800)
                .height(1000)
                .title(chartTitle + " - " + kind)
                .xAxisTitle(xAxisTitle)
                .yAxisTitle(yAxisTitle)
                .build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
        chart.getStyler().setXAxisLabelRotation(90);

        // Używaj przefiltrowanych wyników
        List<String> xLabels = filteredResults.stream()
                .map(r -> "(D=" + r.dimension + ")(Op=" + r.operation + ")")
                .toList();

        List<Double> ratioValues = filteredResults.stream()
                .map(r -> r.ratio)
                .toList();

        chart.addSeries("Gen Ref/Gen Obj Ratio - " + kind, xLabels, ratioValues);

        return chart;
    }


    public void performTestGeneric() {
        // Tworzenie katalogów wejściowych, wyjściowych, wykresów oraz dodatkowego katalogu "wyniki"
        createDirectoriesIfNotExists(INPUT_DIR);
        createDirectoriesIfNotExists(OUTPUT_DIR);
        createDirectoriesIfNotExists(CHARTS_DIR);


        // Utwórz instancję MatrixGenerator
        VectorGenerator<Integer> generator = new VectorGenerator<>(new IntegerOperations());


        // Iterujemy po wszystkich typach macierzy
        for (KindOfVector kind : KindOfVector.values()) {
            System.out.println("Test dla typu macierzy: " + kind);

            // Testujemy dla każdego wymiaru macierzy
            for (int dim : DIMENSIONS) {
                System.out.println("Testujemy macierz " + kind + " o wymiarze " + dim + "x" + dim);

                // Tworzymy macierze z ustalonymi ziarnami: seed 0 dla pierwszej i seed 1 dla drugiej
                Vector<Integer> vectorGenericFirst = generator.createVector(kind, dim, 0L);
                Vector<Integer> vectorGenericSecond = generator.createVector(kind, dim, 1L);

                // Zapisujemy macierze wejściowe do plików z uwzględnieniem typu macierzy w nazwie pliku
                String fileNameFirst = INPUT_DIR + "vector_1_generic" + kind.toString().toLowerCase() + "_" + dim + ".txt";
                String fileNameSecond = INPUT_DIR + "vector_2_generic" + kind.toString().toLowerCase() + "_" + dim + ".txt";

                // Save generated matrix to file (before any computation)
                saveVectorToFile(vectorGenericFirst, fileNameFirst);
                saveVectorToFile(vectorGenericSecond, fileNameSecond);

                // Testowanie operacji na macierzach (dodawanie, odejmowanie, mnożenie, dzielenie)
                aggregatedResults.add(testVectorGenericObjectVsReflect(vectorGenericFirst, vectorGenericSecond, dim,kind,"add"));
                aggregatedResults.add(testVectorGenericObjectVsReflect(vectorGenericFirst, vectorGenericSecond, dim,kind,"subtruct"));


            }
            // Wyświetlamy i zapisujemy zagregowane statystyki
            displayDetailedStatistics();
            saveAggregatedStatisticsToFile();
        }

        // Wyświetlamy i zapisujemy zagregowane statystyki
        displayDetailedStatistics();
        saveAggregatedStatisticsToFile();

    }

    /**
     * Zapisz zagregowane statystyki do pliku tekstowego.
     */
    void saveAggregatedStatisticsToFile() {
        String filename = CHARTS_DIR + "/aggregated_statistics.txt";
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

    void displayDetailedStatistics() {
        System.out.println("\n===== AGREGOWANE STATYSTYKI =====");
        System.out.printf("%-10s %-8s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-10s\n",
                "Operacja", "Dim", "Gen_Mean(ns)", "Gen_Median(ns)", "Gen_Mode(ns)", "Gen_StdDev(ns)",
                "Con_Mean(ns)", "Con_Median(ns)", "Con_Mode(ns)", "Con_StdDev(ns)", "Ratio");
        for (StatisticsResult sr : aggregatedResults) {
            System.out.printf("%-10s %-8d %-15.2f %-15.2f %-15d %-15.2f %-15.2f %-15.2f %-15d %-15.2f %-10.2f\n",
                    sr.operation, sr.dimension, sr.reflectMean, sr.reflectMedian, sr.reflectMode, sr.reflectStdDev,
                    sr.objectMean, sr.objectMedian, sr.objectMode, sr.objectStdDev, sr.ratio);
        }
        System.out.println("==================================\n");
    }

    private StatisticsResult testVectorGenericObjectVsReflect(Vector<Integer> vectorGenericFirst,
                                                              Vector<Integer> vectorGenericSecond,
                                                              int dim,
                                                              KindOfVector kind,
                                                              String operation) {

        List<Long> reflectionTimes = new ArrayList<>();
        List<Long> objectTimes = new ArrayList<>();

        // Warm-up – wykonywane kilka razy przed pomiarem
        for (int i = 0; i < 3; i++) {
            if(operation.equals("add")) {
                vectorGenericFirst.add(vectorGenericSecond);
                VectorReflectionUtil.performOperationReflectVector(vectorGenericFirst, vectorGenericSecond, operation);
            }
            else if(operation.equals("subtruct")) {
                vectorGenericFirst.subtruct(vectorGenericSecond);
                VectorReflectionUtil.performOperationReflectVector(vectorGenericFirst, vectorGenericSecond, operation);
            }
        }

        // Główne pomiary
        for (int i = 0; i < RUNS; i++) {
            // Pomiar operacji refleksyjnej
            long startReflection = System.nanoTime();
            VectorReflectionUtil.performOperationReflectVector(vectorGenericFirst, vectorGenericSecond, operation);
            long endReflection = System.nanoTime();
            reflectionTimes.add(endReflection - startReflection);

            // Pomiar operacji obiektowej
            long startObject = System.nanoTime();
            if(operation.equals("add")) {
                vectorGenericFirst.add(vectorGenericSecond);
            }
            else if(operation.equals("subtruct")) {
                vectorGenericFirst.subtruct(vectorGenericSecond);
            }
            long endObject = System.nanoTime();
            objectTimes.add(endObject - startObject);
        }

        String resultsFilename = OUTPUT_DIR + "vector_performance_"+operation+"_of_reflection_generic" + dim + ".txt";
        saveResultsToFile(resultsFilename, reflectionTimes, objectTimes);
        String statsFilename = OUTPUT_DIR + "vector_statistics_"+operation+"_of_reflection_generic" + dim + ".txt";
        // Poprawiona nazwa pliku statystyk, używająca parametru operation
        StatisticsResult stats = calculateAndSaveStatistics(reflectionTimes, objectTimes, statsFilename, operation, dim, kind);
        System.out.println( operation + " results saved to " + resultsFilename + " and " + statsFilename);
        return stats;
    }

    private StatisticsResult calculateAndSaveStatistics(List<Long> genericReflectTimes,
                                                        List<Long> GenericObjectTimes,
                                                        String filename,
                                                        String operation,
                                                        int dim, KindOfVector kind) {
        DoubleSummaryStatistics genericStats = genericReflectTimes.stream().mapToDouble(Long::doubleValue).
                summaryStatistics();
        DoubleSummaryStatistics concreteStats = GenericObjectTimes.stream().mapToDouble(Long::doubleValue).
                summaryStatistics();

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
            writer.write("Statistics for generic Reflect implementation (Vector<Integer>):\n");
            writer.write(String.format("Mean: %.2f ns\n", genericMean));
            writer.write(String.format("Median: %.2f ns\n", genericMedian));
            writer.write(String.format("Mode: %d ns\n", genericMode));
            writer.write(String.format("Min: %d ns\n", (long) genericStats.getMin()));
            writer.write(String.format("Max: %d ns\n", (long) genericStats.getMax()));
            writer.write(String.format("Standard Deviation: %.2f ns\n", genericStd));
            writer.write("\n");
            writer.write("Statistics for generic object implementation (Vector<Integer>:\n");
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
        return new StatisticsResult(operation, dim, genericMean, genericMedian, genericMode, genericStd,
                concreteMean, concreteMedian, concreteMode, concreteStd, ratio , kind);


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

    public static XYChart createChartRatioVsDim2ForAdd(List<StatisticsResult> results,
                                                       String chartTitle,
                                                       String xAxisTitle,
                                                       String yAxisTitle , String whatoperation) {

        // Tworzymy wykres XY
        XYChart chart = new XYChartBuilder()
                .width(1800)
                .height(1000)
                .title(chartTitle)
                .xAxisTitle(xAxisTitle)
                .yAxisTitle(yAxisTitle)
                .build();

        // Osobne serie dla każdego typu macierzy

               /*
    RANDOM,           // Losowa macierz
    IDENTITY,         // Macierz jednostkowa (tożsamościowa)
    DIAGONAL,         // Macierz diagonalna
    SYMMETRIC,        // Macierz symetryczna
    ANTISYMMETRIC,    // Macierz antysymetryczna (skew-symetryczna)
    LOWER_TRIANGULAR, // Macierz trójkątna dolna
    UPPER_TRIANGULAR  // Macierz trójkątna górna
         */



        List<StatisticsResult> radomResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase(whatoperation))
                .filter(r -> r.kindOfVector == KindOfVector.RANDOM)
                .toList();

        List<StatisticsResult> identityResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase(whatoperation))
                .filter(r -> r.kindOfVector == KindOfVector.ONES)
                .toList();

        List<StatisticsResult> diagResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase(whatoperation))
                .filter(r -> r.kindOfVector == KindOfVector.ZERO)
                .toList();








        // Dodaj serie osobno

        chart.addSeries("Random", radomResults.stream().mapToDouble(r -> r.dimension).toArray(),
                radomResults.stream().mapToDouble(r -> r.ratio).toArray());

        chart.addSeries("Ones", identityResults.stream().mapToDouble(r -> r.dimension).toArray(),
                identityResults.stream().mapToDouble(r -> r.ratio).toArray());

        chart.addSeries("zeros", diagResults.stream().mapToDouble(r -> r.dimension).toArray(),
                diagResults.stream().mapToDouble(r -> r.ratio).toArray());






        return chart;
    }




}
