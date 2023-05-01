package com.example.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Permission{
    PROJECT_READ("project:read"),
    PROJECT_WRITE("project:write"),
    PROJECT_UPDATE("project:update"),
    PROJECT_DELETE("project:delete");

    private final String permission;
}
