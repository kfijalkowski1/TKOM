package parser.parsableObjects;
import inputHandle.Position;
import parser.visitators.IVisitable;

public abstract class AbsFunctionDeclaration extends Statement {
    public AbsFunctionDeclaration(Position pos) {
        super(pos);
    }

}
