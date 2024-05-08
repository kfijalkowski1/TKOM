package parser;

import exceptions.AnalizerException;
import inputHandle.Position;
import inputHandle.StringSource;
import lekser.TokenBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import parser.parsableObjects.*;
import parser.parsableObjects.arithmatic.Increment;
import parser.parsableObjects.arithmatic.PostIncrement;
import parser.parsableObjects.expresions.Expression;
import parser.parsableObjects.expresions.FunctionCall;
import parser.parsableObjects.expresions.ReturnExpression;
import parser.parsableObjects.match.Match;
import parser.parsableObjects.match.MatchCase;
import parser.parsableObjects.structures.StructCall;
import parser.parsableObjects.structures.StructValueAssigment;
import parser.parsableObjects.variables.*;
import parser.parsableObjects.structures.Struct;
import parser.parsableObjects.structures.TaggedUnion;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @ParameterizedTest
    @MethodSource("taggedUnionParser")
    @DisplayName("Test singular creation of single element program parsing")
    void parseTaggedUnion(String text, Object programElement) throws AnalizerException {
        Parser parser = new Parser(new TokenBuilder(new StringSource(text)));
        Program program = parser.parseProgram();
        System.out.println(program.getStatements().get(0)); // TODO delete
        assertEquals(program.getStatements().get(0), programElement);
    }

    private static Stream<Arguments> taggedUnionParser() {
        return Stream.of(
                Arguments.of("TaggedUnion testTUName { int a, int b }",
                        new TaggedUnion("testTUName",
                                List.of(new ConstVariableDeclaration("a", "int", false, new Position()),
                                        new ConstVariableDeclaration("b", "int", false, new Position())),
                                new Position())),
                Arguments.of("struct testStructName { const flt c, testTUName eee }",
                        new Struct("testStructName",
                                List.of(new ConstVariableDeclaration("c", "flt", true, new Position()),
                                        new ConstVariableDeclaration("eee", "testTUName", false, new Position())),
                                new Position())),
                Arguments.of("fun testFunName (int a, flt b) -> int { bool c }",
                        new FunctionDeclaration("testFunName",
                        List.of(new VariableDeclaration("a", "int",  new Position()),
                                new VariableDeclaration("b", "flt", new Position())),
                        List.of(new ConstGlobalVariableDeclaration("c", "bool", false, false, new Position())),
                        "int", new Position())),
                Arguments.of("fun returnFunc (int a, flt b) -> int { return a }",
                        new FunctionDeclaration("returnFunc",
                                List.of(new VariableDeclaration("a", "int",  new Position()),
                                        new VariableDeclaration("b", "flt", new Position())),
                                List.of(new ReturnExpression(new Value(new VariableCall("a", new Position()), new Position()), new Position())),
                                "int", new Position())),
                Arguments.of("fun testFunName (int a, flt b) -> void { funcCall() \n int a }",
                        new FunctionDeclaration("testFunName",
                                List.of(new VariableDeclaration("a", "int",  new Position()),
                                        new VariableDeclaration("b", "flt",  new Position())),
                                List.of(new FunctionCall("funcCall", List.of(), new Position()), new ConstGlobalVariableDeclaration("a", "int", false, false, new Position())),
                                "void", new Position())),
                Arguments.of("fun testFunName (das a, flt b) -> fds { bool c }",
                        new FunctionDeclaration("testFunName",
                                List.of(new VariableDeclaration("a", "das",  new Position()),
                                        new VariableDeclaration("b", "flt",  new Position())),
                                List.of(new ConstGlobalVariableDeclaration("c", "bool", false, false, new Position())),
                                "fds", new Position())),
                Arguments.of("testFunCallName(1, 2)",
                        new FunctionCall("testFunCallName",
                                List.of(new Value(1,  new Position()),
                                        new Value(2,  new Position()))
                                        , new Position())),
                Arguments.of("testFunCallNameRef(&a)",
                        new FunctionCall("testFunCallNameRef",
                                List.of(new Value(new VariableCall("a", new Position(), true),  new Position()))
                                , new Position())),
                Arguments.of("testFunCallName(a, secondFuncCall(1))",
                        new FunctionCall("testFunCallName",
                                List.of(new Value(new VariableCall("a",  new Position()), new Position()),
                                        new Value(new FunctionCall("secondFuncCall",
                                                List.of(new Value(1, new Position())), new Position()), new Position()))
                                , new Position())),
                Arguments.of("const int testVarName = 1",
                        new VariableInit("testVarName", "int", new Value(1,
                                new Position()), true, false, new Position())),
                Arguments.of("gscope const flt testVarName = 1.01",
                        new VariableInit("testVarName", "flt", new Value(1.01f,
                                new Position()), true, true, new Position())),
                Arguments.of("gscope const flt testVarName = 1.01",
                        new VariableInit("testVarName", "flt", new Value(1.01f,
                                new Position()), true, true, new Position())),
                Arguments.of("a = 6",
                        new VariableAssigment(new Value(6, new Position()), "a", new Position())),
                Arguments.of("const &int a = 6",
                        new VariableInit("a", "int", new Value(6, new Position()), true, false, new Position(), true)),
                Arguments.of("& dsa a = 6",
                        new VariableInit("a", "dsa", new Value(6, new Position()), false, false, new Position(), true)),
                Arguments.of("a++",
                        new PostIncrement(new VariableCall("a", new Position()), new Position())),
                Arguments.of("&a++",
                        new PostIncrement(new VariableCall("a", new Position(), true), new Position())),
                Arguments.of("a+=1",
                        new Increment(new VariableCall("a", new Position()), new Value(1, new Position()), new Position())),
                Arguments.of("point.x = 1",
                        new StructValueAssigment(new StructCall("point", List.of("x"), new Position()), new Value(1, new Position()), new Position())),
                Arguments.of("match s {\n" +
                                "                  Shape.cir(value) {\n" +
                                "                   int a\n" +
                                "                   }}",
                        new Match("s",
                                List.of(new MatchCase("Shape", "cir", "value",
                                        List.of(new ConstGlobalVariableDeclaration("a", "int", false, false, new Position())), new Position())), new Position())),
                Arguments.of("Shape cShape = Shape.cir(c)",
                        new VariableInit("cShape", "Shape", new Value(new StructInit("Shape", List.of("cir"), List.of(new Value(new VariableCall("c", new Position()), new Position())), new Position()), new Position()), false, false, new Position()))

        );
    }
}