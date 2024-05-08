package parser.parsableObjects.arithmatic.results;

import inputHandle.Position;
import lekser.TokenType;
import parser.parsableObjects.expresions.Expression;
import parser.visitators.IVisitable;
import parser.visitators.IVisitator;

import java.util.Objects;

public abstract class ArithmeticResult extends Expression {
    Expression left;
    Expression right;

    public ArithmeticResult(Expression left, Expression right, Position pos) {
        super(pos);
        this.left = left;
        this.right = right;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArithmeticResult that = (ArithmeticResult) o;
        return Objects.equals(left, that.left) && Objects.equals(right, that.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
