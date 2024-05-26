package parser.parsableObjects.variables;

import inputHandle.Position;
import parser.parsableObjects.expresions.Expression;
import parser.visitators.IVisitor;

public abstract class Variable extends Expression {

    public Variable(Position pos) {
        super(pos);
    }

    @Override
    public void accept(IVisitor iv) {
        iv.visit(this);
    }
}
