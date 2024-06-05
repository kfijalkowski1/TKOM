package parser.parsableObjects.blocks.arithmeticStandalone;

import inputHandle.Position;
import interpreter.exceptions.InterperterException;
import parser.parsableObjects.expression.VariableCall;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

public class PostIncrement extends ArithmaticStandalone implements IVisitable {
    public PostIncrement(VariableCall variable, Position pos) {
        super(variable, pos);
    }

    public String getVariableName() {
        return ((VariableCall) getVariable()).getName();
    }

    @Override
    public void accept(IVisitor iv) throws InterperterException {
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
