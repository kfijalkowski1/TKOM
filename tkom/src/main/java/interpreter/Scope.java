package interpreter;

import interpreter.embedded.Value;
import interpreter.embedded.ValueType;
import parser.parsableObjects.variables.Type;

import java.util.HashMap;
import java.util.Map;

public class Scope {

    Map<String, Value> variables = new HashMap<>();

    public void addVariable(String name, Value value) {
        variables.put(name, value);
    }

    public void addVariable(String name, Type type, Object value) {
        variables.put(name, new Value(value, type.getName(), type.isReference(), false));
    }

    public void addVariable(String name, Object value, ValueType type) {
        variables.put(name, new Value(value, type));
    }

    public void addVariable(String name, Type type, Object value, Boolean isConst) {
        variables.put(name, new Value(value, type.getName(), type.isReference(), isConst));
    }

    public void addRefVariable(String name, Value value) {
        variables.put(name, value);
    }
}
