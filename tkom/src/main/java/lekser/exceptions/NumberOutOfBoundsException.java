package lekser.exceptions;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class NumberOutOfBoundsException extends LekserException {

    public NumberOutOfBoundsException(ImmutablePair<Integer, Integer> pos, String txt) {
        super(pos, txt);
    }
}
