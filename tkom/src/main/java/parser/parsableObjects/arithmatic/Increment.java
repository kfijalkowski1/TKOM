package parser.parsableObjects.arithmatic;
import inputHandle.Position;

import parser.parsableObjects.expresions.Expression;
import parser.parsableObjects.variables.VariableCall;
import parser.visitators.IVisitable;
import parser.visitators.IVisitator;

public class Increment extends ArithmaticStandalone implements IVisitable {
    private Expression value;

    public Increment(VariableCall variable, Expression value, Position pos) {
        super(variable, pos);
        this.value = value;
    }

    @Override
    public void accept(IVisitator iv) {
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
