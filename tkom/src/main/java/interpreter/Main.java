package interpreter;


import inputHandle.FileSource;
import inputHandle.Source;
import inputHandle.StringSource;
import lekser.Token;
import lekser.TokenBuilder;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
        Source sr = new StringSource("int a = 5");
        TokenBuilder tb = new TokenBuilder(sr);
        System.out.println(tb.getNextToken().getTokenType());

    }
}