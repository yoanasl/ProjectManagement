package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateProjectRequest {

    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private List<Integer> teamMembers;
}
