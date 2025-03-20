package org.magister.matrix;




import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.magister.helper.IntegerOperations;
import org.magister.helper.NumberOperations;

// Implementacja generyczna z użyciem refleksji
// Generyczna klasa macierzy
@Getter
@Setter
@AllArgsConstructor

public class Matrix<T extends Number> {
    private T[][] data;
    private NumberOperations<T> operations;
    private int rows;
    private int cols;



    public Matrix(T[][] data, NumberOperations<T> operations ) {
        if (data == null || data.length == 0 || data[0].length == 0) {
            throw new IllegalArgumentException("Dane macierzy nie mogą być puste");
        }
        this.rows = data.length;
        this.cols = data[0].length;
        this.operations = operations;


        // Kopiowanie danych aby uniknąć modyfikacji zewnętrznej
        this.data = (T[][]) new Number[rows][cols];
        for (int i = 0; i < rows; i++) {
            if (data[i].length != cols) {
                throw new IllegalArgumentException("Wszystkie wiersze muszą mieć taką samą długość");
            }
            System.arraycopy(data[i], 0, this.data[i], 0, cols);
        }
    }

    // Metoda dodawania macierzy
    public Matrix<T> add(Matrix<T> matrix) {
        if (rows != matrix.rows || cols != matrix.cols) {
            throw new IllegalArgumentException("Macierze muszą mieć identyczne wymiary do dodawania");
        }


        T[][] result = (T[][]) new Number[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = operations.add(data[i][j], matrix.data[i][j]);
            }
        }
        return new Matrix<>(result, operations);
    }

    // Metoda odejmowania macierzy
    public Matrix<T> subtract(Matrix<T> matrix) {
        if (rows != matrix.rows || cols != matrix.cols) {
            throw new IllegalArgumentException("Macierze muszą mieć identyczne wymiary do odejmowania");
        }

        T[][] result = (T[][]) new Number[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = operations.subtract(data[i][j], matrix.data[i][j]);
            }
        }
        return new Matrix<>(result, operations);
    }

    // Metoda mnożenia macierzy
    public Matrix<T> multiply(Matrix<T> matrix) {
        if (cols != matrix.rows) {
            throw new IllegalArgumentException("Liczba kolumn pierwszej macierzy musi być równa liczbie wierszy drugiej");
        }


        T[][] result = (T[][]) new Number[rows][matrix.cols];

        // Inicjalizacja wynikowej macierzy zerami
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < matrix.cols; j++) {
                result[i][j] = operations.zero();
            }
        }

        // Mnożenie macierzy
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < matrix.cols; j++) {
                for (int k = 0; k < cols; k++) {
                    T product = operations.multiply(data[i][k], matrix.data[k][j]);
                    result[i][j] = operations.add(result[i][j], product);
                }
            }
        }

        return new Matrix<>(result, operations);
    }




    /* metoda dzielenia  */


    // Metoda dzielenia macierzy: A / B = A * B^{-1}


    public Matrix<T> divide(Matrix<T> matrix) {
        if (!matrix.determinant().equals(operations.zero())) {
       //     System.out.println("dzielenie" + operations.one());
            return this.multiply(matrix.inverse());
        } else{
           // System.out.println(" blad w dzielniu ");
           // throw new RuntimeException("blad w dzieleniu");
            return null;
        }

    }
    // Metoda obliczania macierzy odwrotnej (Gauss-Jordan elimination)

    public Matrix<T> inverse() {
        if (rows != cols) {
            throw new IllegalArgumentException("Macierz musi być kwadratowa, aby obliczyć odwrotność");
        }
        int n = rows;
        // Tworzymy rozszerzoną macierz [A | I]
        T[][] augmented = (T[][]) new Number[n][2 * n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(data[i], 0, augmented[i], 0, n);
            for (int j = n; j < 2 * n; j++) {
                augmented[i][j] = (j - n == i) ? operations.one() : operations.zero();
            }
        }

        // Eliminacja Gauss-Jordana
        for (int i = 0; i < n; i++) {
            // Jeśli pivot jest równy zero, zamień wiersze
            if (augmented[i][i].equals(operations.zero())) {
                boolean swapped = false;
                for (int k = i + 1; k < n; k++) {
                    if (!augmented[k][i].equals(operations.zero())) {
                        T[] temp = augmented[i];
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
            T pivot = augmented[i][i];
            for (int j = 0; j < 2 * n; j++) {
                augmented[i][j] = operations.divide(augmented[i][j], pivot);
            }
            // Eliminujemy pozostałe elementy w kolumnie pivotu
            for (int k = 0; k < n; k++) {
                if (k == i) continue;
                T factor = augmented[k][i];
                for (int j = 0; j < 2 * n; j++) {
                    T product = operations.multiply(factor, augmented[i][j]);
                    augmented[k][j] = operations.subtract(augmented[k][j], product);
                }
            }
        }

        // Wyodrębniamy macierz odwrotną (prawa połowa rozszerzonej macierzy)
        T[][] invData = (T[][]) new Number[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(augmented[i], 0 + n, invData[i], 0, n);
        }
        return new Matrix<>(invData, operations);
    }
    // Metoda obliczania wyznacznika (tylko dla macierzy kwadratowych)
    public T determinant() {
        if (rows != cols) {
            throw new IllegalArgumentException("Wyznacznik można obliczyć tylko dla macierzy kwadratowej");
        }

        // Dla macierzy 1x1
        if (rows == 1) {
            return data[0][0];
        }

        // Dla macierzy 2x2
        if (rows == 2) {
            T a = data[0][0];
            T b = data[0][1];
            T c = data[1][0];
            T d = data[1][1];

            T ad = operations.multiply(a, d);
            T bc = operations.multiply(b, c);

            return operations.subtract(ad, bc);
        }

        // Dla większych macierzy używamy rozwinięcia Laplace'a
        T result = operations.zero();
        for (int j = 0; j < cols; j++) {
            Matrix<T> subMatrix = getSubMatrix(0, j);
            T subDet = subMatrix.determinant();
            T multiplier = j % 2 == 0 ? data[0][j] : operations.multiply(operations.multiply(data[0][j], operations.one()), operations.multiply(operations.one(), operations.subtract(operations.zero(), operations.one())));
            T term = operations.multiply(multiplier, subDet);
            result = operations.add(result, term);
        }

        return result;
    }

    // Metoda pomocnicza do tworzenia podmacierzy poprzez usunięcie wiersza i kolumny
    private Matrix<T> getSubMatrix(int excludeRow, int excludeCol) {
        T[][] subData = (T[][]) new Number[rows - 1][cols - 1];

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

        return new Matrix<>(subData, operations);
    }

    public T getElement(int i, int j) {
        if (i < 0 || i >= rows || j < 0 || j >= cols) {
            throw new IndexOutOfBoundsException("Indeksy poza zakresem macierzy: " + i + ", " + j);
        }
        return data[i][j];
    }


    // Metoda sprawdzająca odwracalność macierzy
    public boolean isInvertible() {
        if (rows != cols) {
            return false; // Tylko macierze kwadratowe mogą być odwracalne
        }

        // Macierz jest odwracalna, jeśli jej wyznacznik jest różny od zera
        T det = determinant();
        return !det.equals(operations.zero());
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
