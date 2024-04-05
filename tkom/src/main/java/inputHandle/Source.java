package inputHandle;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.IOException;

public interface Source {
    public char getNextChar();

    /**
     * sets currentChar to next char
     * @return next character
     */
    public char getCurrentChar();
    public ImmutablePair<Integer, Integer> getPossition();
}
