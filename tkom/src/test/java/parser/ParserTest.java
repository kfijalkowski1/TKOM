package parser;

import exceptions.AnalizerException;
import inputHandle.Position;
import inputHandle.StringSource;
import lekser.TokenBuilder;
import lekser.TokenType;
import lekser.exceptions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import parser.exceptions.ParserException;
import parser.parsableObjects.*;
import parser.parsableObjects.expression.*;
import parser.parsableObjects.blocks.arithmeticStandalone.Increment;
import parser.parsableObjects.blocks.arithmeticStandalone.PostIncrement;
import parser.parsableObjects.conditional.ElIfConditional;
import parser.parsableObjects.conditional.ElseConditional;
import parser.parsableObjects.conditional.IfConditional;
import parser.parsableObjects.conditional.WhileConditional;
import parser.parsableObjects.blocks.FunctionCall;
import parser.parsableObjects.blocks.ReturnBlock;
import parser.parsableObjects.expression.arithmatic.results.*;
import parser.parsableObjects.match.Match;
import parser.parsableObjects.match.MatchCase;
import parser.parsableObjects.expression.StructCall;
import parser.parsableObjects.structures.StructValueAssigment;
import parser.parsableObjects.variables.*;
import parser.parsableObjects.structures.Struct;
import parser.parsableObjects.structures.TaggedUnion;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {



    @ParameterizedTest
    @MethodSource("singularBlocks")
    @DisplayName("Test singular creation of single element program parsing")
    void parseTaggedUnion(String text, Object programElement) throws AnalizerException {
        testSingle(text, programElement);
    }

    private static Stream<Arguments> singularBlocks() throws ParserException {
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
                                List.of(new ReturnBlock(new VariableCall("a", new Position()), new Position())),
                                "int", new Position())),
                Arguments.of("fun returnFunc (&int a, &gfdt b) -> int { return a }",
                        new FunctionDeclaration("returnFunc",
                                List.of(new VariableDeclaration("a", "int",  new Position(), true),
                                        new VariableDeclaration("b", "gfdt", new Position(), true)),
                                List.of(new ReturnBlock(new VariableCall("a", new Position()), new Position())),
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
                                List.of(new LiteralValue(1,  TokenType.INT_NUMBER, new Position()),
                                        new LiteralValue(2,  TokenType.INT_NUMBER, new Position()))
                                        , new Position())),
                Arguments.of("emptyRun()",
                        new FunctionCall("emptyRun",
                                List.of()
                                , new Position())),
                Arguments.of("testFunCallNameRef(&a)",
                        new FunctionCall("testFunCallNameRef",
                                List.of(new VariableCall("a", new Position(), true))
                                , new Position())),
                Arguments.of("testFunCallName(a, secondFuncCall(1))",
                        new FunctionCall("testFunCallName",
                                List.of(new VariableCall("a",  new Position()),
                                        new FunctionCall("secondFuncCall",
                                                List.of(new LiteralValue(1, TokenType.INT_NUMBER, new Position())), new Position()))
                                , new Position())),
                Arguments.of("const int testVarName = 1",
                        new VariableInit("testVarName", "int", new LiteralValue(1, TokenType.INT_NUMBER,
                                new Position()), true, false, new Position())),
                Arguments.of("gscope const flt testVarName = 1.01",
                        new VariableInit("testVarName", "flt", new LiteralValue(1.01f, TokenType.FLT_NUMBER,
                                new Position()), true, true, new Position())),
                Arguments.of("gscope const flt testVarName = 1.01",
                        new VariableInit("testVarName", "flt", new LiteralValue(1.01f, TokenType.FLT_NUMBER,
                                new Position()), true, true, new Position())),
                Arguments.of("a = 6",
                        new VariableAssigment(new LiteralValue(6, TokenType.INT_NUMBER, new Position()), "a", new Position())),
                Arguments.of("const &int a = 6",
                        new VariableInit("a", "int", new LiteralValue(6, TokenType.INT_NUMBER, new Position()), true, false, new Position(), true)),
                Arguments.of("& dsa a = 6",
                        new VariableInit("a", "dsa", new LiteralValue(6, TokenType.INT_NUMBER, new Position()), false, false, new Position(), true)),
                Arguments.of("a++",
                        new PostIncrement(new VariableCall("a", new Position()), new Position())),
                Arguments.of("&a++",
                        new PostIncrement(new VariableCall("a", new Position(), true), new Position())),
                Arguments.of("a+=1",
                        new Increment(new VariableCall("a", new Position()), new LiteralValue(1, TokenType.INT_NUMBER, new Position()), new Position())),
                Arguments.of("point.x = 1",
                        new StructValueAssigment(new StructCall("point", List.of("x"), new Position()), new LiteralValue(1, TokenType.INT_NUMBER, new Position()), new Position())),

                Arguments.of("b = a or b",
                        new VariableAssigment(new OrExpression(
                                new VariableCall("a", new Position()),
                                new VariableCall("b", new Position()), new Position()),
                                "b", new Position())),
                Arguments.of("b = a > b",
                        new VariableAssigment(new LogicalExpression(
                                new VariableCall("a", new Position()),
                                new VariableCall("b", new Position()),
                                TokenType.MORE_OP,
                                new Position()),
                                "b", new Position())),
                Arguments.of("b = 4 > 2",
                        new VariableAssigment(
                                new LogicalExpression(
                                        new LiteralValue(4, TokenType.INT_NUMBER, new Position()),
                                        new LiteralValue(2, TokenType.INT_NUMBER, new Position()),
                                        TokenType.MORE_OP, new Position()),
                                "b", new Position())),
                Arguments.of("b = g + 8",
                        new VariableAssigment(
                                new AddResult(
                                        new VariableCall("g", new Position()),
                                        new LiteralValue(8, TokenType.INT_NUMBER, new Position()), new Position()), "b", new Position())),
                Arguments.of("b = g() + 8",
                        new VariableAssigment(
                                new AddResult(
                                        new FunctionCall("g", List.of(), new Position()),
                                        new LiteralValue(8, TokenType.INT_NUMBER, new Position()), new Position()), "b", new Position())),
                Arguments.of("int b = c.d > e.f",
                        new VariableInit("b", "int",
                                new LogicalExpression(
                                        new StructCall("c", List.of("d"), new Position()),
                                        new StructCall("e", List.of("f"), new Position()),
                                        TokenType.MORE_OP, new Position()),
                                false, false, new Position())),
                Arguments.of("b = (g + 4 )+ (d.d ** 2)",
                        new VariableAssigment(
                                new AddResult(
                                        new AddResult(
                                                new VariableCall("g", new Position()),
                                                new LiteralValue(4, TokenType.INT_NUMBER, new Position()), new Position()),
                                        new PowResult(new StructCall("d", List.of("d"), new Position()),
                                                new LiteralValue(2, TokenType.INT_NUMBER, new Position()), new Position()), new Position()),
                                "b", new Position())),
                Arguments.of("a.b = 18",
                        new StructValueAssigment(
                                new StructCall("a", List.of("b"), new Position()),
                                new LiteralValue(18, TokenType.INT_NUMBER, new Position()), new Position())),
                Arguments.of("b = TT.a.c + f.s",
                        new VariableAssigment(
                                new AddResult(new StructCall("TT", List.of("a", "c"), new Position()),
                                        new StructCall("f", List.of("s"), new Position()), new Position()),
                                "b", new Position())),
                Arguments.of("b = g",
                        new VariableAssigment(new VariableCall("g", new Position()), "b", new Position())),
                Arguments.of("if ((a > b) and e) { print(\"a is bigger\") }",
                        new IfConditional(
                                new AndExpression(
                                        new LogicalExpression(
                                                new VariableCall("a", new Position()),
                                                new VariableCall("b", new Position()), TokenType.MORE_OP, new Position()),
                                        new VariableCall("e", new Position()), new Position()),
                                List.of(new FunctionCall("print", List.of(new LiteralValue("a is bigger", TokenType.STRING_VALUE, new Position())), new Position())),
                                List.of(), new Position())),

                Arguments.of("match s {\n" +
                                "                  Shape.cir(value) {\n" +
                                "                   int a\n" +
                                "                   }}",
                        new Match("s",
                                List.of(new MatchCase("Shape", "cir", "value",
                                        List.of(new ConstGlobalVariableDeclaration("a", "int", false, false, new Position())), new Position())), new Position())),
                Arguments.of("Shape cShape = Shape.cir(c)",
                        new VariableInit("cShape", "Shape", new TaggedUnionInit("Shape", List.of("cir"), new VariableCall("c", new Position()), new Position()), false, false, new Position()))

        );
    }

    @ParameterizedTest
    @MethodSource("conditionalBlocks")
    @DisplayName("Test singular creation of single conditional expression")
    void parseConditionalBlocks(String text, Object programElement) throws AnalizerException {
        testSingle(text, programElement);
    }


    private static Stream<Arguments> conditionalBlocks() throws ParserException {
        return Stream.of(
                Arguments.of("if(a) { int b }",
                        new IfConditional(new VariableCall("a", new Position()),
                                List.of(new ConstGlobalVariableDeclaration("b", "int", false, false, new Position())), List.of(), new Position())),
                Arguments.of("if(a > b) { return a }",
                        new IfConditional(new LogicalExpression(
                                new VariableCall("a", new Position()),
                                new VariableCall("b", new Position()),
                                TokenType.MORE_OP,
                                new Position()),
                                List.of(new ReturnBlock(new VariableCall("a", new Position()), new Position())), List.of(), new Position())),
                Arguments.of("if( a < b ) { return a }",
                        new IfConditional(new LogicalExpression(
                                new VariableCall("a", new Position()),
                                new VariableCall("b", new Position()),
                                TokenType.LESS_OP,
                                new Position()),
                                List.of(new ReturnBlock(new VariableCall("a", new Position()), new Position())), List.of(), new Position())),
                Arguments.of("if(a >= b) { return a }",
                        new IfConditional(new LogicalExpression(
                                new VariableCall("a", new Position()),
                                new VariableCall("b", new Position()),
                                TokenType.MORE_EQ_OP,
                                new Position()),
                                List.of(new ReturnBlock(new VariableCall("a", new Position()), new Position())), List.of(), new Position())),
                Arguments.of("if(a <= b) { return a }",
                        new IfConditional(new LogicalExpression(
                                new VariableCall("a", new Position()),
                                new VariableCall("b", new Position()),
                                TokenType.LESS_EQ_OP,
                                new Position()),
                                List.of(new ReturnBlock(new VariableCall("a", new Position()), new Position())), List.of(), new Position())),
                Arguments.of("if(a == b) { return a }",
                        new IfConditional(new LogicalExpression(
                                new VariableCall("a", new Position()),
                                new VariableCall("b", new Position()),
                                TokenType.EQ_OP,
                                new Position()),
                                List.of(new ReturnBlock(new VariableCall("a", new Position()), new Position())), List.of(), new Position())),
                Arguments.of("if(a or b) { return a }",
                        new IfConditional(new OrExpression(
                                new VariableCall("a", new Position()),
                                new VariableCall("b", new Position()),
                                new Position()),
                                List.of(new ReturnBlock(new VariableCall("a", new Position()), new Position())), List.of(), new Position())),
                Arguments.of("if(a and b) { return a }",
                        new IfConditional(new AndExpression(
                                new VariableCall("a", new Position()),
                                new VariableCall("b", new Position()),
                                new Position()),
                                List.of(new ReturnBlock(new VariableCall("a", new Position()), new Position())), List.of(), new Position())),
                Arguments.of("if(a and not b) { return a }",
                        new IfConditional(new AndExpression(
                                new VariableCall("a", new Position()),
                                new NegatedExpression (new VariableCall("b", new Position())),
                                new Position()),
                                List.of(new ReturnBlock(new VariableCall("a", new Position()), new Position())), List.of(), new Position())),
                Arguments.of("if(a and not b) { return a }",
                        new IfConditional(new AndExpression(
                                new VariableCall("a", new Position()),
                                new NegatedExpression (new VariableCall("b", new Position())),
                                new Position()),
                                List.of(new ReturnBlock(new VariableCall("a", new Position()), new Position())), List.of(), new Position())),
                Arguments.of("if(not (a and b)) { return a }",
                        new IfConditional(new NegatedExpression (new AndExpression(
                                new VariableCall("a", new Position()),
                                new VariableCall("b", new Position()),
                                new Position())),
                                List.of(new ReturnBlock(new VariableCall("a", new Position()), new Position())), List.of(), new Position())),
                Arguments.of("if(a or b and c) { return a }",
                        new IfConditional(new OrExpression(
                                new VariableCall("a", new Position()),
                                new AndExpression(
                                        new VariableCall("b", new Position()),
                                        new VariableCall("c", new Position()),
                                        new Position()),
                                new Position()),
                                List.of(new ReturnBlock(new VariableCall("a", new Position()), new Position())), List.of(), new Position())),
                Arguments.of("if((a or b) and c) { return a }",
                        new IfConditional(new AndExpression(
                                new OrExpression(
                                        new VariableCall("a", new Position()),
                                        new VariableCall("b", new Position()),
                                        new Position()),
                                new VariableCall("c", new Position()),
                                new Position()),
                                List.of(new ReturnBlock(new VariableCall("a", new Position()), new Position())), List.of(), new Position())),
                Arguments.of("while(true) { return a }",
                        new WhileConditional(
                                
                                        new LiteralValue(true, TokenType.TRUE_KEYWORD, new Position()),
                                List.of(new ReturnBlock(new VariableCall("a", new Position()), new Position())),
                                new Position())),
                Arguments.of("if(true) { return a } " +
                                "elif(false) { return b } " +
                                "else { return c }",
                        new IfConditional(
                                
                                        new LiteralValue(true, TokenType.TRUE_KEYWORD, new Position()),
                                List.of(new ReturnBlock(new VariableCall("a", new Position()), new Position())),
                                List.of(new ElIfConditional(
                                        
                                                new LiteralValue(false, TokenType.FALSE_KEYWORD, new Position()),
                                        List.of(new ReturnBlock(new VariableCall("b", new Position()), new Position())),
                                        new Position()),
                                        new ElseConditional(
                                                List.of(new ReturnBlock(new VariableCall("b", new Position()), new Position())),
                                                new Position())),

                                new Position()))
        );
    }

    @ParameterizedTest
    @MethodSource("mathBlocks")
    @DisplayName("Test singular creation of single conditional expression")
    void parseMatchBlocks(String text, Object programElement) throws AnalizerException {
        testSingle(text, programElement);
    }


    private static Stream<Arguments> mathBlocks() throws ParserException {
        return Stream.of(
                Arguments.of("a = a + b",
                        new VariableAssigment(new AddResult(
                                new VariableCall("a", new Position()),
                                new VariableCall("b", new Position()),
                                new Position()), "a", new Position())),
                Arguments.of("a = a - b",
                        new VariableAssigment(new SubtractResult(
                                new VariableCall("a", new Position()),
                                new VariableCall("b", new Position()),
                                new Position()), "a", new Position())),
                Arguments.of("a = a * 2",
                        new VariableAssigment(new MultiplyResult(
                                new VariableCall("a", new Position()),
                                new LiteralValue(2, TokenType.INT_NUMBER, new Position()),
                                new Position()), "a", new Position())),
                Arguments.of("a = 3 / 2.5",
                        new VariableAssigment(new DivideResult(
                                new LiteralValue(3, TokenType.INT_NUMBER, new Position()),
                                new LiteralValue(2.5f, TokenType.FLT_NUMBER, new Position()),
                                new Position()), "a", new Position())),
                Arguments.of("a = curNum() % 2.5",
                        new VariableAssigment(new ModuloResult(
                                new FunctionCall("curNum", List.of(), new Position()),
                                new LiteralValue(2.5f, TokenType.FLT_NUMBER, new Position()),
                                new Position()), "a", new Position())),
                Arguments.of("a = curNum() % 2.5 + 2",
                        new VariableAssigment(new AddResult(
                                new ModuloResult(
                                    new FunctionCall("curNum", List.of(), new Position()),
                                    new LiteralValue(2.5f, TokenType.FLT_NUMBER, new Position()),
                                    new Position()),
                                new LiteralValue(2, TokenType.INT_NUMBER, new Position()), new Position()),
                                "a", new Position())),
                Arguments.of("a = 2 + 2 * 2",
                        new VariableAssigment(new AddResult(
                                new LiteralValue(2, TokenType.INT_NUMBER, new Position()),
                                new MultiplyResult(
                                        new LiteralValue(2, TokenType.INT_NUMBER, new Position()),
                                        new LiteralValue(2, TokenType.INT_NUMBER, new Position()),
                                        new Position()),
                                new Position()),
                                "a", new Position())),
                Arguments.of("a = (1 + 2**3) * 4",
                        new VariableAssigment(new MultiplyResult(
                                new AddResult(
                                        new LiteralValue(1, TokenType.INT_NUMBER, new Position()),
                                        new PowResult(
                                                new LiteralValue(2, TokenType.INT_NUMBER, new Position()),
                                                new LiteralValue(3, TokenType.INT_NUMBER, new Position()),
                                                new Position()),
                                        new Position()),
                                new LiteralValue(4, TokenType.INT_NUMBER, new Position()),
                                new Position()),
                                "a", new Position())),
                Arguments.of("a = ((1 + 2)**3) * 4",
                        new VariableAssigment(new MultiplyResult(
                                new PowResult(
                                        new AddResult(
                                                new LiteralValue(1, TokenType.INT_NUMBER, new Position()),
                                                new LiteralValue(2, TokenType.INT_NUMBER, new Position()),
                                                new Position()
                                        ),
                                        new LiteralValue(3, TokenType.INT_NUMBER, new Position()),
                                        new Position()),
                                new LiteralValue(4, TokenType.INT_NUMBER, new Position()),
                                new Position()),
                                "a", new Position()))

        );
    }

    private static void testSingle(String text, Object programElement) throws AnalizerException {
        Parser parser = new Parser(new TokenBuilder(new StringSource(text)));
        Program program = parser.parseProgram();
        assertEquals(programElement, program.getStatements().get(0));
    }

    @Test
    void parseHuge() throws AnalizerException {
        String code = "" +
                "struct Point {\n" +
                "\tflt x,\n" +
                "\tflt y\n" +
                "}\n" +
                "\n" +
                "struct Rectangle {\n" +
                "\tPoint a,\n" +
                "\tPoint b,\n" +
                "\tconst str color\n" +
                "}\n" +
                "\n" +
                "struct Circle {\n" +
                "\tPoint center,\n" +
                "\tflt radius\n" +
                "}\n" +
                "\n" +
                "TaggedUnion Shape {\n" +
                "\tCircle cir,\n" +
                "\tRectangle rec\n" +
                "}\n" +
                "\n" +
                "fun circleArea(&Circle c) -> flt {\n" +
                "\treturn (3.14 * c.radius**2)\n" +
                "}\n" +
                "\n" +
                "fun rectangleArea(&Rectangle r) -> flt {\n" +
                "\treturn ((r.a.x - r.b.x) * (r.a.y - r.b.y))\n" +
                "}\n" +
                "\n" +
                "fun shapeArea(&Shape s) -> flt {\n" +
                "\tmatch s {\n" +
                "\t\tShape.cir(value) {\n" +
                "\t\t\treturn circleArea(value)\n" +
                "\t\t}\n" +
                "\t\tShape.rec(value) {\n" +
                "\t\t\treturn rectangleArea(value)\n" +
                "\t\t}}\n" +
                "\t}\n" +
                "\n\n" +
                "fun runFun() -> void {\n" +
                "\tCircle c = Circle(Point(1.2, 2.1), 5.4)\n" +
                "\tRectangle r = Rectangle(Point(1.1, 1.0), Point(2.1, 1.2))\n" +
                "\tShape cShape = Shape.cir(c)\n" +
                "\tShape rShape = Shape.rec(r)\n" +
                "\t\n" +
                "\tflt cArea = shapeArea(&cShape)\n" +
                "\tflt rArea = shapeArea(&rShape)\n" +
                "\tif (cArea > rArea) {\n" +
                "\t\tprint(\"Circle won\")\n" +
                "\t} elif (cArea < rArea) {\n" +
                "\t\tprint(\"rec won\")\n" +
                "\t} else {\n" +
                "\t\tprint(\"draw\")\n" +
                "\t}\n" +
                "}\n" +
                "\n" +
                "runFun()";

        Parser parser = new Parser(new TokenBuilder(new StringSource(code)));
        Program program = parser.parseProgram();

        List<Statement> statements = List.of(
                new Struct("Point",
                        List.of(new ConstVariableDeclaration("x", "flt", false, new Position()),
                                new ConstVariableDeclaration("y", "flt", false, new Position())),
                        new Position()),
                new Struct("Rectangle",
                        List.of(new ConstVariableDeclaration("a", "Point", false, new Position()),
                                new ConstVariableDeclaration("b", "Point", false, new Position()),
                                new ConstVariableDeclaration("color", "str", true, new Position())),
                        new Position()),
                new Struct("Circle",
                        List.of(new ConstVariableDeclaration("center", "Point", false, new Position()),
                                new ConstVariableDeclaration("radius", "flt", false, new Position())),
                        new Position()),
                new TaggedUnion("Shape",
                        List.of(new ConstVariableDeclaration("cir", "Circle", false, new Position()),
                                new ConstVariableDeclaration("rec", "Rectangle", false, new Position())),
                        new Position()),
                new FunctionDeclaration("circleArea",
                        List.of(new VariableDeclaration("c", "Circle", new Position(), true)),
                        List.of(new ReturnBlock(new MultiplyResult(
                                new LiteralValue(3.14f, TokenType.FLT_NUMBER, new Position()),
                                new PowResult(
                                        new StructCall("c", List.of("radius"), new Position()),
                                        new LiteralValue(2, TokenType.INT_NUMBER, new Position()),
                                        new Position()),
                                new Position()), new Position())),
                        "flt", new Position()),
                new FunctionDeclaration("rectangleArea",
                        List.of(new VariableDeclaration("r", "Rectangle", new Position(), true)),
                        List.of(new ReturnBlock(new MultiplyResult(
                                new SubtractResult(
                                        new StructCall("r", List.of("a", "x"), new Position()),
                                        new StructCall("r", List.of("b", "x"), new Position()),
                                        new Position()),
                                new SubtractResult(
                                        new StructCall("r", List.of("a", "y"), new Position()),
                                        new StructCall("r", List.of("b", "y"), new Position()),
                                        new Position()),
                                new Position()), new Position())),
                        "flt", new Position()),
                new FunctionDeclaration("shapeArea",
                        List.of(new VariableDeclaration("s", "Shape", new Position(), true)),
                        List.of(new Match("s",
                                List.of(new MatchCase("Shape", "cir", "value",
                                                List.of(new ReturnBlock(new FunctionCall("circleArea",
                                                        List.of(new VariableCall("value", new Position())), new Position()), new Position())), new Position()),
                                        new MatchCase("Shape", "rec", "value",
                                                List.of(new ReturnBlock(new FunctionCall("rectangleArea",
                                                        List.of(new VariableCall("value", new Position())), new Position()), new Position())), new Position())),
                                new Position())), "flt", new Position()),
                new FunctionDeclaration("runFun",
                        List.of(),
                        List.of(new VariableInit("c", "Circle",
                                        new FunctionCall("Circle",
                                                List.of(new FunctionCall("Point",
                                                                List.of(new LiteralValue(1.2f, TokenType.FLT_NUMBER, new Position()),
                                                                        new LiteralValue(2.1f, TokenType.FLT_NUMBER, new Position())),
                                                                new Position()),
                                                        new LiteralValue(5.4f, TokenType.FLT_NUMBER, new Position())), new Position()),
                                        false, false, new Position()),
                                new VariableInit("r", "Rectangle",
                                        new FunctionCall("Rectangle",
                                                List.of(new FunctionCall("Point",
                                                                List.of(new LiteralValue(1.1f, TokenType.FLT_NUMBER, new Position()),
                                                                        new LiteralValue(1.0f, TokenType.FLT_NUMBER, new Position())),
                                                                new Position()),
                                                        new FunctionCall("Point",
                                                                List.of(new LiteralValue(2.1f, TokenType.FLT_NUMBER, new Position()),
                                                                        new LiteralValue(1.2f, TokenType.FLT_NUMBER, new Position())),
                                                                new Position())), new Position()),
                                        false, false, new Position()),
                                new VariableInit("cShape", "Shape",
                                        new TaggedUnionInit("Shape",
                                                List.of("cir"),
                                                new VariableCall("c", new Position()),
                                                new Position()), false, false, new Position()),
                                new VariableInit("rShape", "Shape",
                                        new TaggedUnionInit("Shape",
                                                List.of("rec"),
                                                new VariableCall("r", new Position()),
                                                new Position()), false, false, new Position()),
                                new VariableInit("cArea", "flt",
                                        new FunctionCall("shapeArea",
                                                List.of(new VariableCall("cShape", new Position(), true)),
                                                new Position()), false, false, new Position()),
                                new VariableInit("rArea", "flt",
                                        new FunctionCall("shapeArea",
                                                List.of(new VariableCall("rShape", new Position(), true)),
                                                new Position()), false, false, new Position()),
                                new IfConditional(new LogicalExpression(
                                        new VariableCall("cArea", new Position()),
                                        new VariableCall("rArea", new Position()),
                                        TokenType.MORE_OP,
                                        new Position()),
                                        List.of(
                                                new FunctionCall("print",
                                                        List.of(new LiteralValue("Circle won", TokenType.STRING_VALUE, new Position())),
                                                        new Position())),
                                        List.of(
                                                new ElIfConditional(new LogicalExpression(
                                                        new VariableCall("cArea", new Position()),
                                                        new VariableCall("rArea", new Position()),
                                                        TokenType.LESS_OP,
                                                        new Position()),
                                                        List.of(
                                                                new FunctionCall("print",
                                                                        List.of(new LiteralValue("rec won", TokenType.STRING_VALUE, new Position())),
                                                                        new Position())),
                                                        new Position()),
                                                new ElseConditional(
                                                        List.of(
                                                                new FunctionCall("print",
                                                                        List.of(new LiteralValue("draw", TokenType.STRING_VALUE, new Position())),
                                                                        new Position())),
                                                        new Position())),
                                        new Position())),
                        "void", new Position()),
                new FunctionCall("runFun", List.of(), new Position())

        );
        assertEquals(statements, program.getStatements());
    }


    @ParameterizedTest
    @MethodSource("errorsTest")
    @DisplayName("test if classes throw errors")
    void testErrorThrowing(Class<ParserException> ex, String msg, String text) throws LekserException {
        Parser parser = new Parser(new TokenBuilder(new StringSource(text)));

        ParserException exception = assertThrows(ex, () -> {
            Program program = parser.parseProgram();
        });

        String actualMessage = exception.getMessage();

        assertEquals(msg, actualMessage);
    }

    private static Stream<Arguments> errorsTest() {
        return Stream.of(
                Arguments.of(
                        ParserException.class,
                        "Parser finished because of exception at: line: 1, character: 0" +
                                "\tEmpty program or no correct statement found.",
                        ""),
                Arguments.of(
                        ParserException.class,
                        "Parser finished because of exception at: line: 1, character: 1" +
                                "\tMissing value after increment operation",
                        "a+="),
                Arguments.of(
                        ParserException.class,
                        "Parser finished because of exception at: line: 1, character: 7" +
                                "\tNo closing bracket in condition",
                        "if (a not b) { return a }"),
                Arguments.of(
                        ParserException.class,
                        "Parser finished because of exception at: line: 1, character: 1" +
                                "\tNo identifier when creating struct",
                        "struct {int a}")
                ,
                Arguments.of(
                        ParserException.class,
                        "Parser finished because of exception at: line: 1, character: 1" +
                                "\tNo '{' after struct name",
                        "struct a int a int b}")
                ,
                Arguments.of(
                        ParserException.class,
                        "Parser finished because of exception at: line: 1, character: 1" +
                                "\tNo variables in struct a",
                        "struct a {}")
                ,
                Arguments.of(
                        ParserException.class,
                        "Parser finished because of exception at: line: 1, character: 7" +
                                "\tNo right side of arithmetic expression",
                        "a = 5 +")
                ,
                Arguments.of(
                        ParserException.class,
                        "Parser finished because of exception at: line: 1, character: 7" +
                                "\tNo right side of arithmetic expression",
                        "a = 5 *")
                ,
                Arguments.of(
                        ParserException.class,
                        "Parser finished because of exception at: line: 1, character: 6" +
                                "\tNo right side of arithmetic expression",
                        "a = 5**")
                ,
                Arguments.of(
                        ParserException.class,
                        "Parser finished because of exception at: line: 1, character: 7" +
                                "\tUnknown token",
                        "a = 5 ^ 6")
                ,
                Arguments.of(
                        ParserException.class,
                        "Parser finished because of exception at: line: 1, character: 1" +
                                "\tNo valid block after a",
                        "a " +
                        "if (a) { return a }")
                ,
                Arguments.of(
                        ParserException.class,
                        "Parser finished because of exception at: line: 1, character: 4" +
                                "\tMissing value in variable assigment",
                        "a =" +
                                "if (a) { return a }")
                ,
                Arguments.of(
                        ParserException.class,
                        "Parser finished because of exception at: line: 1, character: 10" +
                                "\tMissing match case in match expression",
                        "match a {}")
                ,
                Arguments.of(
                        ParserException.class,
                        "Parser finished because of exception at: line: 1, character: 1" +
                                "\tNo name in match expresion",
                        "match  {}")
                ,
                Arguments.of(
                        ParserException.class,
                        "Parser finished because of exception at: line: 1, character: 1" +
                                "\tMissing '{' in match expression",
                        "match a }")
                ,
                Arguments.of(
                        ParserException.class,
                        "Parser finished because of exception at: line: 1, character: 10" +
                                "\tMissing match case in match expression",
                        "match a {}")
                ,
                Arguments.of(
                        ParserException.class,
                        "Parser finished because of exception at: line: 1, character: 38" +
                                "\tMissing '}' in match expression",
                        "match a { Shape.cir(value) { return a}")
                ,
                Arguments.of(
                        ParserException.class,
                        "Parser finished because of exception at: line: 1, character: 11" +
                                "\tNo variable declaration after 'const' keyword",
                        "struct a {const }")
                ,
                Arguments.of(
                        ParserException.class,
                        "Parser finished because of exception at: line: 1, character: 7" +
                                "\tNo right side of or condition",
                        "if (a or) { return a }")
                ,
                Arguments.of(
                        ParserException.class,
                        "Parser finished because of exception at: line: 1, character: 7" +
                                "\tNo right side of or condition",
                        "if (a and) { return a }")
                ,
                Arguments.of(
                        ParserException.class,
                        "Parser finished because of exception at: line: 1, character: 1" +
                                "\tUnknown sequence of tokens.",
                        "else (a) { return a }")
                ,
                Arguments.of(
                        ParserException.class,
                        "Parser finished because of exception at: line: 1, character: 12" +
                                "\tMissing value in function call",
                        "funkcjaa(a,)")
                ,
                Arguments.of(
                        ParserException.class,
                        "Parser finished because of exception at: line: 1, character: 11" +
                                "\tCannot negate and make negative at the same time",
                        "a = - not 5")
                ,
                Arguments.of(
                        ParserException.class,
                        "Parser finished because of exception at: line: 1, character: 12" +
                                "\tCannot negate and make negative at the same time",
                        "b = not -  5")
        );
    }

    @ParameterizedTest
    @MethodSource("negativeValues")
    @DisplayName("Test negative values")
    void testNegativeValues(String txt, VariableAssigment programElement) throws AnalizerException {
        Parser parser = new Parser(new TokenBuilder(new StringSource(txt)));

        Program program = parser.parseProgram();
        assertEquals(programElement, program.getStatements().get(0));
    }

    public static Stream<Arguments> negativeValues() {
        return Stream.of(
                Arguments.of("a = -1", new VariableAssigment(new NegativeExpression (new LiteralValue(1, TokenType.INT_NUMBER, new Position())), "a", new Position())),
                Arguments.of("a = -b", new VariableAssigment(new NegativeExpression (new VariableCall("b", new Position())), "a", new Position()))
        );
    }

    @Test
    void otherMinusTest() throws AnalizerException {
        Parser parser = new Parser(new TokenBuilder(new StringSource("a = 1 - -b")));

        VariableAssigment programElement = new VariableAssigment(
                new SubtractResult(new LiteralValue(1, TokenType.INT_NUMBER, new Position()), new NegativeExpression (new VariableCall("b", new Position())), new Position() ),
                "a", new Position());

        Program program = parser.parseProgram();
        assertEquals(programElement, program.getStatements().get(0));
    }

}