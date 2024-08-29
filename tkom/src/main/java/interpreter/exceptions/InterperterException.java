package interpreter.exceptions;

import inputHandle.Position;

public class InterperterException extends Exception {
    public InterperterException(String message, Position pos) {
        super(("Interpreter run into error: %s, in line %d, " +
                "character %d").formatted(message, pos.getLine(), pos.getcharacter()));
    }
}
