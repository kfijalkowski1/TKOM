package interpreter.embedded;

import inputHandle.Position;
import interpreter.exceptions.InterperterException;

import java.util.List;
import java.util.Vector;

public class LastValue {
    List<Object> lastValue;

    public LastValue() {
        lastValue = new Vector<>();
    }

    public void addLastValue(Object value) {
        lastValue.add(value);
    }

    public Value getSingleLastValue(Position pos) throws InterperterException {
        try {
            Object tmp = lastValue.get(lastValue.size() - 1);
            lastValue = lastValue.subList(0, lastValue.size() - 1);
            return (Value) tmp;
        } catch (IncompatibleClassChangeError | IndexOutOfBoundsException e1) {
            throw new InterperterException("Expected a single value", pos);
        }
    }

    public List<Value> getLastValueList(Position pos) throws InterperterException {
        Object tmp = lastValue.get(lastValue.size() - 1);
        lastValue = lastValue.subList(0, lastValue.size() - 1);
        try {
            return (List<Value>) tmp;
        } catch (IncompatibleClassChangeError e) {
            throw new InterperterException("Expected a single value", pos);
        }
    }

    public void cutLastValue(int size) {
        lastValue = lastValue.subList(0, size);
    }

    public int size() {
        return lastValue.size();
    }
}
