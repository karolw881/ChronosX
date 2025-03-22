package org.magister.vector;

import org.magister.helper.Numberxx;
import org.magister.helper.NumberxxOperations;

import java.util.Random;

public class VectorGenerator {

    private final NumberxxOperations operations;

    public VectorGenerator(NumberxxOperations operations) {
        this.operations = operations;
    }

    /**
     * Generuje wektor na podstawie przekazanego typu z enuma KindOfVector.
     *
     * @param kind rodzaj wektora
     * @param dimension wymiar wektora
     * @param seed ziarno dla generatora liczb losowych
     * @return wygenerowany wektor
     */
    public Vector<Numberxx> createVector(KindOfVector kind, int dimension, long seed) {
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
    public Vector<Numberxx> createRandomVector(int dimension, long seed) {
        Random random = new Random(seed);
        Numberxx[] data = new Numberxx[dimension];
        for (int i = 0; i < dimension; i++) {
            data[i] = operations.valueOf(random.nextInt(10));
        }
        return new Vector<>(data, operations);
    }

    // Generuje wektor zerowy
    public Vector<Numberxx> createZeroVector(int dimension) {
        Numberxx[] data = new Numberxx[dimension];
        for (int i = 0; i < dimension; i++) {
            data[i] = operations.zero();
        }
        return new Vector<>(data, operations);
    }

    // Generuje wektor z samymi jedynkami
    public Vector<Numberxx> createOnesVector(int dimension) {
        Numberxx[] data = new Numberxx[dimension];
        for (int i = 0; i < dimension; i++) {
            data[i] = operations.one();
        }
        return new Vector<>(data, operations);
    }
}