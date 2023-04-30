package com.gameserver.snake.persistence;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private int numApplesEaten;

    @Column(nullable = false)
    private boolean playedWithOthers;

    // if the player did not play with others, this will be null
    private Boolean didWinGame;

    public SnakeScore() {
    }

    public SnakeScore(User user, int numApplesEaten, boolean playedWithOthers) {
        this.user = user;
        this.numApplesEaten = numApplesEaten;
        this.playedWithOthers = playedWithOthers;
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

    public int getNumApplesEaten() {
        return this.numApplesEaten;
    }

    public void setNumApplesEaten(int numApplesEaten) {
        this.numApplesEaten = numApplesEaten;
    }

    public boolean isPlayedWithOthers() {
        return this.playedWithOthers;
    }

    public boolean getPlayedWithOthers() {
        return this.playedWithOthers;
    }

    public void setPlayedWithOthers(boolean playedWithOthers) {
        this.playedWithOthers = playedWithOthers;
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
