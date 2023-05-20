package com.gameserver.snake.persistence;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gameserver.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class SnakeScore {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    @JsonProperty("Apples Eaten")
    private int numApplesEaten;

    @Column(nullable = false)
    @JsonProperty("Played With Others")
    private boolean playedWithOthers;

    // if the player did not play with others, this will be null
    @JsonProperty("Have Won")
    private Boolean didWinGame;

    /**
     * Constructs an empty SnakeScore object.
     */
    public SnakeScore() {
    }

    /**
     * Constructs a SnakeScore object with the specified user, number of apples
     * eaten, and played with others flag. The didWin attribute has to be set
     * separately.
     *
     * @param user             The user associated with the score.
     * @param numApplesEaten   The number of apples eaten in the game.
     * @param playedWithOthers Indicates whether the game was played with others.
     */
    public SnakeScore(User user, int numApplesEaten, boolean playedWithOthers) {
        this.user = user;
        this.numApplesEaten = numApplesEaten;
        this.playedWithOthers = playedWithOthers;
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
     * @return User
     */
    public User getUser() {
        return this.user;
    }

    
    /** 
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }

    
    /** 
     * @return int
     */
    public int getNumApplesEaten() {
        return this.numApplesEaten;
    }

    
    /** 
     * @param numApplesEaten
     */
    public void setNumApplesEaten(int numApplesEaten) {
        this.numApplesEaten = numApplesEaten;
    }

    
    /** 
     * @return boolean
     */
    public boolean isPlayedWithOthers() {
        return this.playedWithOthers;
    }

    
    /** 
     * @return boolean
     */
    public boolean getPlayedWithOthers() {
        return this.playedWithOthers;
    }

    
    /** 
     * @param playedWithOthers
     */
    public void setPlayedWithOthers(boolean playedWithOthers) {
        this.playedWithOthers = playedWithOthers;
    }

    
    /** 
     * @return Boolean
     */
    public Boolean isDidWinGame() {
        return this.didWinGame;
    }

    
    /** 
     * @return Boolean
     */
    public Boolean getDidWinGame() {
        return this.didWinGame;
    }

    
    /** 
     * @param didWinGame
     */
    public void setDidWinGame(Boolean didWinGame) {
        this.didWinGame = didWinGame;
    }

}
