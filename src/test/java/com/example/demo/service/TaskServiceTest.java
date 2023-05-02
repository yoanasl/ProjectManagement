package com.example.demo.service;

import com.example.demo.dto.CreateTaskRequest;
import com.example.demo.dto.UpdateTaskModel;
import com.example.demo.entity.*;
import com.example.demo.exceptions.TaskNotFoundException;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.StatusRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest{

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TaskService taskService;
    @Mock
    private StatusRepository statusRepository;

    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        User user = User.builder()
                .email("john.doe@example.com")
                .password("password")
                .name("John Doe")
                .build();
    }

    @Test
    public void testGetTaskById(){
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        Optional<Task> optionalTask = Optional.of(task);

        when(taskRepository.getTaskById(taskId)).thenReturn(optionalTask);

        Task result = taskService.getTaskById(taskId);

        assertEquals(taskId, result.getId());
        verify(taskRepository, times(1)).getTaskById(taskId);
    }

    @Test
    public void testGetTaskByIdNotFound(){
        Long taskId = 2L;
        Optional<Task> optionalTask = Optional.empty();

        when(taskRepository.getTaskById(taskId)).thenReturn(optionalTask);

        assertThrows(TaskNotFoundException.class, () -> {
            taskService.getTaskById(taskId);
        });

        verify(taskRepository, times(1)).getTaskById(taskId);
    }


    @Test
    public void testUpdateTask(){
        Long taskId = 1L;
        UpdateTaskModel updateRequest = new UpdateTaskModel();
        updateRequest.setName("Updated Task");
        updateRequest.setDescription("Updated Description");
        updateRequest.setStartDate(LocalDate.now().plusDays(1).toString());
        updateRequest.setEndDate(LocalDate.now().plusDays(2).toString());
        updateRequest.setStatusId(2);

        Task task = new Task();
        task.setId(taskId);
        task.setName("Task");
        task.setDescription("Description");
        task.setStartDate(LocalDate.now().toString());
        task.setEndDate(LocalDate.now().plusDays(1).toString());
        task.setStatus(new Status(1, "Open"));

        Status newStatus = new Status(2, "In Progress");

        // mock behavior of repositories
        when(taskRepository.getTaskById(taskId)).thenReturn(Optional.of(task));
        when(statusRepository.findById(updateRequest.getStatusId())).thenReturn(Optional.of(newStatus));

        // call method
        taskService.updateTask(taskId, updateRequest);

        // verify behavior
        verify(taskRepository, times(1)).getTaskById(taskId);
        verify(statusRepository, times(1)).findById(updateRequest.getStatusId());
        verify(taskRepository, times(1)).save(task);

        // check that task was updated correctly
        assertEquals(updateRequest.getName(), task.getName());
        assertEquals(updateRequest.getDescription(), task.getDescription());
        assertEquals(updateRequest.getStartDate(), task.getStartDate());
        assertEquals(updateRequest.getEndDate(), task.getEndDate());
        assertEquals(newStatus, task.getStatus());
    }


    @Test
    public void testCreateTask(){
        // Set up test data
        CreateTaskRequest createRequest = new CreateTaskRequest("Task name", "Task description",
                1, 2, LocalDate.now().toString(), LocalDate.now().plusDays(1).toString(), 1);
        Long projectId = 1L;
        User user = User.builder()
                .email("john.doe@example.com")
                .password("password")
                .name("John Doe")
                .build();

        Project project = new Project("Test Project", "Test Description", "2023-05-01", "2023-05-31");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        Status status = new Status(1, "To do");
        when(statusRepository.findById(createRequest.getStatus())).thenReturn(Optional.of(status));
        Task savedTask = new Task(createRequest.getName(), createRequest.getDescription(),
                createRequest.getPriority(), createRequest.getStartDate(), createRequest.getEndDate());

        savedTask.setStatus(status);
        savedTask.setProject(project);
        savedTask.setUser(user);
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // Call method under test
        Task createdTask = taskService.createTask(createRequest, projectId);

        // Verify result
        assertEquals(savedTask, createdTask);
    }


//    @Test
//    public void testAddComment() {
//        // Arrange
//        Long taskId = 1L;
//        Comment newComment = Comment.builder()
//                .date(LocalDate.now().toString())
//                .id(1L)
//                .task(new Task())
//                .text("Good job")
//                .user(new User())
//                .build();
//
//        Task task = new Task();
//        task.setId(taskId);
//        List<Comment> comments = new ArrayList<>();
//        comments.add(newComment);
//        task.setComments(comments);
//
//        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
//        when(taskRepository.save(task)).thenReturn(task);
//
//        // Act
//        taskService.addComment(taskId, newComment);
//
//        // Assert
//        verify(taskRepository, times(1)).findById(taskId);
//        verify(taskRepository, times(1)).save(task);
//
//        assertThat(task.getComments().size()).isEqualTo(1);
//        assertThat(task.getComments().get(0)).isEqualTo(newComment);
//    }


    @Test
    public void testGetAllComments(){
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);
        List<Comment> comments = new ArrayList<>();
        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setText("Comment 1");
        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setText("Comment 2");
        comments.add(comment1);
        comments.add(comment2);
        task.setComments(comments);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        List<Comment> result = taskService.getAllComments(taskId);
        assertEquals(2, result.size());
        assertEquals(comment1.getId(), result.get(0).getId());
        assertEquals(comment1.getText(), result.get(0).getText());
        assertEquals(comment2.getId(), result.get(1).getId());
        assertEquals(comment2.getText(), result.get(1).getText());
    }

}