package com.example.demo.controller;

import com.example.demo.controller.dto.CreateTaskRequest;
import com.example.demo.entity.Task;
import com.example.demo.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/task")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping("/get/{id}")
    public String getTask(@PathVariable Long id, Model model) {
        Task task = service.getTaskById(id);
        model.addAttribute("task", task);
        return "taskView";
    }

    //we are in the view of some particular project and we have button for create task
    @PostMapping("/create")
    public void createTask(@RequestBody CreateTaskRequest request) {

        service.createTask(request);
        //todo: return to the view of the process with all current tasks.
        //todo: the view of the process will be on kanban board
        //todo: return such view

    }
}
