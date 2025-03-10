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
    public static <T extends Number > Vector<T> performOperation(Vector<T> vector1, Vector<T> vector2, String operationName) {
        try {
            Method method = Vector.class.getMethod(operationName, Vector.class);
            Vector<T> result = (Vector<T>) method.invoke(vector1, vector2);
            return result;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Błąd podczas wykonywania operacji '" + operationName +
                    "' na wektorach za pomocą refleksji: " + e.getMessage(), e);
        }
    }

    /**
     * Wywołuje metodę dotProduct na wektorach za pomocą refleksji.
     *
     * @param vector1 pierwszy wektor
     * @param vector2 drugi wektor
     * @param <T>     typ elementów wektora
     * @return iloczyn skalarny jako obiekt typu T
     */
    public static <T extends Number> T calculateDotProduct(Vector<T> vector1, Vector<T> vector2) {
        try {
            Method method = Vector.class.getMethod("dotProduct", Vector.class);
            T result = (T) method.invoke(vector1, vector2);
            return result;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Błąd podczas obliczania iloczynu skalarnego wektorów za pomocą refleksji: "
                    + e.getMessage(), e);
        }
    }
}

