package lekser.exceptions;

import inputHandle.Position;

public class NumberOutOfBoundsException extends LekserException {

    public NumberOutOfBoundsException(Position pos, String txt) {
        super(pos, txt);
    }
}
