package parser.parsableObjects.structures;

import inputHandle.Position;
import parser.parsableObjects.variables.ConstVariableDeclaration;
import parser.visitators.IVisitable;
import parser.visitators.IVisitator;

import java.util.List;

public class TaggedUnion extends Structure implements IVisitable {
    public TaggedUnion(String name, List<ConstVariableDeclaration> variablesDecl, Position pos) {
        super(pos);
        this.name = name;
        this.variablesDecl = variablesDecl;
    }


    @Override
    public void accept(IVisitator iv) {
        iv.visit(this);
    }
}
