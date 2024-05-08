package parser.parsableObjects.variables;

import inputHandle.Position;
import parser.parsableObjects.expresions.Expression;
import parser.visitators.IVisitable;
import parser.visitators.IVisitator;

public class VariableAssigment extends Expression implements IVisitable {
    Expression assignedValue;
    String variableName;

    public VariableAssigment(Expression assignedValue, String name, Position pos) {
        super(pos);
        this.assignedValue = assignedValue;
        this.variableName = name;
    }

    @Override
    public void accept(IVisitator iv) {
        iv.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariableAssigment that = (VariableAssigment) o;
        return assignedValue.equals(that.assignedValue) && variableName.equals(that.variableName);
    }

    @Override
    public int hashCode() {
        return assignedValue.hashCode() + variableName.hashCode();
    }
}
