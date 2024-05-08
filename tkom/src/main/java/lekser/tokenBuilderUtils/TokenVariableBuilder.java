package lekser.tokenBuilderUtils;

import inputHandle.Position;
import inputHandle.Source;
import lekser.Token;
import lekser.TokenType;
import lekser.exceptions.IncorrectValueToken;
import lekser.exceptions.NameTooLongException;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;

import static lekser.tokenBuilderUtils.BuildersUtils.END_OF_FILE;
import static lekser.tokenBuilderUtils.BuildersUtils.MAX_VARIABLE_NAME;

public class TokenVariableBuilder {

    private static final Map<String, TokenType> keywords = Map.ofEntries(
            new AbstractMap.SimpleEntry<String, TokenType>("int", TokenType.INT_KEYWORD),
            new AbstractMap.SimpleEntry<String, TokenType>("flt", TokenType.FLT_KEYWORD),
            new AbstractMap.SimpleEntry<String, TokenType>("fun", TokenType.FUN_KEYWORD),
            new AbstractMap.SimpleEntry<String, TokenType>("match", TokenType.MATCH_KEYWORD),
            new AbstractMap.SimpleEntry<String, TokenType>("str", TokenType.STR_KEYWORD),
            new AbstractMap.SimpleEntry<String, TokenType>("bool", TokenType.BOOL_KEYWORD),
            new AbstractMap.SimpleEntry<String, TokenType>("struct", TokenType.STRUCT_KEYWORD),
            new AbstractMap.SimpleEntry<String, TokenType>("TaggedUnion", TokenType.TaggedUnion_KEYWORD),
            new AbstractMap.SimpleEntry<String, TokenType>("const", TokenType.CONST_KEYWORD),
            new AbstractMap.SimpleEntry<String, TokenType>("gscope", TokenType.GSCOPE_KEYWORD),
            new AbstractMap.SimpleEntry<String, TokenType>("while", TokenType.WHILE_KEYWORD),
            new AbstractMap.SimpleEntry<String, TokenType>("if", TokenType.IF_KEYWORD),
            new AbstractMap.SimpleEntry<String, TokenType>("elif", TokenType.ELIF_KEYWORD),
            new AbstractMap.SimpleEntry<String, TokenType>("else", TokenType.ELSE_KEYWORD),
            new AbstractMap.SimpleEntry<String, TokenType>("and", TokenType.AND_KEYWORD),
            new AbstractMap.SimpleEntry<String, TokenType>("or", TokenType.OR_KEYWORD),
            new AbstractMap.SimpleEntry<String, TokenType>("not", TokenType.NOT_KEYWORD),
            new AbstractMap.SimpleEntry<String, TokenType>("return", TokenType.RETURN_KEYWORD),
            new AbstractMap.SimpleEntry<String, TokenType>("true", TokenType.TRUE_KEYWORD),
            new AbstractMap.SimpleEntry<String, TokenType>("false", TokenType.FALSE_KEYWORD),
            new AbstractMap.SimpleEntry<String, TokenType>("break", TokenType.BREAK_KEYWORD),
            new AbstractMap.SimpleEntry<String, TokenType>("void", TokenType.VOID_KEYWORD)
    );

    public static Token buildName(Source src) throws NameTooLongException, IncorrectValueToken {
        if (!Character.isLetter(src.getCurrentChar())) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        Position pos = src.getPossition();
        int counter = 0;
        while (Character.isLetter(src.getCurrentChar()) && counter < MAX_VARIABLE_NAME+1) {
            sb.append(src.getCurrentChar());
            src.getNextChar(); // consume
            counter++;
        }
        if (counter == MAX_VARIABLE_NAME+1) {
            throw new NameTooLongException(pos, "Name too long, max length: %d".formatted(MAX_VARIABLE_NAME));
        }
        TokenType keywordToken = keywords.get(sb.toString());
        if (Objects.isNull(keywordToken)) {
            return new Token(TokenType.NAME, pos, sb.toString());
        }
        return new Token(keywordToken, pos);

    }

    public static Token buildComment(Source src) {
        if (src.getCurrentChar() != '#') {
            return null;
        }
        Position pos = src.getPossition();
        src.getNextChar();

        while (src.getCurrentChar() != '\n' && src.getCurrentChar() != '\r' && src.getCurrentChar() != END_OF_FILE ) {
            src.getNextChar(); // build
        }
        src.getNextChar(); // consume
        return new Token(TokenType.COMMENT, pos);
    }
}
