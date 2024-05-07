package lekser.exceptions;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class UnexpectedEndOfFile extends LekserException {

    public UnexpectedEndOfFile(ImmutablePair<Integer, Integer> pos, String txt) {
        super(pos, txt);
    }
}
