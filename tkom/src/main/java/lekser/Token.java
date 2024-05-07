package lekser;

import lekser.exceptions.IncorrectValueToken;
import lekser.exceptions.NoValueToken;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Token {
    private final TokenType tokenType;
    private Object value;
    private final ImmutablePair<Integer, Integer> possition;

    public Token(TokenType tokenType, ImmutablePair<Integer, Integer> possition, Object value) throws IncorrectValueToken {
        checkTokenValue(tokenType, value, possition);
        this.tokenType = tokenType;
        this.possition = possition;
        this.value = value;
    }

    private static void checkTokenValue(TokenType tokenType, Object value, ImmutablePair<Integer, Integer> possition) throws IncorrectValueToken {
        List<TokenType> stringValueTokens = Arrays.asList(TokenType.STRING_VALUE, TokenType.NAME);
        List<TokenType> numericValueTokens = Arrays.asList(TokenType.INT_NUMBER, TokenType.FLT_NUMBER);

        if (stringValueTokens.contains(tokenType) && !(value instanceof String)){
            throw new IncorrectValueToken(possition, "String");
        } else if (tokenType == TokenType.INT_NUMBER && !(value instanceof Integer)){
            throw new IncorrectValueToken(possition, "Integer");
        } else if (tokenType == TokenType.FLT_NUMBER && !(value instanceof Float)){
            throw new IncorrectValueToken(possition, "Integer");
        } else if (!stringValueTokens.contains(tokenType) && !numericValueTokens.contains(tokenType)) {
            throw new IncorrectValueToken(possition, "None");
        }
    }

    public Token(TokenType tokenType, ImmutablePair<Integer, Integer> possition) {
        this.tokenType = tokenType;
        this.possition = possition;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public Object getValue() throws NoValueToken {
        if (Objects.isNull(value)) {
            throw new NoValueToken("Token has no value");
        }
        return value;
    }

    public ImmutablePair<Integer, Integer> getPossition() {
        return possition;
    }
}
