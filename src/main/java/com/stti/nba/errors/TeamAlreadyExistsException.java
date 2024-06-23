package com.stti.nba.errors;

public class TeamAlreadyExistsException extends RuntimeException {

    public TeamAlreadyExistsException(String message){
        super(message);
    }

}
