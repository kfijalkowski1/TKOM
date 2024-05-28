package parser.subParsers;

import exceptions.AnalizerException;
import inputHandle.Position;
import lekser.TokenType;
import parser.Parser;
import parser.exceptions.ParserException;
import parser.parsableObjects.Statement;
import parser.parsableObjects.blocks.Block;
import parser.parsableObjects.expression.Expression;
import parser.parsableObjects.expression.VariableCall;

import static parser.subParsers.ParseFunctionCall.parseFuncCall;
import static parser.subParsers.mathParser.IncrementParsers.parseAllIncrement;
import static parser.subParsers.variableParsers.StructCallParser.parseStructParameterAssign;
import static parser.subParsers.variableParsers.VariableAssigmentParser.parseVariableAssigment;
import static parser.subParsers.variableParsers.VariableInitParser.parseVariableInitCustomTypeBeginning;

public class NameBlockParser {
    /**
     * EBNF: name,  (variable_assignemt | function_call | arithmatic_standalone | name_variable_init | struct_assign)
     *
     * @return Block.
     */

    public static Statement parseNameBlock(Parser par) throws AnalizerException {
        if (par.getToken().getTokenType() != TokenType.NAME) {
            return null;
        }
        Position pos = par.getToken().getPosition();
        String name = (String) par.getToken().getValue();
        par.consumeToken();

        // variable_assignemt
        Block functionCall = parseVariableAssigment(par, name, pos);
        if (functionCall != null) {
            return functionCall;
        }

        // function_call
        Expression funcCall = parseFuncCall(par, name, pos);
        if (funcCall != null) {
            return funcCall;
        }

        // arithmatic_standalone
        Block structCall = parseAllIncrement(par, new VariableCall(name, pos), pos);
        if (structCall != null) {
            return structCall;
        }

        // name_variable_init
        Block name_variable_init =  parseVariableInitCustomTypeBeginning(par, name, pos);
        if (name_variable_init != null) {
            return name_variable_init;
        }

        // struct_assign
        Block structAssign = parseStructParameterAssign(par, name, pos);
        if (structAssign != null) {
            return structAssign;
        }

        throw new ParserException(pos, "No valid block after %s".formatted(name));

    }
}
