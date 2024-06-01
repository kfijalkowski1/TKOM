package parser.parsableObjects.expression;

import interpreter.exceptions.InterperterException;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

import java.util.Objects;

public class NegatedExpression  extends Expression implements IVisitable {
    private Expression expression;

    public NegatedExpression(Expression expression) {
        super(expression.getPosition());
        this.expression = expression;

    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "NegatedExpression{" +
                "expression=" + expression +
                '}';
    }

    @Override
    public void accept(IVisitor iv) throws InterperterException {
        iv.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NegatedExpression that = (NegatedExpression) o;
        return Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(expression);
    }
}
