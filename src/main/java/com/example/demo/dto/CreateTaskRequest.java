package com.example.demo.dto;

import com.example.demo.entity.Project;
import com.example.demo.entity.User;
import com.example.demo.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateTaskRequest {

    private String name;
    private String description;
    private int priority;
    private Status status;

    private String startDate;
    private String endDate;

    private Project project;
    private User user;
}
