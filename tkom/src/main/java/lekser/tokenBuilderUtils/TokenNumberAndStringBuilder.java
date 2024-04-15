package lekser.tokenBuilderUtils;

import inputHandle.Source;
import lekser.Token;
import lekser.TokenType;
import lekser.exceptions.IncorrectValueToken;
import lekser.exceptions.NumberOutOfBoundsException;
import lekser.exceptions.StringTooLongException;
import lekser.exceptions.UnexpectedEndOfFile;
import org.apache.commons.lang3.tuple.ImmutablePair;

import static lekser.tokenBuilderUtils.BuildersUtils.*;

public class TokenNumberAndStringBuilder {

    public static Token stringBuilder(Source src) throws StringTooLongException, UnexpectedEndOfFile, IncorrectValueToken {
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

    public static Token buildNumber(Source src) throws NumberOutOfBoundsException, IncorrectValueToken {
        if (!Character.isDigit(src.getCurrentChar())) {
            return null;
        }
        ImmutablePair<Integer, Integer> pos = src.getPossition();

        if (src.getCurrentChar() == '0') {
            char nextChar = src.getNextChar();
            if (nextChar == '.') {
                float fltVal = getFloatDecimal(src, 0, pos);
                return new Token(TokenType.FLT_NUMBER, pos, fltVal);
            }
            return new Token(TokenType.INT_NUMBER, pos, 0);
        }
        int value = 0;
        while (Character.isDigit(src.getCurrentChar())) {
            int curNum = src.getCurrentChar() - '0';
            checkIfNumberValid(value, curNum, pos);
            src.getNextChar();
            value = value * 10 + curNum;
        }
        if (src.getCurrentChar() == '.') {
            float fltVal = getFloatDecimal(src, value, pos);
            return new Token(TokenType.FLT_NUMBER, pos, fltVal);
        }

        return new Token(TokenType.INT_NUMBER, pos, value);


    }

    private static float getFloatDecimal(Source src, Integer curVal, ImmutablePair pos) throws NumberOutOfBoundsException {
        int counter = 0;
        int value = 0;
        while (Character.isDigit(src.getNextChar()) && counter < FLOAT_ACCURACY) {
            int curNum = src.getCurrentChar() - '0';
            value = value * 10 + curNum;
            counter++;
        }
         if (counter == FLOAT_ACCURACY) {
             throw new NumberOutOfBoundsException(pos, "Too many numbers after . max amount: %d".formatted(FLOAT_ACCURACY-1));
         }
         return ((float) curVal + (float) value / (float)Math.pow(10, counter));
    }

    private static void checkIfNumberValid(Integer valLong, Integer valShort, ImmutablePair pos) throws NumberOutOfBoundsException {
        if (valLong < MAX_INT / 10) {
            return;
        }
        if (valLong > (MAX_INT / 10) || (valLong == MAX_INT / 10 && valShort > MAX_INT % 10)) {
            throw new NumberOutOfBoundsException(pos, "Number too big, max int: %d".formatted(MAX_INT));
        }
    }
}
