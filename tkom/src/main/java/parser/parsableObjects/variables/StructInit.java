package parser.parsableObjects.variables;

import inputHandle.Position;
import parser.parsableObjects.expresions.Expression;
import parser.parsableObjects.structures.StructCall;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

import java.util.List;

public class StructInit extends StructCall implements IVisitable {

    private final List<Expression> initValue;

    public StructInit(String structName, List<String> parameters, List<Expression> initValue, Position position) {
        super(structName, parameters, position);
        this.initValue = initValue;
    }

    @Override
    public void accept(IVisitor iv) {
        iv.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StructInit)) return false;
        if (!super.equals(o)) return false;
        StructInit that = (StructInit) o;
        return initValue.equals(that.initValue);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + initValue.hashCode();
    }
}
