package parser.parsableObjects.variables;

import inputHandle.Position;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

public class ConstGlobalVariableDeclaration extends VariableDeclaration implements IVisitable {
    private final Boolean isConst;
    private final Boolean isGlobal;

    public ConstGlobalVariableDeclaration(String name, String type, Boolean isConst, Boolean isGlobal, Position pos) {
        super(name, type, pos);
        this.isConst = isConst;
        this.isGlobal = isGlobal;
    }

    public ConstGlobalVariableDeclaration(String name, String type, Boolean isConst, Boolean isGlobal, Position pos, Boolean isReference) {
        super(name, type, pos, isReference);
        this.isConst = isConst;
        this.isGlobal = isGlobal;
    }

    @Override
    public void accept(IVisitor iv) {
        iv.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConstGlobalVariableDeclaration)) return false;
        ConstGlobalVariableDeclaration that = (ConstGlobalVariableDeclaration) o;
        return name.equals(that.name) && type.equals(that.type) && isConst.equals(that.isConst) && isGlobal.equals(that.isGlobal);
    }

    @Override
    public int hashCode() {
        return name.hashCode() + type.hashCode() + isConst.hashCode() + isGlobal.hashCode();
    }

}
