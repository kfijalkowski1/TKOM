package parser.parsableObjects.expression;

import inputHandle.Position;
import parser.parsableObjects.Statement;
import parser.visitators.IVisitable;

public abstract class Expression extends Statement implements IVisitable {
    Boolean isNegative = false;
    Boolean isNegated = false;

    public Expression(Position pos) {
        super(pos);
    }

    public Boolean isNegative() {
        return isNegative;
    }

    public void setNegative(Boolean negative) {
        isNegative = negative;
    }

    public Boolean isNegated() {
        return isNegated;
    }

    public void setNegated(Boolean negated) {
        isNegated = negated;
    }
}
