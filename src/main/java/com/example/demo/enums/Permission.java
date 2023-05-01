package com.example.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Permission{
    PROJECT_READ("project:read"),
    PROJECT_WRITE("project:write"),
    PROJECT_UPDATE("project:update"),
    PROJECT_DELETE("project:delete"),
    TASK_READ("task:read"),
    TASK_WRITE("task:write"),
    TASK_UPDATE("task:update"),
    TASK_DELETE("task:delete"),
    COMMENT_READ("comment:read"),
    COMMENT_WRITE("comment:write"),
    COMMENT_UPDATE("comment:update"),
    COMMENT_DELETE("comment:delete");

    private final String permission;
}
