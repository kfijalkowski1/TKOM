package parser.parsableObjects.conditional;

import inputHandle.Position;
import parser.parsableObjects.conditional.condition.Condition;
import parser.parsableObjects.expresions.Expression;

import java.util.List;

public abstract class ConditionConditional extends Conditional {
    private final Condition condition;

    public ConditionConditional(Condition condition, List<Expression> expressions, Position pos) {
        super(expressions, pos);
        this.condition = condition;
    }

    public Condition getCondition() {
        return condition;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConditionConditional)) return false;
        ConditionConditional that = (ConditionConditional) o;
        return super.equals(o) && condition.equals(that.condition);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + condition.hashCode();
    }
}
