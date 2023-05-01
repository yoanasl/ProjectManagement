package com.example.demo.controller;

import com.example.demo.dto.CreateCommentRequest;
import com.example.demo.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/addForm/{id}")
    public String showAddTaskForm(@PathVariable("id") Long taskId, Model model) {


        //model.addAttribute("comments", commentService.getAllForTask(taskId));

        model.addAttribute("taskId", taskId);
        model.addAttribute("createComment", new CreateCommentRequest());

//        CustomLogger.logInfo("See");

        return "addComment";
    }

    @PostMapping("/add/{id}")
    public String addNewTask(@PathVariable("id") Long taskId,
                             @ModelAttribute("createComment") CreateCommentRequest createCommentRequest) {

        commentService.createComment(createCommentRequest, taskId);

        return "redirect:/task/get/" + taskId; // redirect to the task view page for the updated task
    }
}
