package org.magister.matrix;

import java.util.Random;

public class Matrix1Generator {

    // Generuje macierz losową o podanym rozmiarze
    public Matrix1 createRandomMatrix1(int dimension, long seed) {
        Random randomLocal = new Random(seed);
        int[][] data = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                data[i][j] = randomLocal.nextInt(10);
            }
        }
        return new Matrix1(data);
    }

    // Generuje macierz jednostkową (tożsamościową)
    public Matrix1 createIdentityMatrix(int dimension) {
        int[][] data = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            data[i][i] = 1;
        }
        return new Matrix1(data);
    }

    // Generuje macierz diagonalną z losowymi elementami na przekątnej
    public Matrix1 createDiagonalMatrix(int dimension, long seed) {
        Random randomLocal = new Random(seed);
        int[][] data = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            data[i][i] = randomLocal.nextInt(10);
        }
        return new Matrix1(data);
    }

    // Generuje macierz symetryczną: elementy [i][j] i [j][i] są takie same
    public Matrix1 createSymmetricMatrix(int dimension, long seed) {
        Random randomLocal = new Random(seed);
        int[][] data = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = i; j < dimension; j++) {
                int value = randomLocal.nextInt(10);
                data[i][j] = value;
                data[j][i] = value;
            }
        }
        return new Matrix1(data);
    }

    // Generuje macierz antysymetryczną (skew-symetryczną)
    // Warunkiem jest, aby dla każdego i, j: a[i][j] = -a[j][i] oraz a[i][i] = 0
    public Matrix1 createAntisymmetricMatrix(int dimension, long seed) {
        Random randomLocal = new Random(seed);
        int[][] data = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            data[i][i] = 0; // przekątna musi być zerowa
            for (int j = i + 1; j < dimension; j++) {
                int value = randomLocal.nextInt(10);
                data[i][j] = value;
                data[j][i] = -value;
            }
        }
        return new Matrix1(data);
    }

    // Generuje macierz trójkątną dolną – elementy powyżej przekątnej są zerowe
    public Matrix1 createLowerTriangularMatrix(int dimension, long seed) {
        Random randomLocal = new Random(seed);
        int[][] data = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j <= i; j++) {
                data[i][j] = randomLocal.nextInt(10);
            }
            for (int j = i + 1; j < dimension; j++) {
                data[i][j] = 0;
            }
        }
        return new Matrix1(data);
    }

    // Generuje macierz trójkątną górną – elementy poniżej przekątnej są zerowe
    public Matrix1 createUpperTriangularMatrix(int dimension, long seed) {
        Random randomLocal = new Random(seed);
        int[][] data = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < i; j++) {
                data[i][j] = 0;
            }
            for (int j = i; j < dimension; j++) {
                data[i][j] = randomLocal.nextInt(10);
            }
        }
        return new Matrix1(data);
    }

    /**
     * Metoda generująca macierz na podstawie przekazanego typu.
     * @param kind typ macierzy określony enumem KindOfMatrix
     * @param dimension wymiar macierzy
     * @param seed ziarno dla losowości (jeśli dotyczy)
     * @return wygenerowana macierz
     */
    public Matrix1 createMatrix1(KindOfMatrix kind, int dimension, long seed) {
        switch (kind) {
            case RANDOM:
                return createRandomMatrix1(dimension, seed);
            case IDENTITY:
                return createIdentityMatrix(dimension);
            case DIAGONAL:
                return createDiagonalMatrix(dimension, seed);
            case SYMMETRIC:
                return createSymmetricMatrix(dimension, seed);
            case ANTISYMMETRIC:
                return createAntisymmetricMatrix(dimension, seed);
            case LOWER_TRIANGULAR:
                return createLowerTriangularMatrix(dimension, seed);
            case UPPER_TRIANGULAR:
                return createUpperTriangularMatrix(dimension, seed);
            default:
                throw new IllegalArgumentException("Nieznany rodzaj macierzy: " + kind);
        }
    }

}
