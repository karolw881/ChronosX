package org.magister.helper;

public class Numberxx extends Number implements Comparable<Numberxx> {
    private final int value;

    public Numberxx(int value) {
        this.value = value;
    }

    public static Numberxx valueOf(int value) {
        return new Numberxx(value);
    }

    public static int nextInt(int bound) {
        if (bound <= 0) {
            throw new IllegalArgumentException("Bound must be positive");
        }
        return (int)(Math.random() * bound);
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    // Metody arytmetyczne
    public Numberxx add(Numberxx other) {
        return new Numberxx(this.value + other.value);
    }

    public Numberxx subtract(Numberxx other) {
        return new Numberxx(this.value - other.value);
    }

    public Numberxx multiply(Numberxx other) {
        return new Numberxx(this.value * other.value);
    }

    public Numberxx divide(Numberxx other) {
        if (other.value == 0) {
            throw new ArithmeticException("Dzielenie przez zero");
        }
        return new Numberxx(this.value / other.value);
    }

    public <T extends Number> T one() {
        return (T) new Numberxx(1);
    }

    @Override
    public int compareTo(Numberxx other) {
        // Compare based on integer value
        return Integer.compare(this.value, other.value);
    }
}