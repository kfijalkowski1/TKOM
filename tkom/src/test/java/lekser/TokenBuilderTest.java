package lekser;

import inputHandle.FileSource;
import inputHandle.Position;
import inputHandle.Source;
import inputHandle.StringSource;
import lekser.exceptions.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


public class TokenBuilderTest {

    @ParameterizedTest
    @MethodSource("singularIntegerSource")
    @DisplayName("Test singular integer values")
    void testIntNumerics(String text, Integer value) throws LekserException, NoValueToken {
        Source src = new StringSource(text);
        TokenBuilder tb = new TokenBuilder(src);
           Token firstToken = tb.getNextToken();
        Assertions.assertEquals(TokenType.INT_NUMBER, firstToken.getTokenType());
        Assertions.assertEquals(value, firstToken.getValue());
        Token endOfToken = tb.getNextToken();
        Assertions.assertEquals(TokenType.END_OF_TEXT, endOfToken.getTokenType());
    }

    private static Stream<Arguments> singularIntegerSource() {
        return Stream.of(
                Arguments.of("1", 1),
                Arguments.of("1 ", 1),
                Arguments.of("\n\n\t855", 855),
                Arguments.of("      0\t\t\t\n", 0),
                Arguments.of("      2147483646\t\t\t\n", 2147483646), // max int -1
                Arguments.of("      2147483647\t\t\t\n", 2147483647) // max int
        );
    }

    @ParameterizedTest
    @MethodSource("singularFloatSource")
    @DisplayName("Test singular float values")
    void testFltNumerics(String text, Float value) throws LekserException, NoValueToken {
        float FLOAT_EPSILON = 0.000001F;
        Source src = new StringSource(text);
        TokenBuilder tb = new TokenBuilder(src);
        Token firstToken = tb.getNextToken();
        float epsilon = value - (float)firstToken.getValue();
        Assertions.assertEquals(TokenType.FLT_NUMBER, firstToken.getTokenType());
        Assertions.assertTrue(Math.abs(epsilon) < FLOAT_EPSILON);
    }

    private static Stream<Arguments> singularFloatSource() {
        return Stream.of(
//                Arguments.of("1.1", 1.1F),
//                Arguments.of("0.1 ", 0.1F),
                Arguments.of("12311.12345 ", 12311.12345F),
                Arguments.of("\n\n\t8.55", 8.55F),
                Arguments.of("      111.0\t\t\t\n", 111.0F),
                Arguments.of("      1.001\t\t\t\n", 1.001F)
        );
    }

    @ParameterizedTest
    @MethodSource("multipleFloatIntSource")
    @DisplayName("Test many different numeric values")
    void testMultipleNumbers(String text, ArrayList<ImmutablePair<TokenType, Object>> values) throws LekserException, NoValueToken {
        TestLongText(text, values);
    }

    private static Stream<Arguments> multipleFloatIntSource() {
        ArrayList<ImmutablePair<TokenType, Object>> firstArray =
                new ArrayList<>(Arrays.asList(
                        new ImmutablePair<>(TokenType.FLT_NUMBER, 0.5F),
                        new ImmutablePair<>(TokenType.FLT_NUMBER, 0.08F),
                        new ImmutablePair<>(TokenType.INT_NUMBER, 199)));
        ArrayList<ImmutablePair<TokenType, Object>> secondArray =
                new ArrayList<>(Arrays.asList(
                        new ImmutablePair<>(TokenType.FLT_NUMBER, 1223.11111F),
                        new ImmutablePair<>(TokenType.INT_NUMBER, 19),
                        new ImmutablePair<>(TokenType.FLT_NUMBER, 88.48F)));

        return Stream.of(
                Arguments.of("0.5\n\t0.08\n199", firstArray),
                Arguments.of("1223.11111\n\t19 88.48", secondArray)
        );
    }

    @ParameterizedTest
    @MethodSource("operatorsSingularAndMultiple")
    @DisplayName("test single and multiple operators")
    void testOperators(String text, List<TokenType> types) throws LekserException {
        testTokenType(text, types);
    }

