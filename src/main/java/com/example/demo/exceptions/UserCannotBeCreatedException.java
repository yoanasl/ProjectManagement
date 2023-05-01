package com.example.demo.exceptions;

public class UserCannotBeCreatedException  extends RuntimeException{
    public UserCannotBeCreatedException(String email){
        super("User with" +email+"cannot be created");
    }
}
