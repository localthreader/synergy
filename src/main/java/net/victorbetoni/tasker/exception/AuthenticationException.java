package net.victorbetoni.tasker.exception;

import javafx.application.Application;
import javafx.stage.Stage;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String msg){
        super(msg);
    }
}
