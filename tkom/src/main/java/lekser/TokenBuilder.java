package lekser;

import inputHandle.Source;
import lekser.exceptions.LekserException;
import lekser.tokenBuilderUtils.ThrowingFunction;
import lekser.tokenBuilderUtils.TokenNumberAndStringBuilder;
import lekser.tokenBuilderUtils.TokenOperatorBuilder;
import lekser.tokenBuilderUtils.TokenVariableBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class TokenBuilder {
    private final Source src;
    private final List<ThrowingFunction<Source, Token, LekserException>> tokenBuilders = getBuilderFunctions();

    public TokenBuilder(Source source) {
        src = source;
    }

    public Token getNextToken() throws LekserException {
        Token result;

        while(Character.isWhitespace(src.getCurrentChar())) {
            src.getNextChar();
        }

        for (ThrowingFunction<Source, Token, LekserException> builder : tokenBuilders) {
             result = builder.apply(src);
            if (result != null) {
                return result;
            }
        }

        return new Token(TokenType.UNKNOWN_TOKEN, src.getPossition());
    }

    private List<ThrowingFunction<Source, Token, LekserException>> getBuilderFunctions() {
        return Arrays.asList(
                TokenNumberAndStringBuilder::buildNumber,
                TokenNumberAndStringBuilder::stringBuilder,
                TokenOperatorBuilder::getOperator,
                TokenVariableBuilder::buildComment,
                TokenVariableBuilder::buildName
        );
    }


}
