package parser.parsableObjects.expresions;

import exceptions.AnalizerException;
import inputHandle.Position;
import lekser.TokenType;
import parser.Parser;
import parser.exceptions.ParserException;
import parser.parsableObjects.variables.Value;
import parser.visitators.IVisitable;
import parser.visitators.IVisitator;

import java.util.List;

public class FunctionCall extends Expression implements IVisitable {
    private String funcName;
    private List<Expression> args; // can be int, flt, name or anything else

    public FunctionCall(String name, List<Expression> args, Position pos) {
        super(pos);
        this.funcName = name;
        this.args = args;
    }

    public String getName() {
        return funcName;
    }

    public List<Expression> getArgs() {
        return args;
    }


    @Override
    public void accept(IVisitator iv) {
        iv.visit(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FunctionCall)) return false;
        FunctionCall that = (FunctionCall) o;
        return funcName.equals(that.funcName) && args.equals(that.args);
    }
}
