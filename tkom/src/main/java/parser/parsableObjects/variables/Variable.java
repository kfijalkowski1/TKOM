package parser.parsableObjects.variables;

import inputHandle.Position;
import parser.parsableObjects.expresions.Expression;
import parser.visitators.IVisitator;

public abstract class Variable extends Expression {

    public Variable(Position pos) {
        super(pos);
    }

    @Override
    public void accept(IVisitator iv) {
        iv.visit(this);
    }
}
