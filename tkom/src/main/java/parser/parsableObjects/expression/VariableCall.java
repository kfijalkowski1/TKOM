package parser.parsableObjects.expression;

import inputHandle.Position;
import interpreter.exceptions.InterperterException;
import parser.parsableObjects.variables.Variable;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

public class VariableCall extends Expression implements IVisitable {
    private final String name;
    private final Boolean isReference;

    public VariableCall(String name, Position pos) {
        super(pos);
        this.name = name;
        this.isReference = false;
    }

    public VariableCall(String name, Position pos, Boolean isReference) {
        super(pos);
        this.name = name;
        this.isReference = isReference;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VariableCall that = (VariableCall) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public void accept(IVisitor iv) throws InterperterException {
        iv.visit(this);
    }
}
