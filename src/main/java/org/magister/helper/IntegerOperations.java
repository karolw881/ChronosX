package org.magister.helper;

/**
 * Implementacja operacji dla Integer
 */
public class IntegerOperations implements NumberOperations<Numberxx> {
    @Override
    public Numberxx add(Numberxx a, Numberxx b) {
        // Assuming Numberxx has a method to get its value
        int result = a.intValue() + b.intValue();
        return Numberxx.valueOf(result);
    }

    @Override
    public Numberxx subtract(Numberxx a, Numberxx b) {
        int result = a.intValue() - b.intValue();
        return Numberxx.valueOf(result);
    }

    @Override
    public Numberxx multiply(Numberxx a, Numberxx b) {
        int result = a.intValue() * b.intValue();
        return Numberxx.valueOf(result);
    }

    @Override
    public Numberxx divide(Numberxx a, Numberxx b) {
        if (b.intValue() == 0) throw new ArithmeticException("Dzielenie przez zero");
        int result = a.intValue() / b.intValue();
        return Numberxx.valueOf(result);
    }

    @Override
    public Numberxx zero() {
        return Numberxx.valueOf(0);
    }

    @Override
    public Numberxx one() {
        return Numberxx.valueOf(1);
    }

    @Override
    public Numberxx valueOf(int value) {
        return Numberxx.valueOf(value);
    }
}