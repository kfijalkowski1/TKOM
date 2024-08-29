package interpreter.embedded.types;

import lekser.TokenType;

public class FltType extends BuildInTypes{
    public static String getName() {
        return TokenType.FLT_KEYWORD.toString();
    }
}
