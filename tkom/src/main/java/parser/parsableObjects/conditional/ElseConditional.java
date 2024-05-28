package parser.parsableObjects.conditional;

import inputHandle.Position;
import parser.parsableObjects.Statement;
import parser.parsableObjects.blocks.Block;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

import java.util.List;

public class ElseConditional extends Conditional implements IVisitable {

    public ElseConditional(List<Statement> blocks, Position pos) {
        super(blocks, pos);
    }

    @Override
    public void accept(IVisitor iv) {
        iv.visit(this);
    }

    // implement equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElseConditional)) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
