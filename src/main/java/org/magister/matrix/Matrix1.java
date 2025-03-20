package org.magister.matrix;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/// Konkretna implementacja macierzy dla liczb całkowitych
@Getter
@Setter
@AllArgsConstructor
public class Matrix1 {
    private int[][] data;
    private int rows;
    private int cols;


    public Matrix1(int[][] data) {
        if (data == null || data.length == 0 || data[0].length == 0) {
            throw new IllegalArgumentException("Dane macierzy nie mogą być puste");
        }
        this.rows = data.length;
        this.cols = data[0].length;

        // Kopiowanie danych
        this.data = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            if (data[i].length != cols) {
                throw new IllegalArgumentException("Wszystkie wiersze muszą mieć taką samą długość");
            }
            System.arraycopy(data[i], 0, this.data[i], 0, cols);
        }
    }



    // Metoda dodawania macierzy
    public Matrix1 add(Matrix1 matrix) {
        if (rows != matrix.rows || cols != matrix.cols) {
            throw new IllegalArgumentException("Macierze muszą mieć identyczne wymiary do dodawania");
        }

        int[][] result = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = data[i][j] + matrix.data[i][j];
            }
        }
        return new Matrix1(result);
    }

    // Metoda odejmowania macierzy
    public Matrix1 subtract(Matrix1 matrix) {
        if (rows != matrix.rows || cols != matrix.cols) {
            throw new IllegalArgumentException("Macierze muszą mieć identyczne wymiary do odejmowania");
        }

        int[][] result = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = data[i][j] - matrix.data[i][j];
            }
        }
        return new Matrix1(result);
    }


    // Metoda sprawdzająca odwracalność macierzy
    public boolean isInvertible() {
        if (rows != cols) {
            return false; // Tylko macierze kwadratowe mogą być odwracalne
        }

        // Macierz jest odwracalna, jeśli jej wyznacznik jest różny od zera
        return determinant() != 0;
    }

    //  tworzenia podmacierzy poprzez usunięcie wiersza i kolumny
    private Matrix1 getSubMatrix(int excludeRow, int excludeCol) {
        int[][] subData = new int[rows - 1][cols - 1];

        int r = 0;
        for (int i = 0; i < rows; i++) {
            if (i == excludeRow) continue;

            int c = 0;
            for (int j = 0; j < cols; j++) {
                if (j == excludeCol) continue;
                subData[r][c] = data[i][j];
                c++;
            }
            r++;
        }

        return new Matrix1(subData);
    }


    // Metoda obliczania wyznacznika
    public int determinant() {
        if (rows != cols) {
            throw new IllegalArgumentException("Wyznacznik można obliczyć tylko dla macierzy kwadratowej");
        }

        // Dla macierzy 1x1
        if (rows == 1) {
            return data[0][0];
        }

        // Dla macierzy 2x2
        if (rows == 2) {
            return data[0][0] * data[1][1] - data[0][1] * data[1][0];
        }

        // Dla większych macierzy używamy rozwinięcia Laplace'a
        int result = 0;
        for (int j = 0; j < cols; j++) {
            Matrix1 subMatrix = getSubMatrix(0, j);
            int subDet = subMatrix.determinant();
            int sign = (j % 2 == 0) ? 1 : -1;
            result += sign * data[0][j] * subDet;
        }

        return result;
    }


    // Metoda obliczająca macierz odwrotną


    // Metoda mnożenia macierzy
    public Matrix1 multiply(Matrix1 matrix) {
        if (cols != matrix.rows) {
            throw new IllegalArgumentException("Liczba kolumn pierwszej macierzy musi być równa liczbie wierszy drugiej");
        }

        int[][] result = new int[rows][matrix.cols];

        // Mnożenie macierzy
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < matrix.cols; j++) {
                result[i][j] = 0;
                for (int k = 0; k < cols; k++) {
                    result[i][j] += data[i][k] * matrix.data[k][j];
                }
            }
        }

        return new Matrix1(result);
    }




    // Metoda dzielenia macierzy: A / B = A * B^{-1}
    public Matrix1 divide(Matrix1 matrix) {
        if (!matrix.isInvertible()) {
            throw new IllegalArgumentException("Macierz dzielnik nie jest odwracalna");
        }

        // Dzielenie to mnożenie przez macierz odwrotną
        return multiply(matrix.inverse());
    }

    // Metoda obliczania macierzy odwrotnej (Gauss-Jordan elimination)
    public Matrix1 inverse() {
        if (rows != cols) {
            throw new IllegalArgumentException("Macierz musi być kwadratowa, aby obliczyć odwrotność");
        }

        int n = rows;

        // Tworzymy rozszerzoną macierz [A | I]
        int[][] augmented = new int[n][2 * n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(data[i], 0, augmented[i], 0, n);
            for (int j = n; j < 2 * n; j++) {
                augmented[i][j] = (j - n == i) ? 1 : 0;
            }
        }

        // Eliminacja Gauss-Jordana
        for (int i = 0; i < n; i++) {
            // Jeśli pivot jest równy zero, zamień wiersze
            if (augmented[i][i] == 0) {
                boolean swapped = false;
                for (int k = i + 1; k < n; k++) {
                    if (augmented[k][i] != 0) {
                        // Zamiana wierszy
                        int[] temp = augmented[i];
                        augmented[i] = augmented[k];
                        augmented[k] = temp;
                        swapped = true;
                        break;
                    }
                }
                if (!swapped) {
                    throw new ArithmeticException("Macierz jest osobliwa i nie ma odwrotności");
                }
            }

            // Normalizujemy wiersz, aby pivot stał się jedynką
            // Uwaga: dla int to może powodować utratę precyzji
            int pivot = augmented[i][i];
            if (pivot != 1) {
                // Sprawdzamy czy można dokładnie znormalizować
                boolean allDivisible = true;
                for (int j = 0; j < 2 * n; j++) {
                    if (augmented[i][j] % pivot != 0 && augmented[i][j] != 0) {
                        allDivisible = false;
                        break;
                    }
                }

                if (!allDivisible) {
                    throw new ArithmeticException("Nie można obliczyć dokładnej odwrotności w liczbach całkowitych");
                }

                for (int j = 0; j < 2 * n; j++) {
                    augmented[i][j] /= pivot;
                }
            }

            // Eliminujemy pozostałe elementy w kolumnie pivotu
            for (int k = 0; k < n; k++) {
                if (k == i) continue;
                int factor = augmented[k][i];
                for (int j = 0; j < 2 * n; j++) {
                    augmented[k][j] -= factor * augmented[i][j];
                }
            }
        }

        // Wyodrębniamy macierz odwrotną (prawa połowa rozszerzonej macierzy)
        int[][] invData = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(augmented[i], 0 + n, invData[i], 0, n);
        }

        return new Matrix1(invData);
    }

    // Metoda wyświetlania macierzy
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            sb.append("[");
            for (int j = 0; j < cols; j++) {
                sb.append(data[i][j]);
                if (j < cols - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]\n");
        }
        return sb.toString();
    }
}