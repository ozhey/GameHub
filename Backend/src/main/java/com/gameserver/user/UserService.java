package com.gameserver.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for user and auth-related operations.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Performs user login by checking the credentials.
     *
     * @param user The user object containing the login credentials.
     * @return True if the login is successful, false otherwise.
     */
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

    /**
     * Creates a new user.
     *
     * @param user The user object containing the user information.
     * @throws UserAlreadyExistsException if a user with the same username already
     *                                    exists.
     */
    public void createUser(User user) {
        Optional<User> userByUsername = userRepository.findByUsername(user.getUsername());
        if (userByUsername.isPresent()) {
            throw new UserAlreadyExistsException("username is in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    /**
     * Retrieves a user object by their username.
     *
     * @param username The username of the user.
     * @return The User object if found, or null if not found.
     */
    public User getUserByUserName(String username) {
        Optional<User> userByUsername = userRepository.findByUsername(username);
        if (userByUsername.isPresent()) {
            return userByUsername.get();
        }
        return null;
    }
}
