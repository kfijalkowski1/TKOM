package parser.parsableObjects.structures;

import inputHandle.Position;
import interpreter.exceptions.InterperterException;
import parser.parsableObjects.variables.ConstVariableDeclaration;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

import java.util.List;

public class Struct extends Structure implements IVisitable {
    public Struct(String name, List<ConstVariableDeclaration> variables, Position pos) {
        super(pos);
        this.name = name;
        this.variablesDecl = variables;
    }

    @Override
    public void accept(IVisitor iv) throws InterperterException {
        iv.visit(this);
    }
}
