package com.gameserver.snake.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gameserver.snake.models.Snake;
import com.gameserver.user.User;
import com.gameserver.user.UserService;

@Service
public class SnakeScoreService {

    @Autowired
    private UserService userService;

    @Autowired
    private SnakeScoreRepository snakeScoreRepository;

    public List<SnakeScore> getUserGames(String username) {
        User user = userService.getUserByUserName(username);
        return snakeScoreRepository.findByUser(user);
    }

    public List<SnakeScoreAggregate> getLeaderboard() {
        List<Object[]> rows = snakeScoreRepository.getLeaderboard();
        List<SnakeScoreAggregate> leaderboard = new ArrayList<>();
        for (Object[] row : rows) {
            String username = (String) row[0];
            int totalApplesEaten = ((Long)row[1]).intValue();
            int totalGamesPlayed = ((Long)row[2]).intValue();
            int gamesPlayedWithOthers = ((Long)row[3]).intValue();
            int gamesWon = ((Long)row[4]).intValue();
            SnakeScoreAggregate aggregatedSnakeScore = new SnakeScoreAggregate(username, totalApplesEaten, totalGamesPlayed, gamesPlayedWithOthers, gamesWon);
            leaderboard.add(aggregatedSnakeScore);
        }
        return leaderboard;
    }
    public void persistGameResult(Map<String, Snake> snakes, String winner) {
        List<SnakeScore> snakeScores = new ArrayList<>();
        boolean playedWithOthers = snakes.size() > 1 ? true : false;
        for (Map.Entry<String, Snake> snakeEntry : snakes.entrySet()) {
            String playerName = snakeEntry.getKey();
            User user = userService.getUserByUserName(playerName);
            int numApplesEaten = snakeEntry.getValue().getScore();
            SnakeScore snakeScore = new SnakeScore(user, numApplesEaten, playedWithOthers);
            if (playedWithOthers) {
                if (snakeEntry.getValue().getColor().equals(winner)) {
                    snakeScore.setDidWinGame(true);
                } else {
                    snakeScore.setDidWinGame(false);
                }
            }
            snakeScores.add(snakeScore);
        }
        snakeScoreRepository.saveAll(snakeScores);
    }

}
