package interpreter.dynamic;

import interpreter.embedded.ValueType;

import java.util.LinkedHashMap;
import java.util.Objects;

/*

class desribes dynamic types aka types created via struct/tagged union
 */
public class DynamicType implements AllTypes {
    private LinkedHashMap<String, ValueType> fields = new LinkedHashMap<>();

    public DynamicType(LinkedHashMap<String, ValueType> fields) {
        this.fields = fields;
    }

    public LinkedHashMap<String, ValueType> getFields() {
        return fields;
    }

    public void setFields(LinkedHashMap<String, ValueType> fields) {
        this.fields = fields;
    }

    public void addField(String name, ValueType value) {
        fields.put(name, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DynamicType that = (DynamicType) o;
        return Objects.equals(fields, that.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fields);
    }
}
