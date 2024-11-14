package com.psira.resources;

import com.psira.dto.UserLoginDTO;
import com.psira.UserService;
import com.psira.entity.User;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


@Path("/api")
public class UserResource {

    @Inject
    UserService userService;

    private static final Log LOG = LogFactory.getLog(UserResource.class);


    @POST
    @Path("/user")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(@Valid User user) {
        User createdUser = userService.createUser(user);
        return Response.status(Response.Status.CREATED).entity(createdUser).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(UserLoginDTO userLogin) {
        return userService.login(userLogin);
    }

    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logout(@Context HttpHeaders headers) {
        String token = headers.getHeaderString("token"); //.substring("Bearer ".length());
        return userService.logout(token);
    }

    @GET
    @Path("/validate-session")
    public Response validateSession(@Context HttpHeaders headers) {
        String token =  headers.getHeaderString("token"); //.substring("Bearer ".length());
        return userService.validateSession(token);
    }

    @PUT
    @Path("/user/id/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") Long id, @Valid User user) {
        User updatedUser = userService.updateUser(id, user);
        if (updatedUser != null) {
            return Response.ok(updatedUser).build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
    }

    @DELETE
    @Path("/user/id/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        userService.deleteUser(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        List<User> users = userService.getAllUsers();
        return Response.ok(users).build();
    }

    @GET
    @Path("/user/email/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByEmail(@PathParam("email") String email) {
        User user = userService.getUserByEmail(email);
        if (user != null) {
            return Response.ok(user).build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
    }
    @GET
    @Path("/user/username/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByUsername(@PathParam("username") String username) {
        User user = userService.getUserByUsername(username);
        if (user != null) {
            return Response.ok(user).build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
    }
}
