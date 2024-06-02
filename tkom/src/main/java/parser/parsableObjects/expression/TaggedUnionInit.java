package parser.parsableObjects.expression;

import inputHandle.Position;
import interpreter.exceptions.InterperterException;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

import java.util.List;

public class TaggedUnionInit extends StructCall implements IVisitable {

    private final Expression initValue;

    public TaggedUnionInit(String structName, List<String> parameters, Expression initValue, Position position) {
        super(structName, parameters, position);
        this.initValue = initValue;
    }

    public Expression getInitValue() {
        return initValue;
    }

    @Override
    public void accept(IVisitor iv) throws InterperterException {
        iv.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaggedUnionInit)) return false;
        if (!super.equals(o)) return false;
        TaggedUnionInit that = (TaggedUnionInit) o;
        return initValue.equals(that.initValue);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + initValue.hashCode();
    }
}
