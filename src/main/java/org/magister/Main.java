package org.magister;


import org.magister.bubbleSort.PerformanceTestBubbleSort;
import org.magister.matrix.PerformanceTestMatrix;
import org.magister.matrix.PerformanceTestMatrixConcreteReflectionVsObject;
import org.magister.matrix.PerformanceTestMatrixGenericofReflectionVsObject;
import org.magister.vector.PerformanceTestVector;
import org.magister.vector.PerformanceTestVectorGenericOfReflectionVsObject;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {



        PerformanceTestBubbleSort performanceTestBubbleSort = new PerformanceTestBubbleSort();
       //     performanceTestBubbleSort.performTests();

        PerformanceTestMatrixGenericofReflectionVsObject performanceTestMatrixGenericofReflectionVsObject = new PerformanceTestMatrixGenericofReflectionVsObject();
      //  performanceTestMatrixGenericofReflectionVsObject.runTest();




        PerformanceTestMatrixConcreteReflectionVsObject performanceTestMatrixConcreteReflectionVsObject = new PerformanceTestMatrixConcreteReflectionVsObject();
      //  performanceTestMatrixConcreteReflectionVsObject.runTest();


        PerformanceTestVectorGenericOfReflectionVsObject performanceTestVectorGenericOfReflectionVsObject = new PerformanceTestVectorGenericOfReflectionVsObject();
        performanceTestVectorGenericOfReflectionVsObject.runTest();



    }
}

