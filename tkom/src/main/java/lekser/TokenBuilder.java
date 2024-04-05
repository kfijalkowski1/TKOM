package lekser;

import inputHandle.Source;
import lekser.tokenBuilderUtils.TokenNumberBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class TokenBuilder {
    private final Source src;

    public TokenBuilder(Source source) {
        src = source;
    }

    public Token getNextToken() {
        List<Function<Source, Token>> tokenBuilders = getBuilderFunctions();
        Token result;
        if (src.getCurrentChar() == '\u0003') {
            return new Token(TokenType.END_OF_TEXT, src.getPossition());
        }
        src.getNextChar();

        while(Character.isWhitespace(src.getCurrentChar())) {
            src.getNextChar();
        }

        for (Function<Source, Token> builder : tokenBuilders) {
             result = builder.apply(src);
            if (result != null) {
                return result;
            }
        }

        if (src.getCurrentChar() == '\u0003') {
            return new Token(TokenType.END_OF_TEXT, src.getPossition());
        }
        return new Token(TokenType.UNKNOWN_TOKEN, src.getPossition());
    }

    private List<Function<Source, Token>> getBuilderFunctions() {
        return Arrays.asList(
                s -> {
                    try {
                        return TokenNumberBuilder.buildNumber(s);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }


}
