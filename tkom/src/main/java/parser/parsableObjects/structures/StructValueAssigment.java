package parser.parsableObjects.structures;

import inputHandle.Position;
import parser.parsableObjects.expresions.Expression;
import parser.parsableObjects.variables.Value;
import parser.visitators.IVisitable;
import parser.visitators.IVisitator;

public class StructValueAssigment extends Expression implements IVisitable {
    StructCall structCall; // last one (when next is null) is the name of the variable in struct
    Value assignedValue;

    public StructValueAssigment(StructCall structCall, Value value, Position pos) {
        super(pos);
        this.structCall = structCall;
        this.assignedValue = value;
    }


    @Override
    public void accept(IVisitator iv) {
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
}
