package inputHandle;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class Position {
    public Integer getcharacter() {
        return character;
    }
    private Integer character = 0;

    public Integer getLine() {
        return line;
    }

    private Integer line = -1;


    public Position(boolean hasLines) {
        if (hasLines) {
            line = 1;
        }
    }

    public Position() {
        line = 1;
    }

    public Position(Integer character, Integer line) {
        this.line = line;
        this.character = character;
    }

    protected void incrementRow() {
        character++;
    }

    protected void incrementLine() {
        character = 0;
        line++;
    }

    public Position getCurrentPossition() {
        Position copy = new Position();
        copy.character = this.character;
        copy.line = this.line;
        return copy;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Position position = (Position) obj;
        return character.equals(position.character) && line.equals(position.line);
    }





}
