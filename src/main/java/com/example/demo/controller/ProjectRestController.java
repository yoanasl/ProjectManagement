package com.example.demo.controller;

import com.example.demo.config.CustomLogger;
import com.example.demo.dto.AddUsersToProjectRequest;
import com.example.demo.service.ProjectServiceImpl;
import com.example.demo.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectRestController {

    private final ProjectServiceImpl projectService;
    private final UserServiceImpl userService;

        @PostMapping("/{id}/team/add")
        public String addUsersToProject(@PathVariable("id") Long projectId,
                                        @RequestBody AddUsersToProjectRequest newTeamMembers) {

            projectService.addUsersToProject(projectId, newTeamMembers.getNewTeamMembers());
            CustomLogger.logInfo(userService.getCurrentUserFromSession().getEmail()
                    + ": User added team members to project with ID " + projectId);

            return "redirect:/projects/" + projectId + "/team";

        }
}
