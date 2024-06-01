package parser.parsableObjects.expression;

import inputHandle.Position;
import lekser.TokenType;
import parser.parsableObjects.blocks.Block;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

public class LiteralValue extends Expression implements IVisitable {

    @Override
    public void accept(IVisitor iv) {
        iv.visit(this);
    }

    private TokenType type;
    private Object value;


    public LiteralValue(Object value, TokenType tokType, Position pos) {
        super(pos);
        type = tokType;
        this.value = value;
    }

    public String getType() {
        return type.getType();
    }

    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LiteralValue)) return false;
        LiteralValue that = (LiteralValue) o;
        return type.equals(that.type) && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return type.hashCode() + value.hashCode();
    }
}
