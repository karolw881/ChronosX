package org.magister.matrix;

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
import java.util.Map;
import java.util.stream.Collectors;


@Getter
@Setter
public class PerformanceTestMatrixGenericofReflectionVsObject extends PerformanceTestMatrix {

    private static final String INPUT_DIR = PerformanceTestMatrix.INPUT_DIR + "GenericOfReflectionVsObject/";
    private static final String OUTPUT_DIR = PerformanceTestMatrix.OUTPUT_DIR + "GenericOfReflectionVsObject/";
    private static final String CHARTS_DIR = PerformanceTestMatrix.CHARTS_DIR + "GenericOfReflectionVsObject/";
    private static final int RUNS = PerformanceTestMatrix.RUNS;
    private static final int[] DIMENSIONS = PerformanceTestMatrix.DIMENSIONS;
  //  private final List<StatisticsResult> aggregatedResults = new ArrayList<>();

    public PerformanceTestMatrixGenericofReflectionVsObject() {
        // Używamy metod odziedziczonych do tworzenia katalogów
        createDirectories();
     //   performTest();
    }



    public void runTest() throws IOException {
        // Czyszczenie poprzednich wyników
        aggregatedResults.clear();
        performTestGeneric();
       // createLineChartForMedian(aggregatedResults , "a" , "b" , "c");
       //\\ showOrSaveLineChartForMean(aggregatedResults);
       // createBarChartForRatio(aggregatedResults , "a" , "b" , "c");

       // showOrSaveBarChartForRatio(aggregatedResults );
       // showOrSaveChartRatioVsDim(aggregatedResults);
        // Iterujemy po wszystkich typach macierzy
        for (KindOfMatrix kind : KindOfMatrix.values()) {
           // showOrSaveBarChartForRatio(aggregatedResults );
           // showOrSaveChartRatioVsDim(aggregatedResults);
            showOrSaveBarChartForRatioWithKind(aggregatedResults,kind);



        }
       // showOrSaveChartRatioVsDim(aggregatedResults);
        // showOrSaveBarChartForRatio(aggregatedResults );
       showOrSaveChartRatioVsDim2ForAdd(aggregatedResults);
        showOrSaveChartRatioVsDim2ForSub(aggregatedResults);
        showOrSaveChartRatioVsDim2ForMultiply(aggregatedResults);

    }





    public void performTestGeneric() {
        // Tworzenie katalogów wejściowych, wyjściowych, wykresów oraz dodatkowego katalogu "wyniki"
        createDirectoriesIfNotExists(INPUT_DIR);
        createDirectoriesIfNotExists(OUTPUT_DIR);
        createDirectoriesIfNotExists(CHARTS_DIR);


        // Utwórz instancję MatrixGenerator
        MatrixGenerator<Integer> generator = new MatrixGenerator<>(new IntegerOperations());


        // Iterujemy po wszystkich typach macierzy
        for (KindOfMatrix kind : KindOfMatrix.values()) {
            System.out.println("Test dla typu macierzy: " + kind);

            // Testujemy dla każdego wymiaru macierzy
            for (int dim : DIMENSIONS) {
                System.out.println("Testujemy macierz " + kind + " o wymiarze " + dim + "x" + dim);

                // Tworzymy macierze z ustalonymi ziarnami: seed 0 dla pierwszej i seed 1 dla drugiej
                Matrix<Integer> matrixGenericFirst = generator.createMatrix(kind, dim, 0L);
                Matrix<Integer> matrixGenericSecond = generator.createMatrix(kind, dim, 1L);

                // Zapisujemy macierze wejściowe do plików z uwzględnieniem typu macierzy w nazwie pliku
                String fileNameFirst = INPUT_DIR + "matrix1_" + kind.toString().toLowerCase() + "_" + dim + ".txt";
                String fileNameSecond = INPUT_DIR + "matrix2_" + kind.toString().toLowerCase() + "_" + dim + ".txt";

               // Save generated matrix to file (before any computation)
                saveMatrixToFile(matrixGenericFirst, fileNameFirst);
                saveMatrixToFile(matrixGenericSecond, fileNameSecond);

                // Testowanie operacji na macierzach (dodawanie, odejmowanie, mnożenie, dzielenie)
                aggregatedResults.add(testAddGenericObjectVsReflect(matrixGenericFirst, matrixGenericSecond, dim,kind));
                aggregatedResults.add(testSubstractGenericObjectVsReflect(matrixGenericFirst, matrixGenericSecond, dim,kind));
                aggregatedResults.add(testMultiplyGenericObjectVsReflect(matrixGenericFirst, matrixGenericSecond, dim,kind));
                 //  aggregatedResults.add(testDivideGenericObjectVsReflect(matrixGenericFirst, matrixGenericSecond, dim,kind));

            }
            // Wyświetlamy i zapisujemy zagregowane statystyki
            displayDetailedStatistics();
            saveAggregatedStatisticsToFile();
        }

        // Wyświetlamy i zapisujemy zagregowane statystyki
        displayDetailedStatistics();
        saveAggregatedStatisticsToFile();

    }


