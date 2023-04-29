package com.example.demo.exceptions;

public class ProjectNotFoundException extends RuntimeException{
    public ProjectNotFoundException(Long projectId){
        super("Project not found with id: " + projectId);
    }
}
