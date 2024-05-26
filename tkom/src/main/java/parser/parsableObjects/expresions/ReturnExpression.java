package parser.parsableObjects.expresions;

import inputHandle.Position;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

public class ReturnExpression extends Expression implements IVisitable {
    private Expression value;

    public ReturnExpression(Expression value, Position pos) {
        super(pos);
        this.value = value;
    }

    @Override
    public void accept(IVisitor iv) {
        iv.visit(this);
    }

    public Expression getValue() {
        return value;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReturnExpression that = (ReturnExpression) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
