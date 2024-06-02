package parser.parsableObjects.conditional;

import inputHandle.Position;
import interpreter.exceptions.InterperterException;
import parser.parsableObjects.Statement;
import parser.parsableObjects.expression.Expression;
import parser.parsableObjects.blocks.Block;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

import java.util.List;

public class ElIfConditional extends ConditionConditional implements IVisitable {

    public ElIfConditional(Expression condition, List<Statement> blocks, Position pos) {
        super(condition, blocks, pos);
    }

    @Override
    public void accept(IVisitor iv) throws InterperterException {
        iv.visit(this);
    }

    // implement equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElIfConditional)) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
