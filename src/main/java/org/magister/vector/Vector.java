package org.magister.vector;
import org.magister.helper.NumberOperations;
import org.magister.helper.Numberxx;
import org.magister.helper.NumberxxOperations;

import java.lang.reflect.Array;

public class Vector<T extends Numberxx> {
    private final T[] coordinates;
    private final NumberxxOperations operations;

    public Vector(T[] coordinates, NumberxxOperations operations) {
        this.coordinates = coordinates.clone();
        this.operations = operations;
    }

    public T[] getCoordinates() {
        return coordinates;
    }

    // Metoda dodawania wektorów (element-wise)
    public Vector<T> add(Vector<T> vector) {
        if (this.coordinates.length != vector.coordinates.length) {
            throw new IllegalArgumentException("Wektory muszą mieć taką samą wymiarowość");
        }
        int n = this.coordinates.length;
        T[] result = (T[]) new Numberxx[n]; // tworzenie tablicy – kompromis ze względu na ograniczenia Javy
        for (int i = 0; i < n; i++) {
            result[i] = (T) operations.add((Numberxx) this.coordinates[i], (Numberxx) vector.coordinates[i]);
        }
        return new Vector<>(result, operations);
    }

    // Metoda obliczania przeciwnego wektora (negacja) poprzez: 0 - coordinate
    public Vector<T> opposite() {
        int n = this.coordinates.length;
        T[] result = (T[]) new Numberxx[n];
        for (int i = 0; i < n; i++) {
            result[i] = (T) operations.subtract(operations.zero(), (Numberxx) this.coordinates[i]);
        }
        return new Vector<>(result, operations);
    }

    // Metoda 1: odejmowanie przez dodanie przeciwnego wektora
    public Vector<T> subtractVectorNegativeAdd(Vector<T> vector) {
        return this.add(vector.opposite());
    }

    // Metoda 2: bezpośrednie odejmowanie elementów
    public Vector<T> subtruct(Vector<T> vector) {
        if (this.coordinates.length != vector.coordinates.length) {
            throw new IllegalArgumentException("Wektory muszą mieć taką samą wymiarowość");
        }
        int n = this.coordinates.length;
        T[] result = (T[]) new Numberxx[n];
        for (int i = 0; i < n; i++) {
            result[i] = (T) operations.subtract((Numberxx) this.coordinates[i], (Numberxx) vector.coordinates[i]);
        }
        return new Vector<>(result, operations);
    }

    // Mnożenie wektora przez skalar
    // And your Vector class method remains similar, using a reflection-safe array creation:
    public Vector<T> multiplyByScalar(Numberxx scalar) {
        int n = this.coordinates.length;
        @SuppressWarnings("unchecked")
        T[] result = (T[]) Array.newInstance(coordinates.getClass().getComponentType(), n);

        for (int i = 0; i < n; i++) {
            // Assuming operations.multiply returns a Numberxx that is castable to T
            result[i] = (T) operations.multiply((Numberxx) this.coordinates[i], (Numberxx) scalar);
        }
        return new Vector<>(result, operations);
    }
    // Iloczyn skalarny (dot product): suma iloczynów odpowiadających sobie współrzędnych
    public T dotProduct(Vector<T> vector) {
        if (this.coordinates.length != vector.coordinates.length) {
            throw new IllegalArgumentException("Wektory muszą mieć taką samą wymiarowość");
        }
        int n = this.coordinates.length;
        T sum = (T) operations.zero();
        for (int i = 0; i < n; i++) {
            T product = (T) operations.multiply((Numberxx) this.coordinates[i], (Numberxx) vector.coordinates[i]);
            sum = (T) operations.add((Numberxx) sum, (Numberxx) product);
        }
        return sum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Vector{");
        for (int i = 0; i < coordinates.length; i++) {
            sb.append(coordinates[i]);
            if (i < coordinates.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
