package interpreter.dynamic;

import interpreter.embedded.ValueType;
import interpreter.exceptions.InterperterException;
import parser.parsableObjects.Statement;
import parser.parsableObjects.variables.VariableDeclaration;
import parser.visitators.IVisitable;
import parser.visitators.IVisitor;

import java.util.List;
import java.util.Objects;

public class DynamicFunction extends CallableFunction implements IVisitable {

    private final List<Statement> blocks;
    private final List<VariableDeclaration> parameters;
    private final String returnType;

    public DynamicFunction(List<Statement> blocks, List<VariableDeclaration> parameters, String returnType) {

        super(blocks.get(0).getPosition());
        this.blocks = blocks;
        this.parameters = parameters;
        this.returnType = returnType;
    }

    public List<Statement> getBlocks() {
        return blocks;
    }

    public String getReturnType() {
        return returnType;
    }

    public List<VariableDeclaration> getParameters() {
        return parameters;
    }

    @Override
    public void accept(IVisitor iv) throws InterperterException {
         iv.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DynamicFunction that = (DynamicFunction) o;
        return Objects.equals(blocks, that.blocks) && Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blocks, parameters);
    }
}
