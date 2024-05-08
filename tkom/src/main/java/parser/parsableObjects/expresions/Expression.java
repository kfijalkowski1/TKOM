package parser.parsableObjects.expresions;

import inputHandle.Position;
import parser.parsableObjects.Statement;
import parser.visitators.IVisitable;

public abstract class Expression extends Statement implements IVisitable {
    public Expression(Position pos) {
        super(pos);
    }
}
