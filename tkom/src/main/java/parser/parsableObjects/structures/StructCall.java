package parser.parsableObjects.structures;

import inputHandle.Position;
import parser.parsableObjects.expresions.Expression;
import parser.visitators.IVisitable;
import parser.visitators.IVisitator;

import java.util.List;
import java.util.Objects;

public class StructCall extends Expression implements IVisitable {
    private final String structName;
    private final List<String> parameters;

    public StructCall(String structName, List<String> parameters, Position position) {
        super(position);
        this.structName = structName;
        this.parameters = parameters;
    }

    public StructCall(String structName, Position position) {
        super(position);
        this.structName = structName;
        this.parameters = null;
    }


    public String getStructName() {
        return structName;
    }

    public Boolean isStructCall() {
        return Objects.isNull(parameters);
    }

    public List<String> getParameters() {
        return parameters;
    }

    @Override
    public void accept(IVisitator iv) {
        iv.visit(this);
    }

    // implement equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StructCall)) return false;
        StructCall that = (StructCall) o;
        return structName.equals(that.structName) && Objects.equals(parameters, that.parameters);
    }
}