    private static Stream<Arguments> operatorsSingularAndMultiple() {
        return Stream.of(
                Arguments.of("+", List.of(TokenType.PLUS_OP)),
                Arguments.of("true", List.of(TokenType.TRUE_KEYWORD)),
                Arguments.of("false", List.of(TokenType.FALSE_KEYWORD)),
                Arguments.of("break", List.of(TokenType.BREAK_KEYWORD)),
                Arguments.of("-", List.of(TokenType.MINUS_OP)),
                Arguments.of("*", List.of(TokenType.MULTIPLE_OP)),
                Arguments.of("/", List.of(TokenType.DIVIDE_OP)),
                Arguments.of("%", List.of(TokenType.MODULO_OP)),
                Arguments.of("++", List.of(TokenType.POST_INCREMENT_OP)),
                Arguments.of("+=", List.of(TokenType.INCREMENT_OP)),
                Arguments.of("**", List.of(TokenType.POW_OP)),
                Arguments.of(">", List.of(TokenType.MORE_OP)),
                Arguments.of("<", List.of(TokenType.LESS_OP)),
                Arguments.of(">=", List.of(TokenType.MORE_EQ_OP)),
                Arguments.of("<=", List.of(TokenType.LESS_EQ_OP)),
                Arguments.of("==", List.of(TokenType.EQ_OP)),
                Arguments.of("!=", List.of(TokenType.NOT_EQ_OP)),
                Arguments.of("=", List.of(TokenType.ASSIGN_OP)),
                Arguments.of("->", List.of(TokenType.RETURN_TYPE_OP)),
                Arguments.of("(", List.of(TokenType.OPEN_SOFT_BRACKETS_OP)),
                Arguments.of(")", List.of(TokenType.CLOSE_SOFT_BRACKETS_OP)),
                Arguments.of("{", List.of(TokenType.OPEN_SHARP_BRACKETS_OP)),
                Arguments.of("}", List.of(TokenType.CLOSE_SHARP_BRACKETS_OP)),
                Arguments.of("&", List.of(TokenType.REFERENCE_OP)),
                Arguments.of(".", List.of(TokenType.DOT_OP)),
                Arguments.of(",", List.of(TokenType.COMA_OP)),
                Arguments.of("++ +", List.of(TokenType.POST_INCREMENT_OP, TokenType.PLUS_OP)),
                Arguments.of("+++", List.of(TokenType.POST_INCREMENT_OP, TokenType.PLUS_OP)),
                Arguments.of("** *", List.of(TokenType.POW_OP, TokenType.MULTIPLE_OP)),
                Arguments.of("***", List.of(TokenType.POW_OP, TokenType.MULTIPLE_OP)),
                Arguments.of("**+", List.of(TokenType.POW_OP, TokenType.PLUS_OP)),
                Arguments.of("+=1++", List.of(TokenType.INCREMENT_OP, TokenType.INT_NUMBER, TokenType.POST_INCREMENT_OP)),
                Arguments.of("1+1", List.of(TokenType.INT_NUMBER, TokenType.PLUS_OP, TokenType.INT_NUMBER)),
                Arguments.of("\n  1 +  1\t", List.of(TokenType.INT_NUMBER, TokenType.PLUS_OP, TokenType.INT_NUMBER)),
                Arguments.of("1**+1++", List.of(TokenType.INT_NUMBER, TokenType.POW_OP, TokenType.PLUS_OP, TokenType.INT_NUMBER, TokenType.POST_INCREMENT_OP)),
                Arguments.of("1** + 1++", List.of(TokenType.INT_NUMBER, TokenType.POW_OP, TokenType.PLUS_OP, TokenType.INT_NUMBER, TokenType.POST_INCREMENT_OP)),
                Arguments.of(" ( 1==1 ) ", List.of(TokenType.OPEN_SOFT_BRACKETS_OP, TokenType.INT_NUMBER, TokenType.EQ_OP, TokenType.INT_NUMBER, TokenType.CLOSE_SOFT_BRACKETS_OP)),
                Arguments.of("(1==1)", List.of(TokenType.OPEN_SOFT_BRACKETS_OP, TokenType.INT_NUMBER, TokenType.EQ_OP, TokenType.INT_NUMBER, TokenType.CLOSE_SOFT_BRACKETS_OP)),
                Arguments.of("{(1++<=1)}", List.of(TokenType.OPEN_SHARP_BRACKETS_OP, TokenType.OPEN_SOFT_BRACKETS_OP, TokenType.INT_NUMBER, TokenType.POST_INCREMENT_OP, TokenType.LESS_EQ_OP, TokenType.INT_NUMBER, TokenType.CLOSE_SOFT_BRACKETS_OP, TokenType.CLOSE_SHARP_BRACKETS_OP))
        );
    }

