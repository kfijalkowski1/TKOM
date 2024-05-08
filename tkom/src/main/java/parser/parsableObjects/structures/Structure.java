package parser.parsableObjects.structures;

import inputHandle.Position;
import parser.parsableObjects.Statement;
import parser.parsableObjects.variables.ConstVariableDeclaration;
import parser.visitators.IVisitable;

import java.util.List;
import java.util.Objects;

public abstract class Structure extends Statement implements IVisitable {

    protected List<ConstVariableDeclaration> variablesDecl;
    protected String name;

    public Structure(Position pos) {
        super(pos);
    }

    public String getName() {
        return name;
    }

    public List<ConstVariableDeclaration> getVariablesDecl() {
        return variablesDecl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Structure)) return false;
        Structure structure = (Structure) o;
        return Objects.equals(variablesDecl, structure.variablesDecl) &&
                Objects.equals(name, structure.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variablesDecl, name);
    }



}
