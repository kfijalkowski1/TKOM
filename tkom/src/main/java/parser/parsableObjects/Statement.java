package parser.parsableObjects;

import inputHandle.Position;
import parser.visitators.IVisitator;

public abstract class Statement {
    private final Position pos;

    public Statement(Position pos) {
        this.pos = pos;
    }

    public Position getPosition() {
        return pos;
    }

}
