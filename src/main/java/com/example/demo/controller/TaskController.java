package com.example.demo.controller;

import com.example.demo.service.TaskService;
import com.example.demo.dto.CreateTaskRequest;
import com.example.demo.entity.Task;
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

    @GetMapping("/editForm/{id}")
    public String showEditTaskForm(@PathVariable("id") Long id, Model model) {
        // retrieve the task with the given id from the database

        Task task = service.getTaskById(id);

        // add the task to the model so it can be displayed in the edit form
        model.addAttribute("task", task);

        return "editTask"; // return the name of the Thymeleaf template for rendering the edit form
    }

    @PostMapping("/edit/{id}")
    public String updateTask(@PathVariable("id") Long id, @ModelAttribute("task") Task task) {
        // update the task in the database
        task.setId(id); // set the id of the task to the id specified in the URL path
       // taskRepository.save(task);

        return "redirect:/task/get/" + id; // redirect to the task view page for the updated task
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
