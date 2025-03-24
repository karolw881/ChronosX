package org.magister.bubbleSort;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.magister.helper.Numberxx;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Getter
@Setter
@AllArgsConstructor
public class BubbleSort<T extends Numberxx> {
    private T[] arr;

    /**
     * Sorts the array using bubble sort.
     *
     * @return the sorted array
     */
    public T[] sort() {
        int n = arr.length;
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                // Use the compareTo method from Numberxx (which implements Comparable<Numberxx>)
                if (arr[j].compareTo(arr[j + 1]) > 0) {
                    T temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) { // Array is already sorted
                break;
            }
        }
        return arr;
    }

    /**
     * Saves the sorted array to a file.
     *
     * @param filename the file path where the sorted array will be saved
     */
    public void saveSortedArrayToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Sorted Array:\n");
            for (T element : arr) {
                writer.write(element.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving sorted array to file: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("BubbleSort: ");
        for (T element : arr) {
            sb.append(element).append(", ");
        }
        return sb.toString();
    }
}
