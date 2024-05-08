package parser.subParsers;

import exceptions.AnalizerException;
import inputHandle.Position;
import lekser.TokenType;
import lekser.exceptions.LekserException;
import parser.Parser;
import parser.parsableObjects.expresions.BreakExpression;
import parser.parsableObjects.expresions.Expression;
import parser.parsableObjects.expresions.ReturnExpression;
import parser.parsableObjects.variables.Value;
import parser.subParsers.variableParsers.ValueParser;

public class ExitExpression {

    /**
     * EBNF: returnExp = "return", value;
     *
     * @return ReturnExpression object.
     *
     * @param par
     * @throws LekserException
     */
    public static ReturnExpression parseReturnExpression(Parser par) throws AnalizerException {
        if (par.getToken().getTokenType() != TokenType.RETURN_KEYWORD) {
            return null;
        }
        Position pos = par.getToken().getPosition();
        par.consumeToken();
        Value value = ValueParser.parseValue(par);
        if (value == null) {
            throw new parser.exceptions.ParserException(par.getToken().getPosition(), "Missing value in return expression");
        }

        return new ReturnExpression(value, pos);

    }

    public static Expression parseExitExpression(Parser parser) throws AnalizerException {
        ReturnExpression returnExpression = parseReturnExpression(parser);
        if (returnExpression != null) {
            return returnExpression;
        }
        return parseBreakExpression(parser);
    }

    private static Expression parseBreakExpression(Parser parser) throws LekserException {
        if (parser.getToken().getTokenType() == TokenType.BREAK_KEYWORD) {
            Position pos = parser.getToken().getPosition();
            parser.consumeToken();
            return new BreakExpression(pos);
        }
        return null;
    }
}
