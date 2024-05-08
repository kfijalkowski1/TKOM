package parser.parsableObjects.variables;

import inputHandle.Position;
import parser.parsableObjects.expresions.Expression;
import parser.visitators.IVisitable;
import parser.visitators.IVisitator;

public class VariableInit extends ConstGlobalVariableDeclaration implements IVisitable {
    public final Value value;


    public VariableInit(String name, String type, Value value, Boolean isConst, Boolean isGlobal, Position pos) {
        super(name, type, isConst, isGlobal, pos);
        this.value = value;
    }

    public VariableInit(String name, String type, Value value, Boolean isConst, Boolean isGlobal,
                        Position pos, Boolean isReference) {
        super(name, type, isConst, isGlobal, pos, isReference);
        this.value = value;
    }

    @Override
    public void accept(IVisitator iv) {
        iv.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VariableInit)) return false;
        if (!super.equals(o)) return false;
        VariableInit that = (VariableInit) o;
        return value.equals(that.value);
    }

}
