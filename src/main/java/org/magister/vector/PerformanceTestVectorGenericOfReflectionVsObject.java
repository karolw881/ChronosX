    package org.magister.vector;


    import lombok.Getter;
    import lombok.Setter;
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
    import java.util.List;
    import java.util.stream.Collectors;

    @Getter
    @Setter
    public class PerformanceTestVectorGenericOfReflectionVsObject extends PerformanceTestVector {
        private static final String INPUT_DIR = PerformanceTestVector.INPUT_DIR + "GenericOfReflectionVsObject/";
        private static final String OUTPUT_DIR = PerformanceTestVector.OUTPUT_DIR + "GenericOfReflectionVsObject/";
        private static final String CHARTS_DIR = PerformanceTestVector.CHARTS_DIR + "GenericOfReflectionVsObject/";
        private static final int RUNS = PerformanceTestVector.RUNS;
        private static final int[] DIMENSIONS = PerformanceTestVector.DIMENSIONS;

        public PerformanceTestVectorGenericOfReflectionVsObject() {
            createDirectories();
            createDirectories();
        }


        public void runTest() throws IOException {
            aggregatedResults.clear(); // Czyszczenie poprzednich wyników
            performTestGeneric();
            for (KindOfVector kind : KindOfVector.values()) {    // Iterujemy po wszystkich typach wektora
                Vizualization.showOrSaveBarChartForRatioWithKindForVector(aggregatedResults, kind,CHARTS_DIR);

            }


            Field[] fields = StatisticsResult.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.getName();
                if (value.equals("kindOfBubbleSort") || value.equals("kindOfVector") || value.equals("kindOfMatrix") || value.equals("dimension") || value.equals("operation")) {
                } else {

                    // System.out.println("Nazwa pola: " + field.getName() + " -> wartość: " + value);
                    Vizualization.showOrSaveChartDiffrenStatistictVsDimWithOperationForVector(aggregatedResults, "add", CHARTS_DIR, field.getName());
                    Vizualization.showOrSaveChartDiffrenStatistictVsDimWithOperationForVector(aggregatedResults, "subtruct", CHARTS_DIR, field.getName());
                    Vizualization.showOrSaveChartDiffrenStatistictVsDimWithOperationForVector(aggregatedResults, "dotProduct", CHARTS_DIR, field.getName());
                    Vizualization.showOrSaveChartDiffrenStatistictVsDimWithOperationForVector(aggregatedResults, "multiplyByScalar", CHARTS_DIR, field.getName());
                    Vizualization.showOrSaveChartDiffrenStatistictVsDimWithOperationForVector(aggregatedResults, "subtractVectorNegativeAdd", CHARTS_DIR, field.getName());
                    Vizualization.showOrSaveChartDiffrenStatistictVsDimWithOperationForVector(aggregatedResults, "opposite", CHARTS_DIR, field.getName());







                    Vizualization.showOrSaveBarChartForOperationVector(aggregatedResults,"add" , CHARTS_DIR , field.getName() );
                    Vizualization.showOrSaveBarChartForOperationVector(aggregatedResults,"subtruct" , CHARTS_DIR  , field.getName() );
                    Vizualization.showOrSaveBarChartForOperationVector(aggregatedResults,"dotProduct" , CHARTS_DIR , field.getName() );
                    Vizualization.showOrSaveBarChartForOperationVector(aggregatedResults,"multiplyByScalar" , CHARTS_DIR, field.getName()   );
                    Vizualization.showOrSaveBarChartForOperationVector(aggregatedResults,"opposite" , CHARTS_DIR  , field.getName() );

                }

            }


            // rysuj wykresy
           // Vizu/alization.showOrSaveChartRatioVsDim2(aggregatedResults, "add");
            showOrSaveChartRatioVsDim2(aggregatedResults, "subtruct");
            showOrSaveChartRatioVsDim2(aggregatedResults, "dotProduct");
            showOrSaveChartRatioVsDim2(aggregatedResults, "multiplyByScalar");
            showOrSaveChartRatioVsDim2(aggregatedResults, "subtractVectorNegativeAdd");
            showOrSaveChartRatioVsDim2(aggregatedResults, "opposite");


        }


        public void performTestGeneric() {
            // Tworzenie katalogów wejściowych, wyjściowych, wykresów oraz dodatkowego katalogu "wyniki"
            createDirectoriesIfNotExists(INPUT_DIR);
            createDirectoriesIfNotExists(OUTPUT_DIR);
            createDirectoriesIfNotExists(CHARTS_DIR);
            createDirectoriesIfNotExists(CHARTS_DIR + "DiffrenStatistictVsDimWithOperationForVector/");


            // Utwórz instancję MatrixGenerator
            VectorGenerator generator = new VectorGenerator(new NumberxxOperations());


            // Iterujemy po wszystkich typach macierzy
            for (KindOfVector kind : KindOfVector.values()) {
                System.out.println("Test dla typu macierzy: " + kind);

                // Testujemy dla każdego wymiaru macierzy
                for (int dim : DIMENSIONS) {
                    System.out.println("Testujemy macierz " + kind + " o wymiarze " + dim + "x" + dim);

                    // Tworzymy macierze z ustalonymi ziarnami: seed 0 dla pierwszej i seed 1 dla drugiej
                    Vector<Numberxx> vectorGenericFirst = generator.createVector(kind, dim, 0L);
                    Vector<Numberxx> vectorGenericSecond = generator.createVector(kind, dim, 1L);

                    // Zapisujemy macierze wejściowe do plików z uwzględnieniem typu macierzy w nazwie pliku
                    String fileNameFirst = INPUT_DIR + "vector_1_generic" + kind.toString().toLowerCase() + "_" + dim + ".txt";
                    String fileNameSecond = INPUT_DIR + "vector_2_generic" + kind.toString().toLowerCase() + "_" + dim + ".txt";

                    // Zapisz macierze do pliku przed obliczeniami
                    saveVectorToFile(vectorGenericFirst, fileNameFirst);
                    saveVectorToFile(vectorGenericSecond, fileNameSecond);

                    // Testowanie operacji na macierzach (dodawanie, odejmowanie, mnożenie, dzielenie)
                    aggregatedResults.add(testGenericObjectVsReflect(vectorGenericFirst, vectorGenericSecond, dim, kind, "add"));
                    aggregatedResults.add(testGenericObjectVsReflect(vectorGenericFirst, vectorGenericSecond, dim, kind, "subtruct"));
                    aggregatedResults.add(testGenericObjectVsReflect(vectorGenericFirst,vectorGenericSecond,dim , kind ,"multiplyByScalar"));
                    aggregatedResults.add(testGenericObjectVsReflect(vectorGenericFirst,vectorGenericSecond,dim , kind ,"dotProduct"));
                    aggregatedResults.add(testGenericObjectVsReflect(vectorGenericFirst,vectorGenericSecond,dim , kind ,"opposite"));
                   aggregatedResults.add(testGenericObjectVsReflect(vectorGenericFirst,vectorGenericSecond,dim , kind ,"subtractVectorNegativeAdd")); // odejmowanie przeciwnym dodawaniem


                }
                // Wyświetlamy i zapisujemy zagregowane statystyki
                CalculationStatistic.saveStatisticsByOperation(OUTPUT_DIR, aggregatedResults, "vector");
            }


            // Wyświetlamy i zapisujemy zagregowane statystyki
            CalculationStatistic.displayDetailedStatisticsByOperation(aggregatedResults , "add" , "vector");
            CalculationStatistic.displayDetailedStatisticsByOperation(aggregatedResults , "subtruct" , "vector");
            CalculationStatistic.displayDetailedStatisticsByOperation(aggregatedResults , "multiplyByScalar" , "vector");
            CalculationStatistic.displayDetailedStatisticsByOperation(aggregatedResults , "dotProduct" , "vector");
            CalculationStatistic.displayDetailedStatisticsByOperation(aggregatedResults , "opposite" , "vector");
            CalculationStatistic.displayDetailedStatisticsByOperation(aggregatedResults , "subtractVectorNegativeAdd" , "vector");


        }
        /**
         * Wykonuje test porównujący operacje obiektowe z refleksyjnymi
         */
        private StatisticsResult testGenericObjectVsReflect(
                Vector<Numberxx> vector1,
                Vector<Numberxx> vector2,
                int dim, KindOfVector kind, String whatOperation) {

            List<Long> reflectionTimes = new ArrayList<>();
            List<Long> objectTimes = new ArrayList<>();

            // Wykonaj rozgrzewkę dla odpowiedniej operacji
            performWarmup(vector1, vector2, whatOperation);

            // Wykonaj pomiary dla odpowiedniej operacji
            performMeasurements(vector1, vector2, whatOperation, reflectionTimes, objectTimes);

            // Zapisz wyniki i statystyki
            return saveResults(reflectionTimes, objectTimes, whatOperation, dim, kind);
        }

        /**
         * Wykonuje operacje rozgrzewające przed właściwymi pomiarami
         */
        private void performWarmup(Vector<Numberxx> vector1, Vector<Numberxx> vector2, String whatOperation) {
            for (int i = 0; i < 3; i++) {
                // Wykonaj operację obiektową
                performObjectOperation(vector1, vector2, whatOperation);
                // Wykonaj operację refleksyjną
                performReflectionOperation(vector1, vector2, whatOperation);
            }
        }

        /**
         * Wykonuje właściwe pomiary dla danej operacji
         */
        private void performMeasurements(
                Vector<Numberxx> vector1,
                Vector<Numberxx> vector2,
                String whatOperation,
                List<Long> reflectionTimes,
                List<Long> objectTimes) {

            for (int i = 0; i < RUNS; i++) {
                // Pomiar operacji refleksyjnej
                long startReflection = System.nanoTime();
                performReflectionOperation(vector1, vector2, whatOperation);
                long endReflection = System.nanoTime();
                reflectionTimes.add(endReflection - startReflection);

                // Pomiar operacji obiektowej
                long startObject = System.nanoTime();
                performObjectOperation(vector1, vector2, whatOperation);
                long endObject = System.nanoTime();
                objectTimes.add(endObject - startObject);
            }
        }

        private void performObjectOperation(Vector<Numberxx> vector1, Vector<Numberxx> vector2, String operation) {
            switch (operation) {
                case "add":
                    vector1.add(vector2);
                    break;
                case "subtruct":
                    vector1.subtruct(vector2);
                    break;
                case "dotProduct":
                    vector1.dotProduct(vector2);
                    break;
                case "multiplyByScalar":
                    vector1.multiplyByScalar(Numberxx.valueOf(1));
                    break;
                case "opposite":
                    vector1.opposite();
                    break;
                case "subtractVectorNegativeAdd":
                    vector1.subtractVectorNegativeAdd(vector2);
                    break;
            }
        }

        private void performReflectionOperation(Vector<Numberxx> vector1, Vector<Numberxx> vector2, String operation) {
            switch (operation) {
                case "add":
                case "subtruct": // Fixed typo here
                case "subtractVectorNegativeAdd":
                    VectorReflectionUtil.performOperationReflectVector(vector1, vector2, operation);
                    break;
                case "dotProduct":
                    VectorReflectionUtil.performOperationReflectVectorForDotProduct(vector1, vector2, operation);
                    break;
                case "multiplyByScalar":

                    VectorReflectionUtil.performOperationReflectVectorForScalar(vector1, Numberxx.valueOf(1), operation);
                    break; // Added break statement here
                case "opposite":
                    VectorReflectionUtil.performOperationReflectVectorForOpposite(vector1, "opposite");
                    break;
                default:
                    throw new IllegalArgumentException("Unknown operation: " + operation);
            }
        }




        private StatisticsResult saveResults(List<Long> reflectionTimes, List<Long> objectTimes,
                                             String operation, int dim, KindOfVector kind) {
            String resultsFilename = OUTPUT_DIR + "vector_performance_" + operation + "_of_reflection_generic" + dim + ".txt";
            saveResultsToFile(resultsFilename, objectTimes, reflectionTimes);

            String statsFilename = OUTPUT_DIR + "vector_statistics_" + operation + "_of_reflection_generic" + dim + ".txt";
            StatisticsResult stats = CalculationStatistic.calculateAndSaveStatistics(reflectionTimes, objectTimes, statsFilename, operation, dim, kind);

            System.out.println(operation + " results saved to " + resultsFilename + " and " + statsFilename);
            return stats;
        }

        /**
         * Zapisz zagregowane statystyki do pliku tekstowego.
         */
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
                            sr.objectMean, sr.objectMedian,  sr.objectStdDev, sr.ratio
                    ));
                }
            } catch (IOException e) {
                System.err.println("Error saving aggregated statistics: " + e.getMessage());
            }
        }

        /**
         * Zapisuje statystyki pogrupowane według rodzaju operacji do pliku.
         * Dla każdej operacji (add, subtract, multiplyByScalar, dotProduct) zbiera dane
         * dla różnych wymiarów i przedstawia je w uporządkowany sposób.
         */
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
                        sr.operation, sr.dimension, sr.reflectMean, sr.reflectMedian, sr.reflectStdDev,
                        sr.objectMean, sr.objectMedian, sr.objectStdDev, sr.ratio);
            }
            System.out.println("==================================\n");
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
                                                           String yAxisTitle, String whatoperation) {

            // Tworzymy wykres XY
            XYChart chart = new XYChartBuilder()
                    .width(1800)
                    .height(1000)
                    .title(chartTitle)
                    .xAxisTitle(xAxisTitle)
                    .yAxisTitle(yAxisTitle)
                    .build();



            List<StatisticsResult> radomResults = results.stream()
                    .filter(r -> r.operation.equalsIgnoreCase(whatoperation))
                    .filter(r -> r.kindOfVector == KindOfVector.RANDOM)
                    .toList();

            System.out.println("Bede rysowac dla : " + whatoperation);

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




            private void showOrSaveChartRatioVsDim2(List<StatisticsResult> results, String whatoperation) throws IOException {
                XYChart chart = createChartRatioVsDim2ForAdd(
                        results,
                        "Wykres: Ratio (x) vs. Dim (y) dla operacji " + whatoperation,
                        "Wymiar (Dim)",
                        "Ratio (Gen Ref / Gen Obj)", whatoperation
                );
                // Zapis do pliku:
                BitmapEncoder.saveBitmap(chart, CHARTS_DIR + "ratio_vs_dim_chartlinear" + whatoperation + ".png", BitmapEncoder.BitmapFormat.PNG);
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

    }
