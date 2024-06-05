package interpreter.embedded.functions;

import inputHandle.Position;
import interpreter.dynamic.CallableFunction;
import interpreter.exceptions.InterperterException;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

public class strFunction extends CallableFunction implements IVisitable {


    public strFunction(Position pos) {
        super(pos);
    }

    @Override
    public void accept(IVisitor iv) throws InterperterException {
        iv.visit(this);
    }
}
