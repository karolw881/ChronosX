package org.magister.helper;

public interface NumberOperations<T extends Number> {
    T add(T a, T b);
    T subtract(T a, T b);
    T multiply(T a, T b);
    T divide(T a, T b);
    T zero();
    T one();
    T valueOf(int value);
}
