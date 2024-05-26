package parser;

import exceptions.AnalizerException;
import inputHandle.Position;
import lekser.Token;
import lekser.TokenBuilder;
import lekser.TokenType;
import lekser.exceptions.LekserException;
import lekser.exceptions.NoValueToken;
import parser.exceptions.ParserException;
import parser.parsableObjects.Program;
import parser.parsableObjects.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static parser.subParsers.StatementParser.parseStatement;

public class Parser {
    private Token token;
    TokenBuilder tb;

    Parser(TokenBuilder tb) throws LekserException {
        this.tb = tb;
        consumeToken();
    }

    public Token getToken() {
        return token;
    }

    public void consumeToken() throws LekserException {
        token = tb.getNextToken();
    }

   /**
     * EBNF: program = statment, {statment} ;
     *
     * @return Program object that contains the list of parsed statements.
    */
    public Program parseProgram() throws AnalizerException {
        List<Statement> statementList = new ArrayList<>();
        Statement statement;
        while (!Objects.isNull(statement = parseStatement(this))) {
            statementList.add(statement);
        }
        if (statementList.isEmpty()) {
            throw new ParserException(new Position(), "Empty program or no correct statement found.");
        }

        return new Program(statementList);
    }

    public void mustBe(TokenType tokenType, ParserException ex) throws ParserException {
        if (this.token.getTokenType() != tokenType) {
            throw ex;
        }

    }

    public Object mustBe(TokenType tokenType, ParserException ex, Boolean getValue) throws ParserException {
        mustBe(tokenType, ex);
        if (getValue) {
            try {
                return token.getValue();
            } catch (NoValueToken e) {
                throw ex;
            }
        }
        return null;
    }

}
