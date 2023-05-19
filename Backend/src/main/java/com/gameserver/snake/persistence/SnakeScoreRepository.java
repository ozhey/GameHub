package com.gameserver.snake.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gameserver.user.User;

/**
 * Repository interface for Snake game scores.
 */
@Repository
public interface SnakeScoreRepository extends JpaRepository<SnakeScore, Long> {

    /**
     * Retrieves the leaderboard of Snake game scores.
     *
     * @return The list of SnakeScoreAggregate objects representing the leaderboard.
     */
    @Query("SELECT new com.gameserver.snake.persistence.SnakeScoreAggregate(u.username, SUM(s.numApplesEaten) AS totalApplesEaten, COUNT(s) AS totalGamesPlayed, SUM(CASE WHEN s.playedWithOthers THEN 1 ELSE 0 END) AS gamesPlayedWithOthers, SUM(CASE WHEN s.didWinGame THEN 1 ELSE 0 END) AS gamesWon) FROM SnakeScore s JOIN s.user u GROUP BY u.username ORDER BY totalApplesEaten DESC")
    List<SnakeScoreAggregate> getLeaderboard();

    /**
     * Retrieves the scores of a user for the Snake game.
     *
     * @param user The user object.
     * @return The list of SnakeScore objects representing the user's scores.
     */
    @Query("SELECT s FROM SnakeScore s JOIN s.user u WHERE u = :user")
    List<SnakeScore> findByUser(@Param("user") User user);

}