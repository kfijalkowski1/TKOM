package parser.parsableObjects.variables;

import inputHandle.Position;
import interpreter.exceptions.InterperterException;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

public class VariableDeclaration extends Variable implements IVisitable {


    public final String name;

    // treat type as possible custom type or primitive type (don't check if it's valid here)
    public final Type type;

    public VariableDeclaration (String name, String type, Position pos) {
        super(pos);
        this.name = name;
        this.type = new Type(type);
    }

    public VariableDeclaration (String name, String type, Position pos, Boolean isReference) {
        super(pos);
        this.name = name;
        this.type = new Type(type, isReference);
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    @Override
    public void accept(IVisitor iv) throws InterperterException {
        iv.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VariableDeclaration)) return false;
        VariableDeclaration that = (VariableDeclaration) o;
        return name.equals(that.name) && type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return name.hashCode() + type.hashCode();
    }
}
