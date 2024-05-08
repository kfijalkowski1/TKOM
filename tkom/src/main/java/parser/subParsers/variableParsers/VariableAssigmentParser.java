package parser.subParsers.variableParsers;

import exceptions.AnalizerException;
import inputHandle.Position;
import lekser.TokenType;
import parser.Parser;
import parser.exceptions.ParserException;
import parser.parsableObjects.expresions.Expression;
import parser.parsableObjects.variables.VariableAssigment;

import static parser.subParsers.ValueParser.parseValue;

public class VariableAssigmentParser {
    /**
     * EBNF: "=", value;
     *
     * @return Statement with on of the types: Structure, Function, Expresion.
     */
    public static VariableAssigment parseVariableAssigment(Parser par, String name, Position pos) throws AnalizerException {
        if(par.getToken().getTokenType() != TokenType.ASSIGN_OP){
            return null;
        }
        par.consumeToken();
        Expression value = parseValue(par);
        if(value == null){
            throw new ParserException(par.getToken().getPosition(), "Missing value in variable assigment");
        }
        return new VariableAssigment(value, name, pos);
    }

}
