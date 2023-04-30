package com.gameserver.snake.persistence;

public class SnakeScoreAggregate {
    private String username;
    private int totalApplesEaten;
    private int totalGamesPlayed;
    private int gamesPlayedWithOthers;
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
