package parser.parsableObjects.variables;

import inputHandle.Position;
import parser.parsableObjects.arithmatic.results.ArithmeticResult;
import parser.parsableObjects.expresions.Expression;
import parser.visitators.IVisitable;
import parser.visitators.IVisitator;

public class Value extends Expression implements IVisitable {

    @Override
    public void accept(IVisitator iv) {
        iv.visit(this);
    }

    private enum ValueType {
        INT,
        FLT,
        STR,
        BOOL,
        NAME,
        EXPRESSION,
        ARITHMETIC_RESULT}

    private ValueType type;
    private Object value;

    public Value(Integer value, Position pos) {
        super(pos);
        type = ValueType.INT;
        this.value = value;
    }

    public Value(Float value, Position pos) {
        super(pos);
        type = ValueType.FLT;
        this.value = value;
    }

    public Value(Boolean value, Position pos) {
        super(pos);
        type = ValueType.BOOL;
        this.value = value;
    }

    public Value(ArithmeticResult value, Position pos) {
        super(pos);
        type = ValueType.ARITHMETIC_RESULT;
        this.value = value;
    }

    public Value(Expression value, Position pos) {
        super(pos);
        type = ValueType.EXPRESSION;
        this.value = value;
    }

    public Value(String value, Boolean isName, Position pos) {
        super(pos);
        if (isName) {
            type = ValueType.NAME;
        } else {
            type = ValueType.STR;
        }
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
