package net.victorbetoni.squadster.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String msg){
        super(msg);
    }
}
