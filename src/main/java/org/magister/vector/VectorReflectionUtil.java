package org.magister.vector;





import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
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


    /**W tym przypadku castowanie jest potrzebne ze względu na sposób działania refleksji w Javie. Chociaż typy generyczne zostały zaprojektowane, aby unikać castowania w normalnym kodzie, refleksja wprowadza pewne komplikacje. Wyjaśnię, dlaczego tak się dzieje:
     Główny powód to wymazywanie typów (type erasure) w Javie. Podczas kompilacji informacje o typach generycznych są usuwane, więc w czasie wykonania metoda invoke() nie ma informacji o typie generycznym. Dlatego:

     Metoda invoke() z klasy Method zwraca obiekt typu Object, niezależnie od rzeczywistego typu zwracanego przez wywoływaną metodę.
     W czasie wykonania JVM nie ma informacji o typie generycznym T, więc nie może automatycznie dokonać konwersji z Object na Vector<T>.
     Programista musi jawnie poinformować kompilator poprzez castowanie, że wynik wywołania metody powinien być traktowany jako Vector<T>.

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

    public static <T extends Number > int performOperationReflectVectorForDotProduct(Vector<T> vector1, Vector<T> vector2, String operationName) {
        try {
            Method method = Vector.class.getMethod(operationName, Vector.class);
            int result = (int) method.invoke(vector1, vector2);
            return result;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Błąd podczas wykonywania operacji '" + operationName +
                    "' na wektorach za pomocą refleksji: " + e.getMessage(), e);
        }
    }


    public static <T extends Number> Vector<T> performOperationReflectVectorForScalar(Vector<T> vector, Number scalar, String operationName) {
        try {
            Method method = Vector.class.getMethod(operationName, Number.class);
            Vector<T> result = (Vector<T>) method.invoke(vector, scalar);
            return result;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Błąd podczas wykonywania operacji '" + operationName +
                    "' na wektorze za pomocą refleksji: " + e.getMessage(), e);
        }
    }

    public static <T extends Number> Vector<T> performOperationReflectVectorForOpposite(Vector<T> vector,  String operationName) {
        try {
            Method method = Vector.class.getMethod(operationName);
            Vector<T> result = (Vector<T>) method.invoke(vector);
            return result;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Błąd podczas wykonywania operacji '" + operationName +
                    "' na wektorze za pomocą refleksji: " + e.getMessage(), e);
        }
    }






    public static Vector1 performOperationReflectVector1(Vector1 vector1, Vector1 vector2, String operation){
        try {
            Method method = Vector1.class.getMethod(operation, Vector1.class);
            Vector1 result = (Vector1) method.invoke(vector1, vector2);
            return result;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Błąd podczas wykonywania operacji '" + operation +
                    "' na wektorach za pomocą refleksji: " + e.getMessage(), e);
        }
    }

    public static int performOperationReflectVectorForDotProduct1(Vector1 vector1, Vector1 vector2, String operation) {
        try {
            Method method = Vector1.class.getMethod(operation, Vector1.class);
            int result = (int) method.invoke(vector1, vector2);
            return result;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Błąd podczas wykonywania operacji '" + operation +
                    "' na wektorach za pomocą refleksji: " + e.getMessage(), e);
        }
    }

    public static Vector1 performOperationReflectVectorForScalar1(Vector1 vector1, int i, String operation) {
        try {
            Method method = Vector1.class.getMethod(operation, int.class);
            Vector1 result = (Vector1) method.invoke(vector1, i);
            return result;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Błąd podczas wykonywania operacji '" + operation +
                    "' na wektorze za pomocą refleksji: " + e.getMessage(), e);

        }
    }

    public static Vector1 performOperationReflectVectorForOpposite1(Vector1 vector1, String operation) {
            try {
                Method method = Vector1.class.getMethod(operation);
                Vector1 result = (Vector1) method.invoke(vector1);
                return result;
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Błąd podczas wykonywania operacji '" + operation  +
                        "' na wektorze za pomocą refleksji: " + e.getMessage(), e);
            }
        }
}

