package inputHandle;


import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.FileReader;
import java.io.IOException;

public class FileSource implements Source {
    private final Possition poss;

    private char currentChar = 0;
    FileReader fileReader;


    public FileSource(String path) throws IOException {
        fileReader = new FileReader(path);
        poss = new Possition(true);
        getNextChar(); // move to first char
    }

    @Override
    public char getNextChar() {
        currentChar = readNextChar();
        if (currentChar == '\n' || currentChar == '\r') { // TODO isnt there a better way?
            poss.incrementLine();
        } else {
            poss.incrementRow();
        }
        return currentChar;

    }

    @Override
    public char getCurrentChar() {
        return currentChar;
    }

    @Override
    public ImmutablePair<Integer, Integer> getPossition() {
        return poss.getCurrentPossition();
    }

    private char readNextChar() {
        int character = -1;
        try {
            character = fileReader.read();
        } catch (IOException e) {
            e.printStackTrace(); // TODO add logger and send to error handler, or only error handler and he logs?
        }
        if (character != -1) {
            return (char) character;
        } else {
            return '\u0003'; // ETX character (ETX - end of text)
        }

    }



}
