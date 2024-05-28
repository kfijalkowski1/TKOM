package parser.parsableObjects.blocks;

import inputHandle.Position;
import parser.parsableObjects.expression.Expression;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

public class ReturnBlock extends Block implements IVisitable {
    private Expression value;

    public ReturnBlock(Expression value, Position pos) {
        super(pos);
        this.value = value;
    }

    @Override
    public void accept(IVisitor iv) {
        iv.visit(this);
    }

    public Expression getValue() {
        return value;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReturnBlock that = (ReturnBlock) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
