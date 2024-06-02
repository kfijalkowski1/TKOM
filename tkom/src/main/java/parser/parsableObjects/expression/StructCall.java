package parser.parsableObjects.expression;

import inputHandle.Position;
import interpreter.exceptions.InterperterException;
import parser.parsableObjects.blocks.Block;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

import java.util.List;
import java.util.Objects;

public class StructCall extends Expression implements IVisitable {
    private final String structName;
    private final List<String> structFilds;

    public StructCall(String structName, List<String> structFilds, Position position) {
        super(position);
        this.structName = structName;
        this.structFilds = structFilds;
    }


    public String getStructName() {
        return structName;
    }

    public List<String> getParameters() {
        return structFilds;
    }

    @Override
    public void accept(IVisitor iv) throws InterperterException {
        iv.visit(this);
    }

    // implement equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StructCall)) return false;
        StructCall that = (StructCall) o;
        return structName.equals(that.structName) && Objects.equals(structFilds, that.structFilds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(structName, structFilds);
    }
}
