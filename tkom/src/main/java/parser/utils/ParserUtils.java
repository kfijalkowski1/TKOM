package parser.utils;

import exceptions.AnalizerException;
import inputHandle.Position;
import lekser.TokenType;
import lekser.exceptions.LekserException;
import parser.Parser;
import parser.exceptions.ParserException;
import parser.parsableObjects.Statement;
import parser.parsableObjects.blocks.Block;
import parser.parsableObjects.expression.Expression;
import parser.parsableObjects.variables.VariableDeclaration;
import parser.parsableObjects.variables.ConstVariableDeclaration;
import parser.subParsers.ValueParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static parser.subParsers.BlockParser.parseBlock;
import static parser.subParsers.ExpressionParser.parseExpression;
import static parser.subParsers.variableParsers.VariableDeclarationParser.parseConstVariableDeclaration;
import static parser.subParsers.variableParsers.VariableDeclarationParser.parseVariableDeclaration;

public class ParserUtils {

    public final static List<TokenType> buildInTypes = Arrays.asList(TokenType.INT_KEYWORD, TokenType.FLT_KEYWORD,
            TokenType.STR_KEYWORD, TokenType.BOOL_KEYWORD);

    public final static List<TokenType> buildInValues = Arrays.asList(TokenType.INT_NUMBER, TokenType.FLT_NUMBER,
            TokenType.STRING_VALUE, TokenType.TRUE_KEYWORD, TokenType.FALSE_KEYWORD);

    public final static List<TokenType> staticReturnTypes = Arrays.asList(TokenType.INT_KEYWORD, TokenType.FLT_KEYWORD,
            TokenType.STR_KEYWORD, TokenType.BOOL_KEYWORD, TokenType.VOID_KEYWORD);

    public static void getListOfVarDeclLong(Parser par, Position pos, String typeName, List<ConstVariableDeclaration> variables) throws ParserException, LekserException {
        ConstVariableDeclaration var;
        while (par.getToken().getTokenType() == TokenType.COMA_OP) {
            par.mustBe(TokenType.COMA_OP, new ParserException(pos, "No ',' after %s variable".formatted(typeName)));
            par.consumeToken();
            var = parseConstVariableDeclaration(par);
            variables.add(var);
        }
    }

    public static void getListOfVarDecl(Parser par, Position pos, String typeName, List<VariableDeclaration> variables) throws ParserException, LekserException {
        VariableDeclaration var;
        while (par.getToken().getTokenType() == TokenType.COMA_OP) {
            par.mustBe(TokenType.COMA_OP, new ParserException(pos, "No ',' after %s variable".formatted(typeName)));
            par.consumeToken();
            var = parseVariableDeclaration(par);
            variables.add(var);
        }
    }

    public static List<Expression> parseComaExpressions(Parser par, String expressionName) throws AnalizerException {
        List<Expression> values = new ArrayList<>();
        Expression expression = parseExpression(par);
        if (expression == null) {
            return values;
        }
        values.add(expression);

        while (par.getToken().getTokenType() == TokenType.COMA_OP) {
            par.consumeToken();
            expression = parseExpression(par);
            if (expression == null) {
                throw new ParserException(par.getToken().getPosition(), "Missing value in %s".formatted(expressionName));
            }
            values.add(expression);
        }
        return values;
    }

    public static final List<TokenType> testers = List.of(
            TokenType.LESS_OP, TokenType.LESS_EQ_OP, TokenType.MORE_OP, TokenType.MORE_EQ_OP, TokenType.EQ_OP, TokenType.NOT_EQ_OP);

    public static List<Statement> getBlocks(Parser parser, Position pos, String blockType) throws AnalizerException {
        List<Statement> blocks = new ArrayList<>();
        Statement block = parseBlock(parser);
        if (Objects.isNull(block)) {
            throw new ParserException(pos, "No blocks in %s".formatted(blockType));
        }
        blocks.add(block);
        // {block}
        while (!Objects.isNull(block = parseBlock(parser))) {
            blocks.add(block);
        }
        return blocks;
    }

    public static Expression getExpValue(Parser par, String expressionType) throws AnalizerException {
        Expression value = parseExpression(par);
        if (value == null) {
            throw new ParserException(par.getToken().getPosition(), "Missing value in %s declaration".formatted(expressionType));
        }
        return value;
    }

}
