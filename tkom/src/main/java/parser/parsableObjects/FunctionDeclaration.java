package parser.parsableObjects;

import inputHandle.Position;
import interpreter.exceptions.InterperterException;
import parser.parsableObjects.variables.VariableDeclaration;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

import java.util.List;
import java.util.Objects;


public class FunctionDeclaration extends Statement implements IVisitable {
    private final String name;
    private final String returnType;
    private final List<VariableDeclaration> parameters;
    private final List<Statement> blocks;

    public FunctionDeclaration(String name, List<VariableDeclaration> parameters, List<Statement> blocks,
                               String returnType, Position pos) {
        super(pos);
        this.name = name;
        this.parameters = parameters;
        this.blocks = blocks;
        this.returnType = returnType;
    }

    public String getName() {
        return name;
    }

    public String getReturnType() {
        return returnType;
    }

    public List<VariableDeclaration> getParameters() {
        return parameters;
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
        FunctionDeclaration that = (FunctionDeclaration) o;
        return Objects.equals(name, that.name) && Objects.equals(returnType, that.returnType) && Objects.equals(parameters, that.parameters) && Objects.equals(blocks, that.blocks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, returnType, parameters, blocks);
    }



}
