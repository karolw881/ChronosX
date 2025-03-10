package org.magister.helper;

 public  class StatisticsResult {
    public String operation;
    public  int dimension;
    public   double genericMean;
     public   double  genericMedian;
     public long genericMode;
     public double genericStdDev;
     public double concreteMean;
     public double concreteMedian;
     public long concreteMode;
     public double concreteStdDev;
     public double ratio;

    public StatisticsResult(String operation, int dimension, double genericMean, double genericMedian, long genericMode,
                            double genericStdDev, double concreteMean, double concreteMedian, long concreteMode,
                            double concreteStdDev, double ratio) {
        this.operation = operation;
        this.dimension = dimension;
        this.genericMean = genericMean;
        this.genericMedian = genericMedian;
        this.genericMode = genericMode;
        this.genericStdDev = genericStdDev;
        this.concreteMean = concreteMean;
        this.concreteMedian = concreteMedian;
        this.concreteMode = concreteMode;
        this.concreteStdDev = concreteStdDev;
        this.ratio = ratio;
    }
}
