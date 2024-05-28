package parser.parsableObjects.expression.arithmatic.results;

import inputHandle.Position;
import parser.parsableObjects.blocks.Block;
import parser.parsableObjects.expression.Expression;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

public class MultiplyResult extends ArithmeticResult implements IVisitable {
    public MultiplyResult(Expression left, Expression right, Position pos) {
        super(left, right, pos);
    }

    // override equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof MultiplyResult) {
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
