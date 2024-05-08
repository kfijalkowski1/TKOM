package parser.parsableObjects.structures;

import inputHandle.Position;
import parser.parsableObjects.variables.ConstVariableDeclaration;
import parser.visitators.IVisitable;
import parser.visitators.IVisitator;

import java.util.List;

public class Struct extends Structure implements IVisitable {
    public Struct(String name, List<ConstVariableDeclaration> variables, Position pos) {
        super(pos);
        this.name = name;
        this.variablesDecl = variables;
    }

    @Override
    public void accept(IVisitator iv) {
        iv.visit(this);
    }
}
