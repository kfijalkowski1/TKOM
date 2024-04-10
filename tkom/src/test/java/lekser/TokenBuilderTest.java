package lekser;

import inputHandle.Source;
import inputHandle.StringSource;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;



public class TokenBuilderTest {

    @ParameterizedTest
    @MethodSource("singularIntegerSource")
    @DisplayName("Test singular integer values")
    void testIntNumerics(String text, Integer value) {
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
                Arguments.of("      0\t\t\t\n", 0)
        );
    }

    @ParameterizedTest
    @MethodSource("singularFloatSource")
    @DisplayName("Test singular float values")
    void testFltNumerics(String text, Float value) {
        Source src = new StringSource(text);
        TokenBuilder tb = new TokenBuilder(src);
        Token firstToken = tb.getNextToken();
        Assertions.assertEquals(TokenType.FLT_NUMBER, firstToken.getTokenType());
        Assertions.assertEquals(value, firstToken.getValue());
    }

    private static Stream<Arguments> singularFloatSource() {
        return Stream.of(
                Arguments.of("1.1", 1.1F),
                Arguments.of("0.1 ", 0.1F),
                Arguments.of("\n\n\t8.55", 8.55F),
                Arguments.of("      111.0\t\t\t\n", 111.0F),
                Arguments.of("      1.001\t\t\t\n", 1.001F)
        );
    }

    @ParameterizedTest
    @MethodSource("multipleFloatIntSource")
    @DisplayName("Test many different numeric values")
    void testMultipleNumbers(String text, ArrayList<ImmutablePair<TokenType, Object>> values) {
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
                        new ImmutablePair<>(TokenType.FLT_NUMBER, 1223.1111111F),
                        new ImmutablePair<>(TokenType.INT_NUMBER, 19),
                        new ImmutablePair<>(TokenType.FLT_NUMBER, 88.48F)));

        return Stream.of(
                Arguments.of("0.5\n\t0.08\n199", firstArray),
                Arguments.of("1223.1111111\n\t19 88.48", secondArray)
        );
    }

    @ParameterizedTest
    @MethodSource("operatorsSingularAndMultiple")
    @DisplayName("test single and multiple operators")
    void testOperators(String text, List<TokenType> types) {
        testTokenType(text, types);
    }

    private static Stream<Arguments> operatorsSingularAndMultiple() {
        return Stream.of(
                Arguments.of("+", List.of(TokenType.PLUS_OP)),
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
    void testString(String text, String value) {
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
                Arguments.of("\"aa \\n \\t \"", "aa \\n \\t "),
                Arguments.of("\"aa \n \t \"", "aa \n \t ")
        );
    }

    @ParameterizedTest
    @MethodSource("differentKeywords")
    @DisplayName("test various keywords")
    void testKeywords(String text, TokenType type) {
        Source src = new StringSource(text);
        TokenBuilder tb = new TokenBuilder(src);
        Token firstToken = tb.getNextToken();
        Assertions.assertEquals(type, firstToken.getTokenType());
    }

    private static Stream<Arguments> differentKeywords() {
        return Stream.of(
                Arguments.of("int", TokenType.INT_KEYWORD),
                Arguments.of("flt", TokenType.FLT_KEYWORD),
                Arguments.of("fun", TokenType.FUN_KEYWORD),
                Arguments.of("match", TokenType.MATCH_KEYWORD),
                Arguments.of("str", TokenType.STR_KEYWORD),
                Arguments.of("bool", TokenType.BOOL_KEYWORD),
                Arguments.of("struct", TokenType.STRUCT_KEYWORD),
                Arguments.of("TaggedUnion", TokenType.TaggedUnion_KEYWORD),
                Arguments.of("print", TokenType.PRINT_KEYWORD),
                Arguments.of("const", TokenType.CONST_KEYWORD),
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
                Arguments.of("int x = 5", TokenType.INT_KEYWORD, TokenType.NAME, TokenType.EQ_OP, TokenType.INT_KEYWORD)
        );
    }

    @ParameterizedTest
    @MethodSource("positionTests")
    @DisplayName("test reading of position")
    void testPositions(String text, Integer column, Integer line) {
        Source src = new StringSource(text);
        TokenBuilder tb = new TokenBuilder(src);
        Token firstToken = tb.getNextToken();
        Assertions.assertEquals(column, firstToken.getPossition().left);
        Assertions.assertEquals(line, firstToken.getPossition().right);
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


    private static void TestLongText(String text, ArrayList<ImmutablePair<TokenType, Object>> values) {
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

    private static void testTokenType(String text, List<TokenType> types) {
        Source src = new StringSource(text);
        TokenBuilder tb = new TokenBuilder(src);
        Token token;
        for (TokenType type : types) {
             token = tb.getNextToken();
            Assertions.assertEquals(type, token.getTokenType());
        }
    }


        //TODO test EOT

}