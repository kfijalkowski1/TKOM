package lekser.exceptions;

import inputHandle.Position;

public class UnexpectedEndOfFile extends LekserException {

    public UnexpectedEndOfFile(Position pos, String txt) {
        super(pos, txt);
    }
}
