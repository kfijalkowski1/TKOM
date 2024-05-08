package parser.parsableObjects;

import inputHandle.Position;
import parser.parsableObjects.variables.ConstGlobalVariableDeclaration;
import parser.parsableObjects.expresions.Expression;
import parser.parsableObjects.variables.VariableDeclaration;
import parser.visitators.IVisitable;
import parser.visitators.IVisitator;

import java.util.List;
import java.util.Objects;


public class FunctionDeclaration extends Statement implements IVisitable {
    private final String name;
    private final String returnType;
    private final List<VariableDeclaration> parameters;
    private final List<Expression> expressions;

    public FunctionDeclaration(String name, List<VariableDeclaration> parameters, List<Expression> expressions,
                               String returnType, Position pos) {
        super(pos);
        this.name = name;
        this.parameters = parameters;
        this.expressions = expressions;
        this.returnType = returnType;
    }

    public String getName() {
        return name;
    }

    public List<VariableDeclaration> getParameters() {
        return parameters;
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
        FunctionDeclaration that = (FunctionDeclaration) o;
        return Objects.equals(name, that.name) && Objects.equals(returnType, that.returnType) && Objects.equals(parameters, that.parameters) && Objects.equals(expressions, that.expressions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, returnType, parameters, expressions);
    }



}
