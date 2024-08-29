package interpreter.embedded;

import java.util.Objects;

public class ValueType {
    String type;
    Boolean isReference;
    Boolean isConst;

    public ValueType(String type, Boolean isReference, Boolean isConst) {
        this.type = type;
        this.isReference = isReference;
        this.isConst = isConst;
    }

    public String getType() {
        return type;
    }

    public Boolean isReference() {
        return isReference;
    }

    public Boolean isConst() {
        return isConst;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setIsReference(Boolean isReference) {
        this.isReference = isReference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValueType valueType = (ValueType) o;
        return Objects.equals(type, valueType.type) && Objects.equals(isReference, valueType.isReference) && Objects.equals(isConst, valueType.isConst);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, isReference, isConst);
    }

    @Override
    public String toString() {
        return "ValueType{" +
                "type='" + type + '\'' +
                ", isReference=" + isReference +
                ", isConst=" + isConst +
                '}';
    }
}
