package org.magister.vector;

import java.util.Random;


public class Vector1Generator {



    /**
     * Generuje wektor losowy o podanym wymiarze
     * @param dimension
     * @param seed
     *
     *
     * @return
     */

    public Vector1 createRandomVector1(int dimension, long seed) {
        Random random = new Random(seed);
        int[] data = new int[dimension];
        for (int i = 0; i < dimension; i++) {
            data[i] = random.nextInt(10); // przykładowo wartości od 0 do 9
        }
        return new Vector1(data);
    }

    /**
     *   Generuje wektor zerowy
     * @param dimension
     * @return
     */

    // Generuje wektor zerowy (wszystkie elementy są zerami)
    public Vector1 createZeroVector1(int dimension) {
        int[] data = new int[dimension]; // domyślnie inicjalizowane zerami
        return new Vector1(data);
    }


    /**
     *   // Generuje wektor, w którym wszystkie elementy są równe 1
     * @param dimension
     * @return
     */


    public Vector1 createOnesVector1(int dimension) {
        int[] data = new int[dimension];
        for (int i = 0; i < dimension; i++) {
            data[i] = 1;
        }
        return new Vector1(data);
    }

    public Vector1 createVector1(KindOfVector kind, int dimension, long seed) {
        switch (kind) {
            case RANDOM:
                return createRandomVector1(dimension, seed);
            case ZERO:
                return createZeroVector1(dimension);
            case ONES:
                return createOnesVector1(dimension);
            default:
                throw new IllegalArgumentException("Nieznany typ wektora: " + kind);
        }
    }


}
