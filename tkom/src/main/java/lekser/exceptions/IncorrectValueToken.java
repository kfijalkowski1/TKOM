package lekser.exceptions;


import inputHandle.Position;

public class IncorrectValueToken extends LekserException {

    public IncorrectValueToken(Position pos, String txt) {
        super(pos, "Token has incorrect value for it's type, expected type: %s".formatted(txt));
    }
}
