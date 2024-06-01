package parser.visitators;

import interpreter.exceptions.InterperterException;

public interface IVisitable {

    public void accept(IVisitor iv) throws InterperterException;
}
