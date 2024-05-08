package parser.parsableObjects.match;

import inputHandle.Position;
import parser.parsableObjects.expresions.Expression;
import parser.visitators.IVisitable;
import parser.visitators.IVisitator;

import java.util.List;

public class MatchCase extends Expression implements IVisitable {
    String taggedUnionName;
    String taggedUnionCase;
    String variableName;
    List<Expression> expressions;

    public MatchCase(String taggedUnionName, String taggedUnionCase, String variableName, List<Expression> expressions, Position pos) {
        super(pos);
        this.taggedUnionName = taggedUnionName;
        this.taggedUnionCase = taggedUnionCase;
        this.variableName = variableName;
        this.expressions = expressions;
    }

    public String getTaggedUnionName() {
        return taggedUnionName;
    }

    public String getTaggedUnionCase() {
        return taggedUnionCase;
    }

    public String getVariableName() {
        return variableName;
    }

    public List<Expression> getExpressions() {
        return expressions;
    }

    @Override
    public void accept(IVisitator iv) {
        iv.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchCase that = (MatchCase) o;
        return taggedUnionName.equals(that.taggedUnionName) && taggedUnionCase.equals(that.taggedUnionCase) && variableName.equals(that.variableName) && expressions.equals(that.expressions);
    }

    @Override
    public int hashCode() {
        return taggedUnionName.hashCode() + taggedUnionCase.hashCode() + variableName.hashCode() + expressions.hashCode();
    }
}
