package com.gameserver.snake.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gameserver.user.User;

@Repository
public interface SnakeScoreRepository extends JpaRepository<SnakeScore, Long> {

    @Query("SELECT u.username, SUM(s.numApplesEaten) AS totalApplesEaten, COUNT(s) AS totalGamesPlayed, SUM(CASE WHEN s.playedWithOthers THEN 1 ELSE 0 END) AS gamesPlayedWithOthers, SUM(CASE WHEN s.didWinGame THEN 1 ELSE 0 END) AS gamesWon FROM SnakeScore s JOIN s.user u GROUP BY u.username ORDER BY totalApplesEaten DESC")
    List<Object[]> getLeaderboard();

    @Query("SELECT s FROM SnakeScore s JOIN s.user u WHERE u = :user")
    List<SnakeScore> findByUser(@Param("user") User user);

}