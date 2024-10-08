package parser.parsableObjects.variables;

import inputHandle.Position;
import interpreter.exceptions.InterperterException;
import parser.parsableObjects.blocks.Block;
import parser.parsableObjects.expression.Expression;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

public class VariableAssigment extends Block implements IVisitable {
    Expression assignedValue;
    String variableName;

    public VariableAssigment(Expression assignedValue, String name, Position pos) {
        super(pos);
        this.assignedValue = assignedValue;
        this.variableName = name;
    }

    public Expression getAssignedValue() {
        return assignedValue;
    }

    public String getVariableName() {
        return variableName;
    }

    @Override
    public void accept(IVisitor iv) throws InterperterException {
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
