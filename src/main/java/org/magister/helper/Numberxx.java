package org.magister.helper;

public class Numberxx extends Number {
    private final int value;

    public Numberxx(int value) {
        this.value = value;
    }

    // Implementacja metod abstrakcyjnych z klasy Number
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

    // Przykładowe metody arytmetyczne
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
}
