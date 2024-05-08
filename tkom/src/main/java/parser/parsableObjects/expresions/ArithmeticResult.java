package parser.parsableObjects.expresions;

import inputHandle.Position;
import parser.Parser;
import parser.visitators.IVisitable;
import parser.visitators.IVisitator;

public class ArithmeticResult extends Expression implements IVisitable {
    public ArithmeticResult(Position pos) {
        super(pos);
    }

    public static ArithmeticResult parseArithmeticResult(Parser par) {
        return null; // TODO implement
    }

    @Override
    public void accept(IVisitator iv) {
        iv.visit(this);
    }
}
