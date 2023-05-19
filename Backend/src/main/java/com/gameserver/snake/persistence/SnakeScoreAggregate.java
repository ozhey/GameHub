package com.gameserver.snake.persistence;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an aggregate score record for the Snake game.
 * Used in aggregate queries in the SnakeScoreRepository.
 */
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

    /**
     * Constructs an empty SnakeScoreAggregate object.
     */
    public SnakeScoreAggregate() {
    }

    /**
     * Constructs a SnakeScoreAggregate object with the specified username, total
     * apples eaten, total games played,
     * games played with others, and games won.
     *
     * @param username              The username associated with the aggregate
     *                              score.
     * @param totalApplesEaten      The total number of apples eaten.
     * @param totalGamesPlayed      The total number of games played.
     * @param gamesPlayedWithOthers The total number of games played with others.
     * @param gamesWon              The total number of games won.
     */
    public SnakeScoreAggregate(String username, Long totalApplesEaten, Long totalGamesPlayed,
            Long gamesPlayedWithOthers, Long gamesWon) {
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
