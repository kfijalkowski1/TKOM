package parser.utils;

import exceptions.AnalizerException;
import inputHandle.Position;
import lekser.TokenType;
import lekser.exceptions.LekserException;
import parser.Parser;
import parser.exceptions.ParserException;
import parser.parsableObjects.expresions.Expression;
import parser.parsableObjects.variables.VariableDeclaration;
import parser.parsableObjects.variables.ConstVariableDeclaration;
import parser.subParsers.ValueParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static parser.subParsers.ExpresionParser.parseExpresion;
import static parser.subParsers.ValueParser.parseValue;
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

    public static List<Expression> parseComaValues(Parser par, String expressionName) throws AnalizerException {
        List<Expression> values = new ArrayList<>();
        Expression value = parseValue(par);
        if (value == null) {
            return values;
        }
        values.add(value);

        while (par.getToken().getTokenType() == TokenType.COMA_OP) {
            par.consumeToken();
            value = parseValue(par);
            if (value == null) {
                throw new ParserException(par.getToken().getPosition(), "Missing value in %s".formatted(expressionName));
            }
            values.add(value);
        }
        return values;
    }

    public static final List<TokenType> testers = List.of(
            TokenType.LESS_OP, TokenType.LESS_EQ_OP, TokenType.MORE_OP, TokenType.MORE_EQ_OP, TokenType.EQ_OP, TokenType.NOT_EQ_OP);

    public static List<Expression> getExpressions(Parser parser, Position pos, String expressionType) throws AnalizerException {
        List<Expression> expressions = new ArrayList<>();
        Expression exp = parseExpresion(parser);
        if (Objects.isNull(exp)) {
            throw new ParserException(pos, "No expressions in %s".formatted(expressionType));
        }
        expressions.add(exp);
        // {expression}
        while (!Objects.isNull(exp = parseExpresion(parser))) {
            expressions.add(exp);
        }
        return expressions;
    }

    public static Expression getExpValue(Parser par, String expressionType) throws AnalizerException {
        Expression value = ValueParser.parseValue(par);
        if (value == null) {
            throw new ParserException(par.getToken().getPosition(), "Missing value in %s declaration".formatted(expressionType));
        }
        return value;
    }

}
