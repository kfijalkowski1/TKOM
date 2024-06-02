package interpreter;


import inputHandle.Source;
import inputHandle.StringSource;
import interpreter.embedded.Value;
import lekser.TokenBuilder;
import parser.Parser;
import parser.parsableObjects.Program;

import java.util.LinkedHashMap;

public class Main {
    public static void main(String[] args) throws Exception {
        Source src = new StringSource("print(10/0)");
        TokenBuilder tokenBuilder = new TokenBuilder(src);
        Parser parser = new Parser(tokenBuilder);
        Program program = parser.parseProgram();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);

    }

    private static void setVariable(Value va) {
        va.setValue(10);
    }
}