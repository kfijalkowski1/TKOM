package lekser;

import inputHandle.Possition;
import org.apache.commons.lang3.tuple.ImmutablePair;

public class Token {
    private final TokenType tokenType;
    private Object value;
    private final ImmutablePair<Integer, Integer> possition;

    public Token(TokenType tokenType, ImmutablePair<Integer, Integer> possition, Object value) {
        this.tokenType = tokenType;
        this.value = value;
        this.possition = possition;
    }
    public Token(TokenType tokenType, ImmutablePair<Integer, Integer> possition) {
        this.tokenType = tokenType;
        this.possition = possition;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public Object getValue() {
        return value;
    }

    public ImmutablePair<Integer, Integer> getPossition() {
        return possition;
    }
}
