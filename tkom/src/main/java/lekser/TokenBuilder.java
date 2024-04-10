package lekser;

import inputHandle.Source;
import lekser.tokenBuilderUtils.TokenNumberAndStringBuilder;
import lekser.tokenBuilderUtils.TokenOperatorBuilder;
import lekser.tokenBuilderUtils.TokenVariableBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class TokenBuilder {
    private final Source src;
    private final List<Function<Source, Token>> tokenBuilders = getBuilderFunctions();

    public TokenBuilder(Source source) {
        src = source;
    }

    public Token getNextToken() {
        Token result;

        while(Character.isWhitespace(src.getCurrentChar())) {
            src.getNextChar();
        }

        for (Function<Source, Token> builder : tokenBuilders) {
             result = builder.apply(src);
            if (result != null) {
                return result;
            }
        }

        return new Token(TokenType.UNKNOWN_TOKEN, src.getPossition());
    }

    private List<Function<Source, Token>> getBuilderFunctions() {
        return Arrays.asList(
                TokenNumberAndStringBuilder::buildNumber,
                TokenNumberAndStringBuilder::stringBuilder,
                TokenOperatorBuilder::plusBuilder,
                TokenOperatorBuilder::minusBuilder,
                TokenOperatorBuilder::multipleBuilder,
                TokenOperatorBuilder::divideBuilder,
                TokenOperatorBuilder::moduloBuilder,
                TokenOperatorBuilder::lessBuilder,
                TokenOperatorBuilder::moreBuilder,
                TokenOperatorBuilder::equalsBuilder,
                TokenOperatorBuilder::notEquals,
                TokenOperatorBuilder::openSharpBrackets,
                TokenOperatorBuilder::closeSharpBrackets,
                TokenOperatorBuilder::openSoftBrackets,
                TokenOperatorBuilder::closeSoftBrackets,
                TokenOperatorBuilder::dotOperator,
                TokenOperatorBuilder::endOfTextOperator,
                TokenOperatorBuilder::refOperator,
                TokenVariableBuilder::buildName
        );
    }


}
