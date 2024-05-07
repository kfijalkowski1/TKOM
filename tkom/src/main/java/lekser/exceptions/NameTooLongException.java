package lekser.exceptions;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class NameTooLongException extends LekserException {

    public NameTooLongException(ImmutablePair<Integer, Integer> pos, String txt) {
        super(pos, txt);
    }
}
