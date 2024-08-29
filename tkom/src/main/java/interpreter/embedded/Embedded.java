package interpreter.embedded;

import inputHandle.Position;
import interpreter.dynamic.AllTypes;
import interpreter.dynamic.CallableFunction;
import interpreter.embedded.functions.*;
import interpreter.embedded.types.BoolType;
import interpreter.embedded.types.FltType;
import interpreter.embedded.types.IntType;
import interpreter.embedded.types.StrType;

import java.util.HashMap;

public class Embedded {
    private static final HashMap<String, CallableFunction> embeddedFunctions = new HashMap<>();
    private static final HashMap<String, AllTypes> embeddedTypes = new HashMap<>();

    static {
        embeddedFunctions.put("Int", new intFunction(new Position(0, 0)));
        embeddedFunctions.put("Flt", new fltFunction(new Position(0, 0)));
        embeddedFunctions.put("Str", new strFunction(new Position(0, 0)));
        embeddedFunctions.put("print", new printFunction(new Position(0, 0)));
    }

    static {
        embeddedTypes.put(IntType.getName(), new IntType());
        embeddedTypes.put(FltType.getName(), new FltType());
        embeddedTypes.put(BoolType.getName(), new BoolType());
        embeddedTypes.put(StrType.getName(), new StrType());
    }

    public static HashMap<String, CallableFunction> getEmbeddedFunctions() {
        return embeddedFunctions;
    }



    public static HashMap<String, AllTypes> getEmbeddedTypes() {
        return embeddedTypes;
    }
}