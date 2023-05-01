package com.example.demo.controller;

import com.example.demo.dto.AddUsersToProjectRequest;
import com.example.demo.service.ProjectServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectRestController {

    private final ProjectServiceImpl projectService;

    @PostMapping("/{id}/team/add")
    public String addUsersToProject(@PathVariable("id") Long projectId,
                                    @RequestBody AddUsersToProjectRequest newTeamMembers) {

        projectService.addUsersToProject(projectId, newTeamMembers.getNewTeamMembers());
        return "redirect:/projects/" + projectId + "/team";

    }
}
