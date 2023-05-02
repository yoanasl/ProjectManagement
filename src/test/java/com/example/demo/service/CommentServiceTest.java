package com.example.demo.service;

import com.example.demo.dto.CreateCommentRequest;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Task;
import com.example.demo.entity.User;
import com.example.demo.repository.CommentRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskService taskService;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private CommentService commentService;

    @Test
    public void testCreateComment() {
        // Set up test data
        CreateCommentRequest request = new CreateCommentRequest();
        request.setText("Test comment");
        Long taskId = 1L;

        User currentUser = new User();
        currentUser.setEmail("test@example.com");

        Task task = new Task();
        task.setId(taskId);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText(request.getText());
        comment.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        comment.setTask(task);
        comment.setUser(currentUser);

        Mockito.when(taskService.getTaskById(taskId)).thenReturn(task);
        Mockito.when(userService.getCurrentUserFromSession()).thenReturn(currentUser);
        Mockito.when(userService.findByEmail(currentUser.getEmail())).thenReturn(currentUser);
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);

        // Call the method being tested
        Comment createdComment = commentService.createComment(request, taskId);

        // Verify the results
        Mockito.verify(commentRepository).save(Mockito.any(Comment.class));
        Mockito.verify(taskService).addComment(taskId, comment);

        // Check that the created comment matches the expected values
        Assert.assertEquals(request.getText(), createdComment.getText());
        Assert.assertEquals(task.getId(), createdComment.getTask().getId());
        Assert.assertEquals(currentUser.getEmail(), createdComment.getUser().getEmail());
    }


}
