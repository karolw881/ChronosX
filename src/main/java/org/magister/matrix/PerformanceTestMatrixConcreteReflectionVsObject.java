package org.magister.matrix;

import lombok.Getter;
import lombok.Setter;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.magister.helper.CalculationStatistic;
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
public class PerformanceTestMatrixConcreteReflectionVsObject extends PerformanceTestMatrix {
    private static final String INPUT_DIR = PerformanceTestMatrix.INPUT_DIR + "ConcreteOfReflectionVsObject/";
    private static final String OUTPUT_DIR = PerformanceTestMatrix.OUTPUT_DIR + "ConcreteOfReflectionVsObject/";
    private static final String CHARTS_DIR = PerformanceTestMatrix.CHARTS_DIR + "ConcreteOfReflectionVsObject/";
    private static final int RUNS = PerformanceTestMatrix.RUNS;
    private static final int[] DIMENSIONS = PerformanceTestMatrix.DIMENSIONS;


    public PerformanceTestMatrixConcreteReflectionVsObject() {
        createDirectories();
    }



    public void runTest() throws IOException {

        aggregatedResults.clear();
        performTestConcrete();


        for(KindOfMatrix kindOfMatrix : KindOfMatrix.values()){

            showOrSaveBarChartForRatioWithKind(aggregatedResults,kindOfMatrix);

        }

        showOrSaveChartRatioVsDimWithOperation(aggregatedResults , "add");
        showOrSaveChartRatioVsDimWithOperation(aggregatedResults,"subtract");
        showOrSaveChartRatioVsDimWithOperation(aggregatedResults , "multiply");


    }


    private static XYChart createChartRatioVsDimWithOperation(List<StatisticsResult> results, String chartTitle, String xAxisTitle, String yAxisTitle, String operation) {
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
                .filter(r -> r.operation.equalsIgnoreCase(operation))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.RANDOM)
                .toList();

        List<StatisticsResult> identityResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase(operation))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.IDENTITY)
                .toList();

        List<StatisticsResult> diagResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase(operation))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.DIAGONAL)
                .toList();

        List<StatisticsResult> symetricResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase(operation))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.SYMMETRIC)
                .toList();

        List<StatisticsResult> antiResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase(operation))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.ANTISYMMETRIC)
                .toList();


        List<StatisticsResult> lowTriaResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase(operation))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.LOWER_TRIANGULAR)
                .toList();

        List<StatisticsResult> upperTriaResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase(operation))
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

    public static void showOrSaveChartRatioVsDimWithOperation(List<StatisticsResult> results , String operation ) throws IOException {
        XYChart chart = createChartRatioVsDimWithOperation(
                results,
                "Wykres: Ratio (x) vs. Dim (y) dla operacji " + operation,
                "Wymiar (Dim)" ,
                "Ratio (Gen Ref / Gen Obj)"
                , operation
        );
        // Zapis do pliku:
        BitmapEncoder.saveBitmap(chart, CHARTS_DIR + "ratio_vs_dim_chartlinear" + operation + ".png", BitmapEncoder.BitmapFormat.PNG);
        // Lub wyświetlenie w okienku Swing:
        // new SwingWrapper<>(chart).displayChart();
    }



    public void performTestConcrete() {
        createDirectoriesIfNotExists(INPUT_DIR);
        createDirectoriesIfNotExists(OUTPUT_DIR);
        createDirectoriesIfNotExists(CHARTS_DIR);



        // Utworz instancje
        Matrix1Generator generator = new Matrix1Generator();



        // Iterujemy po wszystkich typach macierzy
        for (KindOfMatrix kind : KindOfMatrix.values()) {
            // Testujemy dla każdego wymiaru macierzy
            for (int dim : DIMENSIONS) {
                System.out.println("Testujemy macierz " + kind + " o wymiarze " + dim + "x" + dim);


                Matrix1 matrixConcreteFirst = generator.createMatrix1(kind,dim,0L);
                Matrix1 matrixConcreteSecond = generator.createMatrix1(kind,dim,1L);




                // Zapisujemy macierze wejściowe do plików
                saveMatrix1ToFile(matrixConcreteFirst, INPUT_DIR + "matrix1_concrete_" + kind.toString().toLowerCase() + "_" + dim + ".txt");
                saveMatrix1ToFile(matrixConcreteSecond, INPUT_DIR + "matrix2_concrete_" +  kind.toString().toLowerCase() + "_" +dim + ".txt");

                aggregatedResults.add(testAddConcreteObjectVsReflect(matrixConcreteFirst, matrixConcreteSecond, dim,kind));
                aggregatedResults.add(testSubstractConcreteObjectVsReflect(matrixConcreteFirst, matrixConcreteSecond, dim,kind));
                aggregatedResults.add(testMultiplyConcreteObjectVsReflect(matrixConcreteFirst, matrixConcreteSecond, dim,kind));


            }

            CalculationStatistic.saveStatisticsByOperation(OUTPUT_DIR,aggregatedResults);
        }
        // Wyświetlamy i zapisujemy zagregowane statystyki
        CalculationStatistic.displayDetailedStatistics(aggregatedResults);
        CalculationStatistic.saveAggregatedStatisticsToFile();

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
        saveResultsToFile(resultsFilename, reflectionTimes, objectTimes);
        String statsFilename = OUTPUT_DIR + "matrix_statistics_multiply_of_reflection_and_object_concrete" + dim + ".txt";
        StatisticsResult stats = CalculationStatistic.calculateAndSaveStatistics(reflectionTimes, objectTimes, statsFilename, "multiply", dim,kindOfMatrix);
        System.out.println("multiply results saved to " + resultsFilename + " and " + statsFilename);
        return stats;

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
        saveResultsToFile(resultsFilename, reflectionTimes, objectTimes);
        String statsFilename = OUTPUT_DIR + "matrix_statistics_subtract_of_reflection_and_object_concrete" + dim + ".txt";
        StatisticsResult stats = CalculationStatistic.calculateAndSaveStatistics(reflectionTimes, objectTimes, statsFilename, "subtract", dim,kindOfMatrix);
        System.out.println("subtract results saved to " + resultsFilename + " and " + statsFilename);
        return stats;

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
        saveResultsToFile(resultsFilename, reflectionTimes, objectTimes);
        String statsFilename = OUTPUT_DIR + "matrix_statistics_add_of_reflection_and_object_concrete" + dim + ".txt";
        StatisticsResult stats = CalculationStatistic.calculateAndSaveStatistics(reflectionTimes, objectTimes, statsFilename, "Add", dim,kindOfMatrix);
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
                    sr.reflectMean, sr.reflectMedian,  sr.reflectStdDev,
                    sr.objectMean, sr.objectMedian,  sr.objectStdDev,
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














}
