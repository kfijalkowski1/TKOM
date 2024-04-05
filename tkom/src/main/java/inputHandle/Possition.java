package inputHandle;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class Possition {
    private Integer row = 1;
    private Integer line = -1;


    public Possition(boolean hasLines) {
        if (hasLines) {
            line = 1;
        }
    }

    public void incrementRow() {
        row++;
    }

    public void incrementLine() {
        row = 1;
        line++;
    }

    /**
     * @return pair of current line and current row in
     */
    public ImmutablePair<Integer, Integer> getCurrentPossition() {
        return new ImmutablePair<>(row, line);

    }


}