    @ParameterizedTest
    @MethodSource("differentString")
    @DisplayName("test various strings")
    void testString(String text, String value) throws LekserException, NoValueToken {
        Source src = new StringSource(text);
        TokenBuilder tb = new TokenBuilder(src);
        Token firstToken = tb.getNextToken();
        Assertions.assertEquals(TokenType.STRING_VALUE, firstToken.getTokenType());
        Assertions.assertEquals(value, firstToken.getValue());
    }

    private static Stream<Arguments> differentString() {
        return Stream.of(
                Arguments.of("\"ala\"", "ala"),
                Arguments.of("\"  żółto widzęę\"", "  żółto widzęę"),
                Arguments.of("\"aa \\n \\t \"", "aa \n \t "),
                Arguments.of("\"aa \n \t \"", "aa \n \t ")
        );
    }

    @Test
    void testStringFromFile() throws IOException, LekserException, NoValueToken {
        Source src = new FileSource("src/test/java/lekser/test-file.sp");
        TokenBuilder tb = new TokenBuilder(src);
        Token firstToken = tb.getNextToken();
        Token secondToken = tb.getNextToken();
        Token thirdToken = tb.getNextToken();
        Assertions.assertEquals(TokenType.STRING_VALUE, firstToken.getTokenType());
        Assertions.assertEquals("A\n\nbb\tc", firstToken.getValue());
        Assertions.assertEquals("A\\a\n\nbb\tc", secondToken.getValue());
        Assertions.assertEquals("d\n\\\"rr\"\tugi", thirdToken.getValue());
    }

    @ParameterizedTest
    @MethodSource("differentKeywords")
    @DisplayName("test various keywords")
    void testKeywords(String text, TokenType type) throws LekserException {
        Source src = new StringSource(text);
        TokenBuilder tb = new TokenBuilder(src);
        Token firstToken = tb.getNextToken();
        Assertions.assertEquals(type, firstToken.getTokenType());
    }

