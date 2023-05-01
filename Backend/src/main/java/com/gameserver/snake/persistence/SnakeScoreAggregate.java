package com.gameserver.snake.persistence;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SnakeScoreAggregate {
    @JsonProperty("Username")
    private String username;
    @JsonProperty("Apples Eaten")
    private int totalApplesEaten;
    @JsonProperty("Games Played")
    private int totalGamesPlayed;
    @JsonProperty("Online Games Played")
    private int gamesPlayedWithOthers;
    @JsonProperty("Online Games Won")
    private int gamesWon;

    public SnakeScoreAggregate() {
    }

    public SnakeScoreAggregate(String username, int totalApplesEaten, int totalGamesPlayed, int gamesPlayedWithOthers, int gamesWon) {
        this.username = username;
        this.totalApplesEaten = totalApplesEaten;
        this.totalGamesPlayed = totalGamesPlayed;
        this.gamesPlayedWithOthers = gamesPlayedWithOthers;
        this.gamesWon = gamesWon;
    }


    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTotalApplesEaten() {
        return this.totalApplesEaten;
    }

    public void setTotalApplesEaten(int totalApplesEaten) {
        this.totalApplesEaten = totalApplesEaten;
    }

    public int getTotalGamesPlayed() {
        return this.totalGamesPlayed;
    }

    public void setTotalGamesPlayed(int totalGamesPlayed) {
        this.totalGamesPlayed = totalGamesPlayed;
    }

    public int getGamesPlayedWithOthers() {
        return this.gamesPlayedWithOthers;
    }

    public void setGamesPlayedWithOthers(int gamesPlayedWithOthers) {
        this.gamesPlayedWithOthers = gamesPlayedWithOthers;
    }

    public int getGamesWon() {
        return this.gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }
    
}
