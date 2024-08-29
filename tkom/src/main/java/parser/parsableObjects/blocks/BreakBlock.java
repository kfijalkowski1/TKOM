package parser.parsableObjects.blocks;

import inputHandle.Position;
import interpreter.exceptions.InterperterException;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

public class BreakBlock extends Block implements IVisitable {
    public BreakBlock(Position position) {
        super(position);
    }

    @Override
    public void accept(IVisitor iv) throws InterperterException {
        iv.visit(this);
    }
}