    private static Stream<Arguments> differentKeywords() {
        String str50 = String.join("", Collections.nCopies(50, "a"));
        return Stream.of(
                Arguments.of("int", TokenType.INT_KEYWORD),
                Arguments.of("flt", TokenType.FLT_KEYWORD),
                Arguments.of("fun", TokenType.FUN_KEYWORD),
                Arguments.of("match", TokenType.MATCH_KEYWORD),
                Arguments.of("str", TokenType.STR_KEYWORD),
                Arguments.of("bool", TokenType.BOOL_KEYWORD),
                Arguments.of("struct", TokenType.STRUCT_KEYWORD),
                Arguments.of("TaggedUnion", TokenType.TaggedUnion_KEYWORD),
                Arguments.of("const", TokenType.CONST_KEYWORD),
                Arguments.of("gscope", TokenType.GSCOPE_KEYWORD),
                Arguments.of("while", TokenType.WHILE_KEYWORD),
                Arguments.of("if", TokenType.IF_KEYWORD),
                Arguments.of("elif", TokenType.ELIF_KEYWORD),
                Arguments.of("and", TokenType.AND_KEYWORD),
                Arguments.of("or", TokenType.OR_KEYWORD),
                Arguments.of("not", TokenType.NOT_KEYWORD),
                Arguments.of("return", TokenType.RETURN_KEYWORD),
                Arguments.of("intint", TokenType.NAME),
                Arguments.of("fltint", TokenType.NAME),
                Arguments.of("if while booleandsa", TokenType.IF_KEYWORD, TokenType.WHILE_KEYWORD, TokenType.NAME),
                Arguments.of("flt int", TokenType.FLT_KEYWORD, TokenType.INT_KEYWORD),
                Arguments.of(str50, TokenType.NAME), // check max length
                Arguments.of("int x = 5", TokenType.INT_KEYWORD, TokenType.NAME, TokenType.EQ_OP, TokenType.INT_KEYWORD),
                Arguments.of("#int x = 5", TokenType.COMMENT),
                Arguments.of("# int x = 5", TokenType.COMMENT),
                Arguments.of("# int x = 5\n# int x = 5", TokenType.COMMENT, TokenType.COMMENT),
                Arguments.of("int x = 5\n# int x = 5", TokenType.INT_KEYWORD, TokenType.NAME, TokenType.EQ_OP, TokenType.INT_NUMBER, TokenType.COMMENT),
                Arguments.of("@", TokenType.UNKNOWN_TOKEN),
                Arguments.of("void", TokenType.VOID_KEYWORD)
        );
    }

    @ParameterizedTest
    @MethodSource("errorsTest")
    @DisplayName("test if classes throw errors")
    void testErrorThrowing(Class<LekserException> ex, String msg, String text) throws LekserException {
        Source src = new StringSource(text);
        TokenBuilder tb = new TokenBuilder(src);
        LekserException exception = assertThrows(ex, () -> {
            tb.getNextToken();
        });

        String actualMessage = exception.getMessage();

        assertEquals(msg, actualMessage);
    }

    private static Stream<Arguments> errorsTest() {
        String str220 = String.join("", Collections.nCopies(220, "a"));
        String str22 = String.join("", Collections.nCopies(22, "a"));
        String str201 = String.join("", Collections.nCopies(201, "a"));
        String str5000 = String.join("", Collections.nCopies(5000, "a"));
        String str60 = String.join("", Collections.nCopies(60, "a"));
        String str51 = String.join("", Collections.nCopies(51, "a"));
        return Stream.of(
                Arguments.of(StringTooLongException.class, "Lekser finished because of exception at: line: 1, character: 1\tString too long", "\"%s\"".formatted(str220)),
                Arguments.of(StringTooLongException.class, "Lekser finished because of exception at: line: 1, character: 1\tString too long", "\"%s\"".formatted(str5000)),
                Arguments.of(StringTooLongException.class, "Lekser finished because of exception at: line: 1, character: 1\tString too long", "\"%s\"".formatted(str201)),
                Arguments.of(NumberOutOfBoundsException.class, "Lekser finished because of exception at: line: 1, character: 1\tNumber too big, max int: 2147483647", "2147483648"),
                Arguments.of(NumberOutOfBoundsException.class, "Lekser finished because of exception at: line: 1, character: 1\tNumber too big, max int: 2147483647", "3147483640"),
                Arguments.of(NumberOutOfBoundsException.class, "Lekser finished because of exception at: line: 1, character: 1\tNumber too big, max int: 2147483647", "2247483645"),
                Arguments.of(NumberOutOfBoundsException.class, "Lekser finished because of exception at: line: 1, character: 1\tNumber too big, max int: 2147483647", "2247483648"),
                Arguments.of(NumberOutOfBoundsException.class, "Lekser finished because of exception at: line: 1, character: 1\tNumber too big, max int: 2147483647", "2247483648.123"),
                Arguments.of(NumberOutOfBoundsException.class, "Lekser finished because of exception at: line: 1, character: 1\tNumber too big, max int: 2147483647", "9992147483647"),
                Arguments.of(NumberOutOfBoundsException.class, "Lekser finished because of exception at: line: 1, character: 1\tNumber too big, max int: 2147483647", "9992147483647.999"),
                Arguments.of(NumberOutOfBoundsException.class, "Lekser finished because of exception at: line: 1, character: 1\tNumber too big, max int: 2147483647", "9992147483647.999"),
                Arguments.of(NumberOutOfBoundsException.class, "Lekser finished because of exception at: line: 1, character: 1\tToo many numbers after . max amount: 5", "1.123456"),
                Arguments.of(NumberOutOfBoundsException.class, "Lekser finished because of exception at: line: 1, character: 1\tToo many numbers after . max amount: 5", "12311.9999912342"),
                Arguments.of(NameTooLongException.class, "Lekser finished because of exception at: line: 1, character: 1\tName too long, max length: 50", "%s".formatted(str60)),
                Arguments.of(NameTooLongException.class, "Lekser finished because of exception at: line: 1, character: 1\tName too long, max length: 50", "%s".formatted(str51)),
                Arguments.of(UnexpectedEndOfFile.class, "Lekser finished because of exception at: line: 1, character: 1\tEnd of file while reading string", "\"%s".formatted(str22))
        );
    }

