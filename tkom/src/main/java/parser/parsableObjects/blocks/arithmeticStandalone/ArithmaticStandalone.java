package parser.parsableObjects.blocks.arithmeticStandalone;

import inputHandle.Position;
import parser.parsableObjects.blocks.Block;
import parser.parsableObjects.expression.VariableCall;

public abstract class ArithmaticStandalone extends Block {
    private final VariableCall variable;

    public ArithmaticStandalone(VariableCall variable, Position pos) {
        super(pos);
        this.variable = variable;
    }

    public VariableCall getVariable() {
        return variable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArithmaticStandalone)) return false;
        ArithmaticStandalone that = (ArithmaticStandalone) o;
        return variable.equals(that.variable);
    }


    @Override
    public int hashCode() {
        return variable.hashCode();
    }
}
