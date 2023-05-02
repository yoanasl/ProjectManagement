package com.example.demo.service;

import com.example.demo.dto.CreateProjectRequest;
import com.example.demo.dto.ProjectDTO;
import com.example.demo.dto.UpdateProjectRequest;
import com.example.demo.entity.Project;
import com.example.demo.entity.Status;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.exceptions.ProjectNotFoundException;
import com.example.demo.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest{

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Test
    public void testGetProjectById(){
        // Given
        Long projectId = 1L;
        String name = "Project X";
        String description = "This is a test project";
        String startDate = "2023-05-01";
        String endDate = "2023-06-01";

        Project project = Project.builder()
                .id(projectId)
                .name(name)
                .description(description)
                .startDate(startDate)
                .endDate(endDate).build();

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        // When
        Project result = projectService.getProject(projectId);

        // Then
        assertEquals(project, result);
    }

    @Test
    public void testGetProjectByIdNotFound(){
        // Given
        Long projectId = 1L;
        when(projectRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ProjectNotFoundException.class, () -> {
            projectService.getProject(projectId);
        });
    }

    @Test
    void testCreateProject_withValidInput_shouldCreateProjectAndAssignTeamMembers(){
        // Arrange
        CreateProjectRequest createProjectRequest = new CreateProjectRequest();
        createProjectRequest.setName("Project A");
        createProjectRequest.setDescription("This is a test project");
        createProjectRequest.setStartDate("2023-06-01");
        createProjectRequest.setEndDate("2023-12-31");
        createProjectRequest.setTeamMembers(List.of(1, 2, 3));

        when(projectRepository.save(any(Project.class))).thenReturn(new Project());

        User user1 = new User(1L, "john1.doe@example.com", "password", "Test");
        User user2 = new User(2L, "john2.doe@example.com", "password", "Test");
        User user3 = new User(3L, "john3.doe@example.com", "password", "Test");
        when(userService.getAllUsers()).thenReturn(List.of(user1, user2, user3));

        // Act
        Project createdProject = projectService.createProject(createProjectRequest);

        // Assert
        verify(projectRepository).save(any(Project.class));
        verify(userService).addProjectToUser(eq(user1.getEmail()), eq(createdProject));
        verify(userService).addProjectToUser(eq(user2.getEmail()), eq(createdProject));
        verify(userService).addProjectToUser(eq(user3.getEmail()), eq(createdProject));
//        assertEquals(3, createdProject.getTeamMembers().size());
    }


    @Test
    void testCreateProject_withInvalidName_shouldThrowException(){
        // Arrange
        CreateProjectRequest createProjectRequest = new CreateProjectRequest();
        createProjectRequest.setName("");
        createProjectRequest.setDescription("This is a test project");
        createProjectRequest.setStartDate("2023-06-01");
        createProjectRequest.setEndDate("2023-12-31");
        createProjectRequest.setTeamMembers(List.of(1, 2, 3));

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> projectService.createProject(createProjectRequest));
        verify(projectRepository, never()).save(any(Project.class));
        verify(userService, never()).addProjectToUser(anyString(), any(Project.class));
    }


    @Test
    void testUpdateProject_withValidData_shouldUpdateProject(){
        // Arrange
        Long projectId = 1L;
        UpdateProjectRequest updateProjectRequest = new UpdateProjectRequest();
        updateProjectRequest.setName("Updated Project A");
        updateProjectRequest.setDescription("This is an updated test project");
        updateProjectRequest.setStartDate("2023-07-01");
        updateProjectRequest.setEndDate("2023-12-31");

        Project existingProject = new Project();
        existingProject.setId(projectId);
        existingProject.setName("Project A");
        existingProject.setDescription("This is a test project");
        existingProject.setStartDate("2023-06-01");
        existingProject.setEndDate("2023-12-31");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));

        // Act
        projectService.updateProject(projectId, updateProjectRequest);

        // Assert
        verify(projectRepository, times(1)).findById(projectId);
        verify(projectRepository, times(1)).save(existingProject);
        assertEquals("Updated Project A", existingProject.getName());
        assertEquals("This is an updated test project", existingProject.getDescription());
        assertEquals("2023-07-01", existingProject.getStartDate());
        assertEquals("2023-12-31", existingProject.getEndDate());
    }

    @Test
    void testDeleteProject_withValidId_shouldDeleteProject(){
        // Arrange
        Long projectId = 1L;
        Project existingProject = new Project();
        existingProject.setId(projectId);
        existingProject.setName("Project A");
        existingProject.setDescription("This is a test project");
        existingProject.setStartDate("2023-06-01");
        existingProject.setEndDate("2023-12-31");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));

        // Act
        projectService.deleteProject(projectId);

        // Assert
        verify(projectRepository, times(1)).deleteById(projectId);
    }

    @Test
    void testDeleteProject_withNullId_shouldThrowException(){
        // Arrange
        Long projectId = null;

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> projectService.deleteProject(projectId));
        verify(projectRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteProject_withNonexistentId_shouldThrowException(){
        // Arrange
        Long projectId = 1L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(EntityNotFoundException.class, () -> projectService.deleteProject(projectId));
        verify(projectRepository, never()).deleteById(anyLong());
    }

    @Test
    void testFindAllProjects(){
        // Arrange
        Project project1 = new Project("Project A", "Description A", "2023-06-01", "2023-12-31");
        Project project2 = new Project("Project B", "Description B", "2023-07-01", "2023-12-31");
        List<Project> projects = List.of(project1, project2);
        when(projectRepository.findAll()).thenReturn(projects);

        List<ProjectDTO> expectedProjectDTOs = List.of(
                new ProjectDTO(1L, "Project A", "Description A", "2023-06-01", "2023-12-31"),
                new ProjectDTO(2L, "Project B", "Description B", "2023-07-01", "2023-12-31")
        );

        // Act
        List<ProjectDTO> actualProjectDTOs = projectService.findAllProjects();

        // Assert
        assertEquals(expectedProjectDTOs, actualProjectDTOs);
    }


    @Test
    void testGetUsersByProjectId(){
        // Arrange
        Long projectId = 1L;
        User user1 = new User(1L, "john1.doe@example.com", "password", "Test");
        User user2 = new User(2L, "john2.doe@example.com", "password", "Test");
        Project project = new Project();
        project.setId(projectId);
        project.setName("Project A");
        project.setDescription("This is a test project");
        project.setStartDate("2023-06-01");
        project.setEndDate("2023-12-31");
        project.setUsers(List.of(user1, user2));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        // Act
        List<User> result = projectService.getUsersByProjectId(projectId);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));
    }


    @Test
    void testGetUsersThatAreNotTeamMembers(){
        // Arrange
        Long projectId = 1L;

        Project project = new Project();
        project.setId(projectId);

        User user1 = new User();
        user1.setId(1L);
        user1.setName("user1");
        user1.setEmail("user1@example.com");
        user1.setProjects(Collections.emptyList());

        User user2 = new User();
        user2.setId(2L);
        user2.setName("user2");
        user2.setEmail("user2@example.com");
        user2.setProjects(Collections.singletonList(project));

        User user3 = new User();
        user3.setId(3L);
        user3.setName("user3");
        user3.setEmail("user3@example.com");
        user3.setProjects(Collections.singletonList(project));

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2, user3));

        // Act
        List<User> result = projectService.getUsersThatAreNotTeamMembers(projectId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(user1, result.get(0));
    }


    @Test
    void testAddUsersToProject(){
        // Arrange
        Long projectId = 1L;
        List<String> chosenUserIds = List.of("1", "2");

        Project project = new Project();
        project.setId(projectId);
        project.setName("Project A");
        project.setDescription("This is a test project");
        project.setStartDate("2023-06-01");
        project.setEndDate("2023-12-31");

        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@example.com");
        user1.setProjects(new ArrayList<>());

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");
        user2.setProjects(new ArrayList<>());

        List<User> users = List.of(user1, user2);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(userService.getAllUsers()).thenReturn(users);

        // Act
        projectService.addUsersToProject(projectId, chosenUserIds);

        // Assert
        verify(userService, times(1)).addProjectToUser(eq("user1@example.com"), eq(project));
        verify(userService, times(1)).addProjectToUser(eq("user2@example.com"), eq(project));
        verify(projectRepository, times(1)).save(eq(project));
    }


    @Test
    void testGetTasksByStatusId(){
        // Arrange
        Project project = new Project();
        project.setId(1L);
        project.setName("Project A");

        Task task1 = new Task();
        task1.setId(1L);
        task1.setName("Task 1");
        task1.setDescription("This is task 1");
        task1.setStatus(new Status(1, "To Do"));

        Task task2 = new Task();
        task2.setId(2L);
        task2.setName("Task 2");
        task2.setDescription("This is task 2");
        task2.setStatus(new Status(2, "In Progress"));

        Task task3 = new Task();
        task3.setId(3L);
        task3.setName("Task 3");
        task3.setDescription("This is task 3");
        task3.setStatus(new Status(1, "To Do"));

        project.setTasks(Arrays.asList(task1, task2, task3));

        Integer statusId = 1;

        // Act
        List<Task> tasksByStatusId = projectService.getTasksByStatusId(project, statusId);

        // Assert
        assertEquals(2, tasksByStatusId.size());
        assertTrue(tasksByStatusId.contains(task1));
        assertTrue(tasksByStatusId.contains(task3));
    }


}