package interpreter.embedded.functions;

import inputHandle.Position;
import interpreter.dynamic.CallableFunction;
import interpreter.exceptions.InterperterException;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

import java.util.Objects;

public class structInitFunction extends CallableFunction implements IVisitable {

    String name;

    public structInitFunction(Position pos, String name) {
        super(pos);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void accept(IVisitor iv) throws InterperterException {
        iv.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        structInitFunction that = (structInitFunction) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
