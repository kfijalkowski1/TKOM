package parser.subParsers;

import exceptions.AnalizerException;
import inputHandle.Position;
import lekser.TokenType;
import lekser.exceptions.LekserException;
import parser.Parser;
import parser.exceptions.ParserException;
import parser.parsableObjects.FunctionDeclaration;
import parser.parsableObjects.expresions.Expression;
import parser.parsableObjects.variables.VariableDeclaration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static parser.subParsers.ExpresionParser.parseExpresion;
import static parser.subParsers.variableParsers.VariableDeclarationParser.parseVariableDeclaration;
import static parser.utils.ParserUtils.getListOfVarDecl;
import static parser.utils.ParserUtils.staticReturnTypes;

public class FunctionDeclarationParser {

    /**
     * EBNF: = "fun", name, "(", [var_declar, {",", var_declar}], ")", "-", ">", (type | "void"), "{", expresion, {expresion} "}";
     *
     * @param parser
     * @return new FunctionDeclararion
     */
    public static parser.parsableObjects.FunctionDeclaration parseFunctionDeclar(Parser parser) throws AnalizerException {
        // fun
        if (parser.getToken().getTokenType() != TokenType.FUN_KEYWORD) {
            return null;
        }
        Position pos = parser.getToken().getPosition();
        parser.consumeToken();

        // name
        String name = getName(parser, pos);

        // (
        parser.mustBe(TokenType.OPEN_SOFT_BRACKETS_OP,
                new ParserException(pos, "No '(' after function name"));
        parser.consumeToken();

        // [var_declar, {",", var_declar}]
        List<VariableDeclaration> variables = getVariableDeclarations(parser, pos);

        // )
        parser.mustBe(TokenType.CLOSE_SOFT_BRACKETS_OP,
                new ParserException(pos, "No ')' after function parameters"));
        parser.consumeToken();

        // ->
        parser.mustBe(TokenType.RETURN_TYPE_OP,
                new ParserException(pos, "No '->' after function parameters"));
        parser.consumeToken();

        // (type | "void")
        String returnType = getReturnType(parser, pos);

        // {
        parser.mustBe(TokenType.OPEN_SHARP_BRACKETS_OP,
                new ParserException(pos, "No '{' after function return type"));
        parser.consumeToken();

        // expression, {expresion}
        List<Expression> expressions = getExpressions(parser, pos, name);

        // }
        parser.mustBe(TokenType.CLOSE_SHARP_BRACKETS_OP,
                new ParserException(pos, "No '}' after function expressions"));
        parser.consumeToken();

        return new FunctionDeclaration(name, variables, expressions, returnType, pos);

    }

    private static List<Expression> getExpressions(Parser parser, Position pos, String name) throws AnalizerException {
        List<Expression> expressions = new ArrayList<>();
        Expression exp = parseExpresion(parser);
        if (Objects.isNull(exp)) {
            throw new ParserException(pos, "No expressions in function %s".formatted(name));
        }
        expressions.add(exp);
        // {expression}
        while (!Objects.isNull(exp = parseExpresion(parser))) {
            expressions.add(exp);
        }
        return expressions;
    }

    private static String getReturnType(Parser parser, Position pos) throws LekserException, ParserException {
        String returnType = "";
        if (staticReturnTypes.contains(parser.getToken().getTokenType())) {
            returnType = parser.getToken().getTokenType().toString();
            parser.consumeToken();
        } else {
            returnType = (String) parser.mustBe(TokenType.NAME,
                    new ParserException(pos, "No return type in function declaration"), true);
            parser.consumeToken();
        }
        return returnType;
    }

    private static List<VariableDeclaration> getVariableDeclarations(Parser parser, Position pos) throws LekserException, ParserException {
        List<VariableDeclaration> variables = new ArrayList<>();
        VariableDeclaration var = parseVariableDeclaration(parser);
        if (!Objects.isNull(var)) {
            variables.add(var);
            // {"," , var_declar}
            getListOfVarDecl(parser, pos, "function", variables); // adds variables to list
        }
        return variables;
    }

    private static String getName(Parser parser, Position pos) throws ParserException, LekserException {
        String name = (String) parser.mustBe(TokenType.NAME,
                new ParserException(pos, "No name in variable declaration"), true);
        parser.consumeToken();
        return name;
    }
}
