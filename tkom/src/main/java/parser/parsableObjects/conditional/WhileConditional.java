package parser.parsableObjects.conditional;

import inputHandle.Position;
import parser.parsableObjects.conditional.condition.Condition;
import parser.parsableObjects.expresions.Expression;
import parser.visitators.IVisitable;
import parser.visitators.IVisitator;

import java.util.List;

public class WhileConditional extends ConditionConditional implements IVisitable {

    public WhileConditional(Condition condition, List<Expression> expressions, Position pos) {
        super(condition, expressions, pos);
    }

    @Override
    public void accept(IVisitator iv) {
        iv.visit(this);
    }

    // implement equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WhileConditional)) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
