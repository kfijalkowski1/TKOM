package parser.subParsers;

import exceptions.AnalizerException;
import inputHandle.Position;
import lekser.TokenType;
import parser.Parser;
import parser.exceptions.ParserException;
import parser.parsableObjects.expresions.Expression;
import parser.parsableObjects.match.Match;
import parser.parsableObjects.match.MatchCase;

import java.util.ArrayList;
import java.util.List;

import static parser.subParsers.ExpresionParser.parseExpresion;

public class matchParser {
    /**
     * EBNF: matchExp = "match", name, "{", matchCase, {matchCase}, "}"
     *
     * @return Match object.
     */
    public static Match parseMatch(Parser par) throws AnalizerException {
        if (par.getToken().getTokenType() != TokenType.MATCH_KEYWORD) {
            return null;
        }
        Position pos = par.getToken().getPosition();
        par.consumeToken();

        // name
        String name = (String) par.mustBe(TokenType.NAME, new ParserException(pos, "No name in match expresion"), true);
        par.consumeToken();

        // {
        par.mustBe(TokenType.OPEN_SHARP_BRACKETS_OP, new ParserException(pos, "Missing '{' in match expression"));
        par.consumeToken();

        // matchCase
        List<MatchCase> matchCases = new ArrayList<>();

        MatchCase matchCase = parseMatchCase(par);
        if (matchCase == null) {
            throw new ParserException(par.getToken().getPosition(), "Missing match case in match expression");
        }

        // {matchCase}
        while(matchCase != null) {
            matchCases.add(matchCase);
            matchCase = parseMatchCase(par);
        }

        // }
        par.mustBe(TokenType.CLOSE_SHARP_BRACKETS_OP, new ParserException(par.getToken().getPosition(), "Missing '}' in match expression"));

        return new Match(name, matchCases, pos);

    }


    /**
     * EBNF: matchCase = customTypeName, ".", name, "(", name, ")", "{", expresion, {expresion}, "}";
     *
     * @return MatchCase object.
     */
    private static MatchCase parseMatchCase(Parser par) throws AnalizerException {
        if (par.getToken().getTokenType() != TokenType.NAME) {
            return null;
        }
        Position pos = par.getToken().getPosition();
        String taggedUnionName = (String) par.mustBe(TokenType.NAME, new ParserException(pos, "No tagged union name in match case"), true);
        par.consumeToken();

        // .
        par.mustBe(TokenType.DOT_OP, new ParserException(par.getToken().getPosition(), "Missing '.' in match case"));
        par.consumeToken();

        // name
        String unionTypeName = (String) par.mustBe(TokenType.NAME, new ParserException(par.getToken().getPosition(), "No union type name in match case"), true);
        par.consumeToken();

        // (
        par.mustBe(TokenType.OPEN_SOFT_BRACKETS_OP, new ParserException(par.getToken().getPosition(), "Missing '(' in match case"));
        par.consumeToken();

        // name
        String variableName = (String) par.mustBe(TokenType.NAME, new ParserException(par.getToken().getPosition(), "No variable name in match case"), true);
        par.consumeToken();

        // )
        par.mustBe(TokenType.CLOSE_SOFT_BRACKETS_OP, new ParserException(par.getToken().getPosition(), "Missing ')' in match case"));
        par.consumeToken();

        // {
        par.mustBe(TokenType.OPEN_SHARP_BRACKETS_OP, new ParserException(par.getToken().getPosition(), "Missing '{' in match case"));
        par.consumeToken();

        // expresion
        List<Expression> expressions = new ArrayList<>();
        Expression expression = parseExpresion(par);
        if (expression == null) {
            throw new ParserException(par.getToken().getPosition(), "Missing expresion in match case");
        }
        // {expresion}
        while (expression != null) {
            expressions.add(expression);
            expression = parseExpresion(par);
        }

        // }
        par.mustBe(TokenType.CLOSE_SHARP_BRACKETS_OP, new ParserException(par.getToken().getPosition(), "Missing '}' in match case"));
        par.consumeToken();

        return new MatchCase(taggedUnionName, unionTypeName, variableName, expressions, pos);
    }
}
