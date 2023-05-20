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
     * @return Long
     */
    public Long getTotalApplesEaten() {
        return this.totalApplesEaten;
    }

    
    /** 
     * @param totalApplesEaten
     */
    public void setTotalApplesEaten(Long totalApplesEaten) {
        this.totalApplesEaten = totalApplesEaten;
    }

    
    /** 
     * @return Long
     */
    public Long getTotalGamesPlayed() {
        return this.totalGamesPlayed;
    }

    
    /** 
     * @param totalGamesPlayed
     */
    public void setTotalGamesPlayed(Long totalGamesPlayed) {
        this.totalGamesPlayed = totalGamesPlayed;
    }

    
    /** 
     * @return Long
     */
    public Long getGamesPlayedWithOthers() {
        return this.gamesPlayedWithOthers;
    }

    
    /** 
     * @param gamesPlayedWithOthers
     */
    public void setGamesPlayedWithOthers(Long gamesPlayedWithOthers) {
        this.gamesPlayedWithOthers = gamesPlayedWithOthers;
    }

    
    /** 
     * @return Long
     */
    public Long getGamesWon() {
        return this.gamesWon;
    }

    
    /** 
     * @param gamesWon
     */
    public void setGamesWon(Long gamesWon) {
        this.gamesWon = gamesWon;
    }

}
