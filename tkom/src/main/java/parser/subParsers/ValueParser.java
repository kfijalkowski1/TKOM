package parser.subParsers;

import exceptions.AnalizerException;
import lekser.TokenType;
import lekser.exceptions.LekserException;
import parser.Parser;
import parser.exceptions.ParserException;
import parser.parsableObjects.expression.Expression;
import parser.parsableObjects.expression.LiteralValue;
import parser.parsableObjects.expression.VariableCall;

import static parser.subParsers.NameExpressionParser.parseNameExpression;
import static parser.utils.ParserUtils.buildInValues;

public class ValueParser {

    /**
     * value  = literal | name_value;
     *
     * @return Value with one of the types: Value, FunctionCall, ArithmeticResult.
     */
    public static Expression parseUnpackedValue(Parser par) throws AnalizerException {
        LiteralValue literalValue = parseLiteralValue(par);
        if (literalValue != null) {
            par.consumeToken();
            return literalValue;
        }
        Expression callBlock = parseNameExpression(par);
        if (callBlock != null) {
            return callBlock;
        }
        Expression refVariable =  parseRefVariable(par);
        return refVariable;
    }

    /**
     * EBNF: ref_variable = "&", name;
     * @param par
     * @return
     */
    private static Expression parseRefVariable(Parser par) throws LekserException, ParserException {
        if (par.getToken().getTokenType() != TokenType.REFERENCE_OP) {
            return null;
        }
        par.consumeToken();
        String name = (String) par.mustBe(TokenType.NAME,
                new ParserException(par.getToken().getPosition(), "No name in reference variable"), true);
        par.consumeToken();
        return new VariableCall(name, par.getToken().getPosition());
    }

    /**
     * literal_value = int | flt | str | true | false;
     *
     * @return Value with one of the types: Value.
     */
    private static LiteralValue parseLiteralValue(Parser par) throws AnalizerException {
        if (!buildInValues.contains(par.getToken().getTokenType())) {
            return null;
        } else if (par.getToken().getTokenType() == TokenType.TRUE_KEYWORD) {
            return new LiteralValue(true, TokenType.TRUE_KEYWORD, par.getToken().getPosition());
        } else if (par.getToken().getTokenType() == TokenType.FALSE_KEYWORD) {
            return new LiteralValue(false, TokenType.FALSE_KEYWORD, par.getToken().getPosition());
        }
        return new LiteralValue(par.getToken().getValue(), par.getToken().getTokenType(), par.getToken().getPosition());
    }

}
