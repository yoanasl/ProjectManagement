package com.example.demo.controller;

import com.example.demo.config.CustomLogger;
import com.example.demo.dto.CreateCommentRequest;
import com.example.demo.service.CommentService;
import com.example.demo.service.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final UserServiceImpl userService;

    public CommentController(CommentService commentService, UserServiceImpl userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @GetMapping("/addForm/{id}")
    public String showAddTaskForm(@PathVariable("id") Long taskId, Model model) {


        //model.addAttribute("comments", commentService.getAllForTask(taskId));

        model.addAttribute("taskId", taskId);
        model.addAttribute("createComment", new CreateCommentRequest());

//        CustomLogger.logInfo("See");
        CustomLogger.logInfo(userService.getCurrentUserFromSession().getEmail()
                +": User is accessing the add task form for task id " + taskId);
        return "addComment";
    }

    @PostMapping("/add/{id}")
    public String addNewTask(@PathVariable("id") Long taskId,
                             @ModelAttribute("createComment") CreateCommentRequest createCommentRequest) {

        commentService.createComment(createCommentRequest, taskId);
        CustomLogger.logInfo(userService.getCurrentUserFromSession().getEmail()
                +": User added a new comment for task id " + taskId);
        return "redirect:/task/get/" + taskId; // redirect to the task view page for the updated task
    }
}
