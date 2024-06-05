package interpreter.embedded;

import lekser.TokenType;

import java.util.Objects;

public class Value {
    Object value;
    ValueType valueType;

    public Value(Object value, String type, Boolean isReference, Boolean isConst) {
        this.value = value;
        valueType = new ValueType(type, isReference, isConst);
    }

    public Value(Object value, ValueType valueType) {
        this.value = value;
        this.valueType = valueType;
    }

    public String getTypeName() {
        return valueType.getType();
    }

    public Object getValue() {
        return value;
    }

    public String getType() {
        return valueType.getType();
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setReference(Boolean reference) {
        this.valueType.setIsReference(reference);
    }

    public void setType(String type) {
        this.valueType.setType(type);
    }


    // check build in types
    public Boolean isInteger() {
        return valueType.getType().equals(TokenType.INT_KEYWORD.toString());
    }

    public Boolean isFloat() {
        return valueType.getType().equals(TokenType.FLT_KEYWORD.toString());
    }

    public Boolean isString() {
        return valueType.getType().equals(TokenType.STR_KEYWORD.toString());
    }

    public Boolean isBoolean() {
        return valueType.getType().equals(TokenType.BOOL_KEYWORD.toString());
    }

    public boolean isConst() {
        return valueType.isConst();
    }

    public boolean isReference() {
        return valueType.isReference();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Value value1 = (Value) o;
        return Objects.equals(value, value1.value) && Objects.equals(valueType, value1.valueType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, valueType);
    }

    @Override
    public String toString() {
        return "Value{" +
                "value=" + value +
                ", valueType=" + valueType +
                '}';
    }
}
