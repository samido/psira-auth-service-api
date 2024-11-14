package com.psira;

import com.psira.dto.LoginResponse;
import com.psira.dto.UserLoginDTO;
import com.psira.entity.Session;
import com.psira.entity.User;
import com.psira.repository.SessionRepository;
import com.psira.repository.UserRepository;
import com.psira.utils.JWTProvider;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.transaction.Transactional;
import jakarta.inject.Inject;
import java.sql.Timestamp;
import java.util.List;


@RequestScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    @Inject
    SessionRepository sessionRepository;

    @Inject
    JWTProvider jwtProvider;

    public Response login(UserLoginDTO userLogin) {
        User user = userRepository.findByUsername(userLogin.getUsername());

        if (user != null && user.getPassword().equals(userLogin.getPassword())) {
            String token = jwtProvider.createToken(user.getUsername());
            long expirationTime = 300000; // 5 minutes
            Timestamp expirationTimestamp = new Timestamp(System.currentTimeMillis() + expirationTime);
            Session session = new Session(user.getId(), token, expirationTimestamp);
            sessionRepository.persist(session);
            LoginResponse loginResponse = new LoginResponse(token, "Login successful", user.getRole(), user.getUsername());
            return Response.ok(loginResponse).build();
        } else {
            return Response.status(Status.UNAUTHORIZED).entity("Invalid credentials").build();
        }
    }

    @Transactional
    public User createUser(User user) {
        userRepository.persist(user);
        return user;
    }

    @Transactional
    public User updateUser(Long userId, User updatedUser) {
        User user = userRepository.findById(userId);
        if (user != null) {
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setRole(updatedUser.getRole());
            user.setPassword(updatedUser.getPassword()); // Implement password hashing
            return userRepository.update(user);
        }
        return null; // or throw an exception
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.delete(userId);
    }

    @Transactional
    public Response logout(String token) {
        System.out.println("Logout attempt with token: " + token); // Debug log
        Session session = sessionRepository.findByToken(token);
        if (session != null) {
            sessionRepository.deleteByToken(token); // Invalidate the session
            return Response.ok("Logged out successfully").build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
    }

    public Response validateSession(String token) {
        Session session = sessionRepository.findByToken(token);
        if (session != null && session.getExpiresAt().after(new Timestamp(System.currentTimeMillis()))) {
            return Response.ok("Session is valid").build();
        }
        return Response.status(Status.UNAUTHORIZED).entity("Invalid session").build();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User  getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public Response createSession(User user) {
        String token = jwtProvider.createToken(user.getUsername());
        long expirationTime = 300000; // 5 minutes
        Timestamp expirationTimestamp = new Timestamp(System.currentTimeMillis() + expirationTime);
        Session session = new Session(user.getId(), token, expirationTimestamp);
        sessionRepository.persist(session);
        return Response.status(Status.CREATED).entity(session).build();
    }
}
