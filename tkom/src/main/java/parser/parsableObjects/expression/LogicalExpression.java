package parser.parsableObjects.expression;

import inputHandle.Position;
import interpreter.exceptions.InterperterException;
import lekser.TokenType;
import parser.exceptions.ParserException;
import parser.parsableObjects.blocks.Block;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

import static parser.utils.ParserUtils.testers;

public class LogicalExpression extends Expression implements IVisitable {
    Expression left;
    Expression right;
    TokenType tester;


    public LogicalExpression(Expression left, Expression right, TokenType tester, Position pos) throws ParserException {
        super(pos);
        if (!testers.contains(tester)) {
            throw new ParserException(pos, "Invalid tester: " + tester.name());
        }
        this.left = left;
        this.right = right;
        this.tester = tester;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public TokenType getTester() {
        return tester;
    }

    @Override
    public void accept(IVisitor iv) throws InterperterException {
        iv.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogicalExpression)) return false;
        LogicalExpression that = (LogicalExpression) o;
        return left.equals(that.left) && right.equals(that.right) && tester == that.tester;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
