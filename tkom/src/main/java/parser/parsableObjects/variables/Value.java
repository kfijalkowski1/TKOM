package parser.parsableObjects.variables;

import inputHandle.Position;
import lekser.TokenType;
import parser.parsableObjects.expresions.Expression;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

public class Value extends Expression implements IVisitable {

    @Override
    public void accept(IVisitor iv) {
        iv.visit(this);
    }

    private TokenType type;
    private Object value;


    public Value(Object value, TokenType tokType, Position pos) {
        super(pos);
        type = tokType;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Value)) return false;
        Value that = (Value) o;
        return type.equals(that.type) && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return type.hashCode() + value.hashCode();
    }
}
