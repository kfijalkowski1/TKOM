package lekser.exceptions;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class LekserException  extends Exception {
    public LekserException(ImmutablePair<Integer, Integer> pos, String txt) {
        super("Lekser finished because of exception at: line: %d, character: %d\t%s".formatted(pos.right, pos.left, txt));
    }
}
