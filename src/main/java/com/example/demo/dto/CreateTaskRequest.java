package com.example.demo.dto;

import com.example.demo.entity.Project;
import com.example.demo.entity.Status;
import com.example.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateTaskRequest {

    private String name;
    private String description;
    private int priority;
    private int status;

    private String startDate;
    private String endDate;
    private int userId;

}
