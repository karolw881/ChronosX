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

}
