package org.magister;


import org.magister.bubbleSort.PerformanceTestBubbleSort;
import org.magister.matrix.PerformanceTestMatrix;
import org.magister.matrix.PerformanceTestMatrixConcreteReflectionVsObject;
import org.magister.matrix.PerformanceTestMatrixGenericofReflectionVsObject;
import org.magister.vector.PerformanceTestVector;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        PerformanceTestMatrix performanceTestMatrix = new PerformanceTestMatrix();
     //   performanceTestMatrix.performTests();


        PerformanceTestVector performanceTestVector = new PerformanceTestVector();
     ///   performanceTestVector.performTests();


        PerformanceTestBubbleSort performanceTestBubbleSort = new PerformanceTestBubbleSort();
       //  performanceTestBubbleSort.performTests();

        PerformanceTestMatrixGenericofReflectionVsObject performanceTestMatrixGenericofReflectionVsObject = new PerformanceTestMatrixGenericofReflectionVsObject();
        performanceTestMatrixGenericofReflectionVsObject.runTest();




        PerformanceTestMatrixConcreteReflectionVsObject performanceTestMatrixConcreteReflectionVsObject = new PerformanceTestMatrixConcreteReflectionVsObject();
       // performanceTestMatrixConcreteReflectionVsObject.runTest();






    }
}

