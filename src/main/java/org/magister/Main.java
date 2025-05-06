package org.magister;


import org.magister.bubbleSort.PerformanceBubbleSortConcreteReflectVsObject;
import org.magister.bubbleSort.PerformanceBubbleSortGenericReflectVsObject;
import org.magister.bubbleSort.PerformanceTestBubbleSort;
import org.magister.matrix.PerformanceTestMatrix;
import org.magister.matrix.PerformanceTestMatrixConcreteReflectionVsObject;
import org.magister.matrix.PerformanceTestMatrixGenericofReflectionVsObject;
import org.magister.vector.PerformanceTestVector;
import org.magister.vector.PerformanceTestVectorConcreteOfReflectionVsObject;
import org.magister.vector.PerformanceTestVectorGenericOfReflectionVsObject;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//TODO test

        /**
         * Concrete Bubble
         */

        System.out.println("Concrete Bubble");
        PerformanceBubbleSortConcreteReflectVsObject performanceBubbleSortConcreteReflectVsObject = new PerformanceBubbleSortConcreteReflectVsObject();
        performanceBubbleSortConcreteReflectVsObject.runTest();


        /**
         * Generic bubble
         */
        System.out.println("Generic Bubble");
        PerformanceBubbleSortGenericReflectVsObject performanceBubbleSortGenericReflectVsObject = new PerformanceBubbleSortGenericReflectVsObject();
        performanceBubbleSortGenericReflectVsObject.runTest();


        /**
         * Matrix Generic
         */

        System.out.println("Matrix Generic");
        PerformanceTestMatrixGenericofReflectionVsObject performanceTestMatrixGenericofReflectionVsObject = new PerformanceTestMatrixGenericofReflectionVsObject();
        performanceTestMatrixGenericofReflectionVsObject.runTest();


        /**
         * Matrix Concrete
         */

        System.out.println("Matrix Concrete");
        PerformanceTestMatrixConcreteReflectionVsObject performanceTestMatrixConcreteReflectionVsObject = new PerformanceTestMatrixConcreteReflectionVsObject();
        performanceTestMatrixConcreteReflectionVsObject.runTest();


        /**
         * Vector Generic
         */

        System.out.println("Vector Generic");
        PerformanceTestVectorGenericOfReflectionVsObject performanceTestVectorGenericOfReflectionVsObject = new PerformanceTestVectorGenericOfReflectionVsObject();
        performanceTestVectorGenericOfReflectionVsObject.runTest();


        /**
         * Vector Concrete
         */

        System.out.println("Vector Concrete");
        PerformanceTestVectorConcreteOfReflectionVsObject performanceTestVectorConcreteOfReflectionVsObject = new PerformanceTestVectorConcreteOfReflectionVsObject();
        performanceTestVectorConcreteOfReflectionVsObject.runTest();
    }
}
