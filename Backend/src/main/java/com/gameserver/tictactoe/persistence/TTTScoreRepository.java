package com.gameserver.tictactoe.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gameserver.user.User;

/**
 * Repository interface for managing Tic Tac Toe scores.
 */
@Repository
public interface TTTScoreRepository extends JpaRepository<TTTScore, Long> {

    /**
     * Retrieves the leaderboard of Tic Tac Toe scores.
     *
     * @return A list of TTTScoreAggregate objects representing the leaderboard.
     */
    @Query("SELECT new com.gameserver.tictactoe.persistence.TTTScoreAggregate(u.username, COUNT(t) AS gamesPlayed, SUM(CASE WHEN t.didWinGame THEN 1 ELSE 0 END) AS gamesWon) FROM TTTScore t JOIN t.user u GROUP BY u.username ORDER BY gamesWon DESC")
    List<TTTScoreAggregate> getLeaderboard();

    /**
     * Retrieves the Tic Tac Toe scores for a specific user.
     *
     * @param user The User object representing the user.
     * @return A list of TTTScore objects representing the user's games.
     */
    @Query("SELECT t FROM TTTScore t JOIN t.user u WHERE u = :user")
    List<TTTScore> findByUser(@Param("user") User user);

}