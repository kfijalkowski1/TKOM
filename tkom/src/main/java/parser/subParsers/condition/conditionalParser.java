package parser.subParsers.condition;

import exceptions.AnalizerException;
import inputHandle.Position;
import lekser.TokenType;
import parser.Parser;
import parser.exceptions.ParserException;
import parser.parsableObjects.Statement;
import parser.parsableObjects.conditional.*;
import parser.parsableObjects.expression.Expression;
import parser.parsableObjects.blocks.Block;
import parser.subParsers.ExpressionParser;

import java.util.ArrayList;
import java.util.List;

import static parser.utils.ParserUtils.getBlocks;


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
     * "while", condition, "{", block, {block} "}" ;;
     * @return
     */
    private static WhileConditional parseWhileConditional(Parser par) throws AnalizerException {
        if (par.getToken().getTokenType() != TokenType.WHILE_KEYWORD) {
            return null;
        }
        Position pos = par.getToken().getPosition();
        par.consumeToken();
        Expression condition = ExpressionParser.parseCondition(par);
        List<Statement> block = parseBlock(par, "while block");
        return new WhileConditional(condition, block, pos);
    }

    /**
     * "if", condition, "{", block, {block} "}",
     * {"elif", condition, "{", block, {block} "}"}
     * ["else", "{", block, {block} "}"];
     * @return
     */
    private static IfConditional parseIfConditional(Parser par) throws AnalizerException {
        if (par.getToken().getTokenType() != TokenType.IF_KEYWORD) {
            return null;
        }
        Position pos = par.getToken().getPosition();
        par.consumeToken();
        Expression condition = ExpressionParser.parseCondition(par);
        List<Statement> block = parseBlock(par, "if block");

        // elif conditions
        List<Conditional> subConditions = getElIfConditions(par, pos);

        // else conditions
        getElseConditions(par, subConditions, pos);
        return new IfConditional(condition, block, subConditions, pos);

    }

    private static void getElseConditions(Parser par, List<Conditional> subConditions, Position pos) throws AnalizerException {
        if(par.getToken().getTokenType() == TokenType.ELSE_KEYWORD) {
            par.consumeToken();
            List<Statement> elseBlock = parseBlock(par, "else block");
            subConditions.add(new ElseConditional(elseBlock, pos));
        }
    }

    private static List<Conditional> getElIfConditions(Parser par, Position pos) throws AnalizerException {
        List<Conditional> subConditions = new ArrayList<>();
        while(par.getToken().getTokenType() == TokenType.ELIF_KEYWORD) {
            par.consumeToken();
            Expression subCondition = ExpressionParser.parseCondition(par);
            List<Statement> subBlock = parseBlock(par, "elif block");
            subConditions.add(new ElIfConditional(subCondition, subBlock, pos));
        }
        return subConditions;
    }

    private static List<Statement> parseBlock(Parser par, String name) throws AnalizerException {
        par.mustBe(TokenType.OPEN_SHARP_BRACKETS_OP, new ParserException(par.getToken().getPosition(),
                "No opening bracket in condition block"));
        par.consumeToken();
        List<Statement> block = getBlocks(par, par.getToken().getPosition(),name);
        par.mustBe(TokenType.CLOSE_SHARP_BRACKETS_OP, new ParserException(par.getToken().getPosition(),
                "No closing bracket in condition block"));
        par.consumeToken();
        return block;
    }
}
