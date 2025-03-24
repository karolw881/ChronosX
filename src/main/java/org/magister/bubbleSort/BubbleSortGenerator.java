package org.magister.bubbleSort;

import java.util.Random;
import org.magister.helper.Numberxx;
import org.magister.helper.NumberxxOperations;

public class BubbleSortGenerator {

    private final NumberxxOperations operations;

    public BubbleSortGenerator(NumberxxOperations operations) {
        this.operations = operations;
    }

    /**
     * Generates an array of Numberxx according to a specified order.
     *
     * @param kind array type defined by {@link KindOfBubbleSort} (RANDOM, SORTED, REVERSED)
     * @param size array size
     * @param seed seed for random number generator (used when generating random values)
     * @return a BubbleSort instance with the generated array
     */
    public BubbleSort<Numberxx> createArray(KindOfBubbleSort kind, int size, long seed) {
        Random randomLocal = new Random(seed);
        Numberxx[] data = new Numberxx[size];

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
                throw new IllegalArgumentException("Unknown array generation type: " + kind);
        }
        return new BubbleSort<>(data);
    }
}
