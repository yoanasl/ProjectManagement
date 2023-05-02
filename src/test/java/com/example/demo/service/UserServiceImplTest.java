package com.example.demo.service;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Project;
import com.example.demo.entity.User;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class UserServiceImplTest{
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserServiceImpl userService;
    @Test
    void findUserById_UserExists_Success() {
        // Arrange
        Long userId = 1L;
        User expectedUser = new User();
        expectedUser.setId(userId);
        expectedUser.setName("Test User");
        expectedUser.setEmail("test@example.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        // Act
        User actualUser = userService.findUserById(userId);

        // Assert
        assertEquals(expectedUser, actualUser);
        verify(userRepository, times(1)).findById(userId);
    }
    @Test
    void findUserById_UserDoesNotExist_ThrowsUserNotFoundException() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.findUserById(userId));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void findByEmail_UserExists_Success() {
        // Arrange
        String email = "test@example.com";
        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setName("Test User");
        expectedUser.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(expectedUser));

        // Act
        User actualUser = userService.findByEmail(email);

        // Assert
        assertEquals(expectedUser, actualUser);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void findByEmail_UserDoesNotExist_ThrowsRuntimeException() {
        // Arrange
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.findByEmail(email));
        verify(userRepository, times(1)).findByEmail(email);
    }


    @Test
    void getAllUsers_ReturnsListOfUsers() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setName("Test User 1");
        user1.setEmail("test1@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Test User 2");
        user2.setEmail("test2@example.com");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Act
        List<User> users = userService.getAllUsers();

        // Assert
        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
        verify(userRepository, times(1)).findAll();
    }


    @Test
    void findByProjectId_ProjectExists_ReturnsListOfUsers() {
        // Arrange
        Long projectId = 1L;
        List<User> expectedUsers = List.of(new User(), new User());
        when(userRepository.findByProjectsId(projectId)).thenReturn(expectedUsers);

        // Act
        List<User> actualUsers = userService.findByProjectId(projectId);

        // Assert
        assertEquals(expectedUsers, actualUsers);
        verify(userRepository, times(1)).findByProjectsId(projectId);
    }

    @Test
    void findByProjectId_ProjectDoesNotExist_ReturnsEmptyList() {
        // Arrange
        Long projectId = 1L;
        when(userRepository.findByProjectsId(projectId)).thenReturn(Collections.emptyList());

        // Act
        List<User> actualUsers = userService.findByProjectId(projectId);

        // Assert
        assertTrue(actualUsers.isEmpty());
        verify(userRepository, times(1)).findByProjectsId(projectId);
    }


    @Test
    void addComment_AddsCommentToUser_Success() {
        // Arrange
        String userEmail = "test@example.com";
        Comment comment = new Comment();
        comment.setText("Test comment");
        User user = new User();
        user.setEmail(userEmail);
        user.setComments(new ArrayList<>());
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        // Act
        userService.addComment(userEmail, comment);

        // Assert
        verify(userRepository, times(1)).findByEmail(userEmail);
        assertEquals(1, user.getComments().size());
        assertEquals(comment, user.getComments().get(0));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void addProjectToUser_ProjectAdded_Success() {
        // Arrange
        String userEmail = "test@example.com";
        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        userService.addProjectToUser(userEmail, project);

        // Assert
        assertTrue(user.getProjects().contains(project));
        verify(userRepository, times(1)).findByEmail(userEmail);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getCurrentUserFromSession_UserExists_Success() {
        // Arrange
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        String currentUserEmail = "test@example.com";
        when(authentication.getName()).thenReturn(currentUserEmail);
        User expectedUser = new User();
        expectedUser.setEmail(currentUserEmail);
        when(userRepository.findByEmail(currentUserEmail)).thenReturn(Optional.of(expectedUser));

        // Act
        User actualUser = userService.getCurrentUserFromSession();

        // Assert
        assertEquals(expectedUser, actualUser);
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
        verify(userRepository, times(1)).findByEmail(currentUserEmail);
    }

    @Test
    void getCurrentUserFromSession_UserDoesNotExist_ThrowsRuntimeException() {
        // Arrange
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        String currentUserEmail = "test@example.com";
        when(authentication.getName()).thenReturn(currentUserEmail);
        when(userRepository.findByEmail(currentUserEmail)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.getCurrentUserFromSession());
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
        verify(userRepository, times(1)).findByEmail(currentUserEmail);
    }

}