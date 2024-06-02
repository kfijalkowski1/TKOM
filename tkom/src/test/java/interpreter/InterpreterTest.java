package interpreter;


import exceptions.AnalizerException;
import inputHandle.Position;

import inputHandle.Source;
import inputHandle.StringSource;
import interpreter.dynamic.DynamicType;
import interpreter.dynamic.TaggedUnionType;
import interpreter.embedded.Value;
import interpreter.embedded.ValueType;
import interpreter.exceptions.InterperterException;

import lekser.TokenBuilder;
import lekser.TokenType;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import parser.Parser;
import parser.parsableObjects.Program;
import parser.parsableObjects.expression.LiteralValue;
import parser.parsableObjects.expression.arithmatic.results.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;



class InterpreterTest {

    @ParameterizedTest
    @MethodSource("provideArithmeticResultForTesting")
    public void testArithmeticResul(ArithmeticResult arithmeticResult, Value expectedValue, Class<Throwable> expectedException) throws InterperterException {
        Interpreter interpreter = new Interpreter();
        if (expectedException != null) {
            Assertions.assertThrows(expectedException, () -> arithmeticResult.accept(interpreter));
        } else {
            arithmeticResult.accept(interpreter);
            Value result = interpreter.lastValue.getSingleLastValue(new Position());
            Assertions.assertEquals(expectedValue, result);
        }
    }

