package com.example.demo.controller;

import com.example.demo.dto.CreateTaskRequest;
import com.example.demo.dto.UpdateTaskModel;
import com.example.demo.entity.Task;
import com.example.demo.service.StatusService;
import com.example.demo.service.TaskService;
import com.example.demo.service.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;
    private final StatusService statusService;
    private final UserServiceImpl userService;

    public TaskController(TaskService service, StatusService statusService, UserServiceImpl userService) {
        this.taskService = service;
        this.statusService = statusService;
        this.userService = userService;
    }

    @GetMapping("/get/{id}")
    public String getTask(@PathVariable Long id, Model model) {
        Task task = taskService.getTaskById(id);
        model.addAttribute("comments", taskService.getAllComments(id));
        model.addAttribute("task", task);
        return "taskView";
    }

    @GetMapping("/editForm/{id}")
    public String showEditTaskForm(@PathVariable("id") Long id, Model model) {
        // retrieve the task with the given id from the database

        Task task = taskService.getTaskById(id);
        com.example.demo.dto.UpdateTaskModel updateTask = new UpdateTaskModel();
        updateTask.setName(task.getName());
        updateTask.setDescription(task.getDescription());
        updateTask.setStartDate(task.getStartDate());
        updateTask.setEndDate(task.getEndDate());
        updateTask.setStatusId(task.getStatus().getId());

        // add the task to the model so it can be displayed in the edit form
        model.addAttribute("taskId", id);
        model.addAttribute("updateTask", updateTask);

        return "editTask"; // return the name of the Thymeleaf template for rendering the edit form
    }

    @PostMapping("/edit/{id}")
    public String updateTask(@PathVariable("id") Long id,
                             @ModelAttribute("updateTask") UpdateTaskModel task) {
        taskService.updateTask(id, task);

        return "redirect:/task/get/" + id; // redirect to the task view page for the updated task
    }

    @GetMapping("/addTaskForm/{id}")
    public String showAddTaskForm(@PathVariable("id") Long projectId, Model model) {


        model.addAttribute("users", userService.findByProjectId(projectId));

        model.addAttribute("projectId", projectId);
        model.addAttribute("createTask", new CreateTaskRequest());

        return "addTask";
    }

    @PostMapping("/add/{id}")
    public String addNewTask(@PathVariable("id") Long projectId, @ModelAttribute("createTask") CreateTaskRequest createTaskRequest) {

        Task createdTask = taskService.createTask(createTaskRequest, projectId);

        return "redirect:/task/get/" + createdTask.getId(); // redirect to the task view page for the updated task
    }

}