    @Test
    void testTokenNoValueError() {
        Source src = new StringSource("int");
        TokenBuilder tb = new TokenBuilder(src);
        Exception exception = assertThrows(NoValueToken.class, () -> {
            tb.getNextToken().getValue();
        });

        String actualMessage = exception.getMessage();

        assertEquals("Token has no value", actualMessage);
    }

    @ParameterizedTest
    @MethodSource("incorrectTokens")
    void testTokenIntIncorrectValueError(TokenType type, Object value) {
        Exception exception = assertThrows(IncorrectValueToken.class, () -> {
            new Token(type, new Position(true), value);
        });
    }

    private static Stream<Arguments> incorrectTokens() {
        return Stream.of(
                Arguments.of(TokenType.INT_NUMBER, 1.1),
                Arguments.of(TokenType.INT_NUMBER, "aa"),
                Arguments.of(TokenType.INT_NUMBER, null),
                Arguments.of(TokenType.FLT_NUMBER, 1),
                Arguments.of(TokenType.FLT_NUMBER, "aa"),
                Arguments.of(TokenType.FLT_NUMBER, null),
                Arguments.of(TokenType.STRING_VALUE, 1),
                Arguments.of(TokenType.STRING_VALUE, 1.1),
                Arguments.of(TokenType.STRING_VALUE, null),
                Arguments.of(TokenType.NAME, 1),
                Arguments.of(TokenType.NAME, 1.1),
                Arguments.of(TokenType.NAME, null),
                Arguments.of(TokenType.COMMENT, 1),
                Arguments.of(TokenType.DOT_OP, 1)
        );
    }



    @ParameterizedTest
    @MethodSource("positionTests")
    @DisplayName("test reading of position")
    void testPositions(String text, Integer character, Integer line) throws LekserException {
        Source src = new StringSource(text);
        TokenBuilder tb = new TokenBuilder(src);
        Token firstToken = tb.getNextToken();
        Assertions.assertEquals(character, firstToken.getPossition().getcharacter());
        Assertions.assertEquals(line, firstToken.getPossition().getLine());
    }

    private static Stream<Arguments> positionTests() {
        return Stream.of(
                Arguments.of("int", 1, 1),
                Arguments.of("\n1", 1, 2),
                Arguments.of("\n  1.0", 3, 2),
                Arguments.of("  \nflt", 1, 2),
                Arguments.of("\n\n\n  +", 3, 4)
        );
    }

