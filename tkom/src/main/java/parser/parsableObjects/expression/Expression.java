package parser.parsableObjects.expression;

import inputHandle.Position;
import parser.parsableObjects.Statement;
import parser.visitators.IVisitable;

public abstract class Expression extends Statement implements IVisitable {

    public Expression(Position pos) {
        super(pos);
    }
}
