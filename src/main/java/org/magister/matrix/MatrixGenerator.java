package org.magister.matrix;



import java.util.Random;

import org.magister.helper.NumberOperations;
import org.magister.helper.NumberxxOperations;
import org.magister.helper.Numberxx;
import org.magister.helper.NumberxxOperations;

/**
 * Generator macierzy generycznych.
 * Klasa umożliwia tworzenie różnych rodzajów macierzy, określonych przez enum KindOfMatrix,
 * dla typów rozszerzających Number.
 *
 * Uwaga: Zakładamy, że przekazany NumberxxOperations<T> udostępnia metody:
 * - T valueOf(int value) – konwersja liczby int do typu T,
 * - T zero() – zwraca wartość zero dla typu T,
 * - T one() – zwraca wartość jeden dla typu T,
 * - oraz metody arytmetyczne: add, subtract, multiply, divide.
 */
public class MatrixGenerator<T extends Numberxx> {

    private final NumberxxOperations operations;

    public MatrixGenerator(NumberxxOperations operations) {
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
                return (Matrix<T>) createRandomMatrix(dimension, seed);
            case IDENTITY:
                return (Matrix<T>) createIdentityMatrix(dimension);
            case DIAGONAL:
                return (Matrix<T>) createDiagonalMatrix(dimension, seed);
            case SYMMETRIC:
                return (Matrix<T>) createSymmetricMatrix(dimension, seed);
            case ANTISYMMETRIC:
                return (Matrix<T>) createAntisymmetricMatrix(dimension, seed);
            case LOWER_TRIANGULAR:
                return (Matrix<T>) createLowerTriangularMatrix(dimension, seed);
            case UPPER_TRIANGULAR:
                return (Matrix<T>) createUpperTriangularMatrix(dimension, seed);
            default:
                throw new IllegalArgumentException("Nieznany typ macierzy: " + kind);
        }
    }

    protected Matrix<Numberxx> createRandomMatrix(int dimension, long seed) {
        Random randomLocal = new Random(seed);
        Numberxx[][] data = new Numberxx[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                data[i][j] = Numberxx.valueOf(randomLocal.nextInt(10));
            }
        }
        return new Matrix<>(data, new NumberxxOperations());
    }
    public Matrix<Numberxx> createIdentityMatrix(int dimension) {
        Numberxx[][] data = (T[][]) new Number[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                data[i][j] = (i == j) ? operations.one() : operations.zero();
            }
        }
        return new Matrix<>(data, operations);
    }

    public Matrix<Numberxx> createDiagonalMatrix(int dimension, long seed) {
        Random randomLocal = new Random(seed);
        T[][] data = (T[][]) new Number[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                data[i][j] = (T) ((i == j) ? Numberxx.valueOf(randomLocal.nextInt(10)) : operations.zero());
            }
        }
        return new Matrix<>(data, operations);
    }

    public Matrix<Numberxx> createSymmetricMatrix(int dimension, long seed) {
        Random randomLocal = new Random(seed);
        T[][] data = (T[][]) new Number[dimension][dimension];
        // Wypełniamy tylko górną (lub dolną) część macierzy i przypisujemy symetrycznie
        for (int i = 0; i < dimension; i++) {
            for (int j = i; j < dimension; j++) {
                T value = (T) Numberxx.valueOf(randomLocal.nextInt(10));
                data[i][j] = value;
                data[j][i] = value;
            }
        }
        return new Matrix<>(data, operations);
    }

    public Matrix<Numberxx> createAntisymmetricMatrix(int dimension, long seed) {
        Random randomLocal = new Random(seed);
        T[][] data = (T[][]) new Number[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            data[i][i] = (T) operations.zero(); // przekątna musi być zerowa
            for (int j = i + 1; j < dimension; j++) {
                T value = (T) operations.valueOf(randomLocal.nextInt(10));
                data[i][j] = value;
                // element [j][i] = -value, przyjmujemy, że można obliczyć jako zero - value
                data[j][i] = (T) operations.subtract(operations.zero(), value);
            }
        }
        return new Matrix<>(data, operations);
    }

    public Matrix<Numberxx> createLowerTriangularMatrix(int dimension, long seed) {
        Random randomLocal = new Random(seed);
        T[][] data = (T[][]) new Number[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (j <= i) {
                    data[i][j] = (T) operations.valueOf(randomLocal.nextInt(10));
                } else {
                    data[i][j] = (T) operations.zero();
                }
            }
        }
        return new Matrix<>(data, operations);
    }

    public Matrix<Numberxx> createUpperTriangularMatrix(int dimension, long seed) {
        Random randomLocal = new Random(seed);
        T[][] data = (T[][]) new Number[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (j >= i) {
                    data[i][j] = (T) operations.valueOf(randomLocal.nextInt(10));
                } else {
                    data[i][j] = (T) operations.zero();
                }
            }
        }
        return new Matrix<>(data, operations);
    }
}
