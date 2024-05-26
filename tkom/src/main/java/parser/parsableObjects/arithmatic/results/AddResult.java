package parser.parsableObjects.arithmatic.results;

import inputHandle.Position;
import parser.parsableObjects.expresions.Expression;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

public class AddResult extends ArithmeticResult implements IVisitable {
    public AddResult(Expression left, Expression right, Position pos) {
        super(left, right, pos);
    }

    // override equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof AddResult) {
            return super.equals(obj);
        }
        return false;
    }

    @Override
    public void accept(IVisitor iv) {
        iv.visit(this);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
