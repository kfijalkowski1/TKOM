package interpreter;


import inputHandle.FileSource;
import inputHandle.Source;
import lekser.Token;
import lekser.TokenBuilder;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
        Source src = new FileSource("test.sp");
        TokenBuilder tb = new TokenBuilder(src);
        Token token = tb.getNextToken();
        System.out.println("type: " + token.getTokenType() + " value: " + token.getValue().toString());;
         token = tb.getNextToken();
        System.out.println("type: " + token.getTokenType() + " value: ");
         token = tb.getNextToken();
        System.out.println("type: " + token.getTokenType() + " value: ");

    }
}