package com.gameserver.snake.persistence;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class that handles HTTP requests related to Snake game scores.
 */
@RestController
@RequestMapping(path = "api/v1/snake/scores")
public class SnakeScoreController {

    @Autowired
    private SnakeScoreService snakeScoreService;

    /**
     * Retrieves the leaderboard of Snake game scores.
     *
     * @return The list of SnakeScoreAggregate objects representing the leaderboard.
     */
    @GetMapping("/leaderboard")
    public List<SnakeScoreAggregate> getLeaderboard() {
        return snakeScoreService.getLeaderboard();
    }

    /**
     * Retrieves the scores of a user for the Snake game.
     *
     * @param username The username of the user.
     * @return The list of SnakeScore objects representing the user's scores.
     */
    @GetMapping()
    public List<SnakeScore> getUserScores(@RequestParam String username) {
        return snakeScoreService.getUserGames(username);
    }

}
