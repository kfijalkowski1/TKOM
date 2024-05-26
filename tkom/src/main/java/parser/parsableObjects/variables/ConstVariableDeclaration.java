package parser.parsableObjects.variables;

import inputHandle.Position;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

public class ConstVariableDeclaration extends VariableDeclaration implements IVisitable {
    private final Boolean isConst;

    public ConstVariableDeclaration(String name, String type, Boolean isConst, Position pos, Boolean isReference) {
        super(name, type, pos, isReference);
        this.isConst = isConst;
    }

    public ConstVariableDeclaration(String name, String type, Boolean isConst, Position pos) {
        super(name, type, pos);
        this.isConst = isConst;
    }

    @Override
    public void accept(IVisitor iv) {
        iv.visit(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConstVariableDeclaration)) return false;
        if (!super.equals(o)) return false;
        ConstVariableDeclaration that = (ConstVariableDeclaration) o;
        return isConst.equals(that.isConst);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + isConst.hashCode();
    }

}
