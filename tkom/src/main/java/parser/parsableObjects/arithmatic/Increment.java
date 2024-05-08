package parser.parsableObjects.arithmatic;
import inputHandle.Position;
import parser.parsableObjects.variables.Value;
import parser.parsableObjects.variables.VariableCall;
import parser.visitators.IVisitable;
import parser.visitators.IVisitator;

public class Increment extends ArithmaticStandalone implements IVisitable {
    private Value value;

    public Increment(VariableCall variable, Value value, Position pos) {
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
}
