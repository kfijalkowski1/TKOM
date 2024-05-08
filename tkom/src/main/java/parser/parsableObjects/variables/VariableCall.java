package parser.parsableObjects.variables;

import inputHandle.Position;
import parser.visitators.IVisitable;

public class VariableCall extends Variable implements IVisitable {
    private String name;
    private Boolean isReference;

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
}
