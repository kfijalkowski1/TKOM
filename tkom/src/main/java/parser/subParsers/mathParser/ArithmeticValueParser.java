package parser.subParsers.mathParser;

import exceptions.AnalizerException;
import inputHandle.Position;
import lekser.TokenType;
import lekser.exceptions.LekserException;
import parser.Parser;
import parser.exceptions.ParserException;
import parser.parsableObjects.expression.Expression;
import parser.parsableObjects.expression.arithmatic.results.*;
import parser.subParsers.ValueParser;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static parser.subParsers.ExpressionParser.parseExpression;

/**
 * EBNF:
 * arithmetic_result      = arithmetic_2tier, {("+" | "-",), arithmetic_2tier};
 * arithmetic_2tier       = arithmetic_3tier, {("*" | "/" | "%"), arithmetic_3tier};
 * arithmetic_3tier       = arithmetic_prod, {"**", arithmetic_prod}
 * arithmetic_prod        = value | "(" , arithmetic_result, ")";
 */
public class ArithmeticValueParser {

    private final static List<TokenType> arithmeticOperators = Arrays.asList(TokenType.PLUS_OP, TokenType.MINUS_OP);
    private final static List<TokenType> arithmetic2tierOperators = Arrays.asList(TokenType.MULTIPLE_OP, TokenType.DIVIDE_OP, TokenType.MODULO_OP);
    private final static List<TokenType> arithmetic3tierOperators = Arrays.asList(TokenType.POW_OP, TokenType.INCREMENT_OP);

    /**
     * EBNF: arithmetic_result      = arithmetic_2tier, {("+" | "-"), arithmetic_2tier};
     *
     * @param par
     * @return
     */
    public static Expression parseArithmeticValue(Parser par) throws AnalizerException {
        Expression leftResult = parse2tierArithmetic(par);
        while (arithmeticOperators.contains(par.getToken().getTokenType())) {
            Position pos = par.getToken().getPosition();
            TokenType operator = par.getToken().getTokenType();
            par.consumeToken();
            Expression rightResult = parse2tierArithmetic(par);
            if (rightResult == null) {
                throw new ParserException(pos, "No right side of arithmetic expression");
            }
            leftResult = generateResult(leftResult, rightResult, operator, pos);
        }
        return leftResult;
    }



    /**
     * EBNF: arithmetic_2tier       = arithmetic_3tier, {("*" | "/" | "%"), arithmetic_3tier};
     *
     * @param par
     * @return
     */
    private static Expression parse2tierArithmetic(Parser par) throws AnalizerException {
        Expression leftResult = parse3tierArithmetic(par);
        while (arithmetic2tierOperators.contains(par.getToken().getTokenType())) {
            Position pos = par.getToken().getPosition();
            TokenType operator = par.getToken().getTokenType();
            par.consumeToken();
            Expression rightResult = parse3tierArithmetic(par);
            if (rightResult == null) {
                throw new ParserException(pos, "No right side of arithmetic expression");
            }
            leftResult = generateResult(leftResult, rightResult, operator, pos);
        }
        return leftResult;
    }

    /**
     * EBNF: arithmetic_3tier       = arithmetic_prod, {"**", arithmetic_prod }
     *
     * @param par
     * @return
     */
    private static Expression parse3tierArithmetic(Parser par) throws AnalizerException {
        Expression leftResult = parseProdArithmetic(par);
        while (arithmetic3tierOperators.contains(par.getToken().getTokenType())) {
            Position pos = par.getToken().getPosition();
            TokenType operator = par.getToken().getTokenType();
            par.consumeToken();
            Expression rightResult = parseProdArithmetic(par);
            if (rightResult == null) {
                throw new ParserException(pos, "No right side of arithmetic expression");
            }
            leftResult = generateResult(leftResult, rightResult, operator, pos);
        }
        return leftResult;
    }

    /**
     * EBNF: arithmetic_prod  = ([-] | ["not"] ) literal | name_value | "(" expression ")";
     *
     * @param par
     * @return
     */
    private static Expression parseProdArithmetic(Parser par) throws AnalizerException {
        List<Boolean> isNegativeNegated = getNegativeNegated(par);
        boolean isNegative = isNegativeNegated.get(0);
        boolean isNegated = isNegativeNegated.get(1);

        if (par.getToken().getTokenType() == TokenType.OPEN_SOFT_BRACKETS_OP) {
            par.consumeToken();
            Expression result = parseExpression(par);
            if (Objects.isNull(result)) {
                throw new ParserException(par.getToken().getPosition(), "Expected expression after (");
            }
            par.mustBe(TokenType.CLOSE_SOFT_BRACKETS_OP, new ParserException(par.getToken().getPosition(), "No closing bracket in arithmetic expression"));
            par.consumeToken();
            result.setNegative(isNegative);
            result.setNegated(isNegated);
            return result;
        }
        Expression unpackedValue =  ValueParser.parseUnpackedValue(par);
        if (unpackedValue != null) {
            unpackedValue.setNegative(isNegative);
            unpackedValue.setNegated(isNegated);
            return unpackedValue;
        }
        return null;
    }

    private static List<Boolean> getNegativeNegated(Parser par) throws LekserException {
        boolean isNegative = false;
        boolean isNegated = false;
        for(int i = 0; i < 2; i++) {
            switch (par.getToken().getTokenType()) {
                case MINUS_OP -> {
                    isNegative = true;
                    par.consumeToken();
                }
                case NOT_KEYWORD -> {
                    isNegated = true;
                    par.consumeToken();
                }
            }
        }
        return List.of(isNegative, isNegated);
    }


    private static ArithmeticResult generateResult(Expression leftResult, Expression rightResult, TokenType operator, Position pos) throws ParserException {
        return switch (operator) {
            case PLUS_OP -> new AddResult(leftResult, rightResult, pos);
            case MINUS_OP -> new SubtractResult(leftResult, rightResult, pos);
            case MULTIPLE_OP -> new MultiplyResult(leftResult, rightResult, pos);
            case DIVIDE_OP -> new DivideResult(leftResult, rightResult, pos);
            case MODULO_OP -> new ModuloResult(leftResult, rightResult, pos);
            case POW_OP -> new PowResult(leftResult, rightResult, pos);
            default -> throw new ParserException(pos, "Unknown operator in arithmetic expression");
        };
    }

}
