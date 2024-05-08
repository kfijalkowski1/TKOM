package parser.subParsers.condition;

import exceptions.AnalizerException;
import inputHandle.Position;
import lekser.TokenType;
import parser.Parser;
import parser.exceptions.ParserException;
import parser.parsableObjects.conditional.*;
import parser.parsableObjects.conditional.condition.Condition;
import parser.parsableObjects.expresions.Expression;

import java.util.ArrayList;
import java.util.List;

import static parser.utils.ParserUtils.getExpressions;


public class conditionalParser {

    /**
     * EBNF: if_condition
     *     | loop_condition
     *
     * Parse a conditional statement
     * @return Conditional
     */
    public static Conditional parseCondition(Parser par) throws AnalizerException {
        IfConditional ifCondition = parseIfConditional(par);
        if (ifCondition != null) {
            return ifCondition;
        }
        WhileConditional whileConditional = parseWhileConditional(par);
        if (whileConditional != null) {
            return whileConditional;
        }
        return null;
    }


    /**
     * "while", condition, "{", expresion, {(expresion} "}" ;;
     * @return
     */
    private static WhileConditional parseWhileConditional(Parser par) throws AnalizerException {
        if (par.getToken().getTokenType() != TokenType.WHILE_KEYWORD) {
            return null;
        }
        Position pos = par.getToken().getPosition();
        par.consumeToken();
        Condition condition = ConditionParser.parseCondition(par);
        List<Expression> block = parseBlock(par, "while block");
        return new WhileConditional(condition, block, pos);
    }

    /**
     * "if", condition, "{", expresion, {expresion} "}",
     * {"elif", condition, "{", expresion, {expresion} "}"}
     * ["else", "{", expresion, {expresion} "}"];
     * @return
     */
    private static IfConditional parseIfConditional(Parser par) throws AnalizerException {
        if (par.getToken().getTokenType() != TokenType.IF_KEYWORD) {
            return null;
        }
        Position pos = par.getToken().getPosition();
        par.consumeToken();
        Condition condition = ConditionParser.parseCondition(par);
        List<Expression> block = parseBlock(par, "if block");

        List<Conditional> subConditions = new ArrayList<>();
        while(par.getToken().getTokenType() == TokenType.ELIF_KEYWORD) {
            par.consumeToken();
            Condition subCondition = ConditionParser.parseCondition(par);
            List<Expression> subBlock = parseBlock(par, "elif block");
            subConditions.add(new ElIfConditional(subCondition, subBlock, pos));
        }

        if(par.getToken().getTokenType() == TokenType.ELSE_KEYWORD) {
            par.consumeToken();
            List<Expression> elseBlock = parseBlock(par, "else block");
            subConditions.add(new ElseConditional(elseBlock, pos));
        }
        return new IfConditional(condition, block, subConditions, pos);

    }

    private static List<Expression> parseBlock(Parser par, String name) throws AnalizerException {
        par.mustBe(TokenType.OPEN_SHARP_BRACKETS_OP, new ParserException(par.getToken().getPosition(),
                "No opening bracket in condition block"));
        par.consumeToken();
        List<Expression> block = getExpressions(par, par.getToken().getPosition(),name);
        par.mustBe(TokenType.CLOSE_SHARP_BRACKETS_OP, new ParserException(par.getToken().getPosition(),
                "No closing bracket in condition block"));
        par.consumeToken();
        return block;
    }
}
