package org.magister.helper;

import org.magister.matrix.KindOfMatrix;
import org.magister.vector.KindOfVector;

public  class StatisticsResult {
    public String operation;
    public  int dimension;
    public   double reflectMean;
     public   double reflectMedian;
     public long reflectMode;
     public double reflectStdDev;
     public double objectMean;
     public double objectMedian;
     public long objectMode;
     public double objectStdDev;
     public double ratio;
     public KindOfMatrix kindOfMatrix;
     public KindOfVector kindOfVector;


     public StatisticsResult(String operation, int dimension, double reflectMean, double reflectMedian, long reflectMode,
                            double reflectStdDev, double objectMean, double objectMedian, long objectMode,
                            double objectStdDev, double ratio ) {
        this.operation = operation;
        this.dimension = dimension;
        this.reflectMean = reflectMean;
        this.reflectMedian = reflectMedian;
        this.reflectMode = reflectMode;
        this.reflectStdDev = reflectStdDev;
        this.objectMean = objectMean;
        this.objectMedian = objectMedian;
        this.objectMode = objectMode;
        this.objectStdDev = objectStdDev;
        this.ratio = ratio;
    }

    public StatisticsResult(String operation, int dimension, double reflectMean, double reflectMedian, long reflectMode,
                            double reflectStdDev, double objectMean, double objectMedian, long objectMode,
                            double objectStdDev, double ratio , KindOfMatrix kindOfMatrix ) {
        this.operation = operation;
        this.dimension = dimension;
        this.reflectMean = reflectMean;
        this.reflectMedian = reflectMedian;
        this.reflectMode = reflectMode;
        this.reflectStdDev = reflectStdDev;
        this.objectMean = objectMean;
        this.objectMedian = objectMedian;
        this.objectMode = objectMode;
        this.objectStdDev = objectStdDev;
        this.ratio = ratio;
        this.kindOfMatrix = kindOfMatrix;
    }
    public StatisticsResult(String operation, int dimension, double reflectMean, double reflectMedian, long reflectMode,
                            double reflectStdDev, double objectMean, double objectMedian, long objectMode,
                            double objectStdDev, double ratio , KindOfVector kindOfVector) {
        this.operation = operation;
        this.dimension = dimension;
        this.reflectMean = reflectMean;
        this.reflectMedian = reflectMedian;
        this.reflectMode = reflectMode;
        this.reflectStdDev = reflectStdDev;
        this.objectMean = objectMean;
        this.objectMedian = objectMedian;
        this.objectMode = objectMode;
        this.objectStdDev = objectStdDev;
        this.ratio = ratio;
        this.kindOfVector = kindOfVector;
    }
}
