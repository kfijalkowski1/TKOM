package lekser.tokenBuilderUtils;

import inputHandle.Source;
import lekser.Token;
import lekser.TokenType;

public class TokenNumberBuilder {

    public static Token buildNumber(Source src) throws Exception {
        if (!Character.isDigit(src.getCurrentChar())) {
            return null;
        }
        StringBuilder sB = new StringBuilder(src.getCurrentChar());
        sB.append(src.getCurrentChar());

        if (src.getCurrentChar() == '0') {
            char nextChar = src.getNextChar();
            if (Character.isWhitespace(nextChar)) {
                return new Token(TokenType.INT_NUMBER, src.getPossition(), 0);
            } else if (nextChar == '.') {
                sB = getFloatDecimal(src, sB);
                return new Token(TokenType.FLT_NUMBER, src.getPossition(), Float.parseFloat(sB.toString()));
            } else {
                return null; // TODO EXCEPTION
            }
        }
        while (Character.isDigit(src.getNextChar())) {
            sB.append(src.getCurrentChar());
            checkIfNumberValid(sB.toString()); // TODO excpetion custom
        }
        if (src.getCurrentChar() != '.' && !Character.isWhitespace(src.getCurrentChar()) && src.getCurrentChar() != '\u0003') {
            return null; // TODO custom error
        }

        if (src.getCurrentChar() != '.') {
            int value = Integer.parseInt(sB.toString());
            return new Token(TokenType.INT_NUMBER, src.getPossition(), value);
        }
        sB = getFloatDecimal(src, sB);
        // TODO write in docs ma max 10 cyfr po przecinku

        return new Token(TokenType.FLT_NUMBER, src.getPossition(), Float.parseFloat(sB.toString()));

    }

    private static StringBuilder getFloatDecimal(Source src, StringBuilder sB) {
        sB.append('.');
        int counter = 0;
        while (Character.isDigit(src.getNextChar()) && counter < 10) {
            sB.append(src.getCurrentChar());
            counter++;
        }
        return sB;
    }

    public static void checkIfNumberValid(String number) throws Exception {
        int valLong = Integer.parseInt(number.substring(0, number.length() - 1)); // check number 10 times smaller
        int valShort = Integer.parseInt(number.substring(number.length() - 1)); // check least important number
        if (valLong > 214748364 && valShort > 7) {
            throw new Exception("aa"); // TODO change to custom exception
        }
    }
}
