package parser.subParsers.condition;

import exceptions.AnalizerException;
import inputHandle.Position;
import lekser.TokenType;
import parser.Parser;
import parser.exceptions.ParserException;
import parser.parsableObjects.conditional.condition.*;
import parser.parsableObjects.expresions.Expression;


import java.util.Objects;

import static parser.subParsers.ValueParser.parseValue;
import static parser.utils.ParserUtils.testers;

public class ConditionParser {

    /**
     * EBNF: condition              = "(", conditionExp, ")";;
     * doesn't check for  ( )
     * @return Condition object.
     */
    public static Condition parseCondition(Parser par) throws AnalizerException {
        par.mustBe(TokenType.OPEN_SOFT_BRACKETS_OP, new ParserException(par.getToken().getPosition(), "No opening bracket in condition"));
        par.consumeToken();
        Condition condition = parseConditionalExpression(par);
        par.mustBe(TokenType.CLOSE_SOFT_BRACKETS_OP, new ParserException(par.getToken().getPosition(), "No closing bracket in condition"));
        par.consumeToken();
        return condition;
    }

    /**
     * EBNF: conditionExp              = and_condition, {"or", and_condition};
     * doesn't check for  ( )
     * @return Condition object.
     */
    private static Condition parseConditionalExpression(Parser par) throws AnalizerException {
        Condition leftLogicFactor = parseAndCondition(par);
        while (par.getToken().getTokenType() == TokenType.OR_KEYWORD) {
            Position pos = par.getToken().getPosition();
            par.consumeToken();
            Condition rightLogicFactor = parseAndCondition(par);
            if (Objects.isNull(rightLogicFactor)) {
                throw new ParserException(pos, "No right side of or condition");
            }
            leftLogicFactor = new OrCondition(leftLogicFactor, rightLogicFactor, pos);
        }
        return leftLogicFactor;
    }

    /**
     * EBNF: and_condition          = check, {"and", check};
     * @return Condition object.
     *
     * @param par
     * @return
     */
    private static Condition parseAndCondition(Parser par) throws AnalizerException {
        Condition leftLogicFactor = parseCheck(par);
        while (par.getToken().getTokenType() == TokenType.AND_KEYWORD) {
            Position pos = par.getToken().getPosition();
            par.consumeToken();
            Condition rightLogicFactor = parseCheck(par);
            if (Objects.isNull(rightLogicFactor)) {
                throw new ParserException(pos, "No right side of or condition");
            }
            leftLogicFactor = new AndCondition(leftLogicFactor, rightLogicFactor, pos);
        }
        return leftLogicFactor;
    }

    /**
     * EBNF: check             = ["not"], ("(", condition, ")" | "checkCondition");
     * @param par
     * @return
     */
    private static Condition parseCheck(Parser par) throws AnalizerException {
        boolean isNegeted = false;
        if (par.getToken().getTokenType() == TokenType.NOT_KEYWORD) {
            isNegeted = true;
            par.consumeToken();
        }
        if (par.getToken().getTokenType() == TokenType.OPEN_SOFT_BRACKETS_OP) {
            par.consumeToken();
            Condition innerCondition = parseConditionalExpression(par);
            par.mustBe(TokenType.CLOSE_SOFT_BRACKETS_OP, new ParserException(par.getToken().getPosition(),
                    "No closing bracket in inner condition"));
            par.consumeToken();
            return innerCondition.setNegated(isNegeted);
        }
        return parseTestCondition(par).setNegated(isNegeted);
    }

    /**
     * EBNF: test                   = value, tester, value;
     *       tester                 = "<" | "<=" | ">" | ">=" | "==" | "!=";
     * @param par
     * @return
     */
    private static Condition parseTestCondition(Parser par) throws AnalizerException {
        Expression left = parseValue(par);
        Position pos = par.getToken().getPosition();
        if (left == null) {
            throw new ParserException(par.getToken().getPosition(), "No value in condition");
        }

        TokenType tester = par.getToken().getTokenType();
        if (!testers.contains(tester)) {
            if (par.getToken().getTokenType() == TokenType.CLOSE_SOFT_BRACKETS_OP
                    || par.getToken().getTokenType() == TokenType.AND_KEYWORD
                    || par.getToken().getTokenType() == TokenType.OR_KEYWORD) {
                return new ValueCondition(left, pos); // check for if (a) { ... }; if (a or b); if (a and b);
            }
            throw new ParserException(pos, "Invalid tester in condition");
        }
        par.consumeToken();
        Expression right = parseValue(par);
        if (right == null) {
            throw new ParserException(pos, "No value in condition");
        }
        return new TestCondition(left, right, tester, pos);
    }



}
