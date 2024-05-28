package parser.parsableObjects.conditional;

import inputHandle.Position;
import parser.parsableObjects.Statement;
import parser.parsableObjects.blocks.Block;

import java.util.List;

public abstract class Conditional extends Block {
    private final List<Statement> block;

    public Conditional(List<Statement> blocks, Position pos) {
        super(pos);
        this.block = blocks;
    }

    public List<Statement> getBlock() {
        return block;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Conditional)) return false;
        Conditional that = (Conditional) o;
        return block.equals(that.block);
    }

    @Override
    public int hashCode() {
        return block.hashCode();
    }
}
