package lekser.exceptions;


import org.apache.commons.lang3.tuple.ImmutablePair;

public class IncorrectValueToken extends LekserException {

    public IncorrectValueToken(ImmutablePair<Integer, Integer> pos, String txt) {
        super(pos, "Token has incorrect value for it's type, expected type: %s".formatted(txt));
    }
}
