package parser.parsableObjects.expresions;

import inputHandle.Position;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

public class BreakExpression extends Expression implements IVisitable {
    public BreakExpression(Position position) {
        super(position);
    }

    @Override
    public void accept(IVisitor iv) {
        iv.visit(this);
    }
}
