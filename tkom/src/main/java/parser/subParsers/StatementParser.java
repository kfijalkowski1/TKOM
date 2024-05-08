package parser.subParsers;

import exceptions.AnalizerException;
import lekser.TokenType;
import parser.Parser;
import parser.parsableObjects.FunctionDeclaration;
import parser.parsableObjects.Statement;
import parser.parsableObjects.expresions.Expression;
import parser.parsableObjects.structures.Structure;

import java.util.Objects;

import static parser.subParsers.ExpresionParser.parseExpresion;
import static parser.subParsers.FunctionDeclarationParser.parseFunctionDeclar;
import static parser.subParsers.StructureParser.parseStructure;

public class StatementParser {

    /**
     * EBNF: statement = structure
     *                 | function
     *                 | expression;
     *
     * @return Statement with on of the types: Structure, Function, Expresion.
     */
    public static Statement parseStatement(Parser par) throws AnalizerException {
//        if (par.getToken().getTokenType() == TokenType.END_OF_TEXT) {
//            return null;
//        }
        Structure structure = parseStructure(par);
        if (!Objects.isNull(structure)) {
            return structure;
        }
        FunctionDeclaration function = parseFunctionDeclar(par);
        if (!Objects.isNull(function)) {
            return function;
        }
        Expression expression = parseExpresion(par);
        if (!Objects.isNull(expression)) {
            return expression;
        }
        return null;
    }

}
