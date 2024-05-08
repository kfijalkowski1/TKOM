package lekser.tokenBuilderUtils;

import lekser.exceptions.LekserException;

@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Exception> {
    R apply(T t) throws E, LekserException;
}
