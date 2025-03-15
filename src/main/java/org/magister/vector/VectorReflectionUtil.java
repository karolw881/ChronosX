package org.magister.vector;





import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Klasa pomocnicza do wykonywania operacji na wektorach za pomocą refleksji.
 */
public class VectorReflectionUtil {

    /**
     * Wywołuje podaną operację (np. "addVector", "subtractVector2") na wektorach za pomocą refleksji.
     *
     * @param vector1       pierwszy wektor
     * @param vector2       drugi wektor
     * @param operationName nazwa metody operacji (musi przyjmować argument typu Vector)
     * @param <T>           typ elementów wektora
     * @return wynik operacji jako nowy wektor
     */
    public static <T extends Number > Vector<T> performOperationReflectVector(Vector<T> vector1, Vector<T> vector2, String operationName) {
        try {
            Method method = Vector.class.getMethod(operationName, Vector.class);
            Vector<T> result = (Vector<T>) method.invoke(vector1, vector2);
            return result;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Błąd podczas wykonywania operacji '" + operationName +
                    "' na wektorach za pomocą refleksji: " + e.getMessage(), e);
        }
    }


    public static Vector1 performOperationReflectVector1(Vector1 vector1, Vector1 vector2, String operation){
        try {
            Method method = Vector1.class.getMethod(operation, Vector.class);
            Vector1 result = (Vector1) method.invoke(vector1, vector2);
            return result;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Błąd podczas wykonywania operacji '" + operation +
                    "' na wektorach za pomocą refleksji: " + e.getMessage(), e);
        }
    }

}