    @Test
    void testEverything1() throws LekserException, NoValueToken {
        Source src = new StringSource("fun runFun() -> void {\n" +
                " Circle c = Circle(Point(1.2, 2.1), 5.4)\n" +
                " Shape cShape = Shape.cir(c)\n" +
                " flt cArea = shapeArea(&cShape)\n" +
                " if (cArea > rArea) {\n" +
                "   print(\"Circle won\")\n" +
                " }\n" +
                "}");
        ArrayList<ImmutablePair<TokenType, Object>> values = new ArrayList<>(Arrays.asList(
                new ImmutablePair<>(TokenType.FUN_KEYWORD, null),
                new ImmutablePair<>(TokenType.NAME, "runFun"),
                new ImmutablePair<>(TokenType.OPEN_SOFT_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.CLOSE_SOFT_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.RETURN_TYPE_OP, null),
                new ImmutablePair<>(TokenType.VOID_KEYWORD, null),
                new ImmutablePair<>(TokenType.OPEN_SHARP_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.NAME, "Circle"),
                new ImmutablePair<>(TokenType.NAME, "c"),
                new ImmutablePair<>(TokenType.ASSIGN_OP, null),
                new ImmutablePair<>(TokenType.NAME, "Circle"),
                new ImmutablePair<>(TokenType.OPEN_SOFT_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.NAME, "Point"),
                new ImmutablePair<>(TokenType.OPEN_SOFT_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.FLT_NUMBER, 1.2f),
                new ImmutablePair<>(TokenType.COMA_OP, null),
                new ImmutablePair<>(TokenType.FLT_NUMBER, 2.1f),
                new ImmutablePair<>(TokenType.CLOSE_SOFT_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.COMA_OP, null),
                new ImmutablePair<>(TokenType.FLT_NUMBER, 5.4f),
                new ImmutablePair<>(TokenType.CLOSE_SOFT_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.NAME, "Shape"),
                new ImmutablePair<>(TokenType.NAME, "cShape"),
                new ImmutablePair<>(TokenType.ASSIGN_OP, null),
                new ImmutablePair<>(TokenType.NAME, "Shape"),
                new ImmutablePair<>(TokenType.DOT_OP, null),
                new ImmutablePair<>(TokenType.NAME, "cir"),
                new ImmutablePair<>(TokenType.OPEN_SOFT_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.NAME, "c"),
                new ImmutablePair<>(TokenType.CLOSE_SOFT_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.FLT_KEYWORD, null),
                new ImmutablePair<>(TokenType.NAME, "cArea"),
                new ImmutablePair<>(TokenType.ASSIGN_OP, null),
                new ImmutablePair<>(TokenType.NAME, "shapeArea"),
                new ImmutablePair<>(TokenType.OPEN_SOFT_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.REFERENCE_OP, null),
                new ImmutablePair<>(TokenType.NAME, "cShape"),
                new ImmutablePair<>(TokenType.CLOSE_SOFT_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.IF_KEYWORD, null),
                new ImmutablePair<>(TokenType.OPEN_SOFT_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.NAME, "cArea"),
                new ImmutablePair<>(TokenType.MORE_OP, null),
                new ImmutablePair<>(TokenType.NAME, "rArea"),
                new ImmutablePair<>(TokenType.CLOSE_SOFT_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.OPEN_SHARP_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.NAME, "print"),
                new ImmutablePair<>(TokenType.OPEN_SOFT_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.STRING_VALUE, "Circle won"),
                new ImmutablePair<>(TokenType.CLOSE_SOFT_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.CLOSE_SHARP_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.CLOSE_SHARP_BRACKETS_OP, null)));
        ArrayList<Position> positions = new ArrayList<>(Arrays.asList(
                new Position(1, 1), // fun
                new Position(5, 1), // runFun
                new Position(11, 1), // (
                new Position(12, 1), // )
                new Position(14, 1), // ->
                new Position(17, 1), // void
                new Position(22, 1), // {
                new Position(2, 2), // Circle
                new Position(9, 2), // c
                new Position(11, 2), // =
                new Position(13, 2), // Circle
                new Position(19, 2), // (
                new Position(20, 2), // Point
                new Position(25, 2), // (
                new Position(26, 2), // 1.2
                new Position(29, 2), // ,
                new Position(31, 2), // 2.1
                new Position(34, 2), // )
                new Position(35, 2), // ,
                new Position(37, 2), // 5.4
                new Position(40, 2), // )
                new Position(2, 3),  // Shape
                new Position(8, 3),  // cShape
                new Position(15, 3), // =
                new Position(17, 3), // Shape
                new Position(22, 3), // .
                new Position(23, 3), // cir
                new Position(26, 3), // (
                new Position(27, 3), // c
                new Position(28, 3), // )
                new Position(2, 4),  // flt
                new Position(6, 4),  // cArea
                new Position(12, 4), // =
                new Position(14, 4), // shapeArea
                new Position(23, 4), // (
                new Position(24, 4), // &
                new Position(25, 4), // cShape
                new Position(31, 4), // )
                new Position(2, 5),  // if
                new Position(5, 5),  // (
                new Position(6, 5),  // cArea
                new Position(12, 5), // >
                new Position(14, 5), // rArea
                new Position(19, 5), // )
                new Position(21, 5), // {
                new Position(4, 6),  // print
                new Position(9, 6),  // (
                new Position(10, 6),  // "Circle won"
                new Position(22, 6), // )
                new Position(2, 7),  // }
                new Position(1, 8))); // }

        TokenBuilder tb = new TokenBuilder(src);
        Token token = tb.getNextToken();
        int curTokenId = 0;
        while (token.getTokenType() != TokenType.END_OF_TEXT) {
            assertEquals(positions.get(curTokenId).getLine(), token.getPossition().getLine());
            assertEquals(positions.get(curTokenId).getcharacter(), token.getPossition().getcharacter());
            assertEquals(token.getTokenType(), values.get(curTokenId).left);
            if(!Objects.isNull(values.get(curTokenId).right)) {
                assertEquals(token.getValue(), values.get(curTokenId).right);
            }
            curTokenId++;
            token = tb.getNextToken();
        }
    }

