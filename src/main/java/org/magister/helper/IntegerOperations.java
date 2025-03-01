package org.magister.helper;

/**
 * Implementacja operacji dla Integer
 */

public class IntegerOperations implements NumberOperations<Integer> {
    @Override
    public Integer add(Integer a, Integer b) {
        return a + b;
    }

    @Override
    public Integer subtract(Integer a, Integer b) {
        return a - b;
    }

    @Override
    public Integer multiply(Integer a, Integer b) {
        return a * b;
    }

    @Override
    public Integer divide(Integer a, Integer b) {
        if (b == 0) throw new ArithmeticException("Dzielenie przez zero");
        return a / b;
    }

    @Override
    public Integer zero() {
        return 0;
    }

    @Override
    public Integer one() {
        return 1;
    }
}
