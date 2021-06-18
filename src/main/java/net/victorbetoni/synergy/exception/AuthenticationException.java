package net.victorbetoni.synergy.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String msg){
        super(msg);
    }
}
