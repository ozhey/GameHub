package com.gameserver.tictactoe.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gameserver.user.User;
import com.gameserver.user.UserService;

/**
 * Service class for managing Tic Tac Toe scores and leaderboard.
 */
@Service
public class TTTScoreService {

    @Autowired
    private UserService userService;

    @Autowired
    private TTTScoreRepository tttScoreRepository;

    /**
     * Retrieves the list of Tic Tac Toe scores for a specific user.
     *
     * @param username The username of the user.
     * @return A list of TTTScore objects representing the user's games.
     */
    public List<TTTScore> getUserGames(String username) {
        User user = userService.getUserByUserName(username);
        return tttScoreRepository.findByUser(user);
    }

    /**
     * Retrieves the leaderboard of Tic Tac Toe scores.
     *
     * @return A list of TTTScoreAggregate objects representing the leaderboard.
     */
    public List<TTTScoreAggregate> getLeaderboard() {
        return tttScoreRepository.getLeaderboard();
    }

    /**
     * Persists the result of a Tic Tac Toe game.
     * Not available through the REST API.
     * Can only be accessed internally by a Tic Tac Toe game instance.
     *
     * @param winner  The character representing the winner of the game.
     * @param players A map of characters representing the players in the game, with
     *                their corresponding usernames.
     */
    public void persistGameResult(char winner, Map<Character, String> players) {
        List<TTTScore> tttScores = new ArrayList<>();
        for (Map.Entry<Character, String> playerEntry : players.entrySet()) {
            User user = userService.getUserByUserName(playerEntry.getValue());
            boolean didWin = winner == playerEntry.getKey().charValue() ? true : false;
            tttScores.add(new TTTScore(user, didWin));
        }
        tttScoreRepository.saveAll(tttScores);
    }

}
