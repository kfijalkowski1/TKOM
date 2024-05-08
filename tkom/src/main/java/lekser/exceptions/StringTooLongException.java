package lekser.exceptions;

import inputHandle.Position;

public class StringTooLongException extends LekserException {

    public StringTooLongException(Position pos, String txt) {
        super(pos, txt);
    }
}
