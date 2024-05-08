package parser.parsableObjects.conditional;

import inputHandle.Position;
import parser.parsableObjects.expresions.Expression;
import parser.visitators.IVisitable;
import parser.visitators.IVisitator;

import java.util.List;

public class ElseConditional extends Conditional implements IVisitable {

    public ElseConditional(List<Expression> expressions, Position pos) {
        super(expressions, pos);
    }

    @Override
    public void accept(IVisitator iv) {
        iv.visit(this);
    }

    // implement equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElseConditional)) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
