package com.gameserver.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Boolean login(User user) {
        Optional<User> userByUsername = userRepository.findByUsername(user.getUsername());
        if (!userByUsername.isPresent()) {
            return false;
        }
        String encodedPassword = userByUsername.get().getPassword();
        if (userByUsername.isPresent() && passwordEncoder.matches(user.getPassword(), encodedPassword)) {
            return true;
        }
        return false;
    }

    public void createUser(User user) {
        Optional<User> userByUsername = userRepository.findByUsername(user.getUsername());
        if (userByUsername.isPresent()) {
            throw new UserAlreadyExistsException("username is in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
