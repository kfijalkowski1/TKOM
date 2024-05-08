package parser.exceptions;

import exceptions.AnalizerException;
import inputHandle.Position;

public class ParserException extends AnalizerException {
    public ParserException(Position pos, String txt) {
        super("Parser finished because of exception at: line: %d, character: %d\t%s".formatted(pos.getLine(), pos.getcharacter(), txt));
    }
}
