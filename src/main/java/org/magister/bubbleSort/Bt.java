package org.magister.bubbleSort;


import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.XYChart;
import org.magister.helper.StatisticsResult;
import org.magister.vector.KindOfVector;
import org.magister.vector.Vector1;

import java.util.List;

public interface Bt {
    void runTest();

    void performTest();

    void showOrSaveChartRatioVsDim2(List<StatisticsResult> results, String whatoperation);

    XYChart createChartRatioVsDim2(List<StatisticsResult> results,
                                   String chartTitle,
                                   String xAxisTitle,
                                   String yAxisTitle, String whatoperation);

    StatisticsResult testBubbleSortConcreteObjectVsReflect(BubbleSort1 bubbleSort1,
                                                           BubbleSort1 bubbleSort2,
                                                           int dim,
                                                           KindOfBubbleSort kindOfBubbleSort,
                                                           String operation);
    StatisticsResult testBubbleSortGenericObjectVsReflect(BubbleSort bubbleSort1,
                                                           BubbleSort bubbleSort2,
                                                           int dim,
                                                           KindOfBubbleSort kindOfBubbleSort,
                                                           String operation);


    void saveAggregatedStatisticsToFile();

    void saveStatisticsByOperation();

    void displayDetailedStatistics();

    void performObjectOperation(BubbleSort bubbleSort, String operation);

    void createDirectoriesIfNotExists(String path);

    StatisticsResult saveResults(List<Long> reflectionTimes, List<Long> objectTimes,
                                 String operation, int dim, KindOfBubbleSort kindOfBubbleSort);

    void saveBubleSort1ToFile(BubbleSort1 bubbleSort1, String filename);

    StatisticsResult calculateAndSaveStatistics(List<Long> genericReflectTimes,
                                                List<Long> GenericObjectTimes,
                                                String filename,
                                                String operation,
                                                int dim, KindOfBubbleSort kind);

    CategoryChart createBarChartForRatioWithKind(
            List<StatisticsResult> results,
            KindOfBubbleSort kind,
            String chartTitle,
            String xAxisTitle,
            String yAxisTitle
    );

}