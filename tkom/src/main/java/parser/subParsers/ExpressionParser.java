package parser.subParsers;

import exceptions.AnalizerException;
import inputHandle.Position;
import lekser.TokenType;
import parser.Parser;
import parser.exceptions.ParserException;
import parser.parsableObjects.expression.AndExpression;
import parser.parsableObjects.expression.Expression;
import parser.parsableObjects.expression.OrExpression;
import parser.parsableObjects.expression.LogicalExpression;


import java.util.Objects;

import static parser.subParsers.mathParser.ArithmeticValueParser.parseArithmeticValue;
import static parser.utils.ParserUtils.testers;

public class ExpressionParser {

    /**
     * EBNF: condition              = "(", expression, ")";;
     * doesn't check for  ( )
     * @return Condition object.
     */
    public static Expression parseCondition(Parser par) throws AnalizerException {
        par.mustBe(TokenType.OPEN_SOFT_BRACKETS_OP, new ParserException(par.getToken().getPosition(), "No opening bracket in condition"));
        par.consumeToken();
        Expression condition = parseExpression(par);
        par.mustBe(TokenType.CLOSE_SOFT_BRACKETS_OP, new ParserException(par.getToken().getPosition(), "No closing bracket in condition"));
        par.consumeToken();
        return condition;
    }

    /**
     * EBNF: expression              = and_condition, {"or", and_condition};
     * doesn't check for  ( )
     * @return Condition object.
     */
    public static Expression parseExpression(Parser par) throws AnalizerException {
        Expression leftLogicFactor = parseAndCondition(par);
        while (par.getToken().getTokenType() == TokenType.OR_KEYWORD) {
            Position pos = par.getToken().getPosition();
            par.consumeToken();
            Expression rightLogicFactor = parseAndCondition(par);
            if (Objects.isNull(rightLogicFactor)) {
                throw new ParserException(pos, "No right side of or condition");
            }
            leftLogicFactor = new OrExpression(leftLogicFactor, rightLogicFactor, pos);
        }
        return leftLogicFactor;
    }

    /**
     * EBNF: and_condition          = logical_exp, {"and", logical_exp};
     * @return Condition object.
     *
     * @param par
     * @return
     */
    private static Expression parseAndCondition(Parser par) throws AnalizerException {
        Expression leftLogicFactor = parseLogicalExpression(par);
        while (par.getToken().getTokenType() == TokenType.AND_KEYWORD) {
            Position pos = par.getToken().getPosition();
            par.consumeToken();
            Expression rightLogicFactor = parseLogicalExpression(par);
            if (Objects.isNull(rightLogicFactor)) {
                throw new ParserException(pos, "No right side of or condition");
            }
            leftLogicFactor = new AndExpression(leftLogicFactor, rightLogicFactor, pos);
        }
        return leftLogicFactor;
    }

    /**
     * EBNF: arithmetic_result , [tester,  arithmetic_result ]
     * @param par
     * @return
     */
    private static Expression parseLogicalExpression(Parser par) throws AnalizerException {
        Position pos = par.getToken().getPosition();
        Expression leftArithmRes = parseArithmeticValue(par);
        TokenType tester = par.getToken().getTokenType();
        if (testers.contains(tester)) {
            par.consumeToken();
            Expression right = parseArithmeticValue(par);
            if (right == null) {
                throw new ParserException(pos, "No value in condition");
            }
            return new LogicalExpression(leftArithmRes, right, tester, pos);
        }
        return leftArithmRes;
    }

}
