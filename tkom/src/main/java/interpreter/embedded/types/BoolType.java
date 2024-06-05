package interpreter.embedded.types;

import lekser.TokenType;

public class BoolType extends BuildInTypes{
    public static String getName() {
        return TokenType.BOOL_KEYWORD.toString();
    }
}
