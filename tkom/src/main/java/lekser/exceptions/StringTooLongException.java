package lekser.exceptions;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class StringTooLongException extends LekserException {

    public StringTooLongException(ImmutablePair<Integer, Integer> pos, String txt) {
        super(pos, txt);
    }
}
