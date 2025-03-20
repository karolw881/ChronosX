package org.magister.bubbleSort;

import java.lang.reflect.Method;

/**
 * Implementacja z użyciem refleksji
 */

public class BubbleSort<T extends Number & Comparable<T>> {

    public T[] sort(T[] arr) {
        int n = arr.length;
        boolean swapped;

        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                // Porównanie elementów przy użyciu compareTo()
                if (arr[j].compareTo(arr[j + 1]) > 0) {
                    // Zamiana elementów miejscami
                    T temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) { // Jeśli podczas przejścia nie było zamiany, sortowanie jest zakończone
                break;
            }
        }
        return arr;
    }
}