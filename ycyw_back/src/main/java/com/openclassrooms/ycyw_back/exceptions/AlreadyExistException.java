package com.openclassrooms.ycyw_back.exceptions;

public class AlreadyExistException extends Exception{
    public AlreadyExistException (String message) {
        super(message);
    }
}
