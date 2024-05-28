package parser.subParsers;

import exceptions.AnalizerException;
import inputHandle.Position;
import lekser.TokenType;
import parser.Parser;
import parser.exceptions.ParserException;
import parser.parsableObjects.blocks.Block;
import parser.parsableObjects.expression.Expression;
import parser.parsableObjects.expression.VariableCall;

import static parser.subParsers.ParseFunctionCall.parseFuncCall;
import static parser.subParsers.variableParsers.StructCallParser.parseStructCall;

public class NameExpressionParser {
    /**
     * EBNF: name, (variable_call | struct_init_or_call | function_call)
     *
     * @return Block.
     */

    public static Expression parseNameExpression(Parser par) throws AnalizerException {
        if (par.getToken().getTokenType() != TokenType.NAME) {
            return null;
        }
        Position pos = par.getToken().getPosition();
        String name = (String) par.getToken().getValue();
        par.consumeToken();

        // struct_init_or_call
        Expression functionCall = parseFuncCall(par, name, pos);
        if (functionCall != null) {
            return functionCall;
        }

        // function_call
        Expression structCall = parseStructCall(par, name, pos);
        if (structCall != null) {
            return structCall;
        }

        // variable_call
        return new VariableCall(name, pos);

    }
}
