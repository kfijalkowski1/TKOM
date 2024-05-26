package parser.parsableObjects.conditional;

import inputHandle.Position;
import parser.parsableObjects.conditional.condition.Condition;
import parser.parsableObjects.expresions.Expression;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

import java.util.List;

public class IfConditional extends ConditionConditional implements IVisitable {

    private final List<Conditional> subConditions;

    public IfConditional(Condition condition, List<Expression> expressions, List<Conditional> subConditions, Position pos) {
        super(condition, expressions, pos);
        this.subConditions = subConditions;
    }


    public List<Conditional> getSubConditions() {
        return subConditions;
    }

    @Override
    public void accept(IVisitor iv) {
        iv.visit(this);
    }

    // implement equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IfConditional)) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
