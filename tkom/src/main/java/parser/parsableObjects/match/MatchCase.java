package parser.parsableObjects.match;

import inputHandle.Position;
import interpreter.exceptions.InterperterException;
import parser.parsableObjects.Statement;
import parser.parsableObjects.blocks.Block;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

import java.util.List;

public class MatchCase extends Block implements IVisitable {
    String taggedUnionName;
    String taggedUnionCase;
    String variableName;
    List<Statement> blocks;

    public MatchCase(String taggedUnionName, String taggedUnionCase, String variableName, List<Statement> blocks, Position pos) {
        super(pos);
        this.taggedUnionName = taggedUnionName;
        this.taggedUnionCase = taggedUnionCase;
        this.variableName = variableName;
        this.blocks = blocks;
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

    public List<Statement> getBlocks() {
        return blocks;
    }

    @Override
    public void accept(IVisitor iv) throws InterperterException {
        iv.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchCase that = (MatchCase) o;
        return taggedUnionName.equals(that.taggedUnionName) && taggedUnionCase.equals(that.taggedUnionCase) && variableName.equals(that.variableName) && blocks.equals(that.blocks);
    }

    @Override
    public int hashCode() {
        return taggedUnionName.hashCode() + taggedUnionCase.hashCode() + variableName.hashCode() + blocks.hashCode();
    }
}
