package parser.subParsers;

import exceptions.AnalizerException;
import inputHandle.Position;
import lekser.TokenType;
import parser.Parser;
import parser.exceptions.ParserException;
import parser.parsableObjects.expresions.ArithmeticResult;
import parser.parsableObjects.expresions.Expression;
import parser.parsableObjects.variables.VariableCall;

import static parser.subParsers.ParseFunctionCall.parseFuncCall;
import static parser.subParsers.mathParser.IncrementParsers.parseAllIncrement;
import static parser.subParsers.variableParsers.StructCallParser.parseStructCall;
import static parser.subParsers.variableParsers.VariableAssigmentParser.parseVariableAssigment;
import static parser.subParsers.variableParsers.VariableInitParser.parseVariableInitCustomTypeBeginning;

public class NameExpressionParser {
    /**
     * EBNF: callOp = name,  (variable_call | function_call | arithmatic_standalone
     * EBNF:  variable_init  = ["gscope"], ["const"], type, name, "=", value
     * type is the same as name so its name name = value
     *
     * @return Expression.
     */

    public static Expression parseNameExpression(Parser par) throws AnalizerException {
        return parseNameExpression(par, false);
    }

    public static Expression parseNameExpression(Parser par, Boolean isExecutable) throws AnalizerException {
        if (par.getToken().getTokenType() != TokenType.NAME) {
            return null;
        }
        Position pos = par.getToken().getPosition();
        String name = (String) par.getToken().getValue();
        par.consumeToken();
        // (variable_call | function_call | arithmatic_standalone | variable_init | struct_call) // variable init custom type
        Expression functionCall = parseFuncCall(par, name, pos);
        if (functionCall != null) {
            return functionCall;
        }
        Expression arithmaticStandalone = parseAllIncrement(par, new VariableCall(name, pos), pos);
        if (arithmaticStandalone != null) {
            return arithmaticStandalone;
        }
        Expression structCall = parseStructCall(par, name, pos);
        if (structCall != null) {
            return structCall;
        }
        if (!isExecutable) {
            Expression variableAssigment = parseVariableAssigment(par, name, pos);
            if (variableAssigment != null) {
                return variableAssigment;
            }
            Expression variableInit = parseVariableInitCustomTypeBeginning(par, name, pos);
            if (variableInit != null) {
                return variableInit;
            }
        } else {
            return new VariableCall(name, pos);
        }

        throw new ParserException(par.getToken().getPosition(), "Missing operation after name/type");

    }
}
