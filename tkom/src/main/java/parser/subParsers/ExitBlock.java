package parser.subParsers;

import exceptions.AnalizerException;
import inputHandle.Position;
import lekser.TokenType;
import lekser.exceptions.LekserException;
import parser.Parser;
import parser.parsableObjects.blocks.BreakBlock;
import parser.parsableObjects.blocks.Block;
import parser.parsableObjects.blocks.ReturnBlock;
import parser.parsableObjects.expression.Expression;

import static parser.utils.ParserUtils.getExpValue;

public class ExitBlock {

    /**
     * EBNF: returnExp = "return", value;
     *
     * @return ReturnBlock object.
     *
     * @param par
     * @throws LekserException
     */
    public static ReturnBlock parseReturnBlock(Parser par) throws AnalizerException {
        if (par.getToken().getTokenType() != TokenType.RETURN_KEYWORD) {
            return null;
        }
        Position pos = par.getToken().getPosition();
        par.consumeToken();

        Expression value = getExpValue(par, "return expression");

        return new ReturnBlock(value, pos);

    }

    public static Block parseExitBlock(Parser parser) throws AnalizerException {
        ReturnBlock returnBlock = parseReturnBlock(parser);
        if (returnBlock != null) {
            return returnBlock;
        }
        return parseBreakBlock(parser);
    }

    private static Block parseBreakBlock(Parser parser) throws LekserException {
        if (parser.getToken().getTokenType() == TokenType.BREAK_KEYWORD) {
            Position pos = parser.getToken().getPosition();
            parser.consumeToken();
            return new BreakBlock(pos);
        }
        return null;
    }
}
