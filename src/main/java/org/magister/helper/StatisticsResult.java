package org.magister.helper;

import org.magister.bubbleSort.KindOfBubbleSort;
import org.magister.matrix.KindOfMatrix;
import org.magister.vector.KindOfVector;

public class StatisticsResult {
    public String operation;
    public int dimension;

    // Dla implementacji refleksyjnej
    public double reflectMean;
    public double reflectMedian;

    public double reflectStdDev;
    public double reflectQ1;
    public double reflectQ3;
    public double reflectIQR;
    public double reflectCV;
    public double reflectSkewness;
    public double reflectKurtosis;


    // Dla implementacji obiektowej
    public double objectMean;
    public double objectMedian;

    public double objectStdDev;
    public double objectQ1;
    public double objectQ3;
    public double objectIQR;
    public double objectCV;
    public double objectSkewness;
    public double objectKurtosis;


    public double ratio;
    public KindOfMatrix kindOfMatrix;
    public KindOfVector kindOfVector;
    public KindOfBubbleSort kindOfBubbleSort;

    public StatisticsResult(String operation, int dimension,
                            double reflectMean, double reflectMedian,  double reflectStdDev,
                            double reflectQ1, double reflectQ3, double reflectIQR, double reflectCV,
                            double reflectSkewness, double reflectKurtosis,
                            double objectMean, double objectMedian,  double objectStdDev,
                            double objectQ1, double objectQ3, double objectIQR, double objectCV,
                            double objectSkewness, double objectKurtosis,
                            double ratio,
                            KindOfBubbleSort kindOfBubbleSort) {
        this.operation = operation;
        this.dimension = dimension;

        this.reflectMean = reflectMean;
        this.reflectMedian = reflectMedian;
        this.reflectStdDev = reflectStdDev;
        this.reflectQ1 = reflectQ1;
        this.reflectQ3 = reflectQ3;
        this.reflectIQR = reflectIQR;
        this.reflectCV = reflectCV;
        this.reflectSkewness = reflectSkewness;
        this.reflectKurtosis = reflectKurtosis;


        this.objectMean = objectMean;
        this.objectMedian = objectMedian;
        this.objectStdDev = objectStdDev;
        this.objectQ1 = objectQ1;
        this.objectQ3 = objectQ3;
        this.objectIQR = objectIQR;
        this.objectCV = objectCV;
        this.objectSkewness = objectSkewness;
        this.objectKurtosis = objectKurtosis;


        this.ratio = ratio;
        this.kindOfBubbleSort = kindOfBubbleSort;
    }

    public StatisticsResult(String operation, int dimension,
                            double reflectMean, double reflectMedian, double reflectStdDev,
                            double reflectQ1, double reflectQ3, double reflectIQR, double reflectCV,
                            double reflectSkewness, double reflectKurtosis,
                            double objectMean, double objectMedian,  double objectStdDev,
                            double objectQ1, double objectQ3, double objectIQR, double objectCV,
                            double objectSkewness, double objectKurtosis,
                            double ratio,
                         KindOfVector kindOfVector) {
        this.operation = operation;
        this.dimension = dimension;

        this.reflectMean = reflectMean;
        this.reflectMedian = reflectMedian;
        this.reflectStdDev = reflectStdDev;
        this.reflectQ1 = reflectQ1;
        this.reflectQ3 = reflectQ3;
        this.reflectIQR = reflectIQR;
        this.reflectCV = reflectCV;
        this.reflectSkewness = reflectSkewness;
        this.reflectKurtosis = reflectKurtosis;
        this.objectMean = objectMean;
        this.objectMedian = objectMedian;
        this.objectStdDev = objectStdDev;
        this.objectQ1 = objectQ1;
        this.objectQ3 = objectQ3;
        this.objectIQR = objectIQR;
        this.objectCV = objectCV;
        this.objectSkewness = objectSkewness;
        this.objectKurtosis = objectKurtosis;
        this.ratio = ratio;
        this.kindOfVector = kindOfVector;
    }
    public StatisticsResult(String operation, int dimension,
                            double reflectMean, double reflectMedian, double reflectStdDev,
                            double reflectQ1, double reflectQ3, double reflectIQR, double reflectCV,
                            double reflectSkewness, double reflectKurtosis,
                            double objectMean, double objectMedian, double objectStdDev,
                            double objectQ1, double objectQ3, double objectIQR, double objectCV,
                            double objectSkewness, double objectKurtosis,
                            double ratio,
                            KindOfMatrix kindOfMatrix) {
        this.operation = operation;
        this.dimension = dimension;

        this.reflectMean = reflectMean;
        this.reflectMedian = reflectMedian;
        this.reflectStdDev = reflectStdDev;
        this.reflectQ1 = reflectQ1;
        this.reflectQ3 = reflectQ3;
        this.reflectIQR = reflectIQR;
        this.reflectCV = reflectCV;
        this.reflectSkewness = reflectSkewness;
        this.reflectKurtosis = reflectKurtosis;
        this.objectMean = objectMean;
        this.objectMedian = objectMedian;
        this.objectStdDev = objectStdDev;
        this.objectQ1 = objectQ1;
        this.objectQ3 = objectQ3;
        this.objectIQR = objectIQR;
        this.objectCV = objectCV;
        this.objectSkewness = objectSkewness;
        this.objectKurtosis = objectKurtosis;
        this.ratio = ratio;
        this.kindOfMatrix = kindOfMatrix;

    }

}
