package org.magister.vector;



import org.magister.helper.NumberOperations;

public class VectorR<T extends Number> {
    private T[] coordinates;
    private NumberOperations<T> operations;

    public VectorR(T[] coordinates, NumberOperations<T> operations) {
        this.coordinates = coordinates.clone();
        this.operations = operations;
    }

    public T[] getCoordinates() {
        return coordinates;
    }

    // Metoda dodawania wektorów (element-wise)
    public VectorR<T> addVector(VectorR<T> other) {
        if (this.coordinates.length != other.coordinates.length) {
            throw new IllegalArgumentException("Wektory muszą mieć taką samą wymiarowość");
        }
        int n = this.coordinates.length;
        @SuppressWarnings("unchecked")
        T[] result = (T[]) new Number[n]; // tworzenie tablicy – kompromis ze względu na ograniczenia Javy
        for (int i = 0; i < n; i++) {
            result[i] = operations.add(this.coordinates[i], other.coordinates[i]);
        }
        return new VectorR<>(result, operations);
    }

    // Metoda obliczania przeciwnego wektora (negacja) poprzez: 0 - coordinate
    public VectorR<T> opposite() {
        int n = this.coordinates.length;
        @SuppressWarnings("unchecked")
        T[] result = (T[]) new Number[n];
        for (int i = 0; i < n; i++) {
            result[i] = operations.subtract(operations.zero(), this.coordinates[i]);
        }
        return new VectorR<>(result, operations);
    }

    // Metoda 1: odejmowanie przez dodanie przeciwnego wektora
    public VectorR<T> subtractVector1(VectorR<T> other) {
        return this.addVector(other.opposite());
    }

    // Metoda 2: bezpośrednie odejmowanie elementów
    public VectorR<T> subtractVector2(VectorR<T> other) {
        if (this.coordinates.length != other.coordinates.length) {
            throw new IllegalArgumentException("Wektory muszą mieć taką samą wymiarowość");
        }
        int n = this.coordinates.length;
        @SuppressWarnings("unchecked")
        T[] result = (T[]) new Number[n];
        for (int i = 0; i < n; i++) {
            result[i] = operations.subtract(this.coordinates[i], other.coordinates[i]);
        }
        return new VectorR<>(result, operations);
    }

    // Mnożenie wektora przez skalar
    public VectorR<T> multiplyByScalar(T scalar) {
        int n = this.coordinates.length;
        @SuppressWarnings("unchecked")
        T[] result = (T[]) new Number[n];
        for (int i = 0; i < n; i++) {
            result[i] = operations.multiply(this.coordinates[i], scalar);
        }
        return new VectorR<>(result, operations);
    }

    // Iloczyn skalarny (dot product): suma iloczynów odpowiadających sobie współrzędnych
    public T dotProduct(VectorR<T> other) {
        if (this.coordinates.length != other.coordinates.length) {
            throw new IllegalArgumentException("Wektory muszą mieć taką samą wymiarowość");
        }
        int n = this.coordinates.length;
        T sum = operations.zero();
        for (int i = 0; i < n; i++) {
            T product = operations.multiply(this.coordinates[i], other.coordinates[i]);
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
