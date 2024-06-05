package parser.parsableObjects.expression;

import inputHandle.Position;
import interpreter.exceptions.InterperterException;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

public class OrExpression extends Expression implements IVisitable {
    Expression left;
    Expression right;



    public OrExpression(Expression left, Expression right, Position pos) {
        super(pos);
        this.left = left;
        this.right = right;
    }

    @Override
    public void accept(IVisitor iv) throws InterperterException {
        iv.visit(this);
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrExpression)) return false;
        OrExpression that = (OrExpression) o;
        return left.equals(that.left) && right.equals(that.right);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
