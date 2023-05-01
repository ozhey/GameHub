package com.gameserver.tictactoe.persistence;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TTTScoreAggregate {
    @JsonProperty("Username")
    private String username;
    @JsonProperty("Games Played")
    private int totalGamesPlayed;
    @JsonProperty("Games Won")
    private int gamesWon;

    public TTTScoreAggregate() {
    }

    public TTTScoreAggregate(String username, int totalGamesPlayed, int gamesWon) {
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

    public int getTotalGamesPlayed() {
        return this.totalGamesPlayed;
    }

    public void setTotalGamesPlayed(int totalGamesPlayed) {
        this.totalGamesPlayed = totalGamesPlayed;
    }

    public int getGamesWon() {
        return this.gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

}
