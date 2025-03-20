package org.magister.bubbleSort;

import java.util.Random;
import org.magister.helper.NumberOperations;

/**
 * Generator tablic dla sortowania bąbelkowego.
 * Klasa umożliwia tworzenie tablic (T[]) określonych przez enum {@link KindOfBubbleSort},
 * dla typów rozszerzających {@link Number}.
 *
 * Uwaga: Zakładamy, że przekazany NumberOperations<T> udostępnia metodę:
 * - T valueOf(int value) – konwersja liczby int do typu T.
 */
public class BubbleSortGenerator<T extends Number> {

    private final NumberOperations<T> operations;

    public BubbleSortGenerator(NumberOperations<T> operations) {
        this.operations = operations;
    }

    /**
     * Generuje tablicę typu T[] według określonego porządku.
     *
     * @param kind rodzaj tablicy określony przez {@link KindOfBubbleSort} (RANDOM, SORTED, REVERSED)
     * @param size rozmiar tablicy
     * @param seed ziarno dla generatora liczb losowych (używane przy generowaniu wartości losowych)
     * @return wygenerowana tablica T[]
     */
    public T[] createArray(KindOfBubbleSort kind, int size, long seed) {
        Random randomLocal = new Random(seed);
        T[] data = (T[]) new Number[size];

        switch (kind) {
            case RANDOM:
                for (int i = 0; i < size; i++) {
                    data[i] = operations.valueOf(randomLocal.nextInt(1000));
                }
                break;
            case SORTED:
                for (int i = 0; i < size; i++) {
                    data[i] = operations.valueOf(i);
                }
                break;
            case REVERSED:
                for (int i = 0; i < size; i++) {
                    data[i] = operations.valueOf(size - i);
                }
                break;
            default:
                throw new IllegalArgumentException("Nieznany typ generacji tablicy: " + kind);
        }
        return data;
    }
}