    @Test
    void testEverything2() throws LekserException, NoValueToken {
        Source src = new StringSource("fun shapeArea(&Shape s) -> flt {\n" +
                " match s {\n" +
                "  Shape.cir(value) {\n" +
                "   return circleArea(value)\n" +
                "  }\n" +
                "  Shape.rec(value) {\n" +
                "   return rectangleArea(value)\n" +
                "  }\n" +
                " }");
        ArrayList<Position> positions = new ArrayList<>(Arrays.asList(
                new Position(1, 1), // fun
                new Position(5, 1), // shapeArea
                new Position(14, 1), // (
                new Position(15, 1), // &
                new Position(16, 1), // Shape
                new Position(22, 1), // s
                new Position(23, 1), // )
                new Position(25, 1), // ->
                new Position(28, 1), // flt
                new Position(32, 1), // {
                new Position(2, 2),  // match
                new Position(8, 2),  // s
                new Position(10, 2), // {
                new Position(3, 3),  // Shape
                new Position(8, 3),  // .
                new Position(9, 3),  // cir
                new Position(12, 3), // (
                new Position(13, 3), // value
                new Position(18, 3), // )
                new Position(20, 3), // {
                new Position(4, 4),  // return
                new Position(11, 4), // circleArea
                new Position(21, 4), // (
                new Position(22, 4), // value
                new Position(27, 4), // )
                new Position(3, 5),  // }
                new Position(3, 6),  // Shape.rec
                new Position(8, 6),  // .
                new Position(9, 6),  // rec
                new Position(12, 6), // (
                new Position(13, 6), // value
                new Position(18, 6), // )
                new Position(20, 6), // {
                new Position(4, 7),  // return
                new Position(11, 7), // rectangleArea
                new Position(24, 7), // (
                new Position(25, 7), // value
                new Position(30, 7), // )
                new Position(3, 8),  // }
                new Position(2, 9)   // }
        ));
        ArrayList<ImmutablePair<TokenType, Object>> values = new ArrayList<>(Arrays.asList(
                new ImmutablePair<>(TokenType.FUN_KEYWORD, null),
                new ImmutablePair<>(TokenType.NAME, "shapeArea"),
                new ImmutablePair<>(TokenType.OPEN_SOFT_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.REFERENCE_OP, null),
                new ImmutablePair<>(TokenType.NAME, "Shape"),
                new ImmutablePair<>(TokenType.NAME, "s"),
                new ImmutablePair<>(TokenType.CLOSE_SOFT_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.RETURN_TYPE_OP, null),
                new ImmutablePair<>(TokenType.FLT_KEYWORD, null),
                new ImmutablePair<>(TokenType.OPEN_SHARP_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.MATCH_KEYWORD, null),
                new ImmutablePair<>(TokenType.NAME, "s"),
                new ImmutablePair<>(TokenType.OPEN_SHARP_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.NAME, "Shape"),
                new ImmutablePair<>(TokenType.DOT_OP, null),
                new ImmutablePair<>(TokenType.NAME, "cir"),
                new ImmutablePair<>(TokenType.OPEN_SOFT_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.NAME, "value"),
                new ImmutablePair<>(TokenType.CLOSE_SOFT_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.OPEN_SHARP_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.RETURN_KEYWORD, null),
                new ImmutablePair<>(TokenType.NAME, "circleArea"),
                new ImmutablePair<>(TokenType.OPEN_SOFT_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.NAME, "value"),
                new ImmutablePair<>(TokenType.CLOSE_SOFT_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.CLOSE_SHARP_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.NAME, "Shape"),
                new ImmutablePair<>(TokenType.DOT_OP, null),
                new ImmutablePair<>(TokenType.NAME, "rec"),
                new ImmutablePair<>(TokenType.OPEN_SOFT_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.NAME, "value"),
                new ImmutablePair<>(TokenType.CLOSE_SOFT_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.OPEN_SHARP_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.RETURN_KEYWORD, null),
                new ImmutablePair<>(TokenType.NAME, "rectangleArea"),
                new ImmutablePair<>(TokenType.OPEN_SOFT_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.NAME, "value"),
                new ImmutablePair<>(TokenType.CLOSE_SOFT_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.CLOSE_SHARP_BRACKETS_OP, null),
                new ImmutablePair<>(TokenType.CLOSE_SHARP_BRACKETS_OP, null)
        ));
        TokenBuilder tb = new TokenBuilder(src);
        Token token = tb.getNextToken();
        int curTokenId = 0;
        while (token.getTokenType() != TokenType.END_OF_TEXT) {
            assertEquals(positions.get(curTokenId), token.getPossition());
            assertEquals(token.getTokenType(), values.get(curTokenId).left);
            if(!Objects.isNull(values.get(curTokenId).right)) {
                assertEquals(token.getValue(), values.get(curTokenId).right);
            }
            curTokenId++;
            token = tb.getNextToken();
        } }