    private StatisticsResult testDivideGenericObjectVsReflect(Matrix<Integer> matrix1,
                                                              Matrix<Integer> matrix2,
                                                              int dim, KindOfMatrix kind) {
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
        StatisticsResult stats = calculateAndSaveStatistics(reflectionTimes, objectTimes, statsFilename, "Divide", dim,kind);
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
            int dim, KindOfMatrix kind) {


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
        StatisticsResult stats = calculateAndSaveStatistics(reflectionTimes, objectTimes, statsFilename, "Multiply", dim,kind);
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
                    sr.reflectMean, sr.reflectMedian, sr.reflectMode, sr.reflectStdDev,
                    sr.objectMean, sr.objectMedian, sr.objectMode, sr.objectStdDev,
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
            int dim, KindOfMatrix kind
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
        StatisticsResult stats = calculateAndSaveStatistics(reflectionTimes, objectTimes, statsFilename, "Subtract", dim,kind);
        System.out.println("subtrac results saved to " + resultsFilename + " and " + statsFilename);
        return stats;


    }



    StatisticsResult testAddGenericObjectVsReflect(Matrix<Integer> matrix1,
                                                   Matrix<Integer> matrix2,
                                                   int dim, KindOfMatrix kind) {
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
        StatisticsResult stats = calculateAndSaveStatistics(reflectionTimes, objectTimes, statsFilename, "Add", dim,kind);
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
                                                String filename, String operation, int dimension , KindOfMatrix kind) {
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
            writer.write("Statistics for generic object implementation (Matrix<Integer>:\n");
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
                concreteMean, concreteMedian, concreteMode, concreteStd, ratio , kind);
    }

    void saveAggregatedStatisticsToFile() {
        String filename = CHARTS_DIR + "/aggregated_statistics_Generic.txt";
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







    public static XYChart createChartRatioVsDim(List<StatisticsResult> results,
                                                String chartTitle,
                                                String xAxisTitle,
                                                String yAxisTitle) {
        // Filtruj wyniki, zostawiając tylko operacje "add"
        List<StatisticsResult> addResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase("add"))
                .toList();

        // Tworzymy wykres XY
        XYChart chart = new XYChartBuilder()
                .width(1800)
                .height(1000)
                .title(chartTitle)
                .xAxisTitle(xAxisTitle)
                .yAxisTitle(yAxisTitle)
                .build();

        // Na osi X umieszczamy ratio, a na osi Y wymiar (dim)
        double[] xData = addResults.stream().mapToDouble(r -> r.ratio).toArray();
        double[] yData = addResults.stream().mapToDouble(r -> r.dimension).toArray();

        chart.addSeries("Dim vs. Ratio", xData, yData);

        return chart;
    }

    public static void showOrSaveChartRatioVsDim(List<StatisticsResult> results) throws IOException {
        XYChart chart = createChartRatioVsDim(
                results,
                "Wykres: Ratio (x) vs. Dim (y) dla operacji add",
                "Ratio (Gen Ref / Gen Obj)",
                "Wymiar (Dim)"
        );
        // Zapis do pliku:
        BitmapEncoder.saveBitmap(chart, CHARTS_DIR + "ratio_vs_dim_chart.png", BitmapEncoder.BitmapFormat.PNG);
        // Lub wyświetlenie w okienku Swing:
        // new SwingWrapper<>(chart).displayChart();
    }

    public static CategoryChart createBarChartForRatioWithKind(List<StatisticsResult> results,
                                                               String chartTitle,
                                                               String xAxisTitle,
                                                               String yAxisTitle) {
        CategoryChart chart = new CategoryChartBuilder()
                .width(1800)
                .height(1000)
                .title(chartTitle)
                .xAxisTitle(xAxisTitle)
                .yAxisTitle(yAxisTitle)
                .build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
        chart.getStyler().setXAxisLabelRotation(90);

        // Grupujemy wyniki po rodzaju macierzy
        Map<KindOfMatrix, List<StatisticsResult>> groupedByKind = results.stream()
                .collect(Collectors.groupingBy(r -> r.kindOfMatrix));

        // Dla każdej grupy (danego KindOfMatrix) tworzymy osobną serię
        for (Map.Entry<KindOfMatrix, List<StatisticsResult>> entry : groupedByKind.entrySet()) {
            KindOfMatrix kind = entry.getKey();
            List<StatisticsResult> kindResults = entry.getValue();

            // Etykiety na osi X
            List<String> xLabels = kindResults.stream()
                   // .map(r -> "(D=" + r.dimension + ")(Op=" + r.operation + ")" + r.kindOfMatrix)
                    .map(r ->  "" + r.kindOfMatrix)
                    .toList();

            // Wartości ratio (Gen Ref / Gen Obj)
            List<Double> ratioValues = kindResults.stream()
                    .map(r -> r.ratio)
                    .toList();

            // Dodajemy serię na wykresie
            chart.addSeries(kind.name(), xLabels, ratioValues);
        }

        return chart;
    }



    public static XYChart createChartRatioVsDimWithKind(List<StatisticsResult> results,
                                                        String chartTitle,
                                                        String xAxisTitle,
                                                        String yAxisTitle) {
        // Przykład: filtrujemy tylko operację "add"
        List<StatisticsResult> addResults = results.stream()
                .filter(r -> "add".equalsIgnoreCase(r.operation))
                .toList();

        XYChart chart = new XYChartBuilder()
                .width(1800)
                .height(1000)
                .title(chartTitle)
                .xAxisTitle(xAxisTitle)
                .yAxisTitle(yAxisTitle)
                .build();

        // Grupujemy po rodzaju macierzy
        Map<KindOfMatrix, List<StatisticsResult>> groupedByKind = addResults.stream()
                .collect(Collectors.groupingBy(r -> r.kindOfMatrix));

        // Dla każdej grupy tworzymy serię
        for (Map.Entry<KindOfMatrix, List<StatisticsResult>> entry : groupedByKind.entrySet()) {
            KindOfMatrix kind = entry.getKey();
            List<StatisticsResult> kindResults = entry.getValue();

            double[] xData = kindResults.stream()
                    .mapToDouble(r -> r.ratio)
                    .toArray();

            double[] yData = kindResults.stream()
                    .mapToDouble(r -> r.dimension)
                    .toArray();

            // Dodajemy serię z nazwą równą rodzajowi macierzy
            chart.addSeries(kind.name(), xData, yData);
        }

        return chart;
    }

    public static void showOrSaveChartRatioVsDimWithKind(List<StatisticsResult> results) throws IOException {
        XYChart chart = createChartRatioVsDimWithKind(
                results,
                "Wykres: Ratio (x) vs. Dim (y) dla operacji add (z podziałem na KindOfMatrix)",
                "Ratio (Gen Ref / Gen Obj)",
                "Wymiar (Dim)"
        );
        BitmapEncoder.saveBitmap(chart, CHARTS_DIR + "ratio_vs_dim_chart_by_kind.png", BitmapEncoder.BitmapFormat.PNG);
        // new SwingWrapper<>(chart).displayChart();
    }


    public static XYChart createChartRatioVsDim(
            List<StatisticsResult> results,
            KindOfMatrix kind,
            String chartTitle,
            String xAxisTitle,
            String yAxisTitle
    ) {
        // Filtrujemy wyniki, zostawiając tylko operację "add"
        List<StatisticsResult> addResults = results.stream()
                .filter(r -> "add".equalsIgnoreCase(r.operation))
                .toList();

        XYChart chart = new XYChartBuilder()
                .width(1800)
                .height(1000)
                // Dodajemy informację o rodzaju macierzy w tytule
                .title(chartTitle + " - " + kind)
                .xAxisTitle(xAxisTitle)
                .yAxisTitle(yAxisTitle)
                .build();

        // Przygotowujemy dane: ratio -> oś X, dimension -> oś Y
        double[] xData = addResults.stream()
                .mapToDouble(r -> r.ratio)
                .toArray();

        double[] yData = addResults.stream()
                .mapToDouble(r -> r.dimension)
                .toArray();

        // Dodajemy serię
        chart.addSeries("Dim vs. Ratio", xData, yData);

        return chart;
    }

    public static void showOrSaveChartRatioVsDim(List<StatisticsResult> results, KindOfMatrix kind) throws IOException {
        XYChart chart = createChartRatioVsDim(
                results,
                kind,
                "Wykres: Ratio (x) vs. Dim (y) dla operacji add",
                "Ratio (Gen Ref / Gen Obj)",
                "Wymiar (Dim)"
        );
        // Zapis do pliku z informacją o rodzaju macierzy w nazwie
        BitmapEncoder.saveBitmap(chart, CHARTS_DIR + "ratio_vs_dim_chart_" + kind + ".png", BitmapEncoder.BitmapFormat.PNG);
        // new SwingWrapper<>(chart).displayChart();
    }


    /**
     * BAR CHART WITH KIND
     */


    /**
     *
     * @param results
     * @param kind
     * @param chartTitle
     * @param xAxisTitle
     * @param yAxisTitle
     * @return
     */


    public static CategoryChart createBarChartForRatioWithKind(
            List<StatisticsResult> results,
            KindOfMatrix kind,
            String chartTitle,
            String xAxisTitle,
            String yAxisTitle
    ) {
        // Filtruj wyniki tylko dla wskazanego rodzaju macierzy
        List<StatisticsResult> filteredResults = results.stream()
                .filter(r -> r.kindOfMatrix == kind)
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

    /**
     * @param results
     * @param kind
     * @throws IOException
     */

    public static void showOrSaveBarChartForRatioWithKind(List<StatisticsResult> results, KindOfMatrix kind) throws IOException {
        // Tworzymy wykres z uwzględnieniem rodzaju macierzy w tytule
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


    /**
     *
     * @param results
     * @param chartTitle
     * @param xAxisTitle
     * @param yAxisTitle
     * @return
     */


    public static CategoryChart createBarChartForRatio(List<StatisticsResult> results,
                                                       String chartTitle,
                                                       String xAxisTitle,
                                                       String yAxisTitle) {
        CategoryChart chart = new CategoryChartBuilder()
                .width(1800)
                .height(1000)
                .title(chartTitle)
                .xAxisTitle(xAxisTitle)
                .yAxisTitle(yAxisTitle)
                .build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setXAxisLabelRotation(90);

        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);

        List<String> xLabels = results.stream()
                .map(r -> "(D=" + r.dimension + ")(Op=" + r.operation+")")
                .toList();

        List<Double> ratioValues = results.stream().map(r -> r.ratio).toList();

        chart.addSeries("Gen Ref/Gen Obj Ratio", xLabels, ratioValues);

        return chart;
    }


    /**
     *
     * @param results
     * @throws IOException
     */


    public static void showOrSaveBarChartForRatio(List<StatisticsResult> results) throws IOException {
        CategoryChart chart = createBarChartForRatio(
                results,
                "Porównanie Mean (Gen Ref vs. Gen Obj)",
                "Wymiar",
                "Czas [ns]"
        );
        BitmapEncoder.saveBitmap(chart, CHARTS_DIR+"ratio_bar_chart.png", BitmapEncoder.BitmapFormat.PNG);

    }


    /**
     * TESTUJEMY dodawanie antysymetryczne
     * @param results
     * @param chartTitle
     * @param xAxisTitle
     * @param yAxisTitle
     * @return
     */

    public static XYChart createChartRatioVsDim2ForAdd(List<StatisticsResult> results,
                                                       String chartTitle,
                                                       String xAxisTitle,
                                                       String yAxisTitle) {

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
                .filter(r -> r.operation.equalsIgnoreCase("add"))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.RANDOM)
                .toList();

        List<StatisticsResult> identityResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase("add"))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.IDENTITY)
                .toList();

        List<StatisticsResult> diagResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase("add"))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.DIAGONAL)
                .toList();

        List<StatisticsResult> symetricResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase("add"))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.SYMMETRIC)
                .toList();

        List<StatisticsResult> antiResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase("add"))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.ANTISYMMETRIC)
                .toList();


        List<StatisticsResult> lowTriaResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase("add"))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.LOWER_TRIANGULAR)
                .toList();

        List<StatisticsResult> upperTriaResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase("add"))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.UPPER_TRIANGULAR)
                .toList();






        // Dodaj serie osobno

        chart.addSeries("Random", radomResults.stream().mapToDouble(r -> r.dimension).toArray(),
                radomResults.stream().mapToDouble(r -> r.ratio).toArray());

        chart.addSeries("IDENTITYC", identityResults.stream().mapToDouble(r -> r.dimension).toArray(),
                identityResults.stream().mapToDouble(r -> r.ratio).toArray());

        chart.addSeries("DIAGONAL", diagResults.stream().mapToDouble(r -> r.dimension).toArray(),
                diagResults.stream().mapToDouble(r -> r.ratio).toArray());

        chart.addSeries("ANTISYMMETRIC", antiResults.stream().mapToDouble(r -> r.dimension).toArray(),
                antiResults.stream().mapToDouble(r -> r.ratio).toArray());

        chart.addSeries("SYMMETRIC", symetricResults.stream().mapToDouble(r -> r.dimension).toArray(),
                symetricResults.stream().mapToDouble(r -> r.ratio).toArray());


        chart.addSeries("LOWER_TRIANGULAR", lowTriaResults.stream().mapToDouble(r -> r.dimension).toArray(),
                lowTriaResults.stream().mapToDouble(r -> r.ratio).toArray());

        chart.addSeries("UPPER_TRIANGULAR", upperTriaResults.stream().mapToDouble(r -> r.dimension).toArray(),
                upperTriaResults.stream().mapToDouble(r -> r.ratio).toArray());






        return chart;
    }



    public static XYChart createChartRatioVsDim2ForSub(List<StatisticsResult> results,
                                                       String chartTitle,
                                                       String xAxisTitle,
                                                       String yAxisTitle) {

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
                .filter(r -> r.operation.equalsIgnoreCase("subtract"))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.RANDOM)
                .toList();

        List<StatisticsResult> identityResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase("subtract"))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.IDENTITY)
                .toList();

        List<StatisticsResult> diagResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase("subtract"))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.DIAGONAL)
                .toList();

        List<StatisticsResult> symetricResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase("subtract"))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.SYMMETRIC)
                .toList();

        List<StatisticsResult> antiResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase("subtract"))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.ANTISYMMETRIC)
                .toList();


        List<StatisticsResult> lowTriaResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase("subtract"))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.LOWER_TRIANGULAR)
                .toList();

        List<StatisticsResult> upperTriaResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase("subtract"))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.UPPER_TRIANGULAR)
                .toList();






        // Dodaj serie osobno

        chart.addSeries("Random", radomResults.stream().mapToDouble(r -> r.dimension).toArray(),
                radomResults.stream().mapToDouble(r -> r.ratio).toArray());

        chart.addSeries("IDENTITYC", identityResults.stream().mapToDouble(r -> r.dimension).toArray(),
                identityResults.stream().mapToDouble(r -> r.ratio).toArray());

        chart.addSeries("DIAGONAL", diagResults.stream().mapToDouble(r -> r.dimension).toArray(),
                diagResults.stream().mapToDouble(r -> r.ratio).toArray());

        chart.addSeries("ANTISYMMETRIC", antiResults.stream().mapToDouble(r -> r.dimension).toArray(),
                antiResults.stream().mapToDouble(r -> r.ratio).toArray());

        chart.addSeries("SYMMETRIC", symetricResults.stream().mapToDouble(r -> r.dimension).toArray(),
                symetricResults.stream().mapToDouble(r -> r.ratio).toArray());


        chart.addSeries("LOWER_TRIANGULAR", lowTriaResults.stream().mapToDouble(r -> r.dimension).toArray(),
                lowTriaResults.stream().mapToDouble(r -> r.ratio).toArray());

        chart.addSeries("UPPER_TRIANGULAR", upperTriaResults.stream().mapToDouble(r -> r.dimension).toArray(),
                upperTriaResults.stream().mapToDouble(r -> r.ratio).toArray());






        return chart;
    }

    public static void showOrSaveChartRatioVsDim2ForAdd(List<StatisticsResult> results) throws IOException {
        XYChart chart = createChartRatioVsDim2ForAdd(
                results,
                "Wykres: Ratio (x) vs. Dim (y) dla operacji add",
                "Wymiar (Dim)" ,
                "Ratio (Gen Ref / Gen Obj)"
        );
        // Zapis do pliku:
        BitmapEncoder.saveBitmap(chart, CHARTS_DIR + "ratio_vs_dim_chartlinearAdd.png", BitmapEncoder.BitmapFormat.PNG);
        // Lub wyświetlenie w okienku Swing:
        // new SwingWrapper<>(chart).displayChart();
    }


    public static void showOrSaveChartRatioVsDim2ForSub(List<StatisticsResult> results) throws IOException {
        XYChart chart = createChartRatioVsDim2ForSub(
                results,
                "Wykres: Ratio (x) vs. Dim (y) dla operacji sub",
                "Wymiar (Dim)" ,
                "Ratio (Gen Ref / Gen Obj)"
        );
        // Zapis do pliku:
        BitmapEncoder.saveBitmap(chart, CHARTS_DIR + "ratio_vs_dim_chartlinearSub.png", BitmapEncoder.BitmapFormat.PNG);
        // Lub wyświetlenie w okienku Swing:
        // new SwingWrapper<>(chart).displayChart();
    }



    public static XYChart createChartRatioVsDim2ForMult(List<StatisticsResult> results,
                                                       String chartTitle,
                                                       String xAxisTitle,
                                                       String yAxisTitle) {

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
                .filter(r -> r.operation.equalsIgnoreCase("multiply"))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.RANDOM)
                .toList();

        List<StatisticsResult> identityResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase("multiply"))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.IDENTITY)
                .toList();

        List<StatisticsResult> diagResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase("multiply"))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.DIAGONAL)
                .toList();

        List<StatisticsResult> symetricResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase("multiply"))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.SYMMETRIC)
                .toList();

        List<StatisticsResult> antiResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase("multiply"))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.ANTISYMMETRIC)
                .toList();


        List<StatisticsResult> lowTriaResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase("multiply"))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.LOWER_TRIANGULAR)
                .toList();

        List<StatisticsResult> upperTriaResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase("multiply"))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.UPPER_TRIANGULAR)
                .toList();






        // Dodaj serie osobno

        chart.addSeries("Random", radomResults.stream().mapToDouble(r -> r.dimension).toArray(),
                radomResults.stream().mapToDouble(r -> r.ratio).toArray());

        chart.addSeries("IDENTITYC", identityResults.stream().mapToDouble(r -> r.dimension).toArray(),
                identityResults.stream().mapToDouble(r -> r.ratio).toArray());

        chart.addSeries("DIAGONAL", diagResults.stream().mapToDouble(r -> r.dimension).toArray(),
                diagResults.stream().mapToDouble(r -> r.ratio).toArray());

        chart.addSeries("ANTISYMMETRIC", antiResults.stream().mapToDouble(r -> r.dimension).toArray(),
                antiResults.stream().mapToDouble(r -> r.ratio).toArray());

        chart.addSeries("SYMMETRIC", symetricResults.stream().mapToDouble(r -> r.dimension).toArray(),
                symetricResults.stream().mapToDouble(r -> r.ratio).toArray());


        chart.addSeries("LOWER_TRIANGULAR", lowTriaResults.stream().mapToDouble(r -> r.dimension).toArray(),
                lowTriaResults.stream().mapToDouble(r -> r.ratio).toArray());

        chart.addSeries("UPPER_TRIANGULAR", upperTriaResults.stream().mapToDouble(r -> r.dimension).toArray(),
                upperTriaResults.stream().mapToDouble(r -> r.ratio).toArray());






        return chart;
    }


    public static void showOrSaveChartRatioVsDim2ForMultiply(List<StatisticsResult> results) throws IOException {
        XYChart chart = createChartRatioVsDim2ForMult(
                results,
                "Wykres: Ratio (x) vs. Dim (y) dla operacji multiply",
                    "Wymiar (Dim)" ,
                "Ratio (Gen Ref / Gen Obj)"
        );
        // Zapis do pliku:
        BitmapEncoder.saveBitmap(chart, CHARTS_DIR + "ratio_vs_dim_chartlinearmultiply.png", BitmapEncoder.BitmapFormat.PNG);
        // Lub wyświetlenie w okienku Swing:
        // new SwingWrapper<>(chart).displayChart();
    }





}
