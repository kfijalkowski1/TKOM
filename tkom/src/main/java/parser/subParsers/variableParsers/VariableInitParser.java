package parser.subParsers.variableParsers;

import exceptions.AnalizerException;
import inputHandle.Position;
import lekser.TokenType;
import parser.Parser;
import parser.exceptions.ParserException;
import parser.parsableObjects.blocks.arithmeticStandalone.ArithmaticStandalone;
import parser.parsableObjects.blocks.Block;
import parser.parsableObjects.expression.Expression;
import parser.parsableObjects.variables.ConstGlobalVariableDeclaration;
import parser.parsableObjects.expression.VariableCall;
import parser.parsableObjects.variables.VariableInit;

import static parser.subParsers.mathParser.IncrementParsers.parseAllIncrement;
import static parser.utils.ParserUtils.buildInTypes;
import static parser.utils.ParserUtils.getExpValue;

public class VariableInitParser {

    /**
     * EBNF: variableInit =  name, "=", value;
     * first name is type, second name is variable name
     * first name is parsed (called only from nameBlockParser)
     * Beginning
     *
     * @return Statement with on of the types: Structure, Function, Expresion.
     */
    public static VariableInit parseVariableInitCustomTypeBeginning(Parser par, String type, Position pos) throws AnalizerException {
        if (par.getToken().getTokenType() != TokenType.NAME) {
            return null;
        }
        // name
        String name = (String) par.mustBe(TokenType.NAME,
                new parser.exceptions.ParserException(pos, "No name in variable declaration"), true);
        par.consumeToken();

        // =
        par.mustBe(TokenType.ASSIGN_OP, new parser.exceptions.ParserException(par.getToken().getPosition(), "Missing '=' in variable declaration"), false);
        par.consumeToken();

        // value
        Expression value = getExpValue(par, "variable declaration");

        return new VariableInit(name, type, value, false, false, pos);
    }



    /**
     * EBNF: variable_init   = ["gscope"], ["const"], type, name, "=", expression;
     *
     * (type has reference in EBNF: type = ["&"], (normalType | customTypeName);
     *
     * parser variables with not custom type
     */
    public static Block parseVariableInit(Parser par) throws AnalizerException {
        if (!buildInTypes.contains(par.getToken().getTokenType())
                && par.getToken().getTokenType() != TokenType.GSCOPE_KEYWORD
                && par.getToken().getTokenType() != TokenType.CONST_KEYWORD
                && par.getToken().getTokenType() != TokenType.REFERENCE_OP) {
            return null;
        }
        boolean gscoope = false;
        boolean isConst = false;
        boolean isReference = false;

        Position pos = par.getToken().getPosition();

        if (par.getToken().getTokenType() == TokenType.GSCOPE_KEYWORD) {
            gscoope = true;
            par.consumeToken();
        }
        if (par.getToken().getTokenType() == TokenType.CONST_KEYWORD) {
            isConst = true;
            par.consumeToken();
        }
        if (par.getToken().getTokenType() == TokenType.REFERENCE_OP) {
            isReference = true;
            par.consumeToken();
        }

        // type
        String type;
        if (par.getToken().getTokenType() == TokenType.NAME) {
            type = (String) par.mustBe(TokenType.NAME,
                    new ParserException(pos, "No type in variable declaration"), true);
            par.consumeToken();
            if (isReference) { // check for &a++
                ArithmaticStandalone arithmaticStandalone = parseAllIncrement(par, new VariableCall(type, pos, true), pos);
                if (arithmaticStandalone != null) {
                    return arithmaticStandalone;
                }
            }
        } else { // should be built in type
            if (!buildInTypes.contains(par.getToken().getTokenType())) {
                throw new ParserException(par.getToken().getPosition(), "No type in variable declaration");
            }
            type = par.getToken().getTokenType().getType(); // built in type will return not null
            par.consumeToken();
        }

        // name
        String name = (String) par.mustBe(TokenType.NAME,
                new ParserException(pos, "No name in variable declaration"), true);
        par.consumeToken();

        if (par.getToken().getTokenType() != TokenType.ASSIGN_OP) {
            return new ConstGlobalVariableDeclaration(name, type, isConst, gscoope, pos);
        }

        // =
        par.mustBe(TokenType.ASSIGN_OP, new ParserException(par.getToken().getPosition(), "Missing '=' in variable declaration"));
        par.consumeToken();

        // value
        Expression value = getExpValue(par, "variable declaration");

        return new VariableInit(name, type, value, isConst, gscoope, pos, isReference);
    }

}
