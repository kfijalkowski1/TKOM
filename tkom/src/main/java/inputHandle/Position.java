package inputHandle;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class Position {
    private Integer column = 0;
    private Integer line = -1;


    public Position(boolean hasLines) {
        if (hasLines) {
            line = 1;
        }
    }

    public void incrementRow() {
        column++;
    }

    public void incrementLine() {
        column = 0;
        line++;
    }

    /**
     * @return pair of current line and current row in
     */
    public ImmutablePair<Integer, Integer> getCurrentPossition() {
        return new ImmutablePair<>(column, line);

    }


}
