package org.magister.bubbleSort;

import org.magister.helper.Numberxx;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BubbleSortReflectionUtil {
    public static <T extends Numberxx> BubbleSort<T> performOperationReflectBubble(BubbleSort<T> bubbleSort,
                                                                                   String operationName) {
        try {
            Method m = bubbleSort.getClass().getMethod(operationName);
            m.invoke(bubbleSort);
           return bubbleSort;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Błąd podczas wykonywania operacji '" + operationName +
                    "' na wektorach za pomocą refleksji: " + e.getMessage(), e);
        }
    }

    public static BubbleSort1 performOperationReflectBubbleConcrete(BubbleSort1 bubble1,
                                                             String sort) {
        try {
                  Method method = BubbleSort1.class.getMethod(sort);
                  BubbleSort1 result = (BubbleSort1) method.invoke(bubble1);
                  return result;
              } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                 throw new RuntimeException("Błąd podczas wykonywania operacji '" + sort +
                                        "' na wektorze za pomocą refleksji: " + e.getMessage(), e);
             }
         }


    }


