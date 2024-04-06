package lekser.tokenBuilderUtils;

import inputHandle.Source;
import lekser.Token;
import lekser.TokenType;

public class TokenOperatorBuilder {

    public static Token minusBuilder(Source src) {
        return checkTwoPartToken(src, '-', '>', TokenType.MINUS_OP, TokenType.RETURN_TYPE_OP);
    }

    public static Token multipleBuilder(Source src) {
        return checkTwoPartToken(src, '*', '*', TokenType.MULTIPLE_OP, TokenType.POW_OP);
    }

    public static Token divideBuilder(Source src) {
        return checkOnePartToken(src, '/', TokenType.DIVIDE_OP);
    }

    public static Token moduloBuilder(Source src) {
        return checkOnePartToken(src, '%', TokenType.MODULO_OP);
    }

    public static Token lessBuilder(Source src) {
        return checkTwoPartToken(src, '<', '=', TokenType.LESS_OP, TokenType.LESS_EQ_OP);
    }

    public static Token moreBuilder(Source src) {
        return checkTwoPartToken(src, '>', '=', TokenType.MORE_OP, TokenType.MORE_EQ_OP);
    }

    public static Token equalsBuilder(Source src) {
        return checkTwoPartToken(src, '=', '=', TokenType.ASSIGN_OP, TokenType.EQ_OP);
    }

    public static Token notEquals(Source src) {
        return checkTwoPartToken(src, '!', '=', TokenType.UNKNOWN_TOKEN, TokenType.NOT_EQ_OP);
    }

    public static Token openSoftBrackets(Source src) {
        return checkOnePartToken(src, '(', TokenType.OPEN_SOFT_BRACKETS_OP);
    }

    public static Token closeSoftBrackets(Source src) {
        return checkOnePartToken(src, ')', TokenType.CLOSE_SOFT_BRACKETS_OP);
    }

    public static Token openSharpBrackets(Source src) {
        return checkOnePartToken(src, '{', TokenType.OPEN_SHARP_BRACKETS_OP);
    }

    public static Token closeSharpBrackets(Source src) {
        return checkOnePartToken(src, '}', TokenType.CLOSE_SHARP_BRACKETS_OP);
    }

    public static Token dotOperator(Source src) {
        return checkOnePartToken(src, '.', TokenType.DOT_OP);
    }

    public static Token refOperator(Source src) {
        return checkOnePartToken(src, '&', TokenType.REFERENCE_OP);
    }

    public static Token endOfTextOperator(Source src) {
        return checkOnePartToken(src, '\u0003', TokenType.END_OF_TEXT);
    }

    public static Token plusBuilder(Source src) {
        if (src.getCurrentChar() != '+') {
            return null;
        }
        if (src.getNextChar() == '+') {
            src.getNextChar(); // consume this token
            return new Token(TokenType.POST_INCREMENT_OP, src.getPossition());
        }
        if (src.getCurrentChar() == '=') {
            src.getNextChar(); // consume this token
            return new Token(TokenType.INCREMENT_OP, src.getPossition());
        }
        return new Token(TokenType.PLUS_OP, src.getPossition());
    }

    private static Token checkTwoPartToken(
            Source src, char firstChar, char secondChar, TokenType firstReturn, TokenType secondReturn) {
        if (src.getCurrentChar() != firstChar) {
            return null;
        }
        if (src.getNextChar() == secondChar) {
            src.getNextChar(); // consume this token
            return new Token(secondReturn, src.getPossition());
        }
        return new Token(firstReturn, src.getPossition());
    }

    private static Token checkOnePartToken(Source src, char fChar, TokenType returnType) {
        if (src.getCurrentChar() != fChar) {
            return null;
        }
        src.getNextChar(); // consume this token
        return new Token(returnType, src.getPossition());
    }
}
