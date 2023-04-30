package com.example.demo.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateViewModel {

    private String name;
    private String description;
    //private int priority;
    private int statusId;

    private String startDate;
    private String endDate;

}
