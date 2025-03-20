

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.magister.bubbleSort.BubbleSort;
import org.magister.bubbleSort.BubbleSort1;
import org.magister.helper.IntegerOperations;
import org.magister.helper.NumberOperations;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class BubbleSortOperationTest {

    // Stałe określające lokalizację katalogów
    private static final String INPUT_DIR = "src/test/resources/testBubbleSortOperation/input";
    private static final String EXPECTED_DIR = "src/test/resources/testBubbleSortOperation/output/expected";
    private static final String NORMAL_OUTPUT_DIR = "src/test/resources/testBubbleSortOperation/output/normal";
    private static final String GENERIC_OUTPUT_DIR = "src/test/resources/testBubbleSortOperation/output/generic";

    /**
     * Metoda wykonywana przed uruchomieniem testów.
     * Tworzy wymagane katalogi oraz generuje pliki wejściowe i oczekiwane wyjściowe
     * (dla 100 i 1000 elementów), jeśli jeszcze nie istnieją.
     */
    @BeforeClass
    public static void setupFiles() throws IOException {
        // Tworzymy katalogi, jeśli nie istnieją
        Files.createDirectories(Paths.get(INPUT_DIR));
        Files.createDirectories(Paths.get(EXPECTED_DIR));
        Files.createDirectories(Paths.get(NORMAL_OUTPUT_DIR));
        Files.createDirectories(Paths.get(GENERIC_OUTPUT_DIR));

        // Dla 100 elementów
        Path unsorted100Path = Paths.get(INPUT_DIR, "unsorted100.txt");
        Path expected100Path = Paths.get(EXPECTED_DIR, "sorted100.txt");
        if (!Files.exists(unsorted100Path) || !Files.exists(expected100Path)) {
            Integer[] sorted100 = generateSortedArray(100);
            Integer[] unsorted100 = generateUnsortedArray(100);
            writeArrayToFile(unsorted100Path.toString(), unsorted100);
            writeArrayToFile(expected100Path.toString(), sorted100);
        }

        // Dla 1000 elementów
        Path unsorted1000Path = Paths.get(INPUT_DIR, "unsorted1000.txt");
        Path expected1000Path = Paths.get(EXPECTED_DIR, "sorted1000.txt");
        if (!Files.exists(unsorted1000Path) || !Files.exists(expected1000Path)) {
            Integer[] sorted1000 = generateSortedArray(1000);
            Integer[] unsorted1000 = generateUnsortedArray(1000);
            writeArrayToFile(unsorted1000Path.toString(), unsorted1000);
            writeArrayToFile(expected1000Path.toString(), sorted1000);
        }
    }

    // --- Metody pomocnicze ---

    // Generuje posortowaną tablicę Integer o zadanym rozmiarze.
    private static Integer[] generateSortedArray(int size) {
        Integer[] arr = new Integer[size];
        for (int i = 0; i < size; i++) {
            arr[i] = i + 1;
        }
        return arr;
    }

    // Generuje nieposortowaną (losowo przetasowaną) tablicę Integer o zadanym rozmiarze.
    private static Integer[] generateUnsortedArray(int size) {
        Integer[] sorted = generateSortedArray(size);
        List<Integer> list = new ArrayList<>(Arrays.asList(sorted));
        Collections.shuffle(list);
        return list.toArray(new Integer[0]);
    }

    // Zapisuje tablicę liczb do pliku – liczby oddzielone pojedynczą spacją.
    private static void writeArrayToFile(String fileName, Integer[] arr) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (Integer num : arr) {
            sb.append(num).append(" ");
        }
        Files.write(Paths.get(fileName), sb.toString().trim().getBytes());
    }

    // Odczytuje tablicę liczb z pliku.
    private static Integer[] readArrayFromFile(String fileName) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        List<Integer> numbers = new ArrayList<>();
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] tokens = line.split("\\s+");
            for (String token : tokens) {
                numbers.add(Integer.parseInt(token));
            }
        }
        return numbers.toArray(new Integer[0]);
    }

    // Porównanie dwóch tablic Integer.
    private boolean arraysEqual(Integer[] expected, Object[] actual) {
        if (expected.length != actual.length) return false;
        for (int i = 0; i < expected.length; i++) {
            if (!expected[i].equals(actual[i])) return false;
        }
        return true;
    }

    // --- Testy dla BubbleSort1 (Comparator-based, zwykłe int) ---

    @Test
    public void testBubbleSort1Unsorted100() throws Exception {
        Integer[] input = readArrayFromFile(Paths.get(INPUT_DIR, "unsorted100.txt").toString());
        Integer[] expected = readArrayFromFile(Paths.get(EXPECTED_DIR, "sorted100.txt").toString());
        BubbleSort1 sorter = new BubbleSort1();
        Object[] sorted = sorter.sort(input, Comparator.naturalOrder());
        // Zapis wyniku do katalogu normal
        String outputFile = Paths.get(NORMAL_OUTPUT_DIR, "sorted100_normal.txt").toString();
        writeArrayToFile(outputFile, (Integer[]) sorted);
        assertTrue("BubbleSort1 powinien poprawnie posortować 100 elementów.", arraysEqual(expected, sorted));
    }

    @Test
    public void testBubbleSort1Unsorted1000() throws Exception {
        Integer[] input = readArrayFromFile(Paths.get(INPUT_DIR, "unsorted1000.txt").toString());
        Integer[] expected = readArrayFromFile(Paths.get(EXPECTED_DIR, "sorted1000.txt").toString());
        BubbleSort1 sorter = new BubbleSort1();
        Object[] sorted = sorter.sort(input, Comparator.naturalOrder());
        String outputFile = Paths.get(NORMAL_OUTPUT_DIR, "sorted1000_normal.txt").toString();
        writeArrayToFile(outputFile, (Integer[]) sorted);
        assertTrue("BubbleSort1 powinien poprawnie posortować 1000 elementów.", arraysEqual(expected, sorted));
    }

    @Test
    public void testBubbleSort1Sorted100() throws Exception {
        Integer[] input = readArrayFromFile(Paths.get(EXPECTED_DIR, "sorted100.txt").toString());
        BubbleSort1 sorter = new BubbleSort1();
        Object[] sorted = sorter.sort(input, Comparator.naturalOrder());
        String outputFile = Paths.get(NORMAL_OUTPUT_DIR, "sorted100_sorted_normal.txt").toString();
        writeArrayToFile(outputFile, (Integer[]) sorted);
        assertTrue("BubbleSort1 dla już posortowanej tablicy (100 elementów) powinien zwrócić ten sam układ.", arraysEqual(input, sorted));
    }

    @Test
    public void testBubbleSort1Sorted1000() throws Exception {
        Integer[] input = readArrayFromFile(Paths.get(EXPECTED_DIR, "sorted1000.txt").toString());
        BubbleSort1 sorter = new BubbleSort1();
        Object[] sorted = sorter.sort(input, Comparator.naturalOrder());
        String outputFile = Paths.get(NORMAL_OUTPUT_DIR, "sorted1000_sorted_normal.txt").toString();
        writeArrayToFile(outputFile, (Integer[]) sorted);
        assertTrue("BubbleSort1 dla już posortowanej tablicy (1000 elementów) powinien zwrócić ten sam układ.", arraysEqual(input, sorted));
    }

    // --- Testy dla BubbleSort (refleksja, generyki) ---

    /*
    @Test
    public void testBubbleSortReflectionUnsorted100() throws Exception {
        Integer[] input = readArrayFromFile(Paths.get(INPUT_DIR, "unsorted100.txt").toString());
        Integer[] expected = readArrayFromFile(Paths.get(EXPECTED_DIR, "sorted100.txt").toString());
        BubbleSort<Number> sorter = new BubbleSort<>();
        Object[] sorted = sorter.sort(input, "compareTo");
        String outputFile = Paths.get(GENERIC_OUTPUT_DIR, "sorted100_generic.txt").toString();
        writeArrayToFile(outputFile, (Integer[]) sorted);
        assertTrue("BubbleSort (refleksja) powinien poprawnie posortować 100 elementów.", arraysEqual(expected, sorted));
    }

    @Test
    public void testBubbleSortReflectionUnsorted1000() throws Exception {
        Integer[] input = readArrayFromFile(Paths.get(INPUT_DIR, "unsorted1000.txt").toString());
        Integer[] expected = readArrayFromFile(Paths.get(EXPECTED_DIR, "sorted1000.txt").toString());
        BubbleSort<Number> sorter = new BubbleSort<>();
        Object[] sorted = sorter.sort(input, "compareTo");
        String outputFile = Paths.get(GENERIC_OUTPUT_DIR, "sorted1000_generic.txt").toString();
        writeArrayToFile(outputFile, (Integer[]) sorted);
        assertTrue("BubbleSort (refleksja) powinien poprawnie posortować 1000 elementów.", arraysEqual(expected, sorted));
    }

    @Test
    public void testBubbleSortReflectionSorted100() throws Exception {
        Integer[] input = readArrayFromFile(Paths.get(EXPECTED_DIR, "sorted100.txt").toString());
        BubbleSort<Number> sorter = new BubbleSort<>();
        Object[] sorted = sorter.sort(input, "compareTo");
        String outputFile = Paths.get(GENERIC_OUTPUT_DIR, "sorted100_sorted_generic.txt").toString();
        writeArrayToFile(outputFile, (Integer[]) sorted);
        assertTrue("BubbleSort (refleksja) dla już posortowanej tablicy (100 elementów) powinien zwrócić ten sam układ.", arraysEqual(input, sorted));
    }

    @Test
    public void testBubbleSortReflectionSorted1000() throws Exception {
        Integer[] input = readArrayFromFile(Paths.get(EXPECTED_DIR, "sorted1000.txt").toString());
        BubbleSort<Number> sorter = new BubbleSort<>();
        Object[] sorted = sorter.sort(input, "compareTo");
        String outputFile = Paths.get(GENERIC_OUTPUT_DIR, "sorted1000_sorted_generic.txt").toString();
        writeArrayToFile(outputFile, (Integer[]) sorted);
        assertTrue("BubbleSort (refleksja) dla już posortowanej tablicy (1000 elementów) powinien zwrócić ten sam układ.", arraysEqual(input, sorted));
    }

    // --- Test operacji na liczbach (IntegerOperations) ---

    @Test
    public void testIntegerOperations() {
        NumberOperations<Integer> operations = new IntegerOperations();
        assertEquals("Dodawanie 1 + 2 powinno dać 3", Integer.valueOf(3), operations.add(1, 2));
        assertEquals("Odejmowanie 5 - 3 powinno dać 2", Integer.valueOf(2), operations.subtract(5, 3));
        assertEquals("Mnożenie 4 * 3 powinno dać 12", Integer.valueOf(12), operations.multiply(4, 3));
        assertEquals("Dzielenie 10 / 2 powinno dać 5", Integer.valueOf(5), operations.divide(10, 2));
        assertEquals("Metoda zero() powinna zwrócić 0", Integer.valueOf(0), operations.zero());
        assertEquals("Metoda one() powinna zwrócić 1", Integer.valueOf(1), operations.one());

    }

     */
}
