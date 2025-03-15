package org.magister.vector;



import org.magister.helper.NumberOperations;

import java.util.Random;

public class VectorGenerator<T extends Number> {

    private final NumberOperations<T> operations;

    public VectorGenerator(NumberOperations<T> operations) {
        this.operations = operations;
    }

    /**
     * Generuje wektor na podstawie przekazanego typu z enuma KindOfVector.
     *
     * @param kind rodzaj wektora
     * @param dimension wymiar wektora
     * @param seed ziarno dla generatora liczb losowych
     * @return wygenerowany wektor generyczny
     */
    public Vector<T> createVector(KindOfVector kind, int dimension, long seed) {
        switch (kind) {
            case RANDOM:
                return createRandomVector(dimension, seed);
            case ZERO:
                return createZeroVector(dimension);
            case ONES:
                return createOnesVector(dimension);
            default:
                throw new IllegalArgumentException("Nieznany typ wektora: " + kind);
        }
    }

    // Generuje wektor losowy o podanym wymiarze
    public Vector<T> createRandomVector(int dimension, long seed) {
        Random random = new Random(seed);
        T[] data = (T[]) new Number[dimension];
        for (int i = 0; i < dimension; i++) {
            data[i] = operations.valueOf(random.nextInt(10));
        }
        return new Vector<>(data, operations);
    }

    // Generuje wektor zerowy
    public Vector<T> createZeroVector(int dimension) {
        T[] data = (T[]) new Number[dimension];
        for (int i = 0; i < dimension; i++) {
            data[i] = operations.zero();
        }
        return new Vector<>(data, operations);
    }

    // Generuje wektor z samymi jedynkami
    public Vector<T> createOnesVector(int dimension) {
        T[] data = (T[]) new Number[dimension];
        for (int i = 0; i < dimension; i++) {
            data[i] = operations.one();
        }
        return new Vector<>(data, operations);
    }
}

