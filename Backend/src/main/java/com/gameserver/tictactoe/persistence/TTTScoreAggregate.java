package com.gameserver.tictactoe.persistence;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an aggregation of Tic Tac Toe scores for a user.
 */
public class TTTScoreAggregate {
    @JsonProperty("Username")
    private String username;
    @JsonProperty("Games Played")
    private Long totalGamesPlayed;
    @JsonProperty("Games Won")
    private Long gamesWon;

    /**
     * Constructs an empty TTTScoreAggregate object.
     */
    public TTTScoreAggregate() {
    }

    /**
     * Constructs a TTTScoreAggregate object with the specified username, total
     * games played, and games won.
     *
     * @param username         The username associated with the score aggregate.
     * @param totalGamesPlayed The total number of games played by the user.
     * @param gamesWon         The number of games won by the user.
     */
    public TTTScoreAggregate(String username, Long totalGamesPlayed, Long gamesWon) {
        this.username = username;
        this.totalGamesPlayed = totalGamesPlayed;
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
