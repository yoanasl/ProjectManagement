package com.example.demo.Service;

import com.example.demo.dto.CreateTaskRequest;
import com.example.demo.entity.Task;
import com.example.demo.exceptions.TaskNotFoundException;
import com.example.demo.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task getTaskById(Long id) {
        Optional<Task> task = taskRepository.getTaskById(id);
        if (task.isEmpty()) {
            throw new TaskNotFoundException(id);
        }
        return task.get();
    }

    public void createTask(CreateTaskRequest request) {
        //todo: add current user from session as assignee when creating new task
        Task newTask = new Task(request.getName(), request.getDescription(),
                request.getPriority(), request.getStatus(),
                request.getStartDate(), request.getEndDate(),
                request.getProject(), request.getUser());
        taskRepository.save(newTask);
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
