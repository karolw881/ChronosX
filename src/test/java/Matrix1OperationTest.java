

import static org.junit.Assert.*;
import org.junit.Test;
import org.magister.matrix.Matrix1;

public class Matrix1OperationTest {

    /**
     * Pomocnicza metoda porównująca dwuwymiarowe tablice int.
     */
    private void assert2dArrayEquals(String message, int[][] expected, int[][] actual) {
        assertEquals(message + " - liczba wierszy", expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(message + " - wiersz " + i, expected[i], actual[i]);
        }
    }

    // --- Testy dla macierzy 2x2 ---

    @Test
    public void testAddition2x2() {
        int[][] dataA = { {1, 2}, {3, 4} };
        int[][] dataB = { {5, 6}, {7, 8} };
        Matrix1 matrixA = new Matrix1(dataA);
        Matrix1 matrixB = new Matrix1(dataB);

        Matrix1 result = matrixA.add(matrixB);
        int[][] expected = { {6, 8}, {10, 12} };
        assert2dArrayEquals("Dodawanie 2x2", expected, result.getData());

    }

    @Test
    public void testSubtraction2x2() {
        int[][] dataA = { {1, 2}, {3, 4} };
        int[][] dataB = { {5, 6}, {7, 8} };
        Matrix1 matrixA = new Matrix1(dataA);
        Matrix1 matrixB = new Matrix1(dataB);

        Matrix1 result = matrixA.subtract(matrixB);
        int[][] expected = { {-4, -4}, {-4, -4} };
        assert2dArrayEquals("Odejmowanie 2x2", expected, result.getData());
    }

    @Test
    public void testMultiplication2x2() {
        int[][] dataA = { {1, 2}, {3, 4} };
        int[][] dataB = { {5, 6}, {7, 8} };
        Matrix1 matrixA = new Matrix1(dataA);
        Matrix1 matrixB = new Matrix1(dataB);

        Matrix1 result = matrixA.multiply(matrixB);
        int[][] expected = { {19, 22}, {43, 50} };
        assert2dArrayEquals("Mnożenie 2x2", expected, result.getData());
    }

    @Test
    public void testDeterminant2x2() {
        int[][] dataA = { {1, 2}, {3, 4} };
        Matrix1 matrixA = new Matrix1(dataA);

        int det = matrixA.determinant();
        int expected = -2;
        assertEquals("Wyznacznik 2x2", expected, det);
    }

    // --- Testy dla macierzy 3x3 ---

    @Test
    public void testAddition3x3() {
        int[][] dataA = { {1, 2, 3}, {4, 5, 6}, {7, 8, 9} };
        int[][] dataB = { {9, 8, 7}, {6, 5, 4}, {3, 2, 1} };
        Matrix1 matrixA = new Matrix1(dataA);
        Matrix1 matrixB = new Matrix1(dataB);

        Matrix1 result = matrixA.add(matrixB);
        int[][] expected = { {10, 10, 10}, {10, 10, 10}, {10, 10, 10} };
        assert2dArrayEquals("Dodawanie 3x3", expected, result.getData());
    }

    @Test
    public void testSubtraction3x3() {
        int[][] dataA = { {1, 2, 3}, {4, 5, 6}, {7, 8, 9} };
        int[][] dataB = { {9, 8, 7}, {6, 5, 4}, {3, 2, 1} };
        Matrix1 matrixA = new Matrix1(dataA);
        Matrix1 matrixB = new Matrix1(dataB);

        Matrix1 result = matrixA.subtract(matrixB);
        int[][] expected = { {-8, -6, -4}, {-2, 0, 2}, {4, 6, 8} };
        assert2dArrayEquals("Odejmowanie 3x3", expected, result.getData());
    }

    @Test
    public void testMultiplication3x3() {
        int[][] dataA = { {1, 2, 3}, {4, 5, 6}, {7, 8, 9} };
        int[][] dataB = { {9, 8, 7}, {6, 5, 4}, {3, 2, 1} };
        Matrix1 matrixA = new Matrix1(dataA);
        Matrix1 matrixB = new Matrix1(dataB);

        Matrix1 result = matrixA.multiply(matrixB);
        int[][] expected = {
                {30, 24, 18},
                {84, 69, 54},
                {138, 114, 90}
        };
        assert2dArrayEquals("Mnożenie 3x3", expected, result.getData());
    }

