package lekser.tokenBuilderUtils;

import inputHandle.Position;
import inputHandle.Source;
import lekser.Token;
import lekser.TokenType;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class TokenOperatorBuilder {

    public static Map<Character, Function<Source, Token>> tokenBuilders = new HashMap<>();

    static {
        tokenBuilders.put('-', src -> checkTwoPartToken(src, '-', '>', TokenType.MINUS_OP, TokenType.RETURN_TYPE_OP));
        tokenBuilders.put('+', src -> plusBuilder(src));
        tokenBuilders.put('*', src -> checkTwoPartToken(src, '*', '*', TokenType.MULTIPLE_OP, TokenType.POW_OP));
        tokenBuilders.put('/', src -> checkOnePartToken(src, '/', TokenType.DIVIDE_OP));
        tokenBuilders.put('%', src -> checkOnePartToken(src, '%', TokenType.MODULO_OP));
        tokenBuilders.put('<', src -> checkTwoPartToken(src, '<', '=', TokenType.LESS_OP, TokenType.LESS_EQ_OP));
        tokenBuilders.put('>', src -> checkTwoPartToken(src, '>', '=', TokenType.MORE_OP, TokenType.MORE_EQ_OP));
        tokenBuilders.put('=', src -> checkTwoPartToken(src, '=', '=', TokenType.ASSIGN_OP, TokenType.EQ_OP));
        tokenBuilders.put('!', src -> checkTwoPartToken(src, '!', '=', TokenType.UNKNOWN_TOKEN, TokenType.NOT_EQ_OP));
        tokenBuilders.put('(', src -> checkOnePartToken(src, '(', TokenType.OPEN_SOFT_BRACKETS_OP));
        tokenBuilders.put(')', src -> checkOnePartToken(src, ')', TokenType.CLOSE_SOFT_BRACKETS_OP));
        tokenBuilders.put('{', src -> checkOnePartToken(src, '{', TokenType.OPEN_SHARP_BRACKETS_OP));
        tokenBuilders.put('}', src -> checkOnePartToken(src, '}', TokenType.CLOSE_SHARP_BRACKETS_OP));
        tokenBuilders.put('.', src -> checkOnePartToken(src, '.', TokenType.DOT_OP));
        tokenBuilders.put(',', src -> checkOnePartToken(src, '.', TokenType.COMA_OP));
        tokenBuilders.put('&', src -> checkOnePartToken(src, '&', TokenType.REFERENCE_OP));
        tokenBuilders.put('\u0003', src -> checkOnePartToken(src, '\u0003', TokenType.END_OF_TEXT));
    }

    public static Token getOperator(Source src) {
        Function<Source, Token> builder = tokenBuilders.get(src.getCurrentChar());
        if (Objects.isNull(builder)) {
            return null;
        }
        return builder.apply(src);

    }

    public static Token plusBuilder(Source src) {
        Position pos = src.getPossition();
        if (src.getNextChar() == '+') {
            src.getNextChar(); // consume this token
            return new Token(TokenType.POST_INCREMENT_OP, pos);
        }
        if (src.getCurrentChar() == '=') {
            src.getNextChar(); // consume this token
            return new Token(TokenType.INCREMENT_OP, pos);
        }
        return new Token(TokenType.PLUS_OP, pos);
    }

    private static Token checkTwoPartToken(
            Source src, char firstChar, char secondChar, TokenType firstReturn, TokenType secondReturn) {
        Position pos = src.getPossition();
        if (src.getNextChar() == secondChar) {
            src.getNextChar(); // consume this token
            return new Token(secondReturn, pos);
        }
        return new Token(firstReturn, pos);
    }

    private static Token checkOnePartToken(Source src, char fChar, TokenType returnType) {
        Position pos = src.getPossition();
        src.getNextChar(); // consume this token
        return new Token(returnType, pos);
    }
}
