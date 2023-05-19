package com.gameserver.tictactoe.persistence;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Tic Tac Toe scores.
 */
@RestController
@RequestMapping(path = "api/v1/tic_tac_toe/scores")
public class TTTScoreController {

    @Autowired
    private TTTScoreService tttScoreService;

    /**
     * Retrieves the leaderboard of Tic Tac Toe scores.
     *
     * @return A list of TTTScoreAggregate objects representing the response.
     */
    @GetMapping("/leaderboard")
    public List<TTTScoreAggregate> getLeaderboard() {
        return tttScoreService.getLeaderboard();
    }

    /**
     * Retrieves the Tic Tac Toe scores for a specific user.
     *
     * @param username The username of the user.
     * @return A list of TTTScore objects representing the response.
     */
    @GetMapping()
    public List<TTTScore> getUserScores(@RequestParam String username) {
        return tttScoreService.getUserGames(username);
    }

}
