package com.gameserver.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for accessing user data.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by username.
     *
     * @param username The username of the user.
     * @return Optional containing the User object if found, or empty if not found.
     */
    Optional<User> findByUsername(String username);

}