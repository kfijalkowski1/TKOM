package parser.parsableObjects.variables;

import inputHandle.Position;
import interpreter.exceptions.InterperterException;
import parser.parsableObjects.blocks.Block;
import parser.parsableObjects.expression.Expression;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

public class VariableInit extends ConstGlobalVariableDeclaration implements IVisitable {
    public final Expression value;


    public VariableInit(String name, String type, Expression value, Boolean isConst, Boolean isGlobal, Position pos) {
        super(name, type, isConst, isGlobal, pos);
        this.value = value;
    }

    public VariableInit(String name, String type, Expression value, Boolean isConst, Boolean isGlobal,
                        Position pos, Boolean isReference) {
        super(name, type, isConst, isGlobal, pos, isReference);
        this.value = value;
    }

    @Override
    public void accept(IVisitor iv) throws InterperterException {
        iv.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VariableInit)) return false;
        if (!super.equals(o)) return false;
        VariableInit that = (VariableInit) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + value.hashCode();
    }


    public Expression getValue() {
        return value;
    }
}
