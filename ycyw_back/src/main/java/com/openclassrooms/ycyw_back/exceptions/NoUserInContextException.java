package com.openclassrooms.ycyw_back.exceptions;

public class NoUserInContextException extends Exception{
    public NoUserInContextException(String message) {
        super(message);
    }
}
