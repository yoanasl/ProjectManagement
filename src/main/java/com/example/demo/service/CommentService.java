package com.example.demo.service;

import com.example.demo.dto.CreateCommentRequest;
import com.example.demo.entity.Comment;
import com.example.demo.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserServiceImpl userService;
    private final TaskService taskService;

    public CommentService(CommentRepository commentRepository, UserServiceImpl userService, TaskService taskService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.taskService = taskService;
    }

    public Comment createComment(CreateCommentRequest request, Long taskId) {
        Comment comment = new Comment();
        comment.setText(request.getText());

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        comment.setDate(now.format(formatter));

        comment.setTask(taskService.getTaskById(taskId));
        //TODO: set user from session; now hardcoded
        comment.setUser(userService.findByEmail("marina@abv.bg"));

        Comment newComment = commentRepository.save(comment);

        taskService.addComment(taskId, newComment);
        userService.addComment("marina@abv.bg", newComment);

        return newComment;
    }

}
