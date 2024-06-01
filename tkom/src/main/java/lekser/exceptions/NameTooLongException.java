package lekser.exceptions;

import inputHandle.Position;
import org.apache.commons.lang3.tuple.ImmutablePair;

public class NameTooLongException extends LekserException {

    public NameTooLongException(Position pos, String txt) {
        super(pos, txt);
    }
}
