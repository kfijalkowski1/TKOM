package lekser.exceptions;


import exceptions.AnalizerException;

public class NoValueToken extends AnalizerException {

    public NoValueToken(String txt) {
        super(txt);
    }
}
