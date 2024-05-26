package parser.parsableObjects.conditional.condition;

import inputHandle.Position;
import parser.exceptions.ParserException;
import parser.parsableObjects.expresions.Expression;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

public class ValueCondition extends Condition implements IVisitable {
    Expression value;



    public ValueCondition(Expression value, Position pos) throws ParserException {
        super(pos);
        this.value = value;
    }

    @Override
    public void accept(IVisitor iv) {
        iv.visit(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValueCondition)) return false;
        ValueCondition that = (ValueCondition) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
