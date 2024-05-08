package parser.parsableObjects.expresions;

import inputHandle.Position;
import parser.visitators.IVisitable;
import parser.visitators.IVisitator;

public class BreakExpression extends Expression implements IVisitable {
    public BreakExpression(Position position) {
        super(position);
    }

    @Override
    public void accept(IVisitator iv) {
        iv.visit(this);
    }
}
