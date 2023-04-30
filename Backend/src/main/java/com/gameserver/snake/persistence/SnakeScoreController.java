package com.gameserver.snake.persistence;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/snake/scores")
public class SnakeScoreController {

    @Autowired
    private SnakeScoreService snakeScoreService;

    @GetMapping("/leaderboard")
    public List<SnakeScoreAggregate> getLeaderboard() {
        return snakeScoreService.getLeaderboard();
    }

    @GetMapping()
    public List<SnakeScore> getUserScores(@RequestParam String username) {
        return snakeScoreService.getUserGames(username);
    }

}
