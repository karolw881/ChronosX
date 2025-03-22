package org.magister.helper;

import java.lang.reflect.Method;

public class NumberxxOperations implements NumberOperations<Numberxx> {

    private static Method getMethod(String methodName, Class<?>... parameterTypes) {
        try {
            return Numberxx.class.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Method not found: " + methodName, e);
        }
    }

    private static Numberxx invokeMethod(String methodName, Numberxx a, Numberxx b) {
        try {
            Method method = getMethod(methodName, Numberxx.class);
            return (Numberxx) method.invoke(a, b);
        } catch (Exception e) {
            throw new RuntimeException("Error invoking method: " + methodName, e);
        }
    }

    @Override
    public Numberxx add(Numberxx a, Numberxx b) {
        return invokeMethod("add", a, b);
    }

    @Override
    public Numberxx subtract(Numberxx a, Numberxx b) {
        return invokeMethod("subtract", a, b);
    }

    @Override
    public Numberxx multiply(Numberxx a, Numberxx b) {
        return invokeMethod("multiply", a, b);
    }

    @Override
    public Numberxx divide(Numberxx a, Numberxx b) {
        if (b.intValue() == 0) {
            throw new ArithmeticException("Dzielenie przez zero");
        }
        return invokeMethod("divide", a, b);
    }

    @Override
    public Numberxx zero() {
        try {
            Method method = getMethod("valueOf", int.class);
            return (Numberxx) method.invoke(null, 0);
        } catch (Exception e) {
            throw new RuntimeException("Error creating zero", e);
        }
    }

    @Override
    public Numberxx one() {
        try {
            Method method = getMethod("valueOf", int.class);
            return (Numberxx) method.invoke(null, 1);
        } catch (Exception e) {
            throw new RuntimeException("Error creating one", e);
        }
    }

    @Override
    public Numberxx valueOf(int value) {
        try {
            Method method = getMethod("valueOf", int.class);
            return (Numberxx) method.invoke(null, value);
        } catch (Exception e) {
            throw new RuntimeException("Error creating value", e);
        }
    }
}