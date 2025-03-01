package org.magister.matrix;




import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// Klasa pomocnicza do wykonywania operacji na macierzach za pomocą refleksji
public class MatrixReflectionUtil {

    // Wykonanie operacji za pomocą refleksji
    public static <T extends Number> Matrix<T> performOperation(Matrix<T> matrix1, Matrix<T> matrix2, String operationName) {
        try {
            Method method = Matrix.class.getMethod(operationName, Matrix.class);
            Matrix<T> result = (Matrix<T>) method.invoke(matrix1, matrix2);
            return result;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Błąd podczas wykonywania operacji za pomocą refleksji: " + e.getMessage(), e);
        }
    }

    // Sprawdzenie odwracalności za pomocą refleksji
    public static <T extends Number> boolean checkInvertibility(Matrix<T> matrix) {
        try {
            Method method = Matrix.class.getMethod("isInvertible");
            return (boolean) method.invoke(matrix);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Błąd podczas sprawdzania odwracalności za pomocą refleksji: " + e.getMessage(), e);
        }
    }

    // Obliczenie wyznacznika za pomocą refleksji
    public static <T extends Number> T calculateDeterminant(Matrix<T> matrix) {
        try {
            Method method = Matrix.class.getMethod("determinant");
            T result = (T) method.invoke(matrix);
            return result;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Błąd podczas obliczania wyznacznika za pomocą refleksji: " + e.getMessage(), e);
        }
    }
}