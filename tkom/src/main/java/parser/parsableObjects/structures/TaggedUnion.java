package parser.parsableObjects.structures;

import inputHandle.Position;
import parser.parsableObjects.variables.ConstVariableDeclaration;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

import java.util.List;

public class TaggedUnion extends Structure implements IVisitable {
    public TaggedUnion(String name, List<ConstVariableDeclaration> variablesDecl, Position pos) {
        super(pos);
        this.name = name;
        this.variablesDecl = variablesDecl;
    }


    @Override
    public void accept(IVisitor iv) {
        iv.visit(this);
    }
}