    private static Stream<Arguments> provideArithmeticResultForTesting() {
        return Stream.of(
                Arguments.of(
                        new DivideResult(
                                new LiteralValue(4, TokenType.INT_NUMBER, new Position()),
                                new LiteralValue(2, TokenType.INT_NUMBER, new Position()),
                                new Position()
                        ),
                        new Value(2, "int", false, false),
                        null
                ),
                Arguments.of(
                        new DivideResult(
                                new LiteralValue(4.0f, TokenType.FLT_NUMBER, new Position()),
                                new LiteralValue(2.0f, TokenType.FLT_NUMBER, new Position()),
                                new Position()
                        ),
                        new Value(2.0f, "flt", false, false),
                        null
                ),
                Arguments.of(
                        new DivideResult(
                                new LiteralValue(4, TokenType.INT_NUMBER, new Position()),
                                new LiteralValue(0, TokenType.INT_NUMBER, new Position()),
                                new Position()
                        ),
                        null,
                        InterperterException.class
                ),
                Arguments.of(
                        new DivideResult(
                                new LiteralValue(4.0f, TokenType.FLT_NUMBER, new Position()),
                                new LiteralValue(0.0f, TokenType.FLT_NUMBER, new Position()),
                                new Position()
                        ),
                        null,
                        InterperterException.class
                ),
                Arguments.of(
                        new DivideResult(
                                new LiteralValue(4, TokenType.INT_NUMBER, new Position()),
                                new LiteralValue(2.0f, TokenType.FLT_NUMBER, new Position()),
                                new Position()
                        ),
                        null,
                        InterperterException.class
                )
                ,
                Arguments.of(
                        new DivideResult(
                                new DivideResult(
                                        new LiteralValue(4.0f, TokenType.FLT_NUMBER, new Position()),
                                        new LiteralValue(4.0f, TokenType.FLT_NUMBER, new Position()), new Position()),
                                new LiteralValue(2.0f, TokenType.FLT_NUMBER, new Position()),
                                new Position()
                        ),
                        new Value(0.5f, "flt", false, false),
                        null
                ),
                Arguments.of(
                        new DivideResult(
                                new LiteralValue(5, TokenType.INT_NUMBER, new Position()),
                                new LiteralValue(2, TokenType.INT_NUMBER, new Position()),
                                new Position()
                        ),
                        new Value(2, "int", false, false),
                        null
                ),
                Arguments.of(
                        new DivideResult(
                                new LiteralValue(4, TokenType.INT_NUMBER, new Position()),
                                new LiteralValue(3, TokenType.INT_NUMBER, new Position()),
                                new Position()
                        ),
                        new Value(1, "int", false, false),
                        null
                ),
                Arguments.of(
                        new DivideResult(
                                new LiteralValue(10, TokenType.INT_NUMBER, new Position()),
                                new DivideResult(
                                        new LiteralValue(4, TokenType.INT_NUMBER, new Position()),
                                        new DivideResult(
                                                new LiteralValue(12, TokenType.INT_NUMBER, new Position()),
                                                new LiteralValue(6, TokenType.INT_NUMBER, new Position()),
                                                new Position()
                                        ),
                                        new Position()
                                ),
                                new Position()
                        ),
                        new Value(5, "int", false, false),
                        null
                ),
                Arguments.of(
                        new ModuloResult(
                                new LiteralValue(10, TokenType.INT_NUMBER, new Position()),
                                new LiteralValue(3, TokenType.INT_NUMBER, new Position()),
                                new Position()
                        ),
                        new Value(1, "int", false, false),
                        null
                ),
                Arguments.of(
                        new ModuloResult(
                                new LiteralValue(10, TokenType.INT_NUMBER, new Position()),
                                new LiteralValue(0, TokenType.INT_NUMBER, new Position()),
                                new Position()
                        ),
                        null,
                        InterperterException.class
                ),
                Arguments.of(
                        new ModuloResult(
                                new LiteralValue(10, TokenType.INT_NUMBER, new Position()),
                                new LiteralValue(2.0f, TokenType.FLT_NUMBER, new Position()),
                                new Position()
                        ),
                        null,
                        InterperterException.class
                ),
                Arguments.of(
                        new MultiplyResult(
                                new LiteralValue(4, TokenType.INT_NUMBER, new Position()),
                                new LiteralValue(2, TokenType.INT_NUMBER, new Position()),
                                new Position()
                        ),
                        new Value(8, "int", false, false),
                        null
                ),
                Arguments.of(
                        new MultiplyResult(
                                new LiteralValue(4.0f, TokenType.FLT_NUMBER, new Position()),
                                new LiteralValue(2.0f, TokenType.FLT_NUMBER, new Position()),
                                new Position()
                        ),
                        new Value(8.0f, "flt", false, false),
                        null
                ),
                Arguments.of(
                        new MultiplyResult(
                                new LiteralValue("test", TokenType.STRING_VALUE, new Position()),
                                new LiteralValue(2, TokenType.INT_NUMBER, new Position()),
                                new Position()
                        ),
                        new Value("testtest", "str", false, false),
                        null
                ),
                Arguments.of(
                        new MultiplyResult(
                                new LiteralValue(4, TokenType.INT_NUMBER, new Position()),
                                new LiteralValue(2.0f, TokenType.FLT_NUMBER, new Position()),
                                new Position()
                        ),
                        null,
                        InterperterException.class
                ),
                Arguments.of(
                        new PowResult(
                                new LiteralValue(2, TokenType.INT_NUMBER, new Position()),
                                new LiteralValue(3, TokenType.INT_NUMBER, new Position()),
                                new Position()
                        ),
                        new Value(8, "int", false, false),
                        null
                ),
                Arguments.of(
                        new PowResult(
                                new LiteralValue(2.0f, TokenType.FLT_NUMBER, new Position()),
                                new LiteralValue(3, TokenType.INT_NUMBER, new Position()),
                                new Position()
                        ),
                        new Value(8.0f, "flt", false, false),
                        null
                ),
                Arguments.of(
                        new PowResult(
                                new LiteralValue(2, TokenType.INT_NUMBER, new Position()),
                                new LiteralValue(3.0f, TokenType.FLT_NUMBER, new Position()),
                                new Position()
                        ),
                        null,
                        InterperterException.class
                ),
                Arguments.of(
                        new AddResult(
                                new LiteralValue(5, TokenType.INT_NUMBER, new Position()),
                                new LiteralValue(2, TokenType.INT_NUMBER, new Position()),
                                new Position()
                        ),
                        new Value(7, "int", false, false),
                        null
                ),
                Arguments.of(
                        new AddResult(
                                new LiteralValue(4.0f, TokenType.FLT_NUMBER, new Position()),
                                new LiteralValue(3.0f, TokenType.FLT_NUMBER, new Position()),
                                new Position()
                        ),
                        new Value(7.0f, "flt", false, false),
                        null
                ),
                Arguments.of(
                        new AddResult(
                                new LiteralValue(4, TokenType.INT_NUMBER, new Position()),
                                new LiteralValue(2.0f, TokenType.FLT_NUMBER, new Position()),
                                new Position()
                        ),
                        null,
                        InterperterException.class
                ),
                Arguments.of(
                        new AddResult(
                                new LiteralValue("test", TokenType.STRING_VALUE, new Position()),
                                new LiteralValue("test", TokenType.STRING_VALUE, new Position()),
                                new Position()
                        ),
                        new Value("testtest", "str", false, false),
                        null
                ),
                Arguments.of(
                        new SubtractResult(
                                new LiteralValue(5, TokenType.INT_NUMBER, new Position()),
                                new LiteralValue(2, TokenType.INT_NUMBER, new Position()),
                                new Position()
                        ),
                        new Value(3, "int", false, false),
                        null
                ),
                Arguments.of(
                        new SubtractResult(
                                new LiteralValue(4.0f, TokenType.FLT_NUMBER, new Position()),
                                new LiteralValue(3.0f, TokenType.FLT_NUMBER, new Position()),
                                new Position()
                        ),
                        new Value(1.0f, "flt", false, false),
                        null
                ),
                Arguments.of(
                        new SubtractResult(
                                new LiteralValue(4, TokenType.INT_NUMBER, new Position()),
                                new LiteralValue(2.0f, TokenType.FLT_NUMBER, new Position()),
                                new Position()
                        ),
                        null,
                        InterperterException.class
                )


        );
    }


