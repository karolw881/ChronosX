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
        private static final String INPUT_DIR = PerformanceTestVector.INPUT_DIR + "/GenericOfReflectionVsObject/";
        private static final String OUTPUT_DIR = PerformanceTestVector.OUTPUT_DIR + "/GenericOfReflectionVsObject/";
        private static final String CHARTS_DIR = PerformanceTestVector.CHARTS_DIR + "/GenericOfReflectionVsObject/";
        private static final int RUNS = PerformanceTestVector.RUNS;
        private static final int[] DIMENSIONS = PerformanceTestVector.DIMENSIONS;

        public PerformanceTestVectorGenericOfReflectionVsObject() {
            super();
            createDirectories();
            createDirectoriesIfNotExists(OUTPUT_DIR + "statistic/");
        }


        public void runTest() throws IOException {
            aggregatedResults.clear();
            performTestGeneric();
            for (KindOfVector kind : KindOfVector.values()) {
                createDirectoriesIfNotExists(CHARTS_DIR + "RatioBarChart/");
                Vizualization.showOrSaveBarChartForRatioWithKindForVector(aggregatedResults, kind,CHARTS_DIR + "RatioBarChart/");

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

                    Vizualization.showOrSaveChartDiffrenStatistictVsDimWithOperationForVector(aggregatedResults, "add", temp + "add/", field.getName());
                    Vizualization.showOrSaveChartDiffrenStatistictVsDimWithOperationForVector(aggregatedResults, "subtruct", temp + "subtruct/", field.getName());
                    Vizualization.showOrSaveChartDiffrenStatistictVsDimWithOperationForVector(aggregatedResults, "dotProduct", temp + "dotProduct/", field.getName());
                    Vizualization.showOrSaveChartDiffrenStatistictVsDimWithOperationForVector(aggregatedResults, "multiplyByScalar", temp + "multiplyByScalar/", field.getName());
                    Vizualization.showOrSaveChartDiffrenStatistictVsDimWithOperationForVector(aggregatedResults, "subtractVectorNegativeAdd", temp + "subtractVectorNegativeAdd/", field.getName());
                    Vizualization.showOrSaveChartDiffrenStatistictVsDimWithOperationForVector(aggregatedResults, "opposite", temp + "opposite/", field.getName());

                    Vizualization.showOrSaveBarChartForOperationVector(aggregatedResults,"add" , temp2 + "add/" , field.getName() );
                    Vizualization.showOrSaveBarChartForOperationVector(aggregatedResults,"subtruct" , temp2 + "subtruct/"  , field.getName() );
                    Vizualization.showOrSaveBarChartForOperationVector(aggregatedResults,"dotProduct" , temp2 + "dotProduct/" , field.getName() );
                    Vizualization.showOrSaveBarChartForOperationVector(aggregatedResults,"multiplyByScalar" , temp2 + "multiplyByScalar/", field.getName()   );
                    Vizualization.showOrSaveBarChartForOperationVector(aggregatedResults,"subtractVectorNegativeAdd" , temp2 + "subtractVectorNegativeAdd/", field.getName()   );
                    Vizualization.showOrSaveBarChartForOperationVector(aggregatedResults,"opposite" , temp2 + "opposite/"  , field.getName() );
                }
            }

        }






        public void performTestGeneric() {
            // Tworzenie katalogów wejściowych, wyjściowych, wykresów oraz dodatkowego katalogu "wyniki"
            createDirectoriesIfNotExists(INPUT_DIR);
            createDirectoriesIfNotExists(OUTPUT_DIR);
            createDirectoriesIfNotExists(CHARTS_DIR);

            VectorGenerator generator = new VectorGenerator(new NumberxxOperations());


            for (KindOfVector kind : KindOfVector.values()) {

                for (int dim : DIMENSIONS) {

                    Vector<Numberxx> vectorGenericFirst = generator.createVector(kind, dim, 0L);
                    Vector<Numberxx> vectorGenericSecond = generator.createVector(kind, dim, 1L);

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


            /**
             * zmienic na do plku
             */

            CalculationStatistic.writeDetailedStatisticsByVectorKindToFile(aggregatedResults,OUTPUT_DIR + "statistic/" + "DetailedStatisticsByVectorKind.txt");
            CalculationStatistic.writeDetailedStatisticsByOperationToFile(aggregatedResults , "add" , "vector" , OUTPUT_DIR + "statistic/" + "addDetailedStatisticsByOperation.txt");
            CalculationStatistic.writeDetailedStatisticsByOperationToFile(aggregatedResults , "subtruct" , "vector", OUTPUT_DIR + "statistic/" + "subtractDetailedStatisticsByOperation.txt");
            CalculationStatistic.writeDetailedStatisticsByOperationToFile(aggregatedResults , "dotProduct" , "vector" , OUTPUT_DIR + "statistic/" + "dotProductDetailedStatisticsByOperation.txt");
            CalculationStatistic.writeDetailedStatisticsByOperationToFile(aggregatedResults , "multiplyByScalar" , "vector", OUTPUT_DIR + "statistic/" + "multiplyByScalarDetailedStatisticsByOperation.txt");
            CalculationStatistic.writeDetailedStatisticsByOperationToFile(aggregatedResults , "opposite" , "vector" , OUTPUT_DIR + "statistic/" + "oppositeDetailedStatisticsByOperation.txt");
            CalculationStatistic.writeDetailedStatisticsByOperationToFile(aggregatedResults , "subtractVectorNegativeAdd" , "vector" , OUTPUT_DIR + "statistic/" + "subtractVectorNegativeAddDetailedStatisticsByOperation.txt");





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
                case "add": VectorReflectionUtil.performOperationReflectVector(vector1, vector2, operation);break;
                case "subtruct": VectorReflectionUtil.performOperationReflectVector(vector1, vector2, operation);break;
                case "subtractVectorNegativeAdd":
                    VectorReflectionUtil.performOperationReflectVector(vector1, vector2, operation);
                    break;
                case "dotProduct":
                    VectorReflectionUtil.performOperationReflectVectorForDotProduct(vector1, vector2, operation);
                    break;
                case "multiplyByScalar":

                    VectorReflectionUtil.performOperationReflectVectorForScalar(vector1, Numberxx.valueOf(1), operation);
                    break;
                case "opposite":
                    VectorReflectionUtil.performOperationReflectVectorForOpposite(vector1, "opposite");
                    break;
                default:
                    throw new IllegalArgumentException("Unknown operation: " + operation);
            }
        }


        private StatisticsResult saveResults(List<Long> reflectionTimes, List<Long> objectTimes,
                                             String operation, int dim, KindOfVector kind) {

            createDirectoriesIfNotExists(OUTPUT_DIR  + "statistic/" + operation + "/");
            String resultsFilename = OUTPUT_DIR + "statistic/"+ operation + "/" + "vector_performance_" + operation + "_of_reflection_generic" + dim + ".txt";
            saveResultsToFile(resultsFilename, objectTimes, reflectionTimes);

            String statsFilename = OUTPUT_DIR + "statistic/" +  operation + "/" + "vector_statistics_" + operation + "_of_reflection_generic" + dim + ".txt";
            StatisticsResult stats = CalculationStatistic.calculateAndSaveStatistics(reflectionTimes, objectTimes, statsFilename, operation, dim, kind);
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






    }
