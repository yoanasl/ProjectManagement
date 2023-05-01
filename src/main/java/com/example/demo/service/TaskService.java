package com.example.demo.service;

import com.example.demo.dto.CreateTaskRequest;
import com.example.demo.dto.UpdateTaskModel;
import com.example.demo.entity.*;
import com.example.demo.exceptions.TaskNotFoundException;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.StatusRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository, StatusRepository statusRepository, UserRepository userRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    public Task getTaskById(Long id) {
        Optional<Task> task = taskRepository.getTaskById(id);
        if (task.isEmpty()) {
            throw new TaskNotFoundException(id);
        }
        return task.get();
    }


    public void updateTask(Long id, UpdateTaskModel updateRequest) {

        Task taskFound = Optional.of(taskRepository.getTaskById(id)).get()
                .orElseThrow(() -> new TaskNotFoundException(id));
        taskFound.setName(updateRequest.getName());
        taskFound.setDescription(updateRequest.getDescription());
        taskFound.setStartDate(updateRequest.getStartDate());
        taskFound.setEndDate(updateRequest.getEndDate());

        Status status = statusRepository.findById(updateRequest.getStatusId())
                .orElseThrow(() -> new TaskNotFoundException(id));

        taskFound.setStatus(status);
        taskRepository.save(taskFound);
    }

    public Task createTask(CreateTaskRequest createRequest, Long projectId) {

        Task newTask = new Task(createRequest.getName(), createRequest.getDescription(),
                createRequest.getPriority(),  createRequest.getStartDate(),
                createRequest.getEndDate());

        Status status = statusRepository.findById(createRequest.getStatus())
                .orElseThrow(() -> new TaskNotFoundException(1L));
        newTask.setStatus(status);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new TaskNotFoundException(1L));
        newTask.setProject(project);

        User user = userRepository.findById(Long.valueOf(createRequest.getUserId()))
                .orElseThrow(() -> new TaskNotFoundException(1L));
        newTask.setUser(user);

        /*//todo: add current user from session as assignee when creating new task
        //hardcoded:
        newTask.setUser(userRepository.findById(1L).get());
        String user = newTask.getUser().getName();*/
        return taskRepository.save(newTask);
    }

    public void addComment(Long taskId, Comment newComment) {
        Task task = getTaskById(taskId);
        task.getComments().add(newComment);
        taskRepository.save(task);
    }

    public List<Comment> getAllComments(Long taskId) {
        return taskRepository.findById(taskId).get().getComments();
    }


    /*
    Create a new task: Users can create a new task by providing details such as task name,
    description, priority, due date, assignee, etc.

    Update a task: Users can update a task by changing its details such as name,
    description, priority, due date, status, etc.

    Delete a task: Users can delete a task that is no longer needed.

    Assign a task: Users can assign a task to another user or team member.

    Set task status: Users can set the status of a task to "in progress", "completed",
    or "cancelled".

    Track task progress: Users can track the progress of a task by adding comments,
    attaching files, setting reminders, etc.

    Set task dependencies: Users can set dependencies between tasks,
    such that one task cannot be started until another task is completed.

    Set task reminders: Users can set reminders for a task's due date,
    so they don't miss any deadlines.

    View task details: Users can view the details of a task, including its name,
    description, due date, assignee, status, etc.

    Filter tasks: Users can filter tasks based on various criteria such as priority,
    due date, assignee, etc.

    Search tasks: Users can search for specific tasks using keywords or other search criteria.

    Export task data: Users can export task data to various formats
    such as CSV, PDF, or Excel, for reporting or analysis purposes.
    */


}
