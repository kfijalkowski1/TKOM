package parser.parsableObjects.blocks;

import inputHandle.Position;
import parser.parsableObjects.Statement;
import parser.visitators.IVisitable;

public abstract class Block extends Statement implements IVisitable {
    public Block(Position pos) {
        super(pos);
    }
    boolean isNegative = false;

    public void setNegative(boolean negative) {
        isNegative = negative;
    }

    public boolean isNegative() {
        return isNegative;
    }
}
