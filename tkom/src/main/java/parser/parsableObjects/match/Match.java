package parser.parsableObjects.match;

import inputHandle.Position;
import interpreter.exceptions.InterperterException;
import parser.parsableObjects.blocks.Block;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

import java.util.List;

public class Match extends Block implements IVisitable {

    private String variableName;
    private List<MatchCase> cases;

    public Match(String variableName, List<MatchCase> cases, Position pos) {
        super(pos);
        this.variableName = variableName;
        this.cases = cases;
    }

    public String getVariableName() {
        return variableName;
    }

    public List<MatchCase> getCases() {
        return cases;
    }

    @Override
    public void accept(IVisitor iv) throws InterperterException {
        iv.visit(this);
    }

    // implement equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match that = (Match) o;
        return cases.equals(that.cases);
    }

    @Override
    public int hashCode() {
        return cases.hashCode();
    }

}
