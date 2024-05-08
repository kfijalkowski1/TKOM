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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static parser.subParsers.variableParsers.ValueParser.parseValue;
import static parser.subParsers.variableParsers.VariableDeclarationParser.parseConstVariableDeclaration;
import static parser.subParsers.variableParsers.VariableDeclarationParser.parseVariableDeclaration;

public class ParserUtils {

    public final static List<TokenType> buildInTypes = Arrays.asList(TokenType.INT_KEYWORD, TokenType.FLT_KEYWORD,
            TokenType.STR_KEYWORD, TokenType.BOOL_KEYWORD);

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

}