    @ParameterizedTest
    @MethodSource("provideStructsForTesting")
    public void taggedUnionTest(String programText, String typeName, DynamicType addedType, Class<Throwable> expectedException) throws InterperterException, AnalizerException {
        Source src = new StringSource(programText);
        TokenBuilder tokenBuilder = new TokenBuilder(src);
        Parser parser = new Parser(tokenBuilder);
        Program program = parser.parseProgram();
        Interpreter interpreter = new Interpreter();

        if (expectedException != null) {
            Assertions.assertThrows(expectedException, () -> interpreter.interpret(program));
        } else {
            interpreter.interpret(program);
            Assertions.assertEquals(addedType, interpreter.variablesTypes.get(typeName));
        }
    }

    private static Stream<Arguments> provideStructsForTesting() {
        return Stream.of(
                Arguments.of(
                        "TaggedUnion Shape {\n" +
                                "\tint a,\n" +
                                "\tflt b\n" +
                                "}",
                        "Shape",
                        new TaggedUnionType(new LinkedHashMap<String, ValueType>() {{
                            put("a", new ValueType("int", false, false));
                            put("b", new ValueType("flt", false, false));
                        }}),
                        null
                ),
                Arguments.of(
                        "TaggedUnion Shape {\n" +
                                "\tint a,\n" +
                                "\tflt b\n" +
                                "}\n" +
                                "TaggedUnion Add {\n" +
                                "\tint a,\n" +
                                "\tShape b\n" +
                                "}",
                        "Add",
                        new TaggedUnionType(new LinkedHashMap<String, ValueType>() {
                            {
                                put("a", new ValueType("int", false, false));
                                put("b", new ValueType("Shape", false, false));
                            }
                        }),
                        null
                ),
                Arguments.of(
                        "TaggedUnion Shape {\n" +
                                "\tint a,\n" +
                                "\tflt b\n" +
                                "}\n" +
                                "TaggedUnion Shape {\n" +
                                "\tint a,\n" +
                                "\tShape b\n" +
                                "}",
                        "Shape",
                        null,
                        InterperterException.class
                ),
                Arguments.of(
                        "TaggedUnion Add {\n" +
                                "\tint a,\n" +
                                "\tShape b\n" +
                                "}",
                        "Add",
                        null,
                        InterperterException.class
                ),
                Arguments.of(
                        "struct Shape {\n" +
                                "\tconst int a,\n" +
                                "\tflt b\n" +
                                "}",
                        "Shape",
                        new DynamicType(new LinkedHashMap<String, ValueType>() {{
                            put("a", new ValueType("int", false, true));
                            put("b", new ValueType("flt", false, false));
                        }}),
                        null
                ),
                Arguments.of(
                        "struct Shape {\n" +
                                "\tint a,\n" +
                                "\tflt b\n" +
                                "}\n" +
                                "struct Add {\n" +
                                "\tint a,\n" +
                                "\tShape b\n" +
                                "}",
                        "Add",
                        new DynamicType(new LinkedHashMap<String, ValueType>() {{
                            put("a", new ValueType("int", false, false));
                            put("b", new ValueType("Shape", false, false));
                        }}),
                        null
                ),
                Arguments.of(
                        "struct Add {\n" +
                                "\tint a,\n" +
                                "\tAdd b\n" +
                                "}",
                        "Add",
                        null,
                        InterperterException.class
                )


        );
    }

