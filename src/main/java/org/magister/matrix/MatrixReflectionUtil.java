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

    public static  Matrix1 performOperation2(Matrix1 matrix1, Matrix1 matrix2, String operationName) {
        try {
            Method method = Matrix1.class.getMethod(operationName, Matrix1.class);
            Matrix1 result = (Matrix1) method.invoke(matrix1, matrix2);
            return result;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Błąd podczas wykonywania operacji za pomocą refleksji: " + e.getMessage(), e);
        }
    }


}