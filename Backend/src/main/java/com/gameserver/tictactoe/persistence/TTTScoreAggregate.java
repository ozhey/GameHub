package com.gameserver.tictactoe.persistence;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TTTScoreAggregate {
    @JsonProperty("Username")
    private String username;
    @JsonProperty("Games Played")
    private Long totalGamesPlayed;
    @JsonProperty("Games Won")
    private Long gamesWon;

    public TTTScoreAggregate() {
    }

    public TTTScoreAggregate(String username, Long totalGamesPlayed, Long gamesWon) {
        this.username = username;
        this.totalGamesPlayed = totalGamesPlayed;
        this.gamesWon = gamesWon;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getTotalGamesPlayed() {
        return this.totalGamesPlayed;
    }

    public void setTotalGamesPlayed(Long totalGamesPlayed) {
        this.totalGamesPlayed = totalGamesPlayed;
    }

    public Long getGamesWon() {
        return this.gamesWon;
    }

    public void setGamesWon(Long gamesWon) {
        this.gamesWon = gamesWon;
    }

}
