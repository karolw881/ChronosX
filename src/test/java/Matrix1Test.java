
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.magister.matrix.Matrix1;

public class Matrix1Test {

    // Metoda pomocnicza do porównywania macierzy dwuwymiarowych
    private void assertMatrixEquals(int[][] expected, int[][] actual) {
        assertEquals("Liczba wierszy różna", expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals("Różnica w wierszu " + i, expected[i], actual[i]);
        }
    }

    // --- Testy dla macierzy 2x2 ---

    @Test
    public void testAddition2x2() {
        int[][] a = { {1, 2}, {3, 4} };
        int[][] b = { {5, 6}, {7, 8} };

        Matrix1 m1 = new Matrix1(a);
        Matrix1 m2 = new Matrix1(b);
        Matrix1 result = m1.add(m2);

        int[][] expected = { {6, 8}, {10, 12} };
        assertMatrixEquals(expected, result.getData());
    }

    @Test
    public void testSubtraction2x2() {
        int[][] a = { {1, 2}, {3, 4} };
        int[][] b = { {5, 6}, {7, 8} };

        Matrix1 m1 = new Matrix1(a);
        Matrix1 m2 = new Matrix1(b);
        Matrix1 result = m1.subtract(m2);

        int[][] expected = { {-4, -4}, {-4, -4} };
        assertMatrixEquals(expected, result.getData());
    }

    @Test
    public void testMultiplication2x2() {
        int[][] a = { {1, 2}, {3, 4} };
        int[][] b = { {5, 6}, {7, 8} };

        Matrix1 m1 = new Matrix1(a);
        Matrix1 m2 = new Matrix1(b);
        Matrix1 result = m1.multiply(m2);

        int[][] expected = { {19, 22}, {43, 50} };
        assertMatrixEquals(expected, result.getData());
    }

    @Test
    public void testDeterminant2x2() {
        int[][] a = { {1, 2}, {3, 4} };
        Matrix1 m1 = new Matrix1(a);
        int det = m1.determinant();
        int expected = 1 * 4 - 2 * 3; // -2
        assertEquals("Niepoprawny wyznacznik macierzy 2x2", expected, det);
    }

    // --- Testy dla macierzy 3x3 ---

    @Test
    public void testAddition3x3() {
        int[][] a = { {1, 2, 3}, {4, 5, 6}, {7, 8, 9} };
        int[][] b = { {9, 8, 7}, {6, 5, 4}, {3, 2, 1} };

        Matrix1 m1 = new Matrix1(a);
        Matrix1 m2 = new Matrix1(b);
        Matrix1 result = m1.add(m2);

        int[][] expected = { {10, 10, 10}, {10, 10, 10}, {10, 10, 10} };
        assertMatrixEquals(expected, result.getData());
    }

    @Test
    public void testSubtraction3x3() {
        int[][] a = { {1, 2, 3}, {4, 5, 6}, {7, 8, 9} };
        int[][] b = { {9, 8, 7}, {6, 5, 4}, {3, 2, 1} };

        Matrix1 m1 = new Matrix1(a);
        Matrix1 m2 = new Matrix1(b);
        Matrix1 result = m1.subtract(m2);

        int[][] expected = { {-8, -6, -4}, {-2, 0, 2}, {4, 6, 8} };
        assertMatrixEquals(expected, result.getData());
    }

    @Test
    public void testMultiplication3x3() {
        int[][] a = { {1, 2, 3}, {4, 5, 6}, {7, 8, 9} };
        int[][] b = { {9, 8, 7}, {6, 5, 4}, {3, 2, 1} };

        Matrix1 m1 = new Matrix1(a);
        Matrix1 m2 = new Matrix1(b);
        Matrix1 result = m1.multiply(m2);

        int[][] expected = {
                {30, 24, 18},
                {84, 69, 54},
                {138, 114, 90}
        };
        assertMatrixEquals(expected, result.getData());
    }

    @Test
    public void testDeterminant3x3() {
        int[][] a = { {1, 2, 3}, {4, 5, 6}, {7, 8, 9} };
        Matrix1 m1 = new Matrix1(a);
        int det = m1.determinant();
        int expected = 0;  // Macierz 3x3 [1,2,3;4,5,6;7,8,9] jest osobliwa
        assertEquals("Niepoprawny wyznacznik macierzy 3x3", expected, det);
    }

    // --- Testy dla macierzy 4x4 ---

    @Test
    public void testAddition4x4() {
        int[][] a = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };
        int[][] b = {
                {16, 15, 14, 13},
                {12, 11, 10, 9},
                {8, 7, 6, 5},
                {4, 3, 2, 1}
        };

        Matrix1 m1 = new Matrix1(a);
        Matrix1 m2 = new Matrix1(b);
        Matrix1 result = m1.add(m2);

        int[][] expected = {
                {17, 17, 17, 17},
                {17, 17, 17, 17},
                {17, 17, 17, 17},
                {17, 17, 17, 17}
        };
        assertMatrixEquals(expected, result.getData());
    }

    @Test
    public void testSubtraction4x4() {
        int[][] a = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };
        int[][] b = {
                {16, 15, 14, 13},
                {12, 11, 10, 9},
                {8, 7, 6, 5},
                {4, 3, 2, 1}
        };

        Matrix1 m1 = new Matrix1(a);
        Matrix1 m2 = new Matrix1(b);
        Matrix1 result = m1.subtract(m2);

        int[][] expected = {
                {-15, -13, -11, -9},
                {-7, -5, -3, -1},
                {1, 3, 5, 7},
                {9, 11, 13, 15}
        };
        assertMatrixEquals(expected, result.getData());
    }

    @Test
    public void testMultiplication4x4() {
        int[][] a = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };
        int[][] b = {
                {16, 15, 14, 13},
                {12, 11, 10, 9},
                {8, 7, 6, 5},
                {4, 3, 2, 1}
        };

        Matrix1 m1 = new Matrix1(a);
        Matrix1 m2 = new Matrix1(b);
        Matrix1 result = m1.multiply(m2);

        int[][] expected = {
                {80, 70, 60, 50},
                {240, 214, 188, 162},
                {400, 358, 316, 274},
                {560, 502, 444, 386}
        };
        assertMatrixEquals(expected, result.getData());
    }

    @Test
    public void testDeterminant4x4() {
        int[][] a = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12},
                {13, 14, 15, 16}
        };
        Matrix1 m1 = new Matrix1(a);
        int det = m1.determinant();
        int expected = 0; // Macierz jest osobliwa
        assertEquals("Niepoprawny wyznacznik macierzy 4x4", expected, det);
    }

    // --- Testy dla odwrotności i wyjątków ---

    @Test
    public void testInverse2x2() {
        // Test odwrotności dla macierzy jednostkowej
        int[][] identity = { {1, 0}, {0, 1} };
        Matrix1 m = new Matrix1(identity);
        Matrix1 inv = m.inverse();
        assertMatrixEquals(identity, inv.getData());
    }

    @Test(expected = ArithmeticException.class)
    public void testInverseNonInvertible() {
        // Macierz nieodwracalna (wyznacznik = 0)
        int[][] a = { {1, 2}, {2, 4} };
        Matrix1 m = new Matrix1(a);
        m.inverse(); // powinna rzucić wyjątek
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdditionDimensionMismatch() {
        // Próba dodania macierzy o różnych wymiarach
        int[][] a = { {1, 2}, {3, 4} };
        int[][] b = { {1, 2, 3}, {4, 5, 6} };
        Matrix1 m1 = new Matrix1(a);
        Matrix1 m2 = new Matrix1(b);
        m1.add(m2);
    }
}
