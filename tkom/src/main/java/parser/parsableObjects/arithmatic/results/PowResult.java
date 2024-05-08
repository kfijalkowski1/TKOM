package parser.parsableObjects.arithmatic.results;

import inputHandle.Position;
import parser.parsableObjects.expresions.Expression;
import parser.visitators.IVisitable;
import parser.visitators.IVisitator;

public class PowResult extends ArithmeticResult implements IVisitable {
    public PowResult(Expression left, Expression right, Position pos) {
        super(left, right, pos);
    }

    // override equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof PowResult) {
            return super.equals(obj);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public void accept(IVisitator iv) {
        iv.visit(this);
    }
}
