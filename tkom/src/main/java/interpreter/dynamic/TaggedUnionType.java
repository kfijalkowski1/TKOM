package interpreter.dynamic;

import interpreter.embedded.Value;
import interpreter.embedded.ValueType;

import java.util.LinkedHashMap;
import java.util.Map;

public class TaggedUnionType extends DynamicType {

    public TaggedUnionType(LinkedHashMap<String, ValueType> fields) {
        super(fields);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