    private static void TestLongText(String text, ArrayList<ImmutablePair<TokenType, Object>> values) throws LekserException, NoValueToken {
        Source src = new StringSource(text);
        TokenBuilder tb = new TokenBuilder(src);
        Token token;
        for (ImmutablePair<TokenType, Object> value : values) {
            token = tb.getNextToken();
            Assertions.assertEquals(value.left, token.getTokenType());
            if (!Objects.isNull(value.right)) {
                Assertions.assertEquals(value.right, token.getValue());
            }
        }
    }

    private static void testLongTextPossition(String text, ArrayList<ImmutablePair<TokenType, Object>> values) throws LekserException, NoValueToken {
        Source src = new StringSource(text);
        TokenBuilder tb = new TokenBuilder(src);
        Token token;
        for (ImmutablePair<TokenType, Object> value : values) {
            token = tb.getNextToken();
            Assertions.assertEquals(value.left, token.getTokenType());
            if (!Objects.isNull(value.right)) {
                Assertions.assertEquals(value.right, token.getValue());
            }
        }
    }

    private static void testTokenType(String text, List<TokenType> types) throws LekserException {
        Source src = new StringSource(text);
        TokenBuilder tb = new TokenBuilder(src);
        Token token;
        for (TokenType type : types) {
             token = tb.getNextToken();
            Assertions.assertEquals(type, token.getTokenType());
        }
    }




}