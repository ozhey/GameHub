package com.gameserver.snake.persistence;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SnakeScoreAggregate {
    @JsonProperty("Username")
    private String username;
    @JsonProperty("Apples Eaten")
    private Long totalApplesEaten;
    @JsonProperty("Games Played")
    private Long totalGamesPlayed;
    @JsonProperty("Online Games Played")
    private Long gamesPlayedWithOthers;
    @JsonProperty("Online Games Won")
    private Long gamesWon;

    public SnakeScoreAggregate() {
    }

    public SnakeScoreAggregate(String username, Long totalApplesEaten, Long totalGamesPlayed, Long gamesPlayedWithOthers, Long gamesWon) {
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

    public Long getTotalApplesEaten() {
        return this.totalApplesEaten;
    }

    public void setTotalApplesEaten(Long totalApplesEaten) {
        this.totalApplesEaten = totalApplesEaten;
    }

    public Long getTotalGamesPlayed() {
        return this.totalGamesPlayed;
    }

    public void setTotalGamesPlayed(Long totalGamesPlayed) {
        this.totalGamesPlayed = totalGamesPlayed;
    }

    public Long getGamesPlayedWithOthers() {
        return this.gamesPlayedWithOthers;
    }

    public void setGamesPlayedWithOthers(Long gamesPlayedWithOthers) {
        this.gamesPlayedWithOthers = gamesPlayedWithOthers;
    }

    public Long getGamesWon() {
        return this.gamesWon;
    }

    public void setGamesWon(Long gamesWon) {
        this.gamesWon = gamesWon;
    }
    
}