    @Test
    public void testDeterminant3x3() {
        // Używamy macierzy o znanym wyznaczniku:
        // [ [1, 2, 3],
        //   [0, 1, 4],
        //   [5, 6, 0] ]
        // Wyznacznik = 1*(1*0 - 4*6) - 2*(0*0 - 4*5) + 3*(0*6 - 1*5)
        //            = 1*(-24) - 2*(-20) + 3*(-5) = -24 + 40 -15 = 1
        int[][] data = { {1, 2, 3}, {0, 1, 4}, {5, 6, 0} };
        Matrix1 matrix = new Matrix1(data);
        int det = matrix.determinant();
        int expected = 1;
        assertEquals("Wyznacznik 3x3", expected, det);
    }

    // --- Testy dla macierzy 4x4 ---

    @Test
    public void testAddition4x4() {
        int[][] dataA = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };
        int[][] dataB = {
                {16, 15, 14, 13},
                {12, 11, 10, 9},
                {8, 7, 6, 5},
                {4, 3, 2, 1}
        };
        Matrix1 matrixA = new Matrix1(dataA);
        Matrix1 matrixB = new Matrix1(dataB);

        Matrix1 result = matrixA.add(matrixB);
        int[][] expected = {
                {17, 17, 17, 17},
                {17, 17, 17, 17},
                {17, 17, 17, 17},
                {17, 17, 17, 17}
        };
        assert2dArrayEquals("Dodawanie 4x4", expected, result.getData());
    }

    @Test
    public void testSubtraction4x4() {
        int[][] dataA = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };
        int[][] dataB = {
                {16, 15, 14, 13},
                {12, 11, 10, 9},
                {8, 7, 6, 5},
                {4, 3, 2, 1}
        };
        Matrix1 matrixA = new Matrix1(dataA);
        Matrix1 matrixB = new Matrix1(dataB);

        Matrix1 result = matrixA.subtract(matrixB);
        int[][] expected = {
                {-15, -13, -11, -9},
                {-7, -5, -3, -1},
                {1, 3, 5, 7},
                {9, 11, 13, 15}
        };
        assert2dArrayEquals("Odejmowanie 4x4", expected, result.getData());
    }

    @Test
    public void testMultiplication4x4() {
        int[][] dataA = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };
        int[][] dataB = {
                {16, 15, 14, 13},
                {12, 11, 10, 9},
                {8, 7, 6, 5},
                {4, 3, 2, 1}
        };
        Matrix1 matrixA = new Matrix1(dataA);
        Matrix1 matrixB = new Matrix1(dataB);

        Matrix1 result = matrixA.multiply(matrixB);
        int[][] expected = {
                {80, 70, 60, 50},
                {240, 214, 188, 162},
                {400, 358, 316, 274},
                {560, 502, 444, 386}
        };
        assert2dArrayEquals("Mnożenie 4x4", expected, result.getData());
    }

    @Test
    public void testDeterminant4x4() {
        // Używamy macierzy 4x4 osobliwej: [ [1,2,3,4],
        //                                    [5,6,7,8],
        //                                    [9,10,11,12],
        //                                    [13,14,15,16] ]
        // Wyznacznik powinien wynosić 0.
        int[][] data = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };
        Matrix1 matrix = new Matrix1(data);
        int det = matrix.determinant();
        int expected = 0;
        assertEquals("Wyznacznik 4x4", expected, det);
    }

    // --- Test sprawdzający wyrzucenie wyjątku przy niezgodnych wymiarach ---

    @Test(expected = IllegalArgumentException.class)
    public void testAdditionDimensionMismatch() {
        int[][] dataA = { {1, 2}, {3, 4} };
        int[][] dataB = { {5, 6, 7}, {8, 9, 10} };
        Matrix1 matrixA = new Matrix1(dataA);
        Matrix1 matrixB = new Matrix1(dataB);
        matrixA.add(matrixB);
    }
}

