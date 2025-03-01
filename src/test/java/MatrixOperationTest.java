

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.magister.helper.IntegerOperations;
import org.magister.helper.NumberOperations;
import org.magister.matrix.Matrix;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class MatrixOperationTest {

    // Ścieżki do katalogów
    private static final String INPUT_DIR = "src/test/resources/testMatrixOperation/input";
    private static final String EXPECTED_DIR = "src/test/resources/testMatrixOperation/output/expected";
    private static final String RESULT_DIR = "src/test/resources/testMatrixOperation/output/result";

    private static final NumberOperations<Integer> operations = new IntegerOperations();

    /**
     * Przygotowuje katalogi oraz generuje pliki wejściowe i oczekiwane wyniki,
     * jeśli jeszcze nie istnieją.
     */
    @BeforeClass
    public static void setupFiles() throws IOException {
        // Tworzymy wymagane katalogi
        Files.createDirectories(Paths.get(INPUT_DIR));
        Files.createDirectories(Paths.get(EXPECTED_DIR));
        Files.createDirectories(Paths.get(RESULT_DIR));

        // === MACIERZE 2x2 ===
        // Pliki wejściowe: macierzA.txt i macierzB.txt
        Path matrixAPath = Paths.get(INPUT_DIR, "matrixA.txt");
        Path matrixBPath = Paths.get(INPUT_DIR, "matrixB.txt");
        if (!Files.exists(matrixAPath)) {
            // Macierz A: 2x2: [ [1, 2], [3, 4] ]
            Integer[][] matrixA = { {1, 2}, {3, 4} };
            writeMatrixToFile(matrixAPath.toString(), matrixA);
        }
        if (!Files.exists(matrixBPath)) {
            // Macierz B: 2x2: [ [5, 6], [7, 8] ]
            Integer[][] matrixB = { {5, 6}, {7, 8} };
            writeMatrixToFile(matrixBPath.toString(), matrixB);
        }
        // Oczekiwane wyniki dla 2x2
        Path expectedAddition = Paths.get(EXPECTED_DIR, "expected_addition.txt");
        if (!Files.exists(expectedAddition)) {
            Integer[][] addition = { {6, 8}, {10, 12} };
            writeMatrixToFile(expectedAddition.toString(), addition);
        }
        Path expectedSubtraction = Paths.get(EXPECTED_DIR, "expected_subtraction.txt");
        if (!Files.exists(expectedSubtraction)) {
            Integer[][] subtraction = { {-4, -4}, {-4, -4} };
            writeMatrixToFile(expectedSubtraction.toString(), subtraction);
        }
        Path expectedMultiplication = Paths.get(EXPECTED_DIR, "expected_multiplication.txt");
        if (!Files.exists(expectedMultiplication)) {
            Integer[][] multiplication = { {19, 22}, {43, 50} };
            writeMatrixToFile(expectedMultiplication.toString(), multiplication);
        }
        Path expectedDeterminant = Paths.get(EXPECTED_DIR, "expected_determinant.txt");
        if (!Files.exists(expectedDeterminant)) {
            Files.write(expectedDeterminant, "-2".getBytes());
        }

        // === MACIERZE 3x3 ===
        // Pliki wejściowe: matrix3x3_A.txt i matrix3x3_B.txt
        Path matrix3x3_APath = Paths.get(INPUT_DIR, "matrix3x3_A.txt");
        Path matrix3x3_BPath = Paths.get(INPUT_DIR, "matrix3x3_B.txt");
        if (!Files.exists(matrix3x3_APath)) {
            Integer[][] matrix3x3_A = { {1, 2, 3}, {4, 5, 6}, {7, 8, 9} };
            writeMatrixToFile(matrix3x3_APath.toString(), matrix3x3_A);
        }
        if (!Files.exists(matrix3x3_BPath)) {
            Integer[][] matrix3x3_B = { {9, 8, 7}, {6, 5, 4}, {3, 2, 1} };
            writeMatrixToFile(matrix3x3_BPath.toString(), matrix3x3_B);
        }
        // Oczekiwane wyniki dla 3x3
        Path expectedAddition3x3 = Paths.get(EXPECTED_DIR, "expected_addition_3x3.txt");
        if (!Files.exists(expectedAddition3x3)) {
            Integer[][] addition3x3 = { {10, 10, 10}, {10, 10, 10}, {10, 10, 10} };
            writeMatrixToFile(expectedAddition3x3.toString(), addition3x3);
        }
        Path expectedSubtraction3x3 = Paths.get(EXPECTED_DIR, "expected_subtraction_3x3.txt");
        if (!Files.exists(expectedSubtraction3x3)) {
            Integer[][] subtraction3x3 = { {-8, -6, -4}, {-2, 0, 2}, {4, 6, 8} };
            writeMatrixToFile(expectedSubtraction3x3.toString(), subtraction3x3);
        }
        Path expectedMultiplication3x3 = Paths.get(EXPECTED_DIR, "expected_multiplication_3x3.txt");
        if (!Files.exists(expectedMultiplication3x3)) {
            Integer[][] multiplication3x3 = { {30, 24, 18}, {84, 69, 54}, {138, 114, 90} };
            writeMatrixToFile(expectedMultiplication3x3.toString(), multiplication3x3);
        }
        Path expectedDeterminant3x3 = Paths.get(EXPECTED_DIR, "expected_determinant_3x3.txt");
        if (!Files.exists(expectedDeterminant3x3)) {
            // Dla macierzy 3x3 A (1,2,3;4,5,6;7,8,9) wyznacznik wynosi 0
            Files.write(expectedDeterminant3x3, "0".getBytes());
        }

        // === MACIERZE 4x4 ===
        // Pliki wejściowe: matrix4x4_E.txt i matrix4x4_F.txt
        Path matrix4x4_EPath = Paths.get(INPUT_DIR, "matrix4x4_E.txt");
        Path matrix4x4_FPath = Paths.get(INPUT_DIR, "matrix4x4_F.txt");
        if (!Files.exists(matrix4x4_EPath)) {
            Integer[][] matrix4x4_E = { {1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16} };
            writeMatrixToFile(matrix4x4_EPath.toString(), matrix4x4_E);
        }
        if (!Files.exists(matrix4x4_FPath)) {
            Integer[][] matrix4x4_F = { {16, 15, 14, 13}, {12, 11, 10, 9}, {8, 7, 6, 5}, {4, 3, 2, 1} };
            writeMatrixToFile(matrix4x4_FPath.toString(), matrix4x4_F);
        }
        // Oczekiwane wyniki dla 4x4
        Path expectedAddition4x4 = Paths.get(EXPECTED_DIR, "expected_addition_4x4.txt");
        if (!Files.exists(expectedAddition4x4)) {
            Integer[][] addition4x4 = {
                    {17, 17, 17, 17},
                    {17, 17, 17, 17},
                    {17, 17, 17, 17},
                    {17, 17, 17, 17}
            };
            writeMatrixToFile(expectedAddition4x4.toString(), addition4x4);
        }
        Path expectedSubtraction4x4 = Paths.get(EXPECTED_DIR, "expected_subtraction_4x4.txt");
        if (!Files.exists(expectedSubtraction4x4)) {
            Integer[][] subtraction4x4 = {
                    {-15, -13, -11, -9},
                    {-7, -5, -3, -1},
                    {1, 3, 5, 7},
                    {9, 11, 13, 15}
            };
            writeMatrixToFile(expectedSubtraction4x4.toString(), subtraction4x4);
        }
        Path expectedMultiplication4x4 = Paths.get(EXPECTED_DIR, "expected_multiplication_4x4.txt");
        if (!Files.exists(expectedMultiplication4x4)) {
            Integer[][] multiplication4x4 = {
                    {80, 70, 60, 50},
                    {240, 214, 188, 162},
                    {400, 358, 316, 274},
                    {560, 502, 444, 386}
            };
            writeMatrixToFile(expectedMultiplication4x4.toString(), multiplication4x4);
        }
        Path expectedDeterminant4x4 = Paths.get(EXPECTED_DIR, "expected_determinant_4x4.txt");
        if (!Files.exists(expectedDeterminant4x4)) {
            // Macierz 4x4 E jest osobliwa, wyznacznik = 0
            Files.write(expectedDeterminant4x4, "0".getBytes());
        }
    }

    // --- Metody pomocnicze do obsługi macierzy ---

    /**
     * Zapisuje macierz do pliku. Każdy wiersz zapisywany jest w osobnej linii,
     * a elementy oddzielone pojedynczym odstępem.
     * Metoda przyjmuje tablicę typu Number[][], aby obsłużyć dane zwracane przez Matrix.
     */
    private static void writeMatrixToFile(String fileName, Number[][] matrix) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (Number[] row : matrix) {
            for (int i = 0; i < row.length; i++) {
                sb.append(row[i]);
                if (i < row.length - 1) {
                    sb.append(" ");
                }
            }
            sb.append(System.lineSeparator());
        }
        Files.write(Paths.get(fileName), sb.toString().trim().getBytes());
    }

    /**
     * Odczytuje macierz z pliku. Zakłada, że każdy wiersz macierzy jest w osobnej linii,
     * a elementy są oddzielone pojedynczym odstępem.
     */
    private static Integer[][] readMatrixFromFile(String fileName) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        List<Integer[]> matrixList = new ArrayList<>();
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] tokens = line.trim().split("\\s+");
            Integer[] row = new Integer[tokens.length];
            for (int i = 0; i < tokens.length; i++) {
                row[i] = Integer.parseInt(tokens[i]);
            }
            matrixList.add(row);
        }
        return matrixList.toArray(new Integer[0][]);
    }

    /**
     * Porównuje dwie macierze – sprawdza, czy mają te same wymiary i identyczne elementy.
     */
    private boolean matricesEqual(Number[][] expected, Number[][] actual) {
        if (expected.length != actual.length) return false;
        for (int i = 0; i < expected.length; i++) {
            if (expected[i].length != actual[i].length) return false;
            for (int j = 0; j < expected[i].length; j++) {
                if (!expected[i][j].equals(actual[i][j])) return false;
            }
        }
        return true;
    }

    // === Testy dla macierzy 2x2 ===

    @Test
    public void testMatrixAddition2x2() throws Exception {
        Integer[][] dataA = readMatrixFromFile(Paths.get(INPUT_DIR, "matrixA.txt").toString());
        Integer[][] dataB = readMatrixFromFile(Paths.get(INPUT_DIR, "matrixB.txt").toString());
        Matrix<Integer> matrixA = new Matrix<>(dataA, operations);
        Matrix<Integer> matrixB = new Matrix<>(dataB, operations);

        Matrix<Integer> result = matrixA.add(matrixB);
        String resultFile = Paths.get(RESULT_DIR, "addition_result_2x2.txt").toString();
        writeMatrixToFile(resultFile, result.getData());

        Integer[][] expected = readMatrixFromFile(Paths.get(EXPECTED_DIR, "expected_addition.txt").toString());
        assertTrue("Dodawanie macierzy 2x2 nie powiodło się.", matricesEqual(expected, result.getData()));
    }

    @Test
    public void testMatrixSubtraction2x2() throws Exception {
        Integer[][] dataA = readMatrixFromFile(Paths.get(INPUT_DIR, "matrixA.txt").toString());
        Integer[][] dataB = readMatrixFromFile(Paths.get(INPUT_DIR, "matrixB.txt").toString());
        Matrix<Integer> matrixA = new Matrix<>(dataA, operations);
        Matrix<Integer> matrixB = new Matrix<>(dataB, operations);

        Matrix<Integer> result = matrixA.subtract(matrixB);
        String resultFile = Paths.get(RESULT_DIR, "subtraction_result_2x2.txt").toString();
        writeMatrixToFile(resultFile, result.getData());

        Integer[][] expected = readMatrixFromFile(Paths.get(EXPECTED_DIR, "expected_subtraction.txt").toString());
        assertTrue("Odejmowanie macierzy 2x2 nie powiodło się.", matricesEqual(expected, result.getData()));
    }

    @Test
    public void testMatrixMultiplication2x2() throws Exception {
        Integer[][] dataA = readMatrixFromFile(Paths.get(INPUT_DIR, "matrixA.txt").toString());
        Integer[][] dataB = readMatrixFromFile(Paths.get(INPUT_DIR, "matrixB.txt").toString());
        Matrix<Integer> matrixA = new Matrix<>(dataA, operations);
        Matrix<Integer> matrixB = new Matrix<>(dataB, operations);

        Matrix<Integer> result = matrixA.multiply(matrixB);
        String resultFile = Paths.get(RESULT_DIR, "multiplication_result_2x2.txt").toString();
        writeMatrixToFile(resultFile, result.getData());

        Integer[][] expected = readMatrixFromFile(Paths.get(EXPECTED_DIR, "expected_multiplication.txt").toString());
        assertTrue("Mnożenie macierzy 2x2 nie powiodło się.", matricesEqual(expected, result.getData()));
    }

    @Test
    public void testMatrixDeterminant2x2() throws Exception {
        Integer[][] dataA = readMatrixFromFile(Paths.get(INPUT_DIR, "matrixA.txt").toString());
        Matrix<Integer> matrixA = new Matrix<>(dataA, operations);

        Integer det = matrixA.determinant();
        String resultFile = Paths.get(RESULT_DIR, "determinant_result_2x2.txt").toString();
        Files.write(Paths.get(resultFile), det.toString().getBytes());

        String expectedStr = new String(Files.readAllBytes(Paths.get(EXPECTED_DIR, "expected_determinant.txt")));
        Integer expected = Integer.parseInt(expectedStr.trim());
        assertEquals("Wyznacznik macierzy 2x2 jest niepoprawny.", expected, det);
    }

    // === Testy dla macierzy 3x3 ===

    @Test
    public void testMatrixAddition3x3() throws Exception {
        Integer[][] dataA = readMatrixFromFile(Paths.get(INPUT_DIR, "matrix3x3_A.txt").toString());
        Integer[][] dataB = readMatrixFromFile(Paths.get(INPUT_DIR, "matrix3x3_B.txt").toString());
        Matrix<Integer> matrixA = new Matrix<>(dataA, operations);
        Matrix<Integer> matrixB = new Matrix<>(dataB, operations);

        Matrix<Integer> result = matrixA.add(matrixB);
        String resultFile = Paths.get(RESULT_DIR, "addition_result_3x3.txt").toString();
        writeMatrixToFile(resultFile, result.getData());

        Integer[][] expected = readMatrixFromFile(Paths.get(EXPECTED_DIR, "expected_addition_3x3.txt").toString());
        assertTrue("Dodawanie macierzy 3x3 nie powiodło się.", matricesEqual(expected, result.getData()));
    }

    @Test
    public void testMatrixSubtraction3x3() throws Exception {
        Integer[][] dataA = readMatrixFromFile(Paths.get(INPUT_DIR, "matrix3x3_A.txt").toString());
        Integer[][] dataB = readMatrixFromFile(Paths.get(INPUT_DIR, "matrix3x3_B.txt").toString());
        Matrix<Integer> matrixA = new Matrix<>(dataA, operations);
        Matrix<Integer> matrixB = new Matrix<>(dataB, operations);

        Matrix<Integer> result = matrixA.subtract(matrixB);
        String resultFile = Paths.get(RESULT_DIR, "subtraction_result_3x3.txt").toString();
        writeMatrixToFile(resultFile, result.getData());

        Integer[][] expected = readMatrixFromFile(Paths.get(EXPECTED_DIR, "expected_subtraction_3x3.txt").toString());
        assertTrue("Odejmowanie macierzy 3x3 nie powiodło się.", matricesEqual(expected, result.getData()));
    }

    @Test
    public void testMatrixMultiplication3x3() throws Exception {
        Integer[][] dataA = readMatrixFromFile(Paths.get(INPUT_DIR, "matrix3x3_A.txt").toString());
        Integer[][] dataB = readMatrixFromFile(Paths.get(INPUT_DIR, "matrix3x3_B.txt").toString());
        Matrix<Integer> matrixA = new Matrix<>(dataA, operations);
        Matrix<Integer> matrixB = new Matrix<>(dataB, operations);

        Matrix<Integer> result = matrixA.multiply(matrixB);
        String resultFile = Paths.get(RESULT_DIR, "multiplication_result_3x3.txt").toString();
        writeMatrixToFile(resultFile, result.getData());

        Integer[][] expected = readMatrixFromFile(Paths.get(EXPECTED_DIR, "expected_multiplication_3x3.txt").toString());
        assertTrue("Mnożenie macierzy 3x3 nie powiodło się.", matricesEqual(expected, result.getData()));
    }

    @Test
    public void testMatrixDeterminant3x3() throws Exception {
        Integer[][] dataA = readMatrixFromFile(Paths.get(INPUT_DIR, "matrix3x3_A.txt").toString());
        Matrix<Integer> matrixA = new Matrix<>(dataA, operations);

        Integer det = matrixA.determinant();
        String resultFile = Paths.get(RESULT_DIR, "determinant_result_3x3.txt").toString();
        Files.write(Paths.get(resultFile), det.toString().getBytes());

        String expectedStr = new String(Files.readAllBytes(Paths.get(EXPECTED_DIR, "expected_determinant_3x3.txt")));
        Integer expected = Integer.parseInt(expectedStr.trim());
        assertEquals("Wyznacznik macierzy 3x3 jest niepoprawny.", expected, det);
    }

    // === Testy dla macierzy 4x4 ===

    @Test
    public void testMatrixAddition4x4() throws Exception {
        Integer[][] dataE = readMatrixFromFile(Paths.get(INPUT_DIR, "matrix4x4_E.txt").toString());
        Integer[][] dataF = readMatrixFromFile(Paths.get(INPUT_DIR, "matrix4x4_F.txt").toString());
        Matrix<Integer> matrixE = new Matrix<>(dataE, operations);
        Matrix<Integer> matrixF = new Matrix<>(dataF, operations);

        Matrix<Integer> result = matrixE.add(matrixF);
        String resultFile = Paths.get(RESULT_DIR, "addition_result_4x4.txt").toString();
        writeMatrixToFile(resultFile, result.getData());

        Integer[][] expected = readMatrixFromFile(Paths.get(EXPECTED_DIR, "expected_addition_4x4.txt").toString());
        assertTrue("Dodawanie macierzy 4x4 nie powiodło się.", matricesEqual(expected, result.getData()));
    }

    @Test
    public void testMatrixSubtraction4x4() throws Exception {
        Integer[][] dataE = readMatrixFromFile(Paths.get(INPUT_DIR, "matrix4x4_E.txt").toString());
        Integer[][] dataF = readMatrixFromFile(Paths.get(INPUT_DIR, "matrix4x4_F.txt").toString());
        Matrix<Integer> matrixE = new Matrix<>(dataE, operations);
        Matrix<Integer> matrixF = new Matrix<>(dataF, operations);

        Matrix<Integer> result = matrixE.subtract(matrixF);
        String resultFile = Paths.get(RESULT_DIR, "subtraction_result_4x4.txt").toString();
        writeMatrixToFile(resultFile, result.getData());

        Integer[][] expected = readMatrixFromFile(Paths.get(EXPECTED_DIR, "expected_subtraction_4x4.txt").toString());
        assertTrue("Odejmowanie macierzy 4x4 nie powiodło się.", matricesEqual(expected, result.getData()));
    }

    @Test
    public void testMatrixMultiplication4x4() throws Exception {
        Integer[][] dataE = readMatrixFromFile(Paths.get(INPUT_DIR, "matrix4x4_E.txt").toString());
        Integer[][] dataF = readMatrixFromFile(Paths.get(INPUT_DIR, "matrix4x4_F.txt").toString());
        Matrix<Integer> matrixE = new Matrix<>(dataE, operations);
        Matrix<Integer> matrixF = new Matrix<>(dataF, operations);

        Matrix<Integer> result = matrixE.multiply(matrixF);
        String resultFile = Paths.get(RESULT_DIR, "multiplication_result_4x4.txt").toString();
        writeMatrixToFile(resultFile, result.getData());

        Integer[][] expected = readMatrixFromFile(Paths.get(EXPECTED_DIR, "expected_multiplication_4x4.txt").toString());
        assertTrue("Mnożenie macierzy 4x4 nie powiodło się.", matricesEqual(expected, result.getData()));
    }

    @Test
    public void testMatrixDeterminant4x4() throws Exception {
        Integer[][] dataE = readMatrixFromFile(Paths.get(INPUT_DIR, "matrix4x4_E.txt").toString());
        Matrix<Integer> matrixE = new Matrix<>(dataE, operations);

        Integer det = matrixE.determinant();
        String resultFile = Paths.get(RESULT_DIR, "determinant_result_4x4.txt").toString();
        Files.write(Paths.get(resultFile), det.toString().getBytes());

        String expectedStr = new String(Files.readAllBytes(Paths.get(EXPECTED_DIR, "expected_determinant_4x4.txt")));
        Integer expected = Integer.parseInt(expectedStr.trim());
        assertEquals("Wyznacznik macierzy 4x4 jest niepoprawny.", expected, det);
    }

    /**
     * Testuje wyrzucenie wyjątku przy próbie dodawania macierzy o niezgodnych wymiarach.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAdditionDimensionMismatch() {
        // Macierz 2x2
        Integer[][] dataA = { {1, 2}, {3, 4} };
        // Macierz 2x3
        Integer[][] dataB = { {5, 6, 7}, {8, 9, 10} };

        Matrix<Integer> matrixA = new Matrix<>(dataA, operations);
        Matrix<Integer> matrixB = new Matrix<>(dataB, operations);
        matrixA.add(matrixB);
    }
}
