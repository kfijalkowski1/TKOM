package parser.subParsers.variableParsers;

import inputHandle.Position;
import lekser.TokenType;
import lekser.exceptions.LekserException;
import parser.Parser;
import parser.exceptions.ParserException;
import parser.parsableObjects.variables.ConstVariableDeclaration;
import parser.parsableObjects.variables.VariableDeclaration;

import static parser.utils.ParserUtils.buildInTypes;

public class VariableDeclarationParser {

    /**
     * EBNF: ["const"], var_declar;
     *
     * @param parser
     * @return new VariableDeclarationLong
     */
    public static ConstVariableDeclaration parseConstVariableDeclaration(Parser parser) throws LekserException, ParserException {
        Boolean isConst = false;
        Position pos = parser.getToken().getPosition();
        if (parser.getToken().getTokenType() == TokenType.CONST_KEYWORD) {
            isConst = true;
            parser.consumeToken();
        }
        VariableDeclaration var = parseVariableDeclaration(parser);
        if (var == null ) {
            if (isConst) {
                throw new ParserException(pos, "No variable declaration after 'const' keyword");
            }
            return null;
        }
        return new ConstVariableDeclaration(var.name, var.type.getName(), isConst, pos, var.type.isReference());
    }

    /**
     * EBNF: var_declar = type, name;
     * */
    public static VariableDeclaration parseVariableDeclaration(Parser parser) throws LekserException, ParserException {
        if (!buildInTypes.contains(parser.getToken().getTokenType()) && parser.getToken().getTokenType() != TokenType.NAME){
            return null;
        }
        Position pos = parser.getToken().getPosition();

        // type
        String type;
        if (parser.getToken().getTokenType() == TokenType.NAME) {
            type = (String) parser.mustBe(TokenType.NAME,
                    new ParserException(pos, "No type in variable declaration"), true);
        } else {
            type = parser.getToken().getTokenType().toString();
        }
        parser.consumeToken();

        // name
        String name = (String) parser.mustBe(TokenType.NAME,
                new ParserException(pos, "No name in variable declaration"), true);
        parser.consumeToken();

        return new VariableDeclaration(name, type, pos);
    }
}
