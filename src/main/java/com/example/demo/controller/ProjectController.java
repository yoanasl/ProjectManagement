package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.entity.User;
import com.example.demo.service.ProjectServiceImpl;
import com.example.demo.entity.Project;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/projects")
public class ProjectController{

    private final ProjectServiceImpl projectService;
    private final UserService userService;

    public ProjectController(ProjectServiceImpl projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public String getProjectsForUser(@PathVariable("id") Long userId, Model model) {

        User user = userService.findUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("projects", userService.getProjects(user.getEmail()));

        return "welcomeProjectsView";
    }

    @GetMapping("/get/{id}")
    public String getProject(@PathVariable("id") Long id, Model model){
        Project project = projectService.getProject(id);

        model.addAttribute("project", project);

        model.addAttribute("toDoTasks", projectService.getTasksByStatusId(project, 1));
        model.addAttribute("progressTasks", projectService.getTasksByStatusId(project, 2));
        model.addAttribute("doneTasks", projectService.getTasksByStatusId(project, 3));
        return "project";
    }


    @GetMapping("/addProjectForm/{id}")
    public String showAddProjectForm(@PathVariable("id") Long userId, Model model) {

        model.addAttribute("users", userService.getAllUsers());

        model.addAttribute("userId", userId);
        model.addAttribute("createProject", new CreateProjectRequest());

        return "addProject";
    }

    @PostMapping("/add/{id}")
    public String createProject(@ModelAttribute("createProject") CreateProjectRequest createProjectRequest,
                                @PathVariable("id") Long userId){

        Project createdProject = projectService.createProject(createProjectRequest);

        return "redirect:/projects/get/" + createdProject.getId();
    }

    @GetMapping("/editForm/{id}")
    public String showEditProjectForm(@PathVariable("id") Long id, Model model) {
        // retrieve the task with the given id from the database

        Project project = projectService.getProject(id);
        UpdateProjectRequest updateProject = new UpdateProjectRequest();
        updateProject.setName(project.getName());
        updateProject.setDescription(project.getDescription());
        updateProject.setStartDate(project.getStartDate().toString());
        updateProject.setEndDate(project.getEndDate().toString());

        // add the task to the model so it can be displayed in the edit form
        model.addAttribute("projectId", id);
        model.addAttribute("updateProject", updateProject);

        return "editProject"; // return the name of the Thymeleaf template for rendering the edit form
    }

    @PostMapping("/edit/{id}")
    public String updateProject(@PathVariable("id") Long id,
                                @ModelAttribute("updateProject") UpdateProjectRequest updateProject){

        projectService.updateProject(id, updateProject);

        return "redirect:/projects/get/" + id;
    }

    @GetMapping("/{id}/team")
    public String getTeamForProject(@PathVariable("id") Long projectId, Model model) {

        model.addAttribute("project", projectService.getProject(projectId));
        model.addAttribute("team", projectService.getUsersByProjectId(projectId));
        model.addAttribute("availableUsers", projectService.getUsersThatAreNotTeamMembers(projectId));

        return "projectTeam";
    }

    /*@PostMapping("/{id}/team/add")

    public String addUsersToProject(@PathVariable("id") Long projectId,
                                    @ModelAttribute("newTeamMembers") List<String> newTeamMembers) {

        projectService.addUsersToProject(projectId, newTeamMembers);
        return "redirect:/projects/" + projectId + "/team";

    }*/


    @RequestMapping(value = "/deleteProject/{id}", method = RequestMethod.POST)
    public String deleteProject(@PathVariable("id") Long id, Model model){
        projectService.deleteProject(id);
        model.addAttribute("message", "Project deleted successfully.");
        return "redirect:/projects";
    }

    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    public String getAllProjects(Model model){
        List<ProjectDTO> projects = projectService.findAllProjects();
        model.addAttribute("projects", projects);
        return "projects";
    }




}
