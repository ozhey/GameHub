package com.gameserver.snake.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gameserver.snake.models.Snake;
import com.gameserver.user.User;
import com.gameserver.user.UserService;

/**
 * Service class for Snake game scores.
 */
@Service
public class SnakeScoreService {

    @Autowired
    private UserService userService;

    @Autowired
    private SnakeScoreRepository snakeScoreRepository;

    /**
     * Retrieves the scores of a user for the Snake game.
     *
     * @param username The username of the user.
     * @return The list of SnakeScore objects representing the user's scores.
     */
    public List<SnakeScore> getUserGames(String username) {
        User user = userService.getUserByUserName(username);
        return snakeScoreRepository.findByUser(user);
    }

    /**
     * Retrieves the leaderboard of Snake game scores.
     *
     * @return The list of SnakeScoreAggregate objects representing the leaderboard.
     */
    public List<SnakeScoreAggregate> getLeaderboard() {
        return snakeScoreRepository.getLeaderboard();
    }

    /**
     * Persists the result of a Snake game.
     *
     * @param snakes The map of snakes participating in the game.
     * @param winner The color of the winning snake.
     */
    public void persistGameResult(Map<String, Snake> snakes, String winner) {
        List<SnakeScore> snakeScores = new ArrayList<>();
        boolean playedWithOthers = snakes.size() > 1;

        // Iterate through each snake in the game
        for (Map.Entry<String, Snake> snakeEntry : snakes.entrySet()) {
            String playerName = snakeEntry.getKey();
            User user = userService.getUserByUserName(playerName);
            int numApplesEaten = snakeEntry.getValue().getScore();

            // Create a SnakeScore object for the current snake
            SnakeScore snakeScore = new SnakeScore(user, numApplesEaten, playedWithOthers);

            // Set the 'didWinGame' property based on the winner of the game
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
