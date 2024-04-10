package interpreter;


import inputHandle.FileSource;
import inputHandle.Source;
import lekser.Token;
import lekser.TokenBuilder;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append('a');
        sb.append('\n');
        sb.append('a');
        System.out.println(sb);

    }
}