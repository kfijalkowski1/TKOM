package parser.parsableObjects.variables;

import inputHandle.Position;
import parser.parsableObjects.blocks.Block;

public abstract class Variable extends Block {

    public Variable(Position pos) {
        super(pos);
    }

}
