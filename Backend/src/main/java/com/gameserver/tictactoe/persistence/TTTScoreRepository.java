package com.gameserver.tictactoe.persistence;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gameserver.user.User;

@Repository
public interface TTTScoreRepository extends JpaRepository<TTTScore, Long> {

    @Query("SELECT new com.gameserver.tictactoe.persistence.TTTScoreAggregate(u.username, COUNT(t) AS gamesPlayed, SUM(CASE WHEN t.didWinGame THEN 1 ELSE 0 END) AS gamesWon) FROM TTTScore t JOIN t.user u GROUP BY u.username ORDER BY gamesWon DESC")
    List<TTTScoreAggregate> getLeaderboard();

    @Query("SELECT t FROM TTTScore t JOIN t.user u WHERE u = :user")
    List<TTTScore> findByUser(@Param("user") User user);

}