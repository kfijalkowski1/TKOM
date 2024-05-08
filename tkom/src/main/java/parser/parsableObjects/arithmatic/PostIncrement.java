package parser.parsableObjects.arithmatic;

import inputHandle.Position;
import parser.parsableObjects.variables.VariableCall;
import parser.visitators.IVisitable;
import parser.visitators.IVisitator;

public class PostIncrement extends ArithmaticStandalone implements IVisitable {
    public PostIncrement(VariableCall variable, Position pos) {
        super(variable, pos);
    }

    @Override
    public void accept(IVisitator iv) {
        iv.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostIncrement)) return false;
        if (!super.equals(o)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
