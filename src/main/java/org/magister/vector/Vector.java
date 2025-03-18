package org.magister.vector;
import org.magister.helper.NumberOperations;

public class Vector<T extends Number> {
    private T[] coordinates;
    private NumberOperations<T> operations;

    public Vector(T[] coordinates, NumberOperations<T> operations) {
        this.coordinates = coordinates.clone();
        this.operations = operations;
    }

    public T[] getCoordinates() {
        return coordinates;
    }

    //   Metoda dodawania wektorów (element-wise)
    public Vector<T> add(Vector<T> vector) {
        if (this.coordinates.length != vector.coordinates.length) {
            throw new IllegalArgumentException("Wektory muszą mieć taką samą wymiarowość");
        }
        int n = this.coordinates.length;
        T[] result = (T[]) new Number[n]; // tworzenie tablicy – kompromis ze względu na ograniczenia Javy
        for (int i = 0; i < n; i++) {
            result[i] = operations.add(this.coordinates[i], vector.coordinates[i]);
        }
        return new Vector<>(result, operations);
    }

    // Metoda obliczania przeciwnego wektora (negacja) poprzez: 0 - coordinate
    public Vector<T> opposite() {
        int n = this.coordinates.length;
        T[] result = (T[]) new Number[n];
        for (int i = 0; i < n; i++) {
            result[i] = operations.subtract(operations.zero(), this.coordinates[i]);
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
        T[] result = (T[]) new Number[n];
        for (int i = 0; i < n; i++) {
            result[i] = operations.subtract(this.coordinates[i], vector.coordinates[i]);
        }
        return new Vector<>(result, operations);
    }

    // Mnożenie wektora przez skalar
    public Vector<T> multiplyByScalar(T scalar) {
        int n = this.coordinates.length;
        @SuppressWarnings("unchecked")
        T[] result = (T[]) new Number[n];
        for (int i = 0; i < n; i++) {
            result[i] = operations.multiply(this.coordinates[i], scalar);
        }
        return new Vector<>(result, operations);
    }

    // Iloczyn skalarny (dot product): suma iloczynów odpowiadających sobie współrzędnych
    public T dotProduct(Vector<T> vector) {
        if (this.coordinates.length != vector.coordinates.length) {
            throw new IllegalArgumentException("Wektory muszą mieć taką samą wymiarowość");
        }
        int n = this.coordinates.length;
        T sum = operations.zero();
        for (int i = 0; i < n; i++) {
            T product = operations.multiply(this.coordinates[i], vector.coordinates[i]);
            sum = operations.add(sum, product);
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
