package parser.subParsers.variableParsers;

import exceptions.AnalizerException;
import inputHandle.Position;
import lekser.TokenType;
import lekser.exceptions.LekserException;
import parser.Parser;
import parser.exceptions.ParserException;
import parser.parsableObjects.expresions.Expression;
import parser.parsableObjects.structures.StructCall;
import parser.parsableObjects.structures.StructValueAssigment;
import parser.parsableObjects.variables.StructInit;
import parser.parsableObjects.variables.Value;

import java.util.ArrayList;
import java.util.List;

import static parser.subParsers.variableParsers.ValueParser.parseValue;
import static parser.utils.ParserUtils.parseComaValues;

public class StructCallParser {

    /**
     * EBNF: structCall = ".", name, {".", name} ([ "(" value ")"] | "=" value);
     *value is for initilizing TaggedUnion, eg: Shape cShape = Shape.cir(c)
     * @return StructCall object.
     */
    public static Expression parseStructCall(Parser par, String structName, Position pos) throws AnalizerException {
        if (par.getToken().getTokenType() != TokenType.DOT_OP) {
            return null;
        }
        par.consumeToken();
        List<String> parameters = new ArrayList<>();

        String parameter = (String) par.mustBe(TokenType.NAME,
                new parser.exceptions.ParserException(par.getToken().getPosition(), "No name in struct call"), true);
        par.consumeToken();
        parameters.add(parameter);
        // parameters

        while (par.getToken().getTokenType() == TokenType.DOT_OP) {
            parameter = (String) par.mustBe(TokenType.NAME,
                    new ParserException(par.getToken().getPosition(), "No parapetr in struct call"), true);
            par.consumeToken();
            parameters.add(parameter);
        }
        if (par.getToken().getTokenType() == TokenType.ASSIGN_OP) {
            par.consumeToken();
            Value value = parseValue(par);
            return new StructValueAssigment(new StructCall(structName, parameters, pos), value, pos);
        }
        if (par.getToken().getTokenType() == TokenType.OPEN_SOFT_BRACKETS_OP) {
            par.consumeToken();
            List<Expression> values = parseComaValues(par, "Struct initialization");
            par.mustBe(TokenType.CLOSE_SOFT_BRACKETS_OP,
                    new ParserException(par.getToken().getPosition(), "No closing bracket in struct initialization"));
            return new StructInit(structName, parameters, values, pos);
        }
        return new StructCall(structName, parameters, pos);

    }
}
