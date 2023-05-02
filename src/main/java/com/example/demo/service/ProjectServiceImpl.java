package com.example.demo.service;

import com.example.demo.dto.CreateProjectRequest;
import com.example.demo.dto.ProjectDTO;
import com.example.demo.dto.UpdateProjectRequest;
import com.example.demo.entity.Project;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.exceptions.ProjectNotFoundException;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.TeamRepository;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
public class ProjectServiceImpl{

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final ModelMapper modelMapper;
    private final TaskRepository taskRepository;

    private final TeamRepository teamRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository, UserServiceImpl userService, ModelMapper modelMapper, TaskRepository taskRepository, TeamRepository teamRepository){
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.taskRepository = taskRepository;
        this.teamRepository = teamRepository;
    }


    public Project getProject(Long id){
        Optional<Project> project = projectRepository.findById(id);
        if(project.isEmpty()){
            throw new ProjectNotFoundException(id);
        }
        return project.get();
    }

    public Project createProject(CreateProjectRequest createProject){
        if(createProject.getName() == null || createProject.getName().isEmpty()){
            throw new IllegalArgumentException("Project name cannot be empty");
        }
        if(isNameExist(createProject.getName())){
            throw new IllegalArgumentException("Project name already exist");
        }
        if(createProject.getStartDate().compareTo(createProject.getEndDate()) > 0){
            throw new IllegalArgumentException("Project start date cannot be after end date");
        }

        Project project = new Project(createProject.getName(), createProject.getDescription(),
                createProject.getStartDate(), createProject.getEndDate());


        Project createdProject = projectRepository.save(project);
        for(User user : userService.getAllUsers()){
            if(createProject.getTeamMembers().contains(user.getId().intValue())){
                userService.addProjectToUser(user.getEmail(), createdProject);
            }
        }


        return createdProject;
    }

    public void updateProject(Long id, UpdateProjectRequest updateProject){
        if(Strings.isBlank(updateProject.getName())){
            throw new IllegalArgumentException("Project name cannot be null or empty");
        }
        if(updateProject.getStartDate().compareTo(updateProject.getEndDate()) > 0){
            throw new IllegalArgumentException("Project start date cannot be after end date");
        }

        Project existingProject = getProject(id);
        existingProject.setName(updateProject.getName());
        existingProject.setDescription(updateProject.getDescription());
        existingProject.setStartDate(updateProject.getStartDate());
        existingProject.setEndDate(updateProject.getEndDate());

        projectRepository.save(existingProject);

    }


    public void deleteProject(Long id){
        if(id == null){
            throw new IllegalArgumentException("Project ID cannot be null");
        }
        Optional<Project> projectOptional = projectRepository.findById(id);
        if(projectOptional.isEmpty()){
            throw new EntityNotFoundException("Project with ID " + id + " not found");
        }
        projectRepository.deleteById(id);
    }

    public List<ProjectDTO> findAllProjects(){
        List<Project> projects = projectRepository.findAll();
        List<ProjectDTO> projectDTOs = new ArrayList<>();
        for(Project project : projects){
            ProjectDTO projectDTO = new ProjectDTO(project.getId(), project.getName(), project.getDescription(),
                    project.getStartDate(), project.getEndDate());
            projectDTOs.add(projectDTO);
        }
        return projectDTOs;
    }


    public List<User> getUsersByProjectId(Long projectId){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        List<User> users = project.getUsers();

        return users;
    }

    public List<User> getUsersThatAreNotTeamMembers(Long projectId){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        List<User> users = userService.getAllUsers().stream()
                .filter(user -> !user.getProjects().contains(project))
                .collect(Collectors.toList());

        return users;
    }

    public void addUsersToProject(Long projectId, List<String> chosenUserIds){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        List<Long> userIds = chosenUserIds.stream().map(Long::parseLong)
                .collect(Collectors.toList());

        for(User user : getUsersThatAreNotTeamMembers(projectId)){
            if(userIds.contains(user.getId())){
                userService.addProjectToUser(user.getEmail(), project);
            }
        }
        projectRepository.save(project);

    }

    public List<Task> getTasksByStatusId(Project project, Integer statusId){
        return project.getTasks().stream()
                .filter(t -> Objects.equals(t.getStatus().getId(), statusId))
                .collect(Collectors.toList());
    }

    private Boolean isNameExist(String name){
        return projectRepository.findByName(name).isPresent();
    }
}
