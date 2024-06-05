package parser.parsableObjects.structures;

import inputHandle.Position;
import interpreter.exceptions.InterperterException;
import parser.parsableObjects.blocks.Block;
import parser.parsableObjects.expression.Expression;
import parser.parsableObjects.expression.StructCall;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

public class StructValueAssigment extends Block implements IVisitable {
    StructCall structCall; // last one (when next is null) is the name of the variable in struct
    Expression assignedValue;

    public StructValueAssigment(StructCall structCall, Expression value, Position pos) {
        super(pos);
        this.structCall = structCall;
        this.assignedValue = value;
    }

    public Expression getAssignedValue() {
        return assignedValue;
    }

    public StructCall getStructCall() {
        return structCall;
    }

    @Override
    public void accept(IVisitor iv) throws InterperterException {
        iv.visit(this);
    }

    // implement equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StructValueAssigment)) return false;
        StructValueAssigment that = (StructValueAssigment) o;
        return structCall.equals(that.structCall) && assignedValue.equals(that.assignedValue);
    }

    @Override
    public int hashCode() {
        return structCall.hashCode() + assignedValue.hashCode();
    }
}
