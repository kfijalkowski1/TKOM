package lekser.tokenBuilderUtils;

import inputHandle.Source;
import lekser.Token;
import lekser.TokenType;
import org.apache.commons.lang3.tuple.ImmutablePair;

public class TokenNumberAndStringBuilder {

    public static Token stringBuilder(Source src) {
        if (src.getCurrentChar() != '\"') {
            return null;
        }
        ImmutablePair<Integer, Integer> pos = src.getPossition();
        src.getNextChar(); // consume "
        int counter = 0;
        StringBuilder sb = new StringBuilder();
        while (src.getCurrentChar() != '\"') {
            counter++;
            sb.append(src.getCurrentChar());
            src.getNextChar();
            if (counter > 200) {
                throw new RuntimeException(); //TODO make it better
            }
        }
        src.getNextChar(); //consume "
        return new Token(TokenType.STRING_VALUE, pos, sb.toString());
    }
    public static Token buildNumber(Source src)  {
        if (!Character.isDigit(src.getCurrentChar())) {
            return null;
        }
        ImmutablePair<Integer, Integer> pos = src.getPossition();
        StringBuilder sB = new StringBuilder();

        if (src.getCurrentChar() == '0') {
            char nextChar = src.getNextChar();
            if (nextChar == '.') {
                sB = getFloatDecimal(src, sB);
                return new Token(TokenType.FLT_NUMBER, pos, Float.parseFloat(sB.toString()));
            }
            return new Token(TokenType.INT_NUMBER, pos, 0);
        }
        while (Character.isDigit(src.getCurrentChar())) {
            sB.append(src.getCurrentChar());
            checkIfNumberValid(sB.toString());
            src.getNextChar();
        }
        if (src.getCurrentChar() == '.') {
            sB = getFloatDecimal(src, sB);
            return new Token(TokenType.FLT_NUMBER, pos, Float.parseFloat(sB.toString()));
        }

        return new Token(TokenType.INT_NUMBER, pos, Integer.parseInt(sB.toString()));


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

    private static void checkIfNumberValid(String number)  {
        if (number.length() < 9) {
            return;
        }
        int valLong = Integer.parseInt(number.substring(0, number.length() - 1)); // check number 10 times smaller
        int valShort = Integer.parseInt(number.substring(number.length() - 1)); // check least important number
        if (valLong > 214748364 && valShort > 7) {
            throw new RuntimeException("aa"); // TODO change to custom exception and change rundom number
        }
    }
}
