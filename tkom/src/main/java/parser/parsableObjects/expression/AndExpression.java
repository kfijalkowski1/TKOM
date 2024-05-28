package parser.parsableObjects.expression;

import inputHandle.Position;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

public class AndExpression extends Expression implements IVisitable {
    Expression left;
    Expression right;



    public AndExpression(Expression left, Expression right, Position pos) {
        super(pos);
        this.left = left;
        this.right = right;
    }

    @Override
    public void accept(IVisitor iv) {
        iv.visit(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AndExpression)) return false;
        AndExpression that = (AndExpression) o;
        return left.equals(that.left) && right.equals(that.right);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
