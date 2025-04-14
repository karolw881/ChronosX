
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.magister.helper.Numberxx;
import org.magister.helper.NumberxxOperations;
import org.magister.vector.Vector;

public class VectorTest {

    private final NumberxxOperations operations = new NumberxxOperations();

    // Metoda pomocnicza tworząca wektor z podanych liczb
    private Vector<Numberxx> createVector(int... coords) {
        Numberxx[] arr = new Numberxx[coords.length];
        for (int i = 0; i < coords.length; i++) {
            arr[i] = Numberxx.valueOf(coords[i]);
        }
        return new Vector<>(arr, operations);
    }

    // Metoda pomocnicza porównująca dwa wektory (na podstawie ich współrzędnych)
    private void assertVectorEquals(Numberxx[] expected, Vector<Numberxx> actual) {
        Numberxx[] actualCoords = actual.getCoordinates();
        assertEquals("Różna długość wektora", expected.length, actualCoords.length);
        assertArrayEquals("Niezgodne współrzędne wektora", expected, actualCoords);
    }

    // === Testy operacji dodawania ===

    @Test
    public void testAdd() {
        Vector<Numberxx> v1 = createVector(1, 2, 3);
        Vector<Numberxx> v2 = createVector(4, 5, 6);

        Vector<Numberxx> result = v1.add(v2);
        Numberxx[] expected = new Numberxx[]{
                Numberxx.valueOf(5),
                Numberxx.valueOf(7),
                Numberxx.valueOf(9)
        };
        assertVectorEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDimensionMismatch() {
        Vector<Numberxx> v1 = createVector(1, 2);
        Vector<Numberxx> v2 = createVector(1, 2, 3);

        // Próba dodania wektorów o różnych wymiarach – powinien zostać rzucony wyjątek
        v1.add(v2);
    }

    // === Testy operacji negacji (opposite) ===

    @Test
    public void testOpposite() {
        Vector<Numberxx> v = createVector(1, -2, 3);

        Vector<Numberxx> opp = v.opposite();
        Numberxx[] expected = new Numberxx[]{
                Numberxx.valueOf(-1),
                Numberxx.valueOf(2),
                Numberxx.valueOf(-3)
        };
        assertVectorEquals(expected, opp);
    }

    // === Testy odejmowania: subtractVectorNegativeAdd oraz subtruct ===

    @Test
    public void testSubtractVectorNegativeAdd() {
        Vector<Numberxx> v1 = createVector(5, 10, 15);
        Vector<Numberxx> v2 = createVector(1, 2, 3);

        Vector<Numberxx> result = v1.subtractVectorNegativeAdd(v2);
        Numberxx[] expected = new Numberxx[]{
                Numberxx.valueOf(4),
                Numberxx.valueOf(8),
                Numberxx.valueOf(12)
        };
        assertVectorEquals(expected, result);
    }

    @Test
    public void testSubtruct() {
        Vector<Numberxx> v1 = createVector(5, 10, 15);
        Vector<Numberxx> v2 = createVector(1, 2, 3);

        Vector<Numberxx> result = v1.subtruct(v2);
        Numberxx[] expected = new Numberxx[]{
                Numberxx.valueOf(4),
                Numberxx.valueOf(8),
                Numberxx.valueOf(12)
        };
        assertVectorEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubtructDimensionMismatch() {
        Vector<Numberxx> v1 = createVector(1, 2);
        Vector<Numberxx> v2 = createVector(1, 2, 3);
        v1.subtruct(v2);
    }

    // === Test mnożenia wektora przez skalar ===

    @Test
    public void testMultiplyByScalar() {
        Vector<Numberxx> v = createVector(2, -3, 4);
        Numberxx scalar = Numberxx.valueOf(3);

        Vector<Numberxx> result = v.multiplyByScalar(scalar);
        Numberxx[] expected = new Numberxx[]{
                Numberxx.valueOf(6),
                Numberxx.valueOf(-9),
                Numberxx.valueOf(12)
        };
        assertVectorEquals(expected, result);
    }

    // === Test iloczynu skalarnego (dot product) ===

    @Test
    public void testDotProduct() {
        Vector<Numberxx> v1 = createVector(1, 3, -5);
        Vector<Numberxx> v2 = createVector(4, -2, -1);

        Numberxx dot = v1.dotProduct(v2);
        // Obliczenia: 1*4 + 3*(-2) + (-5)*(-1) = 4 - 6 + 5 = 3
        Numberxx expected = Numberxx.valueOf(3);

        assertEquals("Niepoprawny iloczyn skalarny", expected, dot);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDotProductDimensionMismatch() {
        Vector<Numberxx> v1 = createVector(1, 2);
        Vector<Numberxx> v2 = createVector(1, 2, 3);
        v1.dotProduct(v2);
    }

    // === Test metody toString() (opcjonalny) ===

    @Test
    public void testToString() {
        Vector<Numberxx> v = createVector(7, 8, 9);
        String str = v.toString();
        // Spodziewamy się, że ciąg znaków zawiera współrzędne oddzielone przecinkami
        // Przykładowy format: "Vector{7, 8, 9}"
        String expectedSubstring = "7, 8, 9";
        assertEquals("Ciąg znaków nie zawiera oczekiwanych współrzędnych", true, str.contains(expectedSubstring));
    }
}
