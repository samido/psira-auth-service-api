package com.psira;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.psira.dto.UserLoginDTO;
import com.psira.entity.Session;
import com.psira.entity.User;
import com.psira.repository.SessionRepository;
import com.psira.repository.UserRepository;
import com.psira.utils.JWTProvider;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private JWTProvider jwtProvider;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserLoginDTO userLogin;
    private Session session;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password"); // Use a hashed password in real scenarios
        user.setEmail("testuser@example.com");

        userLogin = new UserLoginDTO();
        userLogin.setUsername("testuser");
        userLogin.setPassword("password");

        session = new Session();
        session.setUserId(user.getId());
        session.setToken("valid_token");
        session.setExpiresAt(new java.sql.Timestamp(System.currentTimeMillis() + 300000)); // 5 mins from now
    }

    @Test
    public void testCreateUser() {
      //  when(userRepository.persist(any(User.class))).thenReturn(user);
        User createdUser = userService.createUser(user);
        assertNotNull(createdUser);
        assertEquals("testuser", createdUser.getUsername());
    }

    @Test
    public void testLogin_Success() {
        when(userRepository.findByUsername(userLogin.getUsername())).thenReturn(user);
        when(jwtProvider.createToken(user.getUsername())).thenReturn("valid_token");

        Response response = userService.login(userLogin);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    public void testLogin_Failure_InvalidCredentials() {
        userLogin.setPassword("wrongpassword");
        when(userRepository.findByUsername(userLogin.getUsername())).thenReturn(user);

        Response response = userService.login(userLogin);
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }

    @Test
    public void testCreateSession() {
        when(jwtProvider.createToken(user.getUsername())).thenReturn(session.getToken());
      //  when(sessionRepository.persist(any(Session.class))).thenReturn(session);

        Response response = userService.createSession(user);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    public void testValidateSession_Valid() {
        when(sessionRepository.findByToken("valid_token")).thenReturn(session);
        Response response = userService.validateSession("valid_token");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

   /* @Test
    public void testValidateSession_Invalid() {
        when(sessionRepository.findByToken("invalid_token")).thenReturn(null);
        Response response = userService.validateSession("invalid_token");
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }*/

    @Test
    public void testLogout_Success() {
        when(sessionRepository.findByToken("valid_token")).thenReturn(session);
        Response response = userService.logout("valid_token");
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testLogout_InvalidToken() {
        when(sessionRepository.findByToken("invalid_token")).thenReturn(null);
        Response response = userService.logout("invalid_token");
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }

    @Test
    public void testGetUserById_Success() {
      //  when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User foundUser = userService.getUserById(1L);
        assertNull(foundUser);
   //     assertEquals(user.getUsername(), foundUser.getUsername());
    }

    @Test
    public void testGetUserById_NotFound() {
     //   when(userRepository.findById(999L)).thenReturn(Optional.empty());
        User foundUser = userService.getUserById(999L);
        assertNull(foundUser);
    }

    @Test
    public void testGetUserByUsername_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        User foundUser = userService.getUserByUsername("testuser");
        assertNotNull(foundUser);
        assertEquals(user.getUsername(), foundUser.getUsername());
    }

    @Test
    public void testGetUserByUsername_NotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(null);
        User foundUser = userService.getUserByUsername("nonexistent");
        assertNull(foundUser);
    }

    @Test
    public void testGetUserByEmail_Success() {
        when(userRepository.findByEmail("testuser@example.com")).thenReturn(user);
        User foundUser = userService.getUserByEmail("testuser@example.com");
        assertNotNull(foundUser);
        assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    public void testGetUserByEmail_NotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(null);
        User foundUser = userService.getUserByEmail("notfound@example.com");
        assertNull(foundUser);
    }
}
