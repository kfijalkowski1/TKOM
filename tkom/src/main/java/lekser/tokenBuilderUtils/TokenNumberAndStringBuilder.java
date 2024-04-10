package lekser.tokenBuilderUtils;

import inputHandle.Source;
import lekser.Token;
import lekser.TokenType;
import lekser.exceptions.NumberOutOfBoundsException;
import lekser.exceptions.StringTooLongException;
import lekser.exceptions.UnexpectedEndOfFile;
import org.apache.commons.lang3.tuple.ImmutablePair;

import static lekser.tokenBuilderUtils.BuildersUtils.*;

public class TokenNumberAndStringBuilder {

    public static Token stringBuilder(Source src) throws StringTooLongException, UnexpectedEndOfFile {
        if (src.getCurrentChar() != '\"') {
            return null;
        }
        ImmutablePair<Integer, Integer> pos = src.getPossition();
        src.getNextChar(); // consume "
        int counter = 1;
        StringBuilder sb = new StringBuilder();
        while (src.getCurrentChar() != '\"') {
            if (src.getCurrentChar() == END_OF_FILE) {
                throw new UnexpectedEndOfFile(pos, "End of file while reading string");
            }
            if (counter++ > MAX_STRING) {
                throw new StringTooLongException(pos, "String too long");
            }
            if (src.getCurrentChar() == '\\') {
                handleEscape(src, sb);
                src.getNextChar(); // consume
                continue;
            }
            sb.append(src.getCurrentChar());
            src.getNextChar();

        }
        src.getNextChar(); //consume "
        return new Token(TokenType.STRING_VALUE, pos, sb.toString());
    }

    private static void handleEscape(Source src, StringBuilder sb) {
        switch (src.getNextChar()){
            case '\\':
                sb.append('\\');
                break;
            case 'n':
                sb.append('\n');
                break;
            case '\"':
                sb.append('\"');
                break;
            case 't':
                sb.append('\t');
                break;
            default:
                sb.append('\\');
                sb.append(src.getCurrentChar());
        }

    }

    public static Token buildNumber(Source src) throws NumberOutOfBoundsException {
        if (!Character.isDigit(src.getCurrentChar())) {
            return null;
        }
        ImmutablePair<Integer, Integer> pos = src.getPossition();
        StringBuilder sB = new StringBuilder();

        if (src.getCurrentChar() == '0') {
            char nextChar = src.getNextChar();
            if (nextChar == '.') {
                sB = getFloatDecimal(src, sB, pos);
                return new Token(TokenType.FLT_NUMBER, pos, Float.parseFloat(sB.toString()));
            }
            return new Token(TokenType.INT_NUMBER, pos, 0);
        }
        while (Character.isDigit(src.getCurrentChar())) {
            sB.append(src.getCurrentChar());
            checkIfNumberValid(sB.toString(), pos);
            src.getNextChar();
        }
        if (src.getCurrentChar() == '.') {
            sB = getFloatDecimal(src, sB, pos);
            return new Token(TokenType.FLT_NUMBER, pos, Float.parseFloat(sB.toString()));
        }

        return new Token(TokenType.INT_NUMBER, pos, Integer.parseInt(sB.toString()));


    }

    private static StringBuilder getFloatDecimal(Source src, StringBuilder sB, ImmutablePair pos) throws NumberOutOfBoundsException {
        sB.append('.');
        int counter = 0;
        while (Character.isDigit(src.getNextChar()) && counter < FLOAT_ACCURACY) {
            sB.append(src.getCurrentChar());
            counter++;
        }
         if (counter == FLOAT_ACCURACY) {
             throw new NumberOutOfBoundsException(pos, "Too many numbers after . max amount: %d".formatted(FLOAT_ACCURACY-1));
         }
        return sB;
    }

    private static void checkIfNumberValid(String number, ImmutablePair pos) throws NumberOutOfBoundsException {
        if (number.length() < 10) {
            return;
        }
        int valLong = Integer.parseInt(number.substring(0, number.length() - 1)); // check number 10 times smaller
        int valShort = Integer.parseInt(number.substring(number.length() - 1)); // check least important number
        if (valLong > (MAX_INT / 10) || (valLong == MAX_INT / 10 && valShort > MAX_INT % 10)) {
            throw new NumberOutOfBoundsException(pos, "Number too big, max int: %d".formatted(MAX_INT));
        }
    }
}
