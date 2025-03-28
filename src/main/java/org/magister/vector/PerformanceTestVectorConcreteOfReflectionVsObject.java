package org.magister.vector;

import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.magister.helper.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

public class PerformanceTestVectorConcreteOfReflectionVsObject extends PerformanceTestVector {
    private static final String INPUT_DIR = PerformanceTestVector.INPUT_DIR + "/ConcreteOfReflectionVsObject/";
    private static final String OUTPUT_DIR = PerformanceTestVector.OUTPUT_DIR + "/ConcreteOfReflectionVsObject/";
    private static final String CHARTS_DIR = PerformanceTestVector.CHARTS_DIR + "/ConcreteOfReflectionVsObject/charts/";
    private static final int RUNS = PerformanceTestVector.RUNS;
    private static final int[] DIMENSIONS = PerformanceTestVector.DIMENSIONS;




    public PerformanceTestVectorConcreteOfReflectionVsObject(){
        super();
        createDirectories();
        createDirectoriesIfNotExists(OUTPUT_DIR + "statistic/");

    }

    public void runTest() throws IOException {
        aggregatedResults.clear();
        performTestConcrete();
        for (KindOfVector kind : KindOfVector.values()) {
           createDirectoriesIfNotExists(CHARTS_DIR + "RatioBarChart/");
            Vizualization.showOrSaveBarChartForRatioWithKindForVector(aggregatedResults, kind , CHARTS_DIR + "RatioBarChart/");
        }

        String temp = CHARTS_DIR + "linearChart/byStat/";
        String temp2 = CHARTS_DIR + "barChart/byStat/" ;

        createDirectoriesIfNotExists(temp + "add/");
        createDirectoriesIfNotExists(temp + "subtruct/");
        createDirectoriesIfNotExists(temp + "multiplyByScalar/");
        createDirectoriesIfNotExists(temp + "dotProduct/");
        createDirectoriesIfNotExists(temp + "subtractVectorNegativeAdd/");
        createDirectoriesIfNotExists(temp + "opposite/");
        createDirectoriesIfNotExists(temp2 + "add/");
        createDirectoriesIfNotExists(temp2 + "subtruct/");
        createDirectoriesIfNotExists(temp2 + "multiplyByScalar/");
        createDirectoriesIfNotExists(temp2 + "dotProduct/");
        createDirectoriesIfNotExists(temp2 + "subtractVectorNegativeAdd/");
        createDirectoriesIfNotExists(temp2 + "opposite/");








        Field[] fields = StatisticsResult.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.getName();
            if (value.equals("kindOfBubbleSort") || value.equals("kindOfVector") || value.equals("kindOfMatrix") || value.equals("dimension") || value.equals("operation")) {
            } else {

                // System.out.println("Nazwa pola: " + field.getName() + " -> wartość: " + value);
                Vizualization.showOrSaveChartDiffrenStatistictVsDimWithOperationForVector(aggregatedResults, "add", temp + "add/", field.getName());
                Vizualization.showOrSaveChartDiffrenStatistictVsDimWithOperationForVector(aggregatedResults, "subtruct", temp + "subtruct/", field.getName());
                Vizualization.showOrSaveChartDiffrenStatistictVsDimWithOperationForVector(aggregatedResults, "dotProduct", temp + "dotProduct/", field.getName());
                Vizualization.showOrSaveChartDiffrenStatistictVsDimWithOperationForVector(aggregatedResults, "multiplyByScalar", temp + "multiplyByScalar/", field.getName());
                Vizualization.showOrSaveChartDiffrenStatistictVsDimWithOperationForVector(aggregatedResults, "subtractVectorNegativeAdd", temp2 + "subtractVectorNegativeAdd/", field.getName());
                Vizualization.showOrSaveChartDiffrenStatistictVsDimWithOperationForVector(aggregatedResults, "opposite", temp2 + "opposite/", field.getName());



                Vizualization.showOrSaveBarChartForOperationVector(aggregatedResults,"add" , temp2 + "add/" , field.getName() );
                Vizualization.showOrSaveBarChartForOperationVector(aggregatedResults,"subtruct" , temp2 + "subtruct/"  , field.getName() );
                Vizualization.showOrSaveBarChartForOperationVector(aggregatedResults,"dotProduct" , temp2 + "dotProduct/" , field.getName() );
                Vizualization.showOrSaveBarChartForOperationVector(aggregatedResults,"multiplyByScalar" , temp2 + "multiplyByScalar/", field.getName()   );
                Vizualization.showOrSaveBarChartForOperationVector(aggregatedResults,"subtractVectorNegativeAdd" , temp2 + "subtractVectorNegativeAdd/", field.getName()   );
                Vizualization.showOrSaveBarChartForOperationVector(aggregatedResults,"opposite" , temp2 + "opposite/"  , field.getName() );

            }

        }


