package com.gameserver.user;

import java.util.UUID;

import jakarta.persistence.*;

/**
 * Entity class representing a user.
 */
@Entity
@Table(name = "user_table") // table 'user' is not allowed in postgresql
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    /**
     * Constructs an empty User object.
     */
    public User() {
    }

    /**
     * Constructs a User object with the specified id, username, and password.
     *
     * @param id       The ID of the user.
     * @param username The username of the user.
     * @param password The password of the user.
     */
    public User(UUID id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    
    /** 
     * @return UUID
     */
    public UUID getId() {
        return this.id;
    }

    
    /** 
     * @param id
     */
    public void setId(UUID id) {
        this.id = id;
    }

    
    /** 
     * @return String
     */
    public String getUsername() {
        return this.username;
    }

    
    /** 
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    
    /** 
     * @return String
     */
    public String getPassword() {
        return this.password;
    }

    
    /** 
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    
    /** 
     * @return String
     */
    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", username='" + getUsername() + "'" +
                ", password='" + getPassword() + "'" +
                "}";
    }

}