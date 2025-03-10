package org.magister.vector;



// Implementacja konkretna dla liczb całkowitych
public class Vector1 {
    private int[] coordinates;

    public Vector1(int[] coordinates) {
        this.coordinates = coordinates.clone();
    }

    public Vector1 addVector(Vector1 other) {
        if (this.coordinates.length != other.coordinates.length) {
            throw new IllegalArgumentException("Vectors must have same dimension");
        }
        int[] result = new int[coordinates.length];
        for (int i = 0; i < coordinates.length; i++) {
            result[i] = this.coordinates[i] + other.coordinates[i];
        }
        return new Vector1(result);
    }

    public Vector1 opposite() {
        int[] result = new int[coordinates.length];
        for (int i = 0; i < coordinates.length; i++) {
            result[i] = -coordinates[i];
        }
        return new Vector1(result);
    }

    public Vector1 subVector(Vector1 other) {
        // Metoda 1: bezpośrednie odejmowanie
        return this.addVector(other.opposite());
    }

    public Vector1 subVectorDirect(Vector1 other) {
        // Metoda 2: bezpośrednie odejmowanie
        if (this.coordinates.length != other.coordinates.length) {
            throw new IllegalArgumentException("Vectors must have same dimension");
        }
        int[] result = new int[coordinates.length];
        for (int i = 0; i < coordinates.length; i++) {
            result[i] = this.coordinates[i] - other.coordinates[i];
        }
        return new Vector1(result);
    }

    public Vector1 multiplyByScalar(int scalar) {
        int[] result = new int[coordinates.length];
        for (int i = 0; i < coordinates.length; i++) {
            result[i] = coordinates[i] * scalar;
        }
        return new Vector1(result);
    }

    public int dotProduct(Vector1 other) {
        if (this.coordinates.length != other.coordinates.length) {
            throw new IllegalArgumentException("Vectors must have same dimension");
        }
        int sum = 0;
        for (int i = 0; i < coordinates.length; i++) {
            sum += this.coordinates[i] * other.coordinates[i];
        }
        return sum;
    }

    public double length() {
        int sum = 0;
        for (int coordinate : coordinates) {
            sum += coordinate * coordinate;
        }
        return Math.sqrt(sum);
    }
}