package parser.subParsers.mathParser;

import exceptions.AnalizerException;
import inputHandle.Position;
import lekser.TokenType;
import lekser.exceptions.LekserException;
import parser.Parser;
import parser.exceptions.ParserException;
import parser.parsableObjects.blocks.arithmeticStandalone.ArithmaticStandalone;
import parser.parsableObjects.blocks.arithmeticStandalone.Increment;
import parser.parsableObjects.blocks.arithmeticStandalone.PostIncrement;
import parser.parsableObjects.blocks.Block;
import parser.parsableObjects.expression.Expression;
import parser.parsableObjects.expression.VariableCall;

import static parser.subParsers.ExpressionParser.parseExpression;

public class IncrementParsers {

    /**
     * EBNF: ("++" | ("+=" (name | numeric_value) ))
     * Parse an increment operation
     * @param par the parser
     * @return the increment operation
     */
    public static ArithmaticStandalone parseAllIncrement(Parser par, VariableCall variable, Position pos) throws AnalizerException {
        PostIncrement postInc = parsePostIncrement(par, variable, pos);
        if (postInc != null) {
            return postInc;
        }
        return parseIncrement(par, variable, pos); // if result is null, it will return null
    }

    private static PostIncrement parsePostIncrement(Parser par, VariableCall variable, Position pos) throws LekserException {
        if (par.getToken().getTokenType() != TokenType.POST_INCREMENT_OP) {
            return null;
        }
        par.consumeToken();
        return new PostIncrement(variable, pos);
    }

    private static Increment parseIncrement(Parser par, VariableCall variable, Position pos) throws AnalizerException {
        if (par.getToken().getTokenType() != TokenType.INCREMENT_OP) {
            return null;
        }
        par.consumeToken();
        Expression value = parseExpression(par);
        if (value == null) {
            throw new ParserException(pos, "Missing value after increment operation");
        }
        return new Increment(variable, value, pos);
    }
}
