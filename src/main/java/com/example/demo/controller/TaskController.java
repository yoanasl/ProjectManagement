package com.example.demo.controller;

import com.example.demo.service.TaskService;
import com.example.demo.controller.dto.UpdateViewModel;
import com.example.demo.dto.CreateTaskRequest;
import com.example.demo.entity.Task;
import com.example.demo.service.StatusService;
import com.example.demo.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;
    private final StatusService statusService;

    public TaskController(TaskService service, StatusService statusService) {
        this.taskService = service;
        this.statusService = statusService;
    }

    @GetMapping("/get/{id}")
    public String getTask(@PathVariable Long id, Model model) {
        Task task = taskService.getTaskById(id);
        model.addAttribute("task", task);
        return "taskView";
    }

    @GetMapping("/editForm/{id}")
    public String showEditTaskForm(@PathVariable("id") Long id, Model model) {
        // retrieve the task with the given id from the database

        Task task = taskService.getTaskById(id);
        UpdateViewModel updateTask = new UpdateViewModel();
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
    public String updateTask(@PathVariable("id") Long id, @ModelAttribute("updateTask") UpdateViewModel task) {
        // update the task in the database
        taskService.updateTask(id, task);
       // taskRepository.save(task);

        return "redirect:/task/get/" + id; // redirect to the task view page for the updated task
    }

    //we are in the view of some particular project and we have button for create task
    @PostMapping("/create")
    public void createTask(@RequestBody CreateTaskRequest request) {

        taskService.createTask(request);
        //todo: return to the view of the process with all current tasks.
        //todo: the view of the process will be on kanban board
        //todo: return such view

    }
}