    @ParameterizedTest
    @MethodSource("provideVariableDeclarationForTesting")
    public void variableDeclarationCreatesVariable(String programText, String expectedName, Value expectedValue,
                                                   Class<Throwable> expectedException, String exceptionMessage) throws AnalizerException, InterperterException {

        Source src = new StringSource(programText);
        TokenBuilder tokenBuilder = new TokenBuilder(src);
        Parser parser = new Parser(tokenBuilder);
        Program program = parser.parseProgram();
        Interpreter interpreter = new Interpreter();

        if (expectedException != null) {
            InterperterException exception = (InterperterException) Assertions.assertThrows(expectedException,
                    () -> interpreter.interpret(program));
            Assertions.assertEquals(exceptionMessage, exception.getMessage());
        } else {
            interpreter.interpret(program);
            Value variable = interpreter.getVariable(expectedName);
            Assertions.assertEquals(expectedValue, variable);
        }
    }

    private static Stream<Arguments> provideVariableDeclarationForTesting() {
        return Stream.of(
                Arguments.of(
                        "int x = 5\n",
                        "x", new Value(5, "int", false, false), null, ""
                ),
                Arguments.of(
                        "dsa x = 5\n",
                        "x", null, InterperterException.class, "Interpreter run into error: Type dsa does not exist, in line 1, character 1"
                ),
                Arguments.of(
                        "flt x = 5.0\n",
                        "x", new Value(5.0f, "flt", false, false), null, ""
                ),
                Arguments.of(
                        "str x = \"test\"\n",
                        "x", new Value("test", "str", false, false), null, ""
                ),
                Arguments.of(
                        "bool x = true\n",
                        "x", new Value(true, "bool", false, false), null, ""
                ),
                Arguments.of(
                        "int x",
                        "x", new Value(null, "int", false, false), null, ""
                ),
                Arguments.of(
                        "const int x",
                        "x", new Value(null, "int", false, true), null, ""
                ),
                Arguments.of(
                        "int x = 5\n" +
                                "x = 10",
                        "x", new Value(10, "int", false, false), null, ""
                ),
                Arguments.of(
                        "int x = 5\n" +
                                "int x = 10",
                        "x", null, InterperterException.class, "Interpreter run into error: Variable x already exists, in line 2, character 1"
                ),
                Arguments.of(
                        "int x = 5\n" +
                                "a = 10",
                        "x", null, InterperterException.class, "Interpreter run into error: Variable a does not exist, in line 2, character 1"
                ),
                Arguments.of(
                        "const int x = 5\n" +
                                "x = 10",
                        "x", null, InterperterException.class, "Interpreter run into error: Variable x is const, in line 2, character 1"
                ),
                Arguments.of(
                        "int x = 5\n" +
                                "x = 10.1",
                        "x", null, InterperterException.class, "Interpreter run into error: Incompatible types for assignment, in line 2, character 1"
                ),
                Arguments.of(
                        "struct Point {" +
                                "int a," +
                                "flt b}" +
                                "Point x = Point(1, 2.0)\n",
                        "x", new Value(new LinkedHashMap<String, Value>() {{
                            put("a", new Value(1, "int", false, false));
                            put("b", new Value(2.0f, "flt", false, false));
                        }}
                                , "Point", false, false), null, ""
                ),
                Arguments.of(
                        """
                                struct Point {
                                    int a,
                                    flt b
                                }
                                Point x = Point(1, 2.0, 1)
                                """,
                        "x", null, InterperterException.class, "Interpreter run into error: Incorrect number of values for struct Point, in line 5, character 11"
                ),
                Arguments.of(
                        """
                                struct Point {
                                    int a,
                                    flt b
                                }
                                Point x = Point(1, "2.0")
                                """,
                        "x", null, InterperterException.class, "Interpreter run into error: Incompatible types for struct Point, in line 5, character 11"
                )
                ,
                Arguments.of(
                        """
                                struct Point {
                                    int a,
                                    flt b
                                }
                                Point x = Point(1, 2.0)
                                flt y = x.b
                                """,
                        "y", new Value(2.0f, "flt", false, false), null, ""
                ),
                Arguments.of(
                        """
                                struct Point {
                                    int a,
                                    flt b
                                }
                                struct AA {
                                    Point c,
                                    flt d
                                }
                                Point x = Point(1, 2.0)
                                AA e = AA(x, 3.0)
                                flt y = e.c.b
                                """,
                        "y", new Value(2.0f, "flt", false, false), null, ""
                )
                ,
                Arguments.of(
                        """
                                struct Point {
                                    int a,
                                    flt b
                                }
                                Point x = Point(1, 2.0)
                                flt y = x.b
                                x.b = 3.0
                                flt z = x.b
                                """,
                        "y", new Value(2.0f, "flt", false, false), null, ""
                )
                ,
                Arguments.of(
                        """
                                struct Point {
                                    int a,
                                    flt b
                                }
                                Point x = Point(1, 2.0)
                                flt y = x.b
                                x.b = 3.0
                                flt z = x.b
                                """,
                        "z", new Value(3.0f, "flt", false, false), null, ""
                ),
                Arguments.of(
                        """
                                TaggedUnion Point {
                                    int a,
                                    flt b
                                }
                                Point x = Point.a(1)
                                int a = 0
                                match x {
                                    Point.a(value) {
                                        a = value
                                    }
                                    Point.b(value) {
                                        print(value)
                                    }
                                }
                                
                                """,
                        "a", new Value(1, "int", false, false), null, ""
                ),
                Arguments.of(
                        """
                                TaggedUnion Point {
                                    int a,
                                    flt b
                                }
                                Point x = Point.a(1.0)
                                int a = x
                                """,
                        "a", null, InterperterException.class, "Interpreter run into error: Incompatible types for tagged union Point, in line 5, character 11"
                ),
                Arguments.of(
                        """
                                TaggedUnion Point {
                                    int a,
                                    flt b
                                }
                                Point x = Point.c(1)
                                int a = x
                                """,
                        "a", null, InterperterException.class, "Interpreter run into error: Field c does not exist in tagged union Point, in line 5, character 11"
                ),
                Arguments.of(
                        """
                                TaggedUnion AA {
                                    int a,
                                    flt b
                                }
                                TaggedUnion Point {
                                    int a,
                                    flt b
                                }
                                AA x = Point.a(1)
                                int a = x
                                """,
                        "a", null, InterperterException.class, "Interpreter run into error: Incompatible types for variable x, expected AA, in line 9, character 1"
                )
                ,
                Arguments.of(
                        """
                                int x = 5
                                TaggedUnion Point {
                                    int a,
                                    flt b
                                }
                                Point tg = Point.a(1)
                                match tg {
                                    Point.a(value) {
                                        x = value
                                        print(value)
                                    }
                                    Point.b(value) {
                                        x = value
                                    }
                                }
                                """,
                        "x", new Value(1, "int", false, false), null, ""
                )
                ,
                Arguments.of(
                        """
                                flt x = 5.0
                                TaggedUnion Point {
                                    int a,
                                    flt b
                                }
                                Point tg = Point.b(1.0)
                                match tg {
                                    Point.a(value) {
                                        x = value
                                        print(value)
                                    }
                                    Point.b(value) {
                                        x = value
                                    }
                                }
                                """,
                        "x", new Value(1.0f, "flt", false, false), null, ""
                )
                ,
                Arguments.of(
                        """
                                flt x = 5.0
                                TaggedUnion Point {
                                    int a,
                                    flt b
                                }
                                Point tg = Point.a(0)
                                tg = Point.b(1.0)
                                match tg {
                                    Point.a(value) {
                                        x = value
                                        print(value)
                                    }
                                    Point.b(value) {
                                        x = value
                                    }
                                }
                                """,
                        "x", new Value(1.0f, "flt", false, false), null, ""
                )
                ,
                Arguments.of(
                        """
                                flt x = 5.0
                                TaggedUnion Point {
                                    int a,
                                    flt b
                                }
                                Point tg = Point.b(1.0)
                                match tg {
                                    AAA.a(value) {
                                        x = value
                                        print(value)
                                    }
                                    AAA.b(value) {
                                        x = value
                                    }
                                }
                                """,
                        "x", null, InterperterException.class, "Interpreter run into error: Incompatible types for match, expected Point, in line 7, character 1"
                ),
                Arguments.of(
                        """
                                flt x = 5.0
                                TaggedUnion Point {
                                    int a,
                                    flt b
                                }
                                Point tg = Point.b(1.0)
                                match f {
                                    Point.a(value) {
                                        x = value
                                        print(value)
                                    }
                                    Point.b(value) {
                                        x = value
                                    }
                                }
                                """,
                        "x", null, InterperterException.class, "Interpreter run into error: Variable f does not exist, in line 7, character 1"
                )
                ,
                Arguments.of(
                        """
                                int x = 5
                                x++
                                x++
                                """,
                        "x", new Value(7, "int", false, false), null, ""
                ),
                Arguments.of(
                        """
                                flt x = 5.0
                                x++
                                """,
                        "x", null, InterperterException.class,
                        "Interpreter run into error: Variable x is not an integer so it cannot be incremented, in line 2, character 1"
                ),
                Arguments.of(
                        """
                                const int x = 5
                                x++
                                """,
                        "x", null, InterperterException.class,
                        "Interpreter run into error: Variable x is const so it cannot be incremented, in line 2, character 1"
                ),
                Arguments.of(
                        """
                                const int x = 5
                                x += 3
                                """,
                        "x", null, InterperterException.class,
                        "Interpreter run into error: Variable x is const so it cannot be incremented, in line 2, character 1"
                ),
                Arguments.of(
                        """
                                flt x = 5.0
                                x += 3
                                """,
                        "x", null, InterperterException.class,
                        "Interpreter run into error: Incompatible types for increment, should be the same, in line 2, character 1"
                ),
                Arguments.of(
                        """
                                str x = "a"
                                x += "3"
                                """,
                        "x", null, InterperterException.class,
                        "Interpreter run into error: Post increment value is not an integer or float, in line 2, character 1"
                ),
                Arguments.of(
                        """
                                flt x = 1.0
                                x += 3.0
                                """,
                        "x", new Value(4.0f, "flt", false, false), null, ""
                ),
                Arguments.of(
                        """
                                flt x = 1.0
                                if ( 2 > 1 ) {
                                    x += 3.0
                                }
                                """,
                        "x", new Value(4.0f, "flt", false, false), null, ""
                ),
                Arguments.of(
                        """
                                flt x = 1.0
                                if ( 2 >= 1 ) {
                                    x += 3.0
                                }
                                """,
                        "x", new Value(4.0f, "flt", false, false), null, ""
                ),
                Arguments.of(
                        """
                                flt x = 1.0
                                if ( 2 <= 1 ) {
                                    x += 3.0
                                }
                                """,
                        "x", new Value(1.0f, "flt", false, false), null, ""
                ),
                Arguments.of(
                        """
                                flt x = 1.0
                                if ( 2 < 1 ) {
                                    x += 3.0
                                }
                                """,
                        "x", new Value(1.0f, "flt", false, false), null, ""
                ),
                Arguments.of(
                        """
                                flt x = 1.0
                                if ( 2 == 1 ) {
                                    x += 3.0
                                }
                                """,
                        "x", new Value(1.0f, "flt", false, false), null, ""
                ),
                Arguments.of(
                        """
                                flt x = 1.0
                                if ( 2 != 1 ) {
                                    x += 3.0
                                }
                                """,
                        "x", new Value(4.0f, "flt", false, false), null, ""
                ),
                Arguments.of(
                        """
                                flt x = 1.0
                                if ( true ) {
                                    x += 3.0
                                }
                                """,
                        "x", new Value(4.0f, "flt", false, false), null, ""
                ),
                Arguments.of(
                        """
                                flt x = 1.0
                                if ( false ) {
                                    x += 3.0
                                }
                                """,
                        "x", new Value(1.0f, "flt", false, false), null, ""
                ),
                Arguments.of(
                        """
                                flt x = 1.0
                                if ( false ) {
                                    x += 3.0
                                } else {
                                    x += 30.0
                                }
                                """,
                        "x", new Value(31.0f, "flt", false, false), null, ""
                ),
                Arguments.of(
                        """
                                flt x = 1.0
                                if ( 1 > 2 ) {
                                    x += 30.0
                                } elif ( 1 > 2) {
                                    x += 1.2
                                } else {
                                    x += 3.0
                                }
                                """,
                        "x", new Value(4.0f, "flt", false, false), null, ""
                ),
                Arguments.of(
                        """
                                int x = 0
                                int y = 0
                                while ( x < 4 ) {
                                    x++
                                    y+=2
                                }
                                """,
                        "y", new Value(8, "int", false, false), null, ""
                ),
                Arguments.of(
                        """
                                int x = 0
                                int y = 0
                                while ( x < 4 ) {
                                    x++
                                    y+=2
                                }
                                """,
                        "x", new Value(4, "int", false, false), null, ""
                ),
                Arguments.of(
                        """
                                int x = 0
                                int y = 0
                                while ( false ) {
                                    x++
                                    y+=2
                                }
                                """,
                        "y", new Value(0, "int", false, false), null, ""
                ),
                Arguments.of(
                        """
                                int x = 0
                                int y = 0
                                if ( false and true ) {
                                    x++
                                    y+=2
                                }
                                """,
                        "y", new Value(0, "int", false, false), null, ""
                ),
                Arguments.of(
                        """
                                int x = 0
                                int y = 0
                                if ( false or true ) {
                                    x++
                                    y+=2
                                }
                                """,
                        "y", new Value(2, "int", false, false), null, ""
                ),
                Arguments.of(
                        """
                                int x = 0
                                int y = 0
                                if ( (true or true) and false ) {
                                    x++
                                    y+=2
                                }
                                """,
                        "y", new Value(0, "int", false, false), null, ""
                ),
                Arguments.of(
                        """
                                int x = 3
                                flt y = Flt(x)
                                y += 2.0
                                """,
                        "y", new Value(5.0f, "flt", false, false), null, ""
                ),
                Arguments.of(
                        """
                                bool x = (1 == 1)
                                flt y = Flt(x)
                                y += 2.0
                                """,
                        "y", null, InterperterException.class, "Interpreter run into error: Expected integer in float cast call, in line 2, character 9"
                ),
                Arguments.of(
                        """
                                str x = "(1 == 1)"
                                flt y = Flt(x)
                                y += 2.0
                                """,
                        "y", null, InterperterException.class, "Interpreter run into error: Expected integer in float cast call, in line 2, character 9"
                ),
                Arguments.of(
                        """
                                struct Point {
                                    int a,
                                    flt b
                                }
                                Point x = Point(1, 2.0)
                                flt y = Flt(x)
                                y += 2.0
                                """,
                        "y", null, InterperterException.class, "Interpreter run into error: Expected integer in float cast call, in line 6, character 9"
                ),
                Arguments.of(
                        """
                                flt x = 3.5
                                int y = Int(x)
                                y += 2
                                """,
                        "y", new Value(5, "int", false, false), null, ""
                ),
                Arguments.of(
                        """
                                str x = "3"
                                int y = Int(x)
                                y += 2
                                """,
                        "y", new Value(5, "int", false, false), null, ""
                ),
                Arguments.of(
                        """
                                str x = "3.0"
                                int y = Int(x)
                                y += 2
                                """,
                        "y", null, InterperterException.class, "Interpreter run into error: Expected integer in string to int cast call, in line 2, character 9"
                ),
                Arguments.of(
                        """
                                str x = "ggg"
                                int y = Int(x)
                                y += 2
                                """,
                        "y", null, InterperterException.class, "Interpreter run into error: Expected integer in string to int cast call, in line 2, character 9"
                ),
                Arguments.of(
                        """
                                str x = "ggg"
                                int y = Int(x)
                                y += 2
                                """,
                        "y", null, InterperterException.class, "Interpreter run into error: Expected integer in string to int cast call, in line 2, character 9"
                ),
                Arguments.of(
                        """
                                fun getVal() -> int {
                                    return 5
                                }
                                int y = getVal()
                                """,
                        "y", new Value(5, "int", false, false), null, ""
                ),
                Arguments.of(
                        """
                                int y = 0
                                fun getVal(&int k) -> int {
                                    k = 5
                                }
                                getVal(&y)
                                """,
                        "y", new Value(5, "int", true, false), null, ""
                ),
                Arguments.of(
                        """
                                int y = 0
                                fun getVal(int k) -> int {
                                    k += 5
                                }
                                fun getValRef(&int k) -> int {
                                    k += 5
                                }
                                getValRef(&y)
                                getVal(y)
                                int z  = y
                                """,
                        "z", new Value(5, "int", false, false), null, ""
                ),
                Arguments.of(
                        """
                                int y = 0
                                fun getVal(int k) -> int {
                                    k += 5
                                    return k
                                }
                                fun getValRef(&int k) -> int {
                                    k += 5
                                }
                                getValRef(&y)
                                y = getVal(y)
                                int z  = y
                                """,
                        "z", new Value(10, "int", false, false), null, ""
                ),
                Arguments.of(
                        """
                                int y = 0
                                fun getVal(int k) -> int {
                                    k += 5
                                    return k
                                }
                                fun getValRef(&int k) -> int {
                                    k += 5
                                }
                                getValRef(&y)
                                y = getVal(y)
                                int z  = y
                                """,
                        "z", new Value(10, "int", false, false), null, ""
                ),
                Arguments.of(
                        """
                                fun getVal(int k) -> int {
                                    k += 5
                                    return k
                                }
                                flt y = 0.1
                                y = getVal(y)
                                int z  = y
                                """,
                        "z", null, InterperterException.class, "Interpreter run into error: Incompatible types for parameter k, in line 6, character 5"
                ),
                Arguments.of(
                        """
                                fun getVal(int k) -> int {
                                    k += 5
                                    return k
                                }
                                flt y = 0.1
                                int a = 0
                                y = getVal(y, a)
                                int z  = y
                                """,
                        "z", null, InterperterException.class, "Interpreter run into error: Incorrect number of parameters, in line 7, character 5"
                ),
                Arguments.of(
                        """
                                fun recursionFun(int k) -> void {
                                    k += 5
                                    recursionFun(k)
                                }
                                recursionFun(0)
                                """,
                        "z", null, InterperterException.class, "Interpreter run into error: Max recursion of 500 reached, in line 3, character 5"
                ),
                Arguments.of(
                        """
                                int counter = 0
                                fun recursionFun() -> int {
                                    counter++
                                    if (counter == 499) {
                                        return counter
                                    }
                                    recursionFun()
                                }
                                int a = recursionFun()
                                """,
                        "a", new Value(499, "int", false, false), null, ""
                )


        );
    }

