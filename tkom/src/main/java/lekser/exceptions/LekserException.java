package lekser.exceptions;

import exceptions.AnalizerException;
import inputHandle.Position;

public class LekserException  extends AnalizerException {
    public LekserException(Position pos, String txt) {
        super("Lekser finished because of exception at: line: %d, character: %d\t%s".formatted(pos.getLine(), pos.getcharacter(), txt));
    }
}
