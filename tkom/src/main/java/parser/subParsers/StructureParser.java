package parser.subParsers;

import inputHandle.Position;
import lekser.TokenType;
import lekser.exceptions.LekserException;
import parser.Parser;
import parser.exceptions.ParserException;
import parser.parsableObjects.structures.Struct;
import parser.parsableObjects.structures.Structure;
import parser.parsableObjects.structures.TaggedUnion;
import parser.parsableObjects.variables.ConstVariableDeclaration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static parser.subParsers.variableParsers.VariableDeclarationParser.parseConstVariableDeclaration;
import static parser.utils.ParserUtils.getListOfVarDeclLong;

public class StructureParser {
    /**
     * EBNF: ("struct" | "TaggedUnion"), name, "{, var_declar_l, {var_declar_l}, "}";;
     *
     * @return Statement with on of the types: Structure, Function, Expresion.
     */
    public static Structure parseStructure(Parser par) throws LekserException, ParserException {
        if (par.getToken().getTokenType() != TokenType.TaggedUnion_KEYWORD
                && par.getToken().getTokenType() != TokenType.STRUCT_KEYWORD) {
            return null;
        }
        Position pos = par.getToken().getPosition();
        String typeName = par.getToken().getTokenType().toString();
        par.consumeToken();
        // name
        String name = (String) par.mustBe(TokenType.NAME,
                new ParserException(pos, "No identifier when creating %s".formatted(typeName)), true);
        par.consumeToken();
        // {
        par.mustBe(TokenType.OPEN_SHARP_BRACKETS_OP,
                new ParserException(pos, "No '{' after %s name".formatted(typeName)));
        par.consumeToken();

        // var_declar_l
        List<ConstVariableDeclaration> variables = new ArrayList<>();
        ConstVariableDeclaration var = parseConstVariableDeclaration(par);
        if (Objects.isNull(var)) {
            throw new ParserException(pos, "No variables in %s %s".formatted(typeName, name));
        }
        variables.add(var);

        // {"," , var_declar_l}
        getListOfVarDeclLong(par, pos, typeName, variables);
        // }
        par.mustBe(TokenType.CLOSE_SHARP_BRACKETS_OP,
                new ParserException(pos, "No '}' after %s variables or no , between them".formatted(typeName)));
        par.consumeToken();

        if (typeName.equals(TokenType.STRUCT_KEYWORD.toString())) {
            return new Struct(name, variables, pos);
        } else {
            return new TaggedUnion(name, variables, pos);
        }

    }
}
