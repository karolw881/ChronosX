package org.magister;


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

        /**
         *  bubble  ogolny
         */

        PerformanceTestBubbleSort performanceTestBubbleSort = new PerformanceTestBubbleSort();
       //  performanceTestBubbleSort.performTests();


        /**
         * Generic bubble
         */
        PerformanceBubbleSortGenericReflectVsObject performanceBubbleSortGenericReflectVsObject = new PerformanceBubbleSortGenericReflectVsObject();
        performanceBubbleSortGenericReflectVsObject.performTestGenericBubbleSort();


        /**
         * Matrix Generic
         */


        PerformanceTestMatrixGenericofReflectionVsObject performanceTestMatrixGenericofReflectionVsObject = new PerformanceTestMatrixGenericofReflectionVsObject();
       // performanceTestMatrixGenericofReflectionVsObject.runTest();


        /**
         * Matrix Concrete
         */

        PerformanceTestMatrixConcreteReflectionVsObject performanceTestMatrixConcreteReflectionVsObject = new PerformanceTestMatrixConcreteReflectionVsObject();
       // performanceTestMatrixConcreteReflectionVsObject.runTest();


        /**
         * Vector Generic
         */

        PerformanceTestVectorGenericOfReflectionVsObject performanceTestVectorGenericOfReflectionVsObject = new PerformanceTestVectorGenericOfReflectionVsObject();
        //performanceTestVectorGenericOfReflectionVsObject.runTest();


        /**
         * Vector Concrete
         */
        PerformanceTestVectorConcreteOfReflectionVsObject performanceTestVectorConcreteOfReflectionVsObject = new PerformanceTestVectorConcreteOfReflectionVsObject();
        //performanceTestVectorConcreteOfReflectionVsObject.runTest();
    }
}

