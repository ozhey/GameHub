package com.gameserver.tictactoe.models;

import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.gameserver.tictactoe.persistence.TTTScoreService;

public class Game {

    // internal attributes - not sent over websocket
    private SimpMessagingTemplate smp;
    private String roomId;
    private TTTScoreService tttScoreService;

    // game state attributes - sent over websocket
    private char[][] board;
    private char nextSymbol;
    private char winner;
    private Map<Character, String> players;

    public Game(SimpMessagingTemplate smp, String roomId, TTTScoreService tttScoreService) {
        this.smp = smp;
        this.roomId = roomId;
        this.tttScoreService = tttScoreService;
        this.players = new HashMap<>();
        newGame();
    }

    public void resetGame() {
        newGame();
    }

    public void makeMove(int row, int col, String playerId) {
        if ((row < 0 || row > 2 || col < 0 || col > 2) || (board[row][col] != '-')) {
            return;
        }

        // validate that it's the players turn
        if (!this.players.get(this.nextSymbol).equals(playerId)) {
            return;
        }

        board[row][col] = this.nextSymbol;
        this.nextSymbol = this.nextSymbol == 'X' ? 'O' : 'X';
        this.winner = calcWinner();
        // don't persist if player played against himself
        if (this.winner != '-' && this.players.get('X') != this.players.get('O')) {
            tttScoreService.persistGameResult(this.winner, this.players);
        }
        this.smp.convertAndSend("/topic/ttt_room/" + roomId, this);
    }

    private void newGame() {
        this.board = new char[3][3];
        this.nextSymbol = 'X';
        this.winner = '-';
        initializeBoard();
        this.smp.convertAndSend("/topic/ttt_room/" + roomId, this);
    }

    private void initializeBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board[row][col] = '-';
            }
        }
    }

    private char calcWinner() {
        for (int row = 0; row < 3; row++) {
            char winner = checkLine(board[row][0], board[row][1], board[row][2]);
            if (winner != '-') {
                return winner;
            }
        }

        for (int col = 0; col < 3; col++) {
            char winner = checkLine(board[0][col], board[1][col], board[2][col]);
            if (winner != '-') {
                return winner;
            }
        }

        char winner = checkLine(board[0][0], board[1][1], board[2][2]);
        if (winner != '-') {
            return winner;
        }

        return checkLine(board[0][2], board[1][1], board[2][0]);
    }

    private char checkLine(char c1, char c2, char c3) {
        if (c1 != '-' && c1 == c2 && c2 == c3) {
            return c1;
        }
        return '-';
    }

    public void registerPlayer(String username) {
        if (this.players.size() == 0) {
            players.put('X', username);
            return;
        }
        players.put('O', username);
        // second player registered, send them the game data
        this.smp.convertAndSend("/topic/ttt_room/" + roomId, this);
    }

    public char[][] getBoard() {
        return board;
    }

    public void setBoard(char[][] newBoard) {
        board = newBoard;
    }

    public char getWinner() {
        return this.winner;
    }

    public void setWinner(char winner) {
        this.winner = winner;
    }

    public char getNextSymbol() {
        return this.nextSymbol;
    }

    public void setNextSymbol(char nextSymbol) {
        this.nextSymbol = nextSymbol;
    }

    public Map<Character, String> getPlayers() {
        return this.players;
    }

}
