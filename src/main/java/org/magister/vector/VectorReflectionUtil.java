package org.magister.vector;

import org.magister.helper.Numberxx;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Klasa pomocnicza do wykonywania operacji na wektorach za pomocą refleksji.
 */
public class VectorReflectionUtil extends Numberxx {

    public VectorReflectionUtil(int value) {
        super(value);
    }

    public static void performOperationReflectVectorForScalar(Vector<Numberxx> vector1, int i, String operation) {
        try {


            Method method = Vector.class.getMethod("multiplyByScalar", Numberxx.class);

            Vector<Numberxx> result = (Vector<Numberxx>) method.invoke(vector1, i);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            if (e instanceof InvocationTargetException && e.getCause() != null) {
                throw new RuntimeException("Błąd podczas wykonywania operacji '" + operation +
                        "' na wektorze za pomocą refleksji: " + e.getCause().getMessage(), e);
            } else {
                throw new RuntimeException("Błąd podczas wykonywania operacji '" + operation +
                        "' na wektorze za pomocą refleksji: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public int intValue() {
        return super.intValue();
    }

    @Override
    public long longValue() {
        return super.longValue();
    }

    @Override
    public float floatValue() {
        return super.floatValue();
    }

    @Override
    public double doubleValue() {
        return super.doubleValue();
    }

    public static <T extends Numberxx> Vector<T> performOperationReflectVector(Vector<T> vector1, Vector<T> vector2, String operationName) {
        try {
            Method method = Vector.class.getMethod(operationName, Vector.class);
            Vector<T> result = (Vector<T>) method.invoke(vector1, vector2);
            return result;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Błąd podczas wykonywania operacji '" + operationName +
                    "' na wektorach za pomocą refleksji: " + e.getMessage(), e);
        }
    }

    public static <T extends Numberxx> Numberxx performOperationReflectVectorForDotProduct(Vector<T> vector1, Vector<T> vector2, String operationName) {
        try {
            Method method = Vector.class.getMethod(operationName, Vector.class);
            Numberxx result = (Numberxx) method.invoke(vector1, vector2);
            return result;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Błąd podczas wykonywania operacji '" + operationName +
                    "' na wektorach za pomocą refleksji: " + e.getMessage(), e);
        }
    }



    public static <T extends Numberxx> Vector<T> performOperationReflectVectorForOpposite(Vector<T> vector, String operationName) {
        try {
            Method method = Vector.class.getMethod(operationName);
            Vector<T> result = (Vector<T>) method.invoke(vector);
            return result;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Błąd podczas wykonywania operacji '" + operationName +
                    "' na wektorze za pomocą refleksji: " + e.getMessage(), e);
        }
    }

    public static Vector1 performOperationReflectVector1(Vector1 vector1, Vector1 vector2, String operation) {
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

    public static Vector1 performOperationReflectVectorForScalar1(Vector1 vector1, int scalar, String operation) {
        try {

            Method method = Vector1.class.getMethod(operation, int.class);
            Vector1 result = (Vector1) method.invoke(vector1, scalar);
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
            throw new RuntimeException("Błąd podczas wykonywania operacji '" + operation +
                    "' na wektorze za pomocą refleksji: " + e.getMessage(), e);
        }
    }
    // In your reflection utility class
    public static <T extends Numberxx> Vector<T> performOperationReflectVectorForScalar(Vector<T> vector, Numberxx scalar, String operationName) {
        try {
            // Look for an instance method that takes one Numberxx parameter
            Method method = Vector.class.getMethod(operationName, Numberxx.class);
            // Invoke the method on the vector instance passing the scalar

            Vector<T> result = (Vector<T>) method.invoke(vector, scalar);
            return result;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            if (e instanceof InvocationTargetException && e.getCause() != null) {
                throw new RuntimeException("Błąd podczas wykonywania operacji '" + operationName +
                        "' na wektorze za pomocą refleksji: " + e.getCause().getMessage(), e);
            } else {
                throw new RuntimeException("Błąd podczas wykonywania operacji '" + operationName +
                        "' na wektorze za pomocą refleksji: " + e.getMessage(), e);
            }
        }
    }
}