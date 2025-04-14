

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.magister.vector.Vector1;

public class Vector1Test {

    // Metoda pomocnicza porównująca współrzędne wektora z oczekiwanym wynikiem
    private void assertVectorEquals(int[] expected, Vector1 actual) {
        assertArrayEquals("Współrzędne wektora nie zgadzają się", expected, actual.getCoordinates());
    }

    // === Testy operacji dodawania ===

    @Test
    public void testAdd() {
        int[] coords1 = {1, 2, 3};
        int[] coords2 = {4, 5, 6};
        Vector1 v1 = new Vector1(coords1);
        Vector1 v2 = new Vector1(coords2);

        Vector1 sum = v1.add(v2);
        int[] expected = {5, 7, 9};

        assertVectorEquals(expected, sum);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDimensionMismatch() {
        Vector1 v1 = new Vector1(new int[]{1, 2});
        Vector1 v2 = new Vector1(new int[]{1, 2, 3});

        // Próba dodania wektorów o różnych wymiarach – powinien zostać rzucony wyjątek
        v1.add(v2);
    }

    // === Testy operacji odwrotności (zmiana znaków) ===

    @Test
    public void testOpposite() {
        int[] coords = {1, -2, 3};
        Vector1 v = new Vector1(coords);
        Vector1 opp = v.opposite();

        int[] expected = {-1, 2, -3};
        assertVectorEquals(expected, opp);
    }

    // === Testy odejmowania (dwie metody) ===

    @Test
    public void testSubtractVectorNegativeAdd() {
        int[] coords1 = {5, 10, 15};
        int[] coords2 = {1, 2, 3};
        Vector1 v1 = new Vector1(coords1);
        Vector1 v2 = new Vector1(coords2);

        Vector1 diff = v1.subtractVectorNegativeAdd(v2);
        int[] expected = {4, 8, 12};
        assertVectorEquals(expected, diff);
    }

    @Test
    public void testSubtruct() {
        int[] coords1 = {5, 10, 15};
        int[] coords2 = {1, 2, 3};
        Vector1 v1 = new Vector1(coords1);
        Vector1 v2 = new Vector1(coords2);

        Vector1 diff = v1.subtruct(v2);
        int[] expected = {4, 8, 12};
        assertVectorEquals(expected, diff);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubtructDimensionMismatch() {
        Vector1 v1 = new Vector1(new int[]{1, 2});
        Vector1 v2 = new Vector1(new int[]{1, 2, 3});
        v1.subtruct(v2);
    }

    // === Testy operacji mnożenia przez skalar ===

    @Test
    public void testMultiplyByScalar() {
        int[] coords = {2, -3, 4};
        Vector1 v = new Vector1(coords);

        Vector1 result = v.multiplyByScalar(3);
        int[] expected = {6, -9, 12};
        assertVectorEquals(expected, result);
    }

    // === Testy iloczynu skalarnego (dot product) ===

    @Test
    public void testDotProduct() {
        int[] coords1 = {1, 3, -5};
        int[] coords2 = {4, -2, -1};
        Vector1 v1 = new Vector1(coords1);
        Vector1 v2 = new Vector1(coords2);

        int dot = v1.dotProduct(v2);
        // Obliczenia: 1*4 + 3*(-2) + (-5)*(-1) = 4 - 6 + 5 = 3
        assertEquals("Niepoprawny iloczyn skalarny", 3, dot);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDotProductDimensionMismatch() {
        Vector1 v1 = new Vector1(new int[]{1, 2});
        Vector1 v2 = new Vector1(new int[]{1, 2, 3});
        v1.dotProduct(v2);
    }

    // === Test długości wektora ===

    @Test
    public void testLength() {
        int[] coords = {3, 4};
        Vector1 v = new Vector1(coords);

        double length = v.length();
        // Dla wektora (3,4) oczekujemy długości 5 (trójkąt prostokątny 3-4-5)
        assertEquals("Niepoprawna długość wektora", 5.0, length, 0.0001);
    }
}
