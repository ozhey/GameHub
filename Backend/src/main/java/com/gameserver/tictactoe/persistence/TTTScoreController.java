package com.gameserver.tictactoe.persistence;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/tic_tac_toe/scores")
public class TTTScoreController {

    @Autowired
    private TTTScoreService tttScoreService;

    @GetMapping("/leaderboard")
    public List<TTTScoreAggregate> getLeaderboard() {
        return tttScoreService.getLeaderboard();
    }

    @GetMapping()
    public List<TTTScore> getUserScores(@RequestParam String username) {
        return tttScoreService.getUserGames(username);
    }

}
