package interpreter;


import inputHandle.FileSource;
import inputHandle.Source;
import lekser.TokenBuilder;
import parser.Parser;
import parser.parsableObjects.Program;


public class Main {
    public static void main(String[] args) throws Exception {
        Source src = new FileSource("src/test/generalTest.sp");
        TokenBuilder tokenBuilder = new TokenBuilder(src);
        Parser parser = new Parser(tokenBuilder);
        Program program = parser.parseProgram();
        Interpreter interpreter = new Interpreter();
        interpreter.interpret(program);

    }
}