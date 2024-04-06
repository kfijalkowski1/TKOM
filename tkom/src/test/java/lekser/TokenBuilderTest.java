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
                Arguments.of("      111.0\t\t\t\n", 111.0F)
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
    void testOperators(String text, String value) {
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
                Arguments.of("\"aa \\n \\t \"", "aa \\n \\t ")
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


}