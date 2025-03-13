package org.magister.matrix;



import java.util.Random;
import org.magister.helper.NumberOperations;

/**
 * Generator macierzy generycznych.
 * Klasa umożliwia tworzenie różnych rodzajów macierzy, określonych przez enum KindOfMatrix,
 * dla typów rozszerzających Number.
 *
 * Uwaga: Zakładamy, że przekazany NumberOperations<T> udostępnia metody:
 * - T valueOf(int value) – konwersja liczby int do typu T,
 * - T zero() – zwraca wartość zero dla typu T,
 * - T one() – zwraca wartość jeden dla typu T,
 * - oraz metody arytmetyczne: add, subtract, multiply, divide.
 */
public class MatrixGenerator<T extends Number> {

    private final NumberOperations<T> operations;

    public MatrixGenerator(NumberOperations<T> operations) {
        this.operations = operations;
    }

    /**
     * Generuje macierz na podstawie przekazanego typu z enuma KindOfMatrix.
     *
     * @param kind rodzaj macierzy
     * @param dimension wymiar macierzy
     * @param seed ziarno dla generatora liczb losowych (jeśli dotyczy)
     * @return wygenerowana macierz generyczna
     */
    public Matrix<T> createMatrix(KindOfMatrix kind, int dimension, long seed) {
        switch (kind) {
            case RANDOM:
                return createRandomMatrix(dimension, seed);
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
                throw new IllegalArgumentException("Nieznany typ macierzy: " + kind);
        }
    }

    public Matrix<T> createRandomMatrix(int dimension, long seed) {
        Random randomLocal = new Random(seed);
        T[][] data = (T[][]) new Number[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                data[i][j] = operations.valueOf(randomLocal.nextInt(10));
            }
        }
        return new Matrix<>(data, operations);
    }

    public Matrix<T> createIdentityMatrix(int dimension) {
        T[][] data = (T[][]) new Number[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                data[i][j] = (i == j) ? operations.one() : operations.zero();
            }
        }
        return new Matrix<>(data, operations);
    }

    public Matrix<T> createDiagonalMatrix(int dimension, long seed) {
        Random randomLocal = new Random(seed);
        T[][] data = (T[][]) new Number[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                data[i][j] = (i == j) ? operations.valueOf(randomLocal.nextInt(10)) : operations.zero();
            }
        }
        return new Matrix<>(data, operations);
    }

    public Matrix<T> createSymmetricMatrix(int dimension, long seed) {
        Random randomLocal = new Random(seed);
        T[][] data = (T[][]) new Number[dimension][dimension];
        // Wypełniamy tylko górną (lub dolną) część macierzy i przypisujemy symetrycznie
        for (int i = 0; i < dimension; i++) {
            for (int j = i; j < dimension; j++) {
                T value = operations.valueOf(randomLocal.nextInt(10));
                data[i][j] = value;
                data[j][i] = value;
            }
        }
        return new Matrix<>(data, operations);
    }

    public Matrix<T> createAntisymmetricMatrix(int dimension, long seed) {
        Random randomLocal = new Random(seed);
        T[][] data = (T[][]) new Number[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            data[i][i] = operations.zero(); // przekątna musi być zerowa
            for (int j = i + 1; j < dimension; j++) {
                T value = operations.valueOf(randomLocal.nextInt(10));
                data[i][j] = value;
                // element [j][i] = -value, przyjmujemy, że można obliczyć jako zero - value
                data[j][i] = operations.subtract(operations.zero(), value);
            }
        }
        return new Matrix<>(data, operations);
    }

    public Matrix<T> createLowerTriangularMatrix(int dimension, long seed) {
        Random randomLocal = new Random(seed);
        T[][] data = (T[][]) new Number[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (j <= i) {
                    data[i][j] = operations.valueOf(randomLocal.nextInt(10));
                } else {
                    data[i][j] = operations.zero();
                }
            }
        }
        return new Matrix<>(data, operations);
    }

    public Matrix<T> createUpperTriangularMatrix(int dimension, long seed) {
        Random randomLocal = new Random(seed);
        T[][] data = (T[][]) new Number[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (j >= i) {
                    data[i][j] = operations.valueOf(randomLocal.nextInt(10));
                } else {
                    data[i][j] = operations.zero();
                }
            }
        }
        return new Matrix<>(data, operations);
    }
}
