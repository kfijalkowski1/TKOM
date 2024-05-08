package lekser;

import inputHandle.Position;
import lekser.exceptions.IncorrectValueToken;
import lekser.exceptions.NoValueToken;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Token {
    private final TokenType tokenType;
    private Object value;
    private final Position possition;

    public Position getPosition() {
        return possition;
    }

    public Token(TokenType tokenType, Position position, Object value) throws IncorrectValueToken {
        checkTokenValue(tokenType, value, position);
        this.tokenType = tokenType;
        this.possition = position;
        this.value = value;
    }

    private static void checkTokenValue(TokenType tokenType, Object value, Position possition) throws IncorrectValueToken {
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

    public Token(TokenType tokenType, Position possition) {
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

    public Position getPossition() {
        return possition;
    }
}
