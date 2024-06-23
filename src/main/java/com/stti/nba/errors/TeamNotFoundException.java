package com.stti.nba.errors;

public class TeamNotFoundException extends RuntimeException {

    public TeamNotFoundException(String message){
        super(message);
    }
}
