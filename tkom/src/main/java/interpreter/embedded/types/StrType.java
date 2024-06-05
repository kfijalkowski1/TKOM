package interpreter.embedded.types;

import lekser.TokenType;

public class StrType extends BuildInTypes{
    public static String getName() {
        return TokenType.STR_KEYWORD.toString();
    }
}
