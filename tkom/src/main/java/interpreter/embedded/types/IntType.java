package interpreter.embedded.types;

import lekser.TokenType;

public class IntType extends BuildInTypes{
    public static String getName() {
        return TokenType.INT_KEYWORD.toString();
    }
}
