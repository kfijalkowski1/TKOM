package parser.subParsers.variableParsers;

import exceptions.AnalizerException;
import inputHandle.Position;
import lekser.TokenType;
import parser.Parser;
import parser.exceptions.ParserException;
import parser.parsableObjects.blocks.Block;
import parser.parsableObjects.expression.Expression;
import parser.parsableObjects.expression.StructCall;
import parser.parsableObjects.expression.StructInit;
import parser.parsableObjects.structures.StructValueAssigment;

import java.util.ArrayList;
import java.util.List;

import static parser.utils.ParserUtils.getExpValue;
import static parser.utils.ParserUtils.parseComaExpressions;

public class StructCallParser {

    /**
     * struct_init_or_call    = ".", name, {".", name}, [struct_init];
     * struct_init            = "(" expression ")";
     *
     * Struct init: Shape.cir(c)
     * Struct call: Shape.cir.d
     * @return StructCall object.
     */
    public static Expression parseStructCall(Parser par, String structName, Position pos) throws AnalizerException {
        if (par.getToken().getTokenType() != TokenType.DOT_OP) {
            return null;
        }
        par.consumeToken();
        List<String> parameters = new ArrayList<>();

        String struct_filds = (String) par.mustBe(TokenType.NAME,
                new parser.exceptions.ParserException(par.getToken().getPosition(), "No name in struct call"), true);
        par.consumeToken();
        parameters.add(struct_filds);
        // parameters

        while (par.getToken().getTokenType() == TokenType.DOT_OP) {
            par.consumeToken();
            struct_filds = (String) par.mustBe(TokenType.NAME,
                    new ParserException(par.getToken().getPosition(), "No parameter in struct call"), true);
            par.consumeToken();
            parameters.add(struct_filds);
        }

        // ( expression )
        if (par.getToken().getTokenType() == TokenType.OPEN_SOFT_BRACKETS_OP) {
            par.consumeToken();
            List<Expression> values = parseComaExpressions(par, "Struct initialization");
            par.mustBe(TokenType.CLOSE_SOFT_BRACKETS_OP,
                    new ParserException(par.getToken().getPosition(), "No closing bracket in struct initialization"));
            par.consumeToken();
            return new StructInit(structName, parameters, values, pos);
        }
        return new StructCall(structName, parameters, pos);

    }

    /**
     * {".", name}, "=" expression;
     *
     * t.r = 10
     * @return StructCall object.
     */
    public static Block parseStructParameterAssign(Parser par, String name, Position pos) throws AnalizerException {
        if (par.getToken().getTokenType() != TokenType.DOT_OP) {
            return null;
        }
        par.consumeToken();
        List<String> parameters = new ArrayList<>();

        String struct_filds = (String) par.mustBe(TokenType.NAME,
                new parser.exceptions.ParserException(par.getToken().getPosition(), "No name in struct call"), true);
        par.consumeToken();
        parameters.add(struct_filds);
        // parameters

        while (par.getToken().getTokenType() == TokenType.DOT_OP) {
            par.consumeToken();
            struct_filds = (String) par.mustBe(TokenType.NAME,
                    new ParserException(par.getToken().getPosition(), "No parameter in struct call"), true);
            par.consumeToken();
            parameters.add(struct_filds);
        }

        // =
        if (par.getToken().getTokenType() == TokenType.ASSIGN_OP) {
            par.consumeToken();
            Expression value = getExpValue(par, "Struct parameter assign");
            return new StructValueAssigment(new StructCall(name, parameters, pos), value, pos);
        }

        throw new ParserException(par.getToken().getPosition(), "No assign in struct parameter assign");
    }
}
