package com.gameserver.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling user and auth-related operations.
 */
@RestController
@RequestMapping(path = "api/v1/auth")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Endpoint for user login.
     *
     * @param user The user object containing login credentials.
     * @return ResponseEntity containing the login response.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        if (userService.login(user)) {
            return ResponseEntity.ok("{\"username\":\"" + user.getUsername() + "\"}");
        } else {
            return ResponseEntity.badRequest().body("{\"error\":\"username or password is incorrect\"}");
        }
    }

    /**
     * Endpoint for user registration.
     *
     * @param user The user object containing registration information.
     * @return ResponseEntity containing the registration response.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        try {
            userService.createUser(user);
            return ResponseEntity.ok("{\"username\":\"" + user.getUsername() + "\"}");
        } catch (UserAlreadyExistsException ex) {
            return ResponseEntity.badRequest().body("{\"error\":\"a user with this username already exists\"}");
        }
    }

}
