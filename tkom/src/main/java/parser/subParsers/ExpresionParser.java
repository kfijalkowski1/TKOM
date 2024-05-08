package parser.subParsers;

import exceptions.AnalizerException;
import lekser.tokenBuilderUtils.ThrowingFunction;
import parser.Parser;
import parser.parsableObjects.expresions.Expression;
import parser.subParsers.variableParsers.VariableDeclarationParser;
import parser.subParsers.variableParsers.VariableInitParser;

import java.util.Arrays;
import java.util.List;

public class ExpresionParser {
    /**
     * EBNF: expression = conditional
     *                  | variable_init
     *                  | name_expression
     *                  | matchExp;;
     *
     * @return Expression with one of the types: VariableDeclaration.
     */
    public static Expression parseExpresion(Parser par) throws AnalizerException {
        List<ThrowingFunction<Parser, Expression, AnalizerException>> expressionBuilders = getExpressionBuilders();
        Expression result;
        for (ThrowingFunction<Parser, Expression, AnalizerException> builder : expressionBuilders) {
            result = builder.apply(par);
            if (result != null) {
                return result;
            }
        }
        return null;
    }


    private static List<ThrowingFunction<Parser, Expression, AnalizerException>> getExpressionBuilders() {
        return Arrays.asList(
                NameExpressionParser::parseNameExpression,
                VariableInitParser::parseVariableInit,
                ExitExpression::parseExitExpression,
                matchParser::parseMatch
        );
    }
}