        // rysuj wykresy
        // Vizu/alization.showOrSaveChartRatioVsDim2(aggregatedResults, "add");
        //  showOrSaveChartRatioVsDim2(aggregatedResults, "subtruct");
        // showOrSaveChartRatioVsDim2(aggregatedResults, "dotProduct");
        // showOrSaveChartRatioVsDim2(aggregatedResults, "multiplyByScalar");
        //  showOrSaveChartRatioVsDim2(aggregatedResults, "subtractVectorNegativeAdd");
        //  showOrSaveChartRatioVsDim2(aggregatedResults, "opposite");







        // rysuj wykresy
    //    showOrSaveChartRatioVsDim2(aggregatedResults, "add");
     //   showOrSaveChartRatioVsDim2(aggregatedResults, "subtruct");
      //  showOrSaveChartRatioVsDim2(aggregatedResults, "dotProduct");
      //  showOrSaveChartRatioVsDim2(aggregatedResults, "multiplyByScalar");
       // showOrSaveChartRatioVsDim2(aggregatedResults, "subtractVectorNegativeAdd");
       // showOrSaveChartRatioVsDim2(aggregatedResults, "opposite");


    }


    public void performTestConcrete() {
    // Tworzenie katalogów wejściowych, wyjściowych, wykresów oraz dodatkowego katalogu "wyniki"
    createDirectoriesIfNotExists(INPUT_DIR);
    createDirectoriesIfNotExists(OUTPUT_DIR);
    createDirectoriesIfNotExists(CHARTS_DIR);


    // Utwórz instancję MatrixGenerator
    Vector1Generator generator = new Vector1Generator();


    // Iterujemy po wszystkich typach macierzy
    for (KindOfVector kind : KindOfVector.values()) {
      //  System.out.println("Test dla typu macierzy: " + kind);

        // Testujemy dla każdego wymiaru macierzy
        for (int dim : DIMENSIONS) {
        ///    System.out.println("Testujemy macierz " + kind + " o wymiarze " + dim + "x" + dim);

            // Tworzymy macierze z ustalonymi ziarnami: seed 0 dla pierwszej i seed 1 dla drugiej
            Vector1 vectorConcreteFirst = generator.createVector1(kind, dim, 0L);
            Vector1 vectorConcreteSecond = generator.createVector1(kind, dim, 1L);

            // Zapisujemy macierze wejściowe do plików z uwzględnieniem typu macierzy w nazwie pliku
            String fileNameFirst = INPUT_DIR + "vector_1_concrete" + kind.toString().toLowerCase() + "_" + dim + ".txt";
            String fileNameSecond = INPUT_DIR + "vector_2_concrete" + kind.toString().toLowerCase() + "_" + dim + ".txt";

            // Zapisz macierze do pliku przed obliczeniami
            saveVector1ToFile(vectorConcreteFirst, fileNameFirst);
            saveVector1ToFile(vectorConcreteSecond, fileNameSecond);

            // Testowanie operacji na macierzach (dodawanie, odejmowanie, mnożenie, dzielenie)
            aggregatedResults.add(testVectorConcreteObjectVsReflect(vectorConcreteFirst, vectorConcreteSecond, dim, kind, "add"));
            aggregatedResults.add(testVectorConcreteObjectVsReflect(vectorConcreteFirst, vectorConcreteSecond, dim, kind, "subtruct"));
            aggregatedResults.add(testVectorConcreteObjectVsReflect(vectorConcreteFirst,vectorConcreteSecond,dim , kind ,"multiplyByScalar"));
            aggregatedResults.add(testVectorConcreteObjectVsReflect(vectorConcreteFirst,vectorConcreteSecond,dim , kind ,"dotProduct"));
            aggregatedResults.add(testVectorConcreteObjectVsReflect(vectorConcreteFirst,vectorConcreteSecond,dim , kind ,"opposite"));
            aggregatedResults.add(testVectorConcreteObjectVsReflect(vectorConcreteFirst,vectorConcreteSecond,dim , kind ,"subtractVectorNegativeAdd")); // odejmowanie przeciwnym dodawaniem


        }
        // Wyświetlamy i zapisujemy zagregowane statystyki
       // displayDetailedStatistics();
      //  saveAggregatedStatisticsToFile();
        CalculationStatistic.saveStatisticsByOperation(OUTPUT_DIR, aggregatedResults, "vector");

    }

        CalculationStatistic.writeDetailedStatisticsByVectorKindToFile(aggregatedResults,OUTPUT_DIR + "statistic/" + "DetailedStatisticsByVectorKind.txt");
        CalculationStatistic.writeDetailedStatisticsByOperationToFile(aggregatedResults , "add" , "vector" , OUTPUT_DIR + "statistic/" + "addDetailedStatisticsByOperation.txt");
        CalculationStatistic.writeDetailedStatisticsByOperationToFile(aggregatedResults , "subtruct" , "vector", OUTPUT_DIR + "statistic/" + "subtractDetailedStatisticsByOperation.txt");
        CalculationStatistic.writeDetailedStatisticsByOperationToFile(aggregatedResults , "dotProduct" , "vector" , OUTPUT_DIR + "statistic/" + "dotProductDetailedStatisticsByOperation.txt");
        CalculationStatistic.writeDetailedStatisticsByOperationToFile(aggregatedResults , "multiplyByScalar" , "vector", OUTPUT_DIR + "statistic/" + "multiplyByScalarDetailedStatisticsByOperation.txt");
        CalculationStatistic.writeDetailedStatisticsByOperationToFile(aggregatedResults , "opposite" , "vector" , OUTPUT_DIR + "statistic/" + "oppositeDetailedStatisticsByOperation.txt");
        CalculationStatistic.writeDetailedStatisticsByOperationToFile(aggregatedResults , "subtractVectorNegativeAdd" , "vector" , OUTPUT_DIR + "statistic/" + "subtractVectorNegativeAddDetailedStatisticsByOperation.txt");




        // Wyświetlamy i zapisujemy zagregowane statystyki
    //displayDetailedStatistics();
   //saveAggregatedStatisticsToFile();
 //  saveStatisticsByOperation();

}
    void saveAggregatedStatisticsToFile() {
        String filename = CHARTS_DIR + "/aggregated_statistics.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Nagłówki z odpowiednim formatowaniem
            writer.write(String.format("%-15s %-5s %-12s %-14s %-12s %-14s %-12s %-14s %-12s %-14s %-10s%n",
                    "Operacja", "Dim", "Gen_Mean(ns)", "Gen_Median(ns)", "Gen_Mode(ns)", "Gen_StdDev(ns)",
                    "Con_Mean(ns)", "Con_Median(ns)", "Con_Mode(ns)", "Con_StdDev(ns)", "Ratio"));

            writer.write(String.format("%-15s %-5s %-12s %-14s %-12s %-14s %-12s %-14s %-12s %-14s %-10s%n",
                    "---------------", "-----", "------------", "--------------", "------------", "--------------",
                    "------------", "--------------", "------------", "--------------", "----------"));

            // Dane z odpowiednim formatowaniem
            for (StatisticsResult sr : aggregatedResults) {
                writer.write(String.format(
                        "%-15s %-5d %12.2f %14.2f %12d %14.2f %12.2f %14.2f %12d %14.2f %10.2f%n",
                        sr.operation, sr.dimension, sr.reflectMean, sr.reflectMedian, sr.reflectStdDev,
                        sr.objectMean, sr.objectMedian, sr.objectStdDev, sr.ratio
                ));
            }
        } catch (IOException e) {
            System.err.println("Error saving aggregated statistics: " + e.getMessage());
        }
    }
    void saveStatisticsByOperation() {
        String filename = OUTPUT_DIR + "/statistics_by_operation.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Nagłówek pliku
            writer.write("STATYSTYKI POGRUPOWANE WEDŁUG OPERACJI\n");
            writer.write("=====================================\n\n");

            // Lista wszystkich operacji
            List<String> operations = new ArrayList<>();
            for (StatisticsResult sr : aggregatedResults) {
                if (!operations.contains(sr.operation)) {
                    operations.add(sr.operation);
                }
            }

            // Dla każdej operacji
            for (String operation : operations) {
                writer.write(String.format("OPERACJA: %s\n", operation.toUpperCase()));
                writer.write("--------------------------------------\n");

                // Nagłówki tabeli
                writer.write(String.format("%-5s %-12s %-12s %-12s %-12s %-12s %-12s %-10s%n",
                        "Dim", "Direct_Mean", "Reflect_Mean", "Direct_Med", "Reflect_Med", "Direct_Std", "Reflect_Std", "Ratio"));
                writer.write(String.format("%-5s %-12s %-12s %-12s %-12s %-12s %-12s %-10s%n",
                        "-----", "------------", "------------", "------------", "------------", "------------", "------------", "----------"));

                // Znajdź wszystkie wyniki dla tej operacji i posortuj według wymiaru
                List<StatisticsResult> operationResults = aggregatedResults.stream()
                        .filter(sr -> sr.operation.equals(operation))
                        .sorted(Comparator.comparingInt(sr -> sr.dimension))
                        .collect(Collectors.toList());

                // Dla każdego wymiaru dla tej operacji
                for (StatisticsResult sr : operationResults) {
                    writer.write(String.format("%-5d %12.2f %12.2f %12.2f %12.2f %12.2f %12.2f %10.2f%n",
                            sr.dimension,
                            sr.objectMean,
                            sr.reflectMean,
                            sr.objectMedian,
                            sr.reflectMedian,
                            sr.objectStdDev,
                            sr.reflectStdDev,
                            sr.ratio));
                }

                // Dodaj podsumowanie dla tej operacji
                double avgRatio = operationResults.stream().mapToDouble(sr -> sr.ratio).average().orElse(0);
                double minRatio = operationResults.stream().mapToDouble(sr -> sr.ratio).min().orElse(0);
                double maxRatio = operationResults.stream().mapToDouble(sr -> sr.ratio).max().orElse(0);

                writer.write("\nPODSUMOWANIE:\n");
                writer.write(String.format("Średni stosunek: %.2f\n", avgRatio));
                writer.write(String.format("Minimalny stosunek: %.2f\n", minRatio));
                writer.write(String.format("Maksymalny stosunek: %.2f\n", maxRatio));
                writer.write("\n\n");
            }

            // Dodaj ogólne podsumowanie
            writer.write("PODSUMOWANIE OGÓLNE\n");
            writer.write("==================\n");
            writer.write(String.format("%-15s %-12s %-12s %-12s%n",
                    "Operacja", "Śr. Stosunek", "Min Stosunek", "Max Stosunek"));
            writer.write(String.format("%-15s %-12s %-12s %-12s%n",
                    "---------------", "------------", "------------", "------------"));

            for (String operation : operations) {
                List<StatisticsResult> operationResults = aggregatedResults.stream()
                        .filter(sr -> sr.operation.equals(operation))
                        .collect(Collectors.toList());

                double avgRatio = operationResults.stream().mapToDouble(sr -> sr.ratio).average().orElse(0);
                double minRatio = operationResults.stream().mapToDouble(sr -> sr.ratio).min().orElse(0);
                double maxRatio = operationResults.stream().mapToDouble(sr -> sr.ratio).max().orElse(0);

                writer.write(String.format("%-15s %12.2f %12.2f %12.2f%n",
                        operation, avgRatio, minRatio, maxRatio));
            }

            System.out.println("Statystyki pogrupowane według operacji zapisane do pliku: " + filename);

        } catch (IOException e) {
            System.err.println("Błąd podczas zapisywania statystyk pogrupowanych: " + e.getMessage());
        }
    }
    void displayDetailedStatistics() {
        System.out.println("\n===== AGREGOWANE STATYSTYKI =====");
        System.out.printf("%-10s %-8s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-10s\n",
                "Operacja", "Dim", "Gen_Mean(ns)", "Gen_Median(ns)", "Gen_Mode(ns)", "Gen_StdDev(ns)",
                "Con_Mean(ns)", "Con_Median(ns)", "Con_Mode(ns)", "Con_StdDev(ns)", "Ratio");
        for (StatisticsResult sr : aggregatedResults) {
            System.out.printf("%-10s %-8d %-15.2f %-15.2f %-15d %-15.2f %-15.2f %-15.2f %-15d %-15.2f %-10.2f\n",
                    sr.operation, sr.dimension, sr.reflectMean, sr.reflectMedian,  sr.reflectStdDev,
                    sr.objectMean, sr.objectMedian, sr.objectStdDev, sr.ratio);
        }
        System.out.println("==================================\n");
    }






    private StatisticsResult testVectorConcreteObjectVsReflect(Vector1 vectorConcreteFirst,
                                                              Vector1 vectorConcreteSecond,
                                                              int dim,
                                                              KindOfVector kind,
                                                              String operation) {
        List<Long> reflectionTimes = new ArrayList<>();
        List<Long> objectTimes = new ArrayList<>();

        // Warm-up
        for (int i = 0; i < 3; i++) {
            performObjectOperation(vectorConcreteFirst, vectorConcreteSecond, operation);
            performReflectionOperation(vectorConcreteFirst, vectorConcreteSecond, operation);
        }

        // Główne pomiary
        for (int i = 0; i < RUNS; i++) {
            // Pomiar operacji refleksyjnej
            long startReflection = System.nanoTime();
            performReflectionOperation(vectorConcreteFirst, vectorConcreteSecond, operation);
            long endReflection = System.nanoTime();
            reflectionTimes.add(endReflection - startReflection);

            // Pomiar operacji obiektowej
            long startObject = System.nanoTime();
            performObjectOperation(vectorConcreteFirst, vectorConcreteSecond, operation);
            long endObject = System.nanoTime();
            objectTimes.add(endObject - startObject);
        }

        return saveResults(reflectionTimes, objectTimes, operation, dim, kind);
    }

    private void performObjectOperation(Vector1 vector1, Vector1 vector2, String operation) {
        if (operation.equals("add")) {
            vector1.add(vector2);
        } else if (operation.equals("subtract")) {
            vector1.subtruct(vector2);
        } else if (operation.equals("dotProduct")) {
            vector1.dotProduct(vector2);
        } else if (operation.equals("multiplyByScalar")) {
            vector1.multiplyByScalar(1);
        }else if (operation.equals("opposite"))
            vector1.opposite();
        else if (operation.equals("subtractVectorNegativeAdd")) {
            vector1.subtractVectorNegativeAdd(vector1);

        }
    }

    // poprawic na vector1
    private void performReflectionOperation(Vector1 vector1, Vector1 vector2, String operation) {
        if (operation.equals("add") || operation.equals("subtruct") || operation.equals("subtractVectorNegativeAdd") ) {
            VectorReflectionUtil.performOperationReflectVector1(vector1, vector2, operation);
        } else if (operation.equals("dotProduct")) {
            VectorReflectionUtil.performOperationReflectVectorForDotProduct1(vector1, vector2, operation);
        } else if (operation.equals("multiplyByScalar")) {
            VectorReflectionUtil.performOperationReflectVectorForScalar1(vector1, 1, operation);
        }else  if (operation.equals("opposite"))
            VectorReflectionUtil.performOperationReflectVectorForOpposite1(vector1,"opposite");
    }






    private void createDirectoriesIfNotExists(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                //System.out.println("Created directory: " + path);
            } else {
                System.err.println("Failed to create directory: " + path);
            }
        }
    }


    private StatisticsResult saveResults(List<Long> reflectionTimes, List<Long> objectTimes,
                                         String operation, int dim, KindOfVector kind) {


        createDirectoriesIfNotExists(OUTPUT_DIR  + "statistic/" + operation + "/");

        String resultsFilename = OUTPUT_DIR + "statistic/"  + operation + "/" + "vector_performance_" + operation + "_of_reflection_concrete" + dim + ".txt";
        saveResultsToFile(resultsFilename, objectTimes, reflectionTimes);

        String statsFilename = OUTPUT_DIR + "statistic/" +  operation + "/" + "vector_statistics_" + operation + "_of_reflection_concrete" + dim + ".txt";
        StatisticsResult stats = CalculationStatistic.calculateAndSaveStatistics(reflectionTimes, objectTimes, statsFilename, operation, dim, kind);

      //  System.out.println(operation + " results saved to " + resultsFilename + " and " + statsFilename);
        return stats;
    }



    void saveVector1ToFile(Vector1 vector, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Vector Dimension: " + vector.getCoordinates().length + "\n\n");
            for (Number coordinate : vector.getCoordinates()) {
                writer.write(coordinate + "\t");
            }
        } catch (IOException e) {
            System.err.println("Error saving vector to file: " + e.getMessage());
        }
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


}
