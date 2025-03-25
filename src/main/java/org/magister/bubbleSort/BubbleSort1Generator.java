package org.magister.bubbleSort;

import java.util.Random;

public class BubbleSort1Generator {

    /**
     * Generuje tablicę int według określonego porządku.
     *
     * @param kind typ generowanej tablicy określony przez {@link KindOfBubbleSort} (RANDOM, SORTED, REVERSED)
     * @param size rozmiar tablicy
     * @param seed ziarno dla generatora liczb losowych (używane przy generowaniu wartości losowych)
     * @return instancja BubbleSort1 zawierająca wygenerowaną tablicę
     */
    public BubbleSort1 createArray(KindOfBubbleSort kind, int size, long seed) {
        Random randomLocal = new Random(seed);
        int[] data = new int[size];

        switch (kind) {
            case RANDOM:
                for (int i = 0; i < size; i++) {
                    data[i] = randomLocal.nextInt(1000);
                }
                break;
            case SORTED:
                for (int i = 0; i < size; i++) {
                    data[i] = i;
                }
                break;
            case REVERSED:
                for (int i = 0; i < size; i++) {
                    data[i] = size - i;
                }
                break;
            default:
                throw new IllegalArgumentException("Nieznany typ generowania tablicy: " + kind);
        }
        return new BubbleSort1(data);
    }
}
