package parser.parsableObjects.conditional.condition;

import inputHandle.Position;
import parser.exceptions.ParserException;
import parser.visitators.IVisitable;
import parser.visitators.IVisitator;

public class OrCondition extends Condition implements IVisitable {
    Condition left;
    Condition right;



    public OrCondition(Condition left, Condition right, Position pos) {
        super(pos);
        this.left = left;
        this.right = right;
    }

    @Override
    public void accept(IVisitator iv) {
        iv.visit(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrCondition)) return false;
        OrCondition that = (OrCondition) o;
        return left.equals(that.left) && right.equals(that.right);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
