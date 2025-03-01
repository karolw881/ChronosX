package org.magister.helper;


// Implementacja operacji dla Double
public class DoubleOperations implements NumberOperations<Double> {
    @Override
    public Double add(Double a, Double b) {
        return a + b;
    }

    @Override
    public Double subtract(Double a, Double b) {
        return a - b;
    }

    @Override
    public Double multiply(Double a, Double b) {
        return a * b;
    }

    @Override
    public Double divide(Double a, Double b) {
        if (b == 0) throw new ArithmeticException("Dzielenie przez zero");
        return a / b;
    }

    @Override
    public Double zero() {
        return 0.0;
    }

    @Override
    public Double one() {
        return 1.0;
    }
}