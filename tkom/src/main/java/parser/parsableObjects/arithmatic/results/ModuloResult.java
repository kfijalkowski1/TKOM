package parser.parsableObjects.arithmatic.results;

import inputHandle.Position;
import parser.parsableObjects.expresions.Expression;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

public class ModuloResult extends ArithmeticResult implements IVisitable {
    public ModuloResult(Expression left, Expression right, Position pos) {
        super(left, right, pos);
    }

    // override equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof ModuloResult) {
            return super.equals(obj);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public void accept(IVisitor iv) {
        iv.visit(this);
    }
}
