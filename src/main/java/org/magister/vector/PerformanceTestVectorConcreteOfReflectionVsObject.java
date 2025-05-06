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
        for (int dim : DIMENSIONS) {
            System.out.println("kind = " + kind + ", dim = " + dim);
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
        CalculationStatistic.saveStatisticsByOperation(OUTPUT_DIR, aggregatedResults, "vector");
    }

        CalculationStatistic.writeDetailedStatisticsByVectorKindToFile(aggregatedResults,OUTPUT_DIR + "statistic/" + "DetailedStatisticsByVectorKind.txt");
        CalculationStatistic.writeDetailedStatisticsByOperationToFile(aggregatedResults , "add" , "vector" , OUTPUT_DIR + "statistic/" + "addDetailedStatisticsByOperation.txt");
        CalculationStatistic.writeDetailedStatisticsByOperationToFile(aggregatedResults , "subtruct" , "vector", OUTPUT_DIR + "statistic/" + "subtractDetailedStatisticsByOperation.txt");
        CalculationStatistic.writeDetailedStatisticsByOperationToFile(aggregatedResults , "dotProduct" , "vector" , OUTPUT_DIR + "statistic/" + "dotProductDetailedStatisticsByOperation.txt");
        CalculationStatistic.writeDetailedStatisticsByOperationToFile(aggregatedResults , "multiplyByScalar" , "vector", OUTPUT_DIR + "statistic/" + "multiplyByScalarDetailedStatisticsByOperation.txt");
        CalculationStatistic.writeDetailedStatisticsByOperationToFile(aggregatedResults , "opposite" , "vector" , OUTPUT_DIR + "statistic/" + "oppositeDetailedStatisticsByOperation.txt");
        CalculationStatistic.writeDetailedStatisticsByOperationToFile(aggregatedResults , "subtractVectorNegativeAdd" , "vector" , OUTPUT_DIR + "statistic/" + "subtractVectorNegativeAddDetailedStatisticsByOperation.txt");



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
           // System.out.println(startReflection + "ns " + endReflection);
            reflectionTimes.add(endReflection - startReflection);
           // System.out.println(endReflection - startReflection);

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






}
