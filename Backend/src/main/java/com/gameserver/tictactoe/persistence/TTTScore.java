package com.gameserver.tictactoe.persistence;

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

/**
 * Represents a Tic Tac Toe game score for a user.
 */
@Entity
public class TTTScore {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @JsonProperty("Have Won")
    @Column(nullable = false)
    private Boolean didWinGame;

    /**
     * Constructs an empty TTTScore object.
     */
    public TTTScore() {
    }

    /**
     * Constructs a TTTScore object with the specified user and game result.
     *
     * @param user       The User object associated with the score.
     * @param didWinGame The game result indicating whether the user won or not.
     */
    public TTTScore(User user, Boolean didWinGame) {
        this.user = user;
        this.didWinGame = didWinGame;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean isDidWinGame() {
        return this.didWinGame;
    }

    public Boolean getDidWinGame() {
        return this.didWinGame;
    }

    public void setDidWinGame(Boolean didWinGame) {
        this.didWinGame = didWinGame;
    }

}
