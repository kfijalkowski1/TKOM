package parser.parsableObjects.conditional.condition;

import inputHandle.Position;
import parser.parsableObjects.expresions.Expression;

public abstract class Condition extends Expression {
    boolean isNegated = false;

    public Condition setNegated(boolean negated) {
        isNegated = negated;
        return this;
    }

    public Boolean isNegated() {
        return isNegated;
    }

    public Condition(Position pos) {
        super(pos);
    }

}