    @ParameterizedTest
    @MethodSource("provideForGeneralTest")
    public void generalTest(String cR, String result) throws InterperterException, AnalizerException {
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStreamCaptor));

        String programText = """
                # Przykładowy kod
                                
                struct Point {
                	flt x,
                	flt y
                }
                                
                struct Rectangle {
                	Point a,
                	Point b,
                	const str color
                }
                                
                struct Circle {
                	Point center,
                	flt radius
                }
                                
                TaggedUnion Shape {
                	Circle cir,
                	Rectangle rec
                }
                                
                fun circleArea(&Circle c) -> flt {
                	return (3.14 * c.radius**2)
                }
                                
                fun rectangleArea(&Rectangle r) -> flt {
                	return ((r.a.x - r.b.x) * (r.a.y - r.b.y))
                }
                                
                fun shapeArea(&Shape s) -> flt {
                	match s {
                		Shape.cir(value) {
                			return circleArea(&value)
                		}
                		Shape.rec(value) {
                			return rectangleArea(&value)
                		}
                	}
                }
                                
                                
                fun runFun() -> void {
                	Circle c = Circle(Point(1.2, 2.1), %s)
                	Rectangle r = Rectangle(Point(1.0, 1.0), Point(4.14, 4.14), "blue")
                	Shape cShape = Shape.cir(c)
                	Shape rShape = Shape.rec(r)
                	
                	flt cArea = shapeArea(&cShape)
                	flt rArea = shapeArea(&rShape)
                	if (cArea > rArea) {
                		print("Circle won")
                	} elif (cArea < rArea) {
                		print("Rec won")
                	} else {
                		print("draw")
                	}
                }
                                
                runFun()
                """.formatted(cR);

        Source src = new StringSource(programText);
        TokenBuilder tokenBuilder = new TokenBuilder(src);
        Parser parser = new Parser(tokenBuilder);
        Program program = parser.parseProgram();
        Interpreter interpreter = new Interpreter();

        interpreter.interpret(program);

        Assertions.assertEquals(result, outputStreamCaptor.toString().trim());

        System.setOut(originalOut);
    }

    private static Stream<Arguments> provideForGeneralTest() {
        return Stream.of(
                Arguments.of(
                        "10.0",
                        "Circle won"
                ),
                Arguments.of(
                        "0.5",
                        "Rec won"
                ));

    }
}