package parser.parsableObjects.conditional;

import inputHandle.Position;
import parser.parsableObjects.Statement;
import parser.parsableObjects.blocks.Block;
import parser.parsableObjects.expression.Expression;

import java.util.List;

public abstract class ConditionConditional extends Conditional {
    private final Expression condition;

    public ConditionConditional(Expression condition, List<Statement> blocks, Position pos) {
        super(blocks, pos);
        this.condition = condition;
    }

    public Expression getCondition() {
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
