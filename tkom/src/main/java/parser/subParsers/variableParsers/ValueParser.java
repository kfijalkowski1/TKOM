package parser.subParsers.variableParsers;

import exceptions.AnalizerException;
import inputHandle.Position;
import lekser.TokenType;
import lekser.exceptions.LekserException;
import parser.Parser;
import parser.exceptions.ParserException;
import parser.parsableObjects.arithmatic.ArithmaticStandalone;
import parser.parsableObjects.expresions.ArithmeticResult;
import parser.parsableObjects.expresions.Expression;
import parser.parsableObjects.variables.Value;
import parser.parsableObjects.variables.VariableCall;

import static parser.parsableObjects.expresions.ArithmeticResult.parseArithmeticResult;
import static parser.subParsers.NameExpressionParser.parseNameExpression;

public class ValueParser {
    /**
     * value  = literal_value | arithmetic_result | function_call | arithmetic_standalone;
     *
     * @return Value with one of the types: Value, FunctionCall, ArithmeticResult.
     */
    public static Value parseValue(Parser par) throws AnalizerException {
        Value literalValue = parseLiteralValue(par);
        Position pos = par.getToken().getPosition();
        if (literalValue != null) {
            par.consumeToken();
            return literalValue;
        }
        Expression callExpression = parseNameExpression(par, true);
        if (callExpression != null) {
            return new Value(callExpression, pos);
        }
        Expression parseRefVariable = parseRefVariable(par);
        if (parseRefVariable != null) {
            return new Value(parseRefVariable, pos);
        }
        ArithmeticResult arithmeticResult = parseArithmeticResult(par);
        if (arithmeticResult != null) {
            return new Value(arithmeticResult, pos);
        }
        return null;

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
        ArithmeticResult arithmaticStandalone = parseArithmeticResult(par);
        if (arithmaticStandalone != null) {
            return arithmaticStandalone;
        }
        return new VariableCall(name, par.getToken().getPosition());
    }

    /**
     * literal_value = int | flt | str | bool | name | true | false;
     *
     * @return Value with one of the types: Value.
     */
    private static Value parseLiteralValue(Parser par) throws AnalizerException {
        if (par.getToken().getTokenType() == TokenType.INT_NUMBER) {
            return new Value((Integer) par.getToken().getValue(), par.getToken().getPosition());
        } else if (par.getToken().getTokenType() == TokenType.FLT_NUMBER) {
            return new Value((Float) par.getToken().getValue(), par.getToken().getPosition());
        } else if (par.getToken().getTokenType() == TokenType.STRING_VALUE) {
            return new Value((String) par.getToken().getValue(), false, par.getToken().getPosition());
        } else if (par.getToken().getTokenType() == TokenType.TRUE_KEYWORD) {
            return new Value(true, par.getToken().getPosition());
        } else if (par.getToken().getTokenType() == TokenType.FALSE_KEYWORD) {
            return new Value(false, par.getToken().getPosition());
        }
        return null;
    }

}
