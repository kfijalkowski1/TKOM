package interpreter.dynamic;

import inputHandle.Position;
import parser.parsableObjects.Statement;
import parser.visitators.IVisitable;

public abstract class CallableFunction extends Statement implements IVisitable {
    public CallableFunction(Position pos) {
        super(pos);
    }
}
