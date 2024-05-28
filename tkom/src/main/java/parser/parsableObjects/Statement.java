package parser.parsableObjects;

import inputHandle.Position;
import parser.visitators.IVisitable;

public abstract class Statement implements IVisitable {
    private final Position pos;

    public Statement(Position pos) {
        this.pos = pos;
    }

    public Position getPosition() {
        return pos;
    }

}
