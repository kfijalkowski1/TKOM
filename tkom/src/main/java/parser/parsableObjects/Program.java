package parser.parsableObjects;

import java.util.List;

public class Program {
    public List<Statement> getStatements() {
        return statements;
    }

    private final List<Statement> statements;

    public Program(List<Statement> statementList) {
        statements = statementList;
    }
}
