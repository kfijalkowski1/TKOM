package parser.parsableObjects.conditional;

import inputHandle.Position;
import parser.parsableObjects.expresions.Expression;

import java.util.List;

public abstract class Conditional extends Expression {
    private final List<Expression> block;

    public Conditional(List<Expression> expressions, Position pos) {
        super(pos);
        this.block = expressions;
    }

    public List<Expression> getBlock() {
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
