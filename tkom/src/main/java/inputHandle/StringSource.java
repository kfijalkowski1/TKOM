package inputHandle;

import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 * mainly for testing and fast source creation
 */
public class StringSource implements Source {

    private String text;
    private Position pos = new Position(true);
    private char currentChar;
    private int currentTextIndex = 0;

    /**
     * creates source from given string
     * @param text source from witch chars are given
     */
    public StringSource(String text) {
        this.text = text;
        getNextChar(); // move to first char
    }

    @Override
    public char getNextChar() {
        if (currentTextIndex >= text.length()) {
            currentChar = '\u0003';
            return currentChar;
        }
        currentChar =  text.charAt(currentTextIndex++);
        if (currentChar == '\n' || currentChar == '\r') { // TODO isnt there a better way?
            pos.incrementLine();
        } else {
            pos.incrementRow();
        }
        return currentChar;
    }

    @Override
    public char getCurrentChar() {
        return currentChar;
    }

    @Override
    public ImmutablePair<Integer, Integer> getPossition() {
        return pos.getCurrentPossition();
    }
}
