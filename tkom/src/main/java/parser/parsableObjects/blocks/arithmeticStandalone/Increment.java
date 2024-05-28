package parser.parsableObjects.blocks.arithmeticStandalone;
import inputHandle.Position;

import parser.parsableObjects.expression.Expression;
import parser.parsableObjects.expression.VariableCall;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

public class Increment extends ArithmaticStandalone implements IVisitable {
    private Expression value;

    public Increment(VariableCall variable, Expression value, Position pos) {
        super(variable, pos);
        this.value = value;
    }

    @Override
    public void accept(IVisitor iv) {
        iv.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Increment)) return false;
        if (!super.equals(o)) return false;
        Increment that = (Increment) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
