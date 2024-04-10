package lekser.tokenBuilderUtils;

import inputHandle.Source;
import lekser.Token;
import lekser.TokenType;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
            new AbstractMap.SimpleEntry<String, TokenType>("print", TokenType.PRINT_KEYWORD),
            new AbstractMap.SimpleEntry<String, TokenType>("const", TokenType.CONST_KEYWORD),
            new AbstractMap.SimpleEntry<String, TokenType>("while", TokenType.WHILE_KEYWORD),
            new AbstractMap.SimpleEntry<String, TokenType>("if", TokenType.IF_KEYWORD),
            new AbstractMap.SimpleEntry<String, TokenType>("elif", TokenType.ELIF_KEYWORD),
            new AbstractMap.SimpleEntry<String, TokenType>("and", TokenType.AND_KEYWORD),
            new AbstractMap.SimpleEntry<String, TokenType>("or", TokenType.OR_KEYWORD),
            new AbstractMap.SimpleEntry<String, TokenType>("not", TokenType.NOT_KEYWORD),
            new AbstractMap.SimpleEntry<String, TokenType>("return", TokenType.RETURN_KEYWORD)
    );

    public static Token buildName(Source src) {
        if (!Character.isLetter(src.getCurrentChar())) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        ImmutablePair<Integer, Integer> pos = src.getPossition();
        int counter = 0;
        while (!Character.isWhitespace(src.getCurrentChar()) && src.getCurrentChar() != '\u0003' && counter < 100) {
            sb.append(src.getCurrentChar());
            src.getNextChar(); // consume
            counter++;
        }
        if (counter >= 99) {
            throw new RuntimeException("name too long"); // TODO do it better
        }
        TokenType keywordToken = keywords.get(sb.toString());
        if (Objects.isNull(keywordToken)) {
            return new Token(TokenType.NAME, src.getPossition(), sb.toString());
        }
        return new Token(keywordToken, pos);

    }
}
