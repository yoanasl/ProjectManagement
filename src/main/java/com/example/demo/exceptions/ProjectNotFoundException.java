package com.example.demo.exceptions;

public class ProjectNotFoundException extends RuntimeException{
    public ProjectNotFoundException(String s){
        super(s);
    }
}
