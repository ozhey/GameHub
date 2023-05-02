package com.gameserver.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        if (userService.login(user)) {
            return ResponseEntity.ok("{\"username\":\"" + user.getUsername() + "\"}");
        } else {
            return ResponseEntity.badRequest().body("{\"error\":\"username or password is incorrect\"}");
        }
    }

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