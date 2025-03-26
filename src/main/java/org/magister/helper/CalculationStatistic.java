package org.magister.helper;

import cern.colt.list.DoubleArrayList;
import cern.jet.stat.Descriptive;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.moment.Kurtosis;
import org.apache.commons.math3.stat.descriptive.moment.Skewness;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.magister.matrix.KindOfMatrix;
import org.magister.vector.KindOfVector;
import org.magister.bubbleSort.KindOfBubbleSort;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CalculationStatistic {

    // Wersja z KindOfVector
    public static StatisticsResult calculateAndSaveStatistics(
            List<Long> genericReflectTimes,
            List<Long> genericObjectTimes,
            String filename,
            String operation,
            int dimension,
            KindOfVector kindOfVector
    ) {

        double[] reflectArray = genericReflectTimes.stream().mapToDouble(Long::doubleValue).toArray();
        double[] objectArray = genericObjectTimes.stream().mapToDouble(Long::doubleValue).toArray();

        DoubleArrayList reflectColtList = new DoubleArrayList(reflectArray);
        DoubleArrayList objectColtList = new DoubleArrayList(objectArray);

        DescriptiveStatistics reflectStats = new DescriptiveStatistics(reflectArray);
        DescriptiveStatistics objectStats = new DescriptiveStatistics(objectArray);

        // Podstawowe statystyki
        double reflectMean = reflectStats.getMean();
        double objectMean = objectStats.getMean();
        double reflectMedian = new    Median().evaluate(reflectArray);
        double objectMedian = new Median().evaluate(objectArray);
        double reflectStdDev = reflectStats.getStandardDeviation();
        double objectStdDev = objectStats.getStandardDeviation();

        // Dodatkowe statystyki (skośność, kurtoza)
        double reflectSkewness = new Skewness().evaluate(reflectArray);
        double objectSkewness = new Skewness().evaluate(objectArray);
        double reflectKurtosis = new Kurtosis().evaluate(reflectArray);
        double objectKurtosis = new Kurtosis().evaluate(objectArray);

        // Kwantyle
        double reflectQ1 = Descriptive.quantile(reflectColtList, 0.25);
        double objectQ1 = Descriptive.quantile(objectColtList, 0.25);
        double reflectQ3 = Descriptive.quantile(reflectColtList, 0.75);
        double objectQ3 = Descriptive.quantile(objectColtList, 0.75);

        // Rozstęp międzykwartylowy (IQR)
        double reflectIQR = reflectQ3 - reflectQ1;
        double objectIQR = objectQ3 - objectQ1;

        // Współczynnik zmienności (CV)
        double reflectCV = (reflectStdDev / reflectMean) * 100;
        double objectCV = (objectStdDev / objectMean) * 100;

        // Momenty centralne
        double reflectCentralMoment3 = Descriptive.moment(reflectColtList, (int) reflectMean, 3);
        double objectCentralMoment3 = Descriptive.moment(objectColtList, (int) objectMean, 3);
        double reflectCentralMoment4 = Descriptive.moment(reflectColtList, (int) reflectMean, 4);
        double objectCentralMoment4 = Descriptive.moment(objectColtList, (int) objectMean, 4);

        // Stosunek średnich czasów
        double ratio = reflectMean / objectMean;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Advanced Statistics for generic Reflect implementation (Matrix<Integer>):\n");
            writer.write(String.format("Mean: %.2f ns\n", reflectMean));
            writer.write(String.format("Median: %.2f ns\n", reflectMedian));
            writer.write(String.format("Min: %.2f ns\n", reflectStats.getMin()));
            writer.write(String.format("Max: %.2f ns\n", reflectStats.getMax()));
            writer.write(String.format("Standard Deviation: %.2f ns\n", reflectStdDev));
            writer.write(String.format("Q1 (25th percentile): %.2f ns\n", reflectQ1));
            writer.write(String.format("Q3 (75th percentile): %.2f ns\n", reflectQ3));
            writer.write(String.format("IQR: %.2f ns\n", reflectIQR));
            writer.write(String.format("Coefficient of Variation: %.2f%%\n", reflectCV));
            writer.write(String.format("Skewness: %.4f\n", reflectSkewness));
            writer.write(String.format("Kurtosis: %.4f\n", reflectKurtosis));
            writer.write(String.format("3rd Central Moment: %.4f\n", reflectCentralMoment3));
            writer.write(String.format("4th Central Moment: %.4f\n", reflectCentralMoment4));
            writer.write("\n");

            writer.write("Advanced Statistics for generic Object implementation (Matrix<Integer>):\n");
            writer.write(String.format("Mean: %.2f ns\n", objectMean));
            writer.write(String.format("Median: %.2f ns\n", objectMedian));
            writer.write(String.format("Min: %.2f ns\n", objectStats.getMin()));
            writer.write(String.format("Max: %.2f ns\n", objectStats.getMax()));
            writer.write(String.format("Standard Deviation: %.2f ns\n", objectStdDev));
            writer.write(String.format("Q1 (25th percentile): %.2f ns\n", objectQ1));
            writer.write(String.format("Q3 (75th percentile): %.2f ns\n", objectQ3));
            writer.write(String.format("IQR: %.2f ns\n", objectIQR));
            writer.write(String.format("Coefficient of Variation: %.2f%%\n", objectCV));
            writer.write(String.format("Skewness: %.4f\n", objectSkewness));
            writer.write(String.format("Kurtosis: %.4f\n", objectKurtosis));
            writer.write(String.format("3rd Central Moment: %.4f\n", objectCentralMoment3));
            writer.write(String.format("4th Central Moment: %.4f\n", objectCentralMoment4));
            writer.write("\n");

            writer.write(String.format("Ratio of mean times (reflect/object): %.2f\n", ratio));
            writer.write(String.format("Performance difference: %s implementation is %.2f times faster than %s\n",
                    (ratio > 1) ? "Object" : "Reflection",
                    (ratio > 1) ? ratio : 1 / ratio,
                    (ratio > 1) ? "Reflection" : "Object"));
            writer.write("\n");
            writer.write("Kind of Vector: " + kindOfVector + "\n");
        } catch (IOException e) {
            System.err.println("Error saving statistics to file: " + e.getMessage());
        }

        return new StatisticsResult(
                operation, dimension,
                reflectMean, reflectMedian, reflectStdDev,
                reflectQ1, reflectQ3, reflectIQR, reflectCV,
                reflectSkewness, reflectKurtosis,
                objectMean, objectMedian, objectStdDev,
                objectQ1, objectQ3, objectIQR, objectCV,
                objectSkewness, objectKurtosis,
                ratio,
                kindOfVector);
    }

    // Wersja z KindOfBubbleSort
    public static StatisticsResult calculateAndSaveStatistics(
            List<Long> genericObjectTimes,
            List<Long> genericReflectTimes,
            String filename,
            String operation,
            int dimension,
            KindOfBubbleSort kindOfBubbleSort
    ) {

        double[] reflectArray = genericReflectTimes.stream().mapToDouble(Long::doubleValue).toArray();
        double[] objectArray = genericObjectTimes.stream().mapToDouble(Long::doubleValue).toArray();

        DoubleArrayList reflectColtList = new DoubleArrayList(reflectArray);
        DoubleArrayList objectColtList = new DoubleArrayList(objectArray);

        DescriptiveStatistics reflectStats = new DescriptiveStatistics(reflectArray);
        DescriptiveStatistics objectStats = new DescriptiveStatistics(objectArray);

        // Podstawowe statystyki
        double reflectMean = reflectStats.getMean();
        double objectMean = objectStats.getMean();
        double reflectMedian = new Median().evaluate(reflectArray);
        double objectMedian = new Median().evaluate(objectArray);
        double reflectStd = reflectStats.getStandardDeviation();
        double objectStd = objectStats.getStandardDeviation();

        // Dodatkowe statystyki (skośność, kurtoza)
        double reflectSkewness = new Skewness().evaluate(reflectArray);
        double objectSkewness = new Skewness().evaluate(objectArray);
        double reflectKurtosis = new Kurtosis().evaluate(reflectArray);
        double objectKurtosis = new Kurtosis().evaluate(objectArray);

        // Kwantyle
        double reflectQ1 = Descriptive.quantile(reflectColtList, 0.25);
        double objectQ1 = Descriptive.quantile(objectColtList, 0.25);
        double reflectQ3 = Descriptive.quantile(reflectColtList, 0.75);
        double objectQ3 = Descriptive.quantile(objectColtList, 0.75);

        // Rozstęp międzykwartylowy (IQR)
        double reflectIQR = reflectQ3 - reflectQ1;
        double objectIQR = objectQ3 - objectQ1;

        // Współczynnik zmienności (CV)
        double reflectCV = (reflectStd / reflectMean) * 100;
        double objectCV = (objectStd / objectMean) * 100;


        // Stosunek średnich czasów
        double ratio = reflectMean / objectMean;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Advanced Statistics for generic Reflect implementation (Matrix<Integer>):\n");
            writer.write(String.format("Mean: %.2f ns\n", reflectMean));
            writer.write(String.format("Median: %.2f ns\n", reflectMedian));
            writer.write(String.format("Min: %.2f ns\n", reflectStats.getMin()));
            writer.write(String.format("Max: %.2f ns\n", reflectStats.getMax()));
            writer.write(String.format("Standard Deviation: %.2f ns\n", reflectStd));
            writer.write(String.format("Q1 (25th percentile): %.2f ns\n", reflectQ1));
            writer.write(String.format("Q3 (75th percentile): %.2f ns\n", reflectQ3));
            writer.write(String.format("IQR: %.2f ns\n", reflectIQR));
            writer.write(String.format("Coefficient of Variation: %.2f%%\n", reflectCV));
            writer.write(String.format("Skewness: %.4f\n", reflectSkewness));
            writer.write(String.format("Kurtosis: %.4f\n", reflectKurtosis));
            writer.write("\n");

            writer.write("Advanced Statistics for generic Object implementation (Matrix<Integer>):\n");
            writer.write(String.format("Mean: %.2f ns\n", objectMean));
            writer.write(String.format("Median: %.2f ns\n", objectMedian));
            writer.write(String.format("Min: %.2f ns\n", objectStats.getMin()));
            writer.write(String.format("Max: %.2f ns\n", objectStats.getMax()));
            writer.write(String.format("Standard Deviation: %.2f ns\n", objectStd));
            writer.write(String.format("Q1 (25th percentile): %.2f ns\n", objectQ1));
            writer.write(String.format("Q3 (75th percentile): %.2f ns\n", objectQ3));
            writer.write(String.format("IQR: %.2f ns\n", objectIQR));
            writer.write(String.format("Coefficient of Variation: %.2f%%\n", objectCV));
            writer.write(String.format("Skewness: %.4f\n", objectSkewness));
            writer.write(String.format("Kurtosis: %.4f\n", objectKurtosis));
            writer.write("\n");
            writer.write(String.format("Ratio of mean times (reflect/object): %.2f\n", ratio));
            // Poprawione: dodano trzeci argument do formatu
            writer.write(String.format("Performance difference: %s implementation is %.2f times faster than %s\n",
                    (ratio > 1) ? "Object" : "Reflection",
                    (ratio > 1) ? ratio : 1 / ratio,
                    (ratio > 1) ? "Reflection" : "Object"));
            writer.write("\n");
            writer.write("Kind of Matrix: " + kindOfBubbleSort + "\n");
        } catch (IOException e) {
            System.err.println("Error saving statistics to file: " + e.getMessage());
        }

        return new StatisticsResult(
                operation, dimension,
                reflectMean, reflectMedian, reflectStd,
                reflectQ1, reflectQ3, reflectIQR, reflectCV,
                reflectSkewness, reflectKurtosis,
                objectMean, objectMedian, objectStd,
                objectQ1, objectQ3, objectIQR, objectCV,
                objectSkewness, objectKurtosis,
                ratio,
                kindOfBubbleSort);
    }

    // Wersja z KindOfMatrix
    public static StatisticsResult calculateAndSaveStatistics(
            List<Long> genericReflectTimes,
            List<Long> genericObjectTimes,
            String filename,
            String operation,
            int dimension,
            KindOfMatrix kindOfMatrix
    ) {

        double[] reflectArray = genericReflectTimes.stream().mapToDouble(Long::doubleValue).toArray();
        double[] objectArray = genericObjectTimes.stream().mapToDouble(Long::doubleValue).toArray();

        DoubleArrayList reflectColtList = new DoubleArrayList(reflectArray);
        DoubleArrayList objectColtList = new DoubleArrayList(objectArray);

        DescriptiveStatistics reflectStats = new DescriptiveStatistics(reflectArray);
        DescriptiveStatistics objectStats = new DescriptiveStatistics(objectArray);

        // Podstawowe statystyki
        double reflectMean = reflectStats.getMean();
        double objectMean = objectStats.getMean();
        double reflectMedian = new Median().evaluate(reflectArray);
        double objectMedian = new Median().evaluate(objectArray);
        double reflectStd = reflectStats.getStandardDeviation();
        double objectStd = objectStats.getStandardDeviation();

        // Dodatkowe statystyki (skośność, kurtoza)
        double reflectSkewness = new Skewness().evaluate(reflectArray);
        double objectSkewness = new Skewness().evaluate(objectArray);
        double reflectKurtosis = new Kurtosis().evaluate(reflectArray);
        double objectKurtosis = new Kurtosis().evaluate(objectArray);

        // Kwantyle
        double reflectQ1 = Descriptive.quantile(reflectColtList, 0.25);
        double objectQ1 = Descriptive.quantile(objectColtList, 0.25);
        double reflectQ3 = Descriptive.quantile(reflectColtList, 0.75);
        double objectQ3 = Descriptive.quantile(objectColtList, 0.75);

        // Rozstęp międzykwartylowy (IQR)
        double reflectIQR = reflectQ3 - reflectQ1;
        double objectIQR = objectQ3 - objectQ1;

        // Współczynnik zmienności (CV)
        double reflectCV = (reflectStd / reflectMean) * 100;
        double objectCV = (objectStd / objectMean) * 100;
        double ratio = reflectMean / objectMean;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Advanced Statistics for generic Reflect implementation (Matrix<Integer>):\n");
            writer.write(String.format("Mean: %.2f ns\n", reflectMean));
            writer.write(String.format("Median: %.2f ns\n", reflectMedian));
            writer.write(String.format("Min: %.2f ns\n", reflectStats.getMin()));
            writer.write(String.format("Max: %.2f ns\n", reflectStats.getMax()));
            writer.write(String.format("Standard Deviation: %.2f ns\n", reflectStd));
            writer.write(String.format("Q1 (25th percentile): %.2f ns\n", reflectQ1));
            writer.write(String.format("Q3 (75th percentile): %.2f ns\n", reflectQ3));
            writer.write(String.format("IQR: %.2f ns\n", reflectIQR));
            writer.write(String.format("Coefficient of Variation: %.2f%%\n", reflectCV));
            writer.write(String.format("Skewness: %.4f\n", reflectSkewness));
            writer.write(String.format("Kurtosis: %.4f\n", reflectKurtosis));
            writer.write("\n");

            writer.write("Advanced Statistics for generic Object implementation (Matrix<Integer>):\n");
            writer.write(String.format("Mean: %.2f ns\n", objectMean));
            writer.write(String.format("Median: %.2f ns\n", objectMedian));
            writer.write(String.format("Min: %.2f ns\n", objectStats.getMin()));
            writer.write(String.format("Max: %.2f ns\n", objectStats.getMax()));
            writer.write(String.format("Standard Deviation: %.2f ns\n", objectStd));
            writer.write(String.format("Q1 (25th percentile): %.2f ns\n", objectQ1));
            writer.write(String.format("Q3 (75th percentile): %.2f ns\n", objectQ3));
            writer.write(String.format("IQR: %.2f ns\n", objectIQR));
            writer.write(String.format("Coefficient of Variation: %.2f%%\n", objectCV));
            writer.write(String.format("Skewness: %.4f\n", objectSkewness));
            writer.write(String.format("Kurtosis: %.4f\n", objectKurtosis));
            writer.write("\n");

            writer.write(String.format("Ratio of mean times (reflect/object): %.2f\n", ratio));
            // Poprawione: dodano trzeci argument do formatu
            writer.write(String.format("Performance difference: %s implementation is %.2f times faster than %s\n",
                    (ratio > 1) ? "Object" : "Reflection",
                    (ratio > 1) ? ratio : 1 / ratio,
                    (ratio > 1) ? "Reflection" : "Object"));
            writer.write("\n");
            writer.write("Kind of Matrix: " + kindOfMatrix + "\n");
        } catch (IOException e) {
            System.err.println("Error saving statistics to file: " + e.getMessage());
        }

        return new StatisticsResult(
                operation, dimension,
                reflectMean, reflectMedian, reflectStd,
                reflectQ1, reflectQ3, reflectIQR, reflectCV,
                reflectSkewness, reflectKurtosis,
                objectMean, objectMedian, objectStd,
                objectQ1, objectQ3, objectIQR, objectCV,
                objectSkewness, objectKurtosis,
                ratio,
                kindOfMatrix);
    }

    public  static void displayDetailedStatisticsByMatrixKind(List<StatisticsResult> aggregatedResults) {
        System.out.printf("\n===== AGREGOWANE STATYSTYKI =====");
        System.out.printf("%-10s %-5s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s\n",
                "Operacja", "Dim",
                "Ref_Mean(ns)", "Ref_Median(ns)", "Ref_StdDev(ns)", "Ref_Q1(ns)", "Ref_Q3(ns)", "Ref_IQR(ns)",
                "Ref_CV(%)", "Ref_Skew", "Ref_Kurt",
                "Obj_Mean(ns)", "Obj_Median(ns)", "Obj_StdDev(ns)", "Obj_Q1(ns)", "Obj_Q3(ns)", "Obj_IQR(ns)",
                "Obj_CV(%)", "Obj_Skew", "Obj_Kurt",
                "Ratio" , "Kind od matrix");


        for (StatisticsResult sr : aggregatedResults) {
            System.out.printf("%-10s %-5d %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.4f %-15.4f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.4f %-15.4f %-15.2f %s\n",
                    sr.operation,
                    sr.dimension,
                    // Statystyki refleksyjne
                    sr.reflectMean,
                    sr.reflectMedian,
                    sr.reflectStdDev,
                    sr.reflectQ1,
                    sr.reflectQ3,
                    sr.reflectIQR,
                    sr.reflectCV,
                    sr.reflectSkewness,
                    sr.reflectKurtosis,
                    // Statystyki obiektowe
                    sr.objectMean,
                    sr.objectMedian,
                    sr.objectStdDev,
                    sr.objectQ1,
                    sr.objectQ3,
                    sr.objectIQR,
                    sr.objectCV,
                    sr.objectSkewness,
                    sr.objectKurtosis,
                    // Dodatkowe pole
                    sr.ratio , sr.kindOfMatrix.toString());
        }

        System.out.println("==================================\n");
    }


    public static void writeDetailedStatisticsByMatrixKindToFile(List<StatisticsResult> aggregatedResults, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.printf("\n===== AGREGOWANE STATYSTYKI =====\n");
            writer.printf("%-10s %-5s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s\n",
                    "Operacja", "Dim",
                    "Ref_Mean(ns)", "Ref_Median(ns)", "Ref_StdDev(ns)", "Ref_Q1(ns)", "Ref_Q3(ns)", "Ref_IQR(ns)",
                    "Ref_CV(%)", "Ref_Skew", "Ref_Kurt",
                    "Obj_Mean(ns)", "Obj_Median(ns)", "Obj_StdDev(ns)", "Obj_Q1(ns)", "Obj_Q3(ns)", "Obj_IQR(ns)",
                    "Obj_CV(%)", "Obj_Skew", "Obj_Kurt",
                    "Ratio", "Kind od matrix");

            for (StatisticsResult sr : aggregatedResults) {
                writer.printf("%-10s %-5d %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.4f %-15.4f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.4f %-15.4f %-15.2f %s\n",
                        sr.operation,
                        sr.dimension,
                        // Statystyki refleksyjne
                        sr.reflectMean,
                        sr.reflectMedian,
                        sr.reflectStdDev,
                        sr.reflectQ1,
                        sr.reflectQ3,
                        sr.reflectIQR,
                        sr.reflectCV,
                        sr.reflectSkewness,
                        sr.reflectKurtosis,
                        // Statystyki obiektowe
                        sr.objectMean,
                        sr.objectMedian,
                        sr.objectStdDev,
                        sr.objectQ1,
                        sr.objectQ3,
                        sr.objectIQR,
                        sr.objectCV,
                        sr.objectSkewness,
                        sr.objectKurtosis,
                        // Dodatkowe pole
                        sr.ratio, sr.kindOfMatrix.toString());
            }

            writer.printf("==================================\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//        createDirectoriesIfNotExists(OUTPUT_DIR + "statistic/");

    public static void saveStatisticsByOperation(String chartsDir, List<StatisticsResult> aggregatedResults , String whatKind) {
        String filename = chartsDir + "statistic/" + "statistics_by_operation.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("======================================\n\n");

            // Grupowanie wyników według wymiaru
            Map<Integer, List<StatisticsResult>> grouped = aggregatedResults.stream()
                    .collect(Collectors.groupingBy(sr -> sr.dimension));

            // Iteracja po każdej grupie (dla danego wymiaru)
            for (Integer dim : grouped.keySet()) {
                writer.write("Wymiar: " + dim + "\n");
                writer.write("--------------------------------------------------\n");
                for (StatisticsResult sr : grouped.get(dim)) {
                    writer.write("Operacja: " + sr.operation + "\n");

                    // Statystyki dla implementacji refleksyjnej
                    writer.write(String.format("Ref: Mean=%.2f ns, Median=%.2f ns, StdDev=%.2f ns, Q1=%.2f ns, Q3=%.2f ns, IQR=%.2f ns, CV=%.2f%%\n",
                            sr.reflectMean, sr.reflectMedian, sr.reflectStdDev, sr.reflectQ1, sr.reflectQ3, sr.reflectIQR, sr.reflectCV));
                    writer.write(String.format("     Skewness=%.4f, Kurtosis=%.4f\n",
                            sr.reflectSkewness, sr.reflectKurtosis));

                    // Statystyki dla implementacji obiektowej
                    writer.write(String.format("Obj: Mean=%.2f ns, Median=%.2f ns, StdDev=%.2f ns, Q1=%.2f ns, Q3=%.2f ns, IQR=%.2f ns, CV=%.2f%%\n",
                            sr.objectMean, sr.objectMedian, sr.objectStdDev, sr.objectQ1, sr.objectQ3, sr.objectIQR, sr.objectCV));
                    writer.write(String.format("     Skewness=%.4f, Kurtosis=%.4f\n",
                            sr.objectSkewness, sr.objectKurtosis));


                    if (whatKind.equals("matrix") ) {
                        // Dodatkowe informacje
                        writer.write(String.format("Ratio: %.2f, KindOfMatrix: %s\n", sr.ratio, sr.kindOfMatrix));
                        writer.write("--------------------------------------------------\n");
                    } else if (whatKind.equals("vector")) {
                        // Dodatkowe informacje
                        writer.write(String.format("Ratio: %.2f, KindOfMatrix: %s\n", sr.ratio, sr.kindOfVector));
                        writer.write("--------------------------------------------------\n");

                    } else if (whatKind.equals("bubble")) {

                        writer.write(String.format("Ratio: %.2f, KindOfMatrix: %s\n", sr.ratio, sr.kindOfBubbleSort));
                        writer.write("--------------------------------------------------\n");

                    }

                }
                writer.write("\n");
            }
           // System.out.println("Statystyki pogrupowane zapisano do pliku: " + filename);
        } catch (IOException e) {
            System.err.println("Błąd zapisu statystyk pogrupowanych: " + e.getMessage());
        }
    }



    public static void writeDetailedStatisticsByOperationToFile(List<StatisticsResult> aggregatedResults,
                                                                String operation, String whatKind, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.printf("\n===== AGREGOWANE STATYSTYKI DLA OPERACJI: %s =====\n", operation , "\n");
            writer.printf("%-10s %-5s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s\n",
                    "Operacja", "Dim",
                    "Ref_Mean(ns)", "Ref_Median(ns)", "Ref_StdDev(ns)", "Ref_Q1(ns)", "Ref_Q3(ns)", "Ref_IQR(ns)",
                    "Ref_CV(%)", "Ref_Skew", "Ref_Kurt",
                    "Obj_Mean(ns)", "Obj_Median(ns)", "Obj_StdDev(ns)", "Obj_Q1(ns)", "Obj_Q3(ns)", "Obj_IQR(ns)",
                    "Obj_CV(%)", "Obj_Skew", "Obj_Kurt",
                    "Ratio", "Kind of Matrix");

            // Filtrowanie wyników na podstawie operacji
            for (StatisticsResult sr : aggregatedResults) {
                if (sr.operation.equalsIgnoreCase(operation) && whatKind.equals("matrix")) {
                    writer.printf("%-10s %-5d %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.4f %-15.4f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.4f %-15.4f %-15.2f %s\n",
                            sr.operation,
                            sr.dimension,
                            // Statystyki refleksyjne
                            sr.reflectMean,
                            sr.reflectMedian,
                            sr.reflectStdDev,
                            sr.reflectQ1,
                            sr.reflectQ3,
                            sr.reflectIQR,
                            sr.reflectCV,
                            sr.reflectSkewness,
                            sr.reflectKurtosis,
                            // Statystyki obiektowe
                            sr.objectMean,
                            sr.objectMedian,
                            sr.objectStdDev,
                            sr.objectQ1,
                            sr.objectQ3,
                            sr.objectIQR,
                            sr.objectCV,
                            sr.objectSkewness,
                            sr.objectKurtosis,
                            // Dodatkowe pole
                            sr.ratio,
                            sr.kindOfMatrix.toString());
                } else if (sr.operation.equalsIgnoreCase(operation) && whatKind.equals("vector")) {
                    writer.printf("%-10s %-5d %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.4f %-15.4f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.4f %-15.4f %-15.2f %s\n",
                            sr.operation,
                            sr.dimension,
                            // Statystyki refleksyjne
                            sr.reflectMean,
                            sr.reflectMedian,
                            sr.reflectStdDev,
                            sr.reflectQ1,
                            sr.reflectQ3,
                            sr.reflectIQR,
                            sr.reflectCV,
                            sr.reflectSkewness,
                            sr.reflectKurtosis,
                            // Statystyki obiektowe
                            sr.objectMean,
                            sr.objectMedian,
                            sr.objectStdDev,
                            sr.objectQ1,
                            sr.objectQ3,
                            sr.objectIQR,
                            sr.objectCV,
                            sr.objectSkewness,
                            sr.objectKurtosis,
                            // Dodatkowe pole
                            sr.ratio,
                            sr.kindOfVector.toString());
                } else if (sr.operation.equalsIgnoreCase(operation) && whatKind.equals("bubble")) {
                    writer.printf("%-10s %-5d %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.4f %-15.4f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.4f %-15.4f %-15.2f %s\n",
                            sr.operation,
                            sr.dimension,
                            // Statystyki refleksyjne
                            sr.reflectMean,
                            sr.reflectMedian,
                            sr.reflectStdDev,
                            sr.reflectQ1,
                            sr.reflectQ3,
                            sr.reflectIQR,
                            sr.reflectCV,
                            sr.reflectSkewness,
                            sr.reflectKurtosis,
                            // Statystyki obiektowe
                            sr.objectMean,
                            sr.objectMedian,
                            sr.objectStdDev,
                            sr.objectQ1,
                            sr.objectQ3,
                            sr.objectIQR,
                            sr.objectCV,
                            sr.objectSkewness,
                            sr.objectKurtosis,
                            // Dodatkowe pole
                            sr.ratio,
                            sr.kindOfBubbleSort.toString());
                }
            }

            writer.printf("==================================\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void displayDetailedStatisticsByOperation(List<StatisticsResult> aggregatedResults, String operation , String whatKind) {

        System.out.printf("\n===== AGREGOWANE STATYSTYKI DLA OPERACJI: " + operation + " =====");
        System.out.printf("%-10s %-5s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s\n",
                "Operacja", "Dim",
                "Ref_Mean(ns)", "Ref_Median(ns)", "Ref_StdDev(ns)", "Ref_Q1(ns)", "Ref_Q3(ns)", "Ref_IQR(ns)",
                "Ref_CV(%)", "Ref_Skew", "Ref_Kurt",
                "Obj_Mean(ns)", "Obj_Median(ns)", "Obj_StdDev(ns)", "Obj_Q1(ns)", "Obj_Q3(ns)", "Obj_IQR(ns)",
                "Obj_CV(%)", "Obj_Skew", "Obj_Kurt",
                "Ratio", "Kind of Matrix");

        // Filtrowanie wyników na podstawie operacji
        for (StatisticsResult sr : aggregatedResults) {
            if (sr.operation.equalsIgnoreCase(operation) && whatKind.equals("matrix")) {
                System.out.printf("%-10s %-5d %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.4f %-15.4f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.4f %-15.4f %-15.2f %s\n",
                        sr.operation,
                        sr.dimension,
                        // Statystyki refleksyjne
                        sr.reflectMean,
                        sr.reflectMedian,
                        sr.reflectStdDev,
                        sr.reflectQ1,
                        sr.reflectQ3,
                        sr.reflectIQR,
                        sr.reflectCV,
                        sr.reflectSkewness,
                        sr.reflectKurtosis,
                        // Statystyki obiektowe
                        sr.objectMean,
                        sr.objectMedian,
                        sr.objectStdDev,
                        sr.objectQ1,
                        sr.objectQ3,
                        sr.objectIQR,
                        sr.objectCV,
                        sr.objectSkewness,
                        sr.objectKurtosis,
                        // Dodatkowe pole
                        sr.ratio,
                        sr.kindOfMatrix.toString());
            } else if (sr.operation.equalsIgnoreCase(operation) && whatKind.equals("vector")) {
                System.out.printf("%-10s %-5d %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.4f %-15.4f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.4f %-15.4f %-15.2f %s\n",
                        sr.operation,
                        sr.dimension,
                        // Statystyki refleksyjne
                        sr.reflectMean,
                        sr.reflectMedian,
                        sr.reflectStdDev,
                        sr.reflectQ1,
                        sr.reflectQ3,
                        sr.reflectIQR,
                        sr.reflectCV,
                        sr.reflectSkewness,
                        sr.reflectKurtosis,
                        // Statystyki obiektowe
                        sr.objectMean,
                        sr.objectMedian,
                        sr.objectStdDev,
                        sr.objectQ1,
                        sr.objectQ3,
                        sr.objectIQR,
                        sr.objectCV,
                        sr.objectSkewness,
                        sr.objectKurtosis,
                        // Dodatkowe pole
                        sr.ratio,
                        sr.kindOfVector.toString());
            }

            else if (sr.operation.equalsIgnoreCase(operation) && whatKind.equals("bubble")) {
                System.out.printf("%-10s %-5d %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.4f %-15.4f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.2f %-15.4f %-15.4f %-15.2f %s\n",
                        sr.operation,
                        sr.dimension,
                        // Statystyki refleksyjne
                        sr.reflectMean,
                        sr.reflectMedian,
                        sr.reflectStdDev,
                        sr.reflectQ1,
                        sr.reflectQ3,
                        sr.reflectIQR,
                        sr.reflectCV,
                        sr.reflectSkewness,
                        sr.reflectKurtosis,
                        // Statystyki obiektowe
                        sr.objectMean,
                        sr.objectMedian,
                        sr.objectStdDev,
                        sr.objectQ1,
                        sr.objectQ3,
                        sr.objectIQR,
                        sr.objectCV,
                        sr.objectSkewness,
                        sr.objectKurtosis,
                        // Dodatkowe pole
                        sr.ratio,
                        sr.kindOfBubbleSort.toString());
            }
        }

        System.out.printf("==================================\n");
    }


}
