package parser.parsableObjects.conditional.condition;

import inputHandle.Position;
import lekser.TokenType;
import parser.exceptions.ParserException;
import parser.parsableObjects.expresions.Expression;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

import static parser.utils.ParserUtils.testers;

public class TestCondition extends Condition implements IVisitable {
    Expression left;
    Expression right;
    TokenType tester;



    public TestCondition(Expression left, Expression right, TokenType tester, Position pos) throws ParserException {
        super(pos);
        if (!testers.contains(tester)) {
            throw new ParserException(pos, "Invalid tester: " + tester.name());
        }
        this.left = left;
        this.right = right;
        this.tester = tester;
    }

    @Override
    public void accept(IVisitor iv) {
        iv.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestCondition)) return false;
        TestCondition that = (TestCondition) o;
        return left.equals(that.left) && right.equals(that.right) && tester == that.tester;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
