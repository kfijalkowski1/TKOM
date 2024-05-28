package parser.subParsers;

import exceptions.AnalizerException;
import inputHandle.Position;
import lekser.TokenType;
import parser.Parser;
import parser.exceptions.ParserException;
import parser.parsableObjects.blocks.FunctionCall;
import parser.parsableObjects.expression.Expression;

import java.util.List;

import static parser.utils.ParserUtils.parseComaExpressions;

public class ParseFunctionCall {

    /**
     * EBNF: "(", [expression, {"," expression}] ")";
     * @param par parser to parse from
     * @return parsed function call
     */
    public static FunctionCall parseFuncCall(Parser par, String name, Position pos) throws AnalizerException {
        if (par.getToken().getTokenType() != TokenType.OPEN_SOFT_BRACKETS_OP) {
            return null;
        }
        return getCallValues(par, name, pos);

    }



    private static FunctionCall getCallValues(Parser par, String name, Position pos) throws AnalizerException {
        par.consumeToken();

        // [value, {"," value}]
        List<Expression> values = parseComaExpressions(par, "function call");

        // )
        par.mustBe(TokenType.CLOSE_SOFT_BRACKETS_OP, new ParserException(par.getToken().getPosition(), "Missing ')' in function call"), false);
        par.consumeToken();

        return new FunctionCall(name, values, pos);
    }


}
