package com.gameserver.tictactoe.models;

import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.gameserver.tictactoe.persistence.TTTScoreService;

/**
 * Represents a Tic Tac Toe game.
 */
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

    /**
     * Constructs a new Tic Tac Toe game.
     * 
     * @param smp             the SMT used for WebSocketcommunication
     * @param roomId          the ID of the game room
     * @param tttScoreService the service for managing Tic Tac Toe game scores
     */
    public Game(SimpMessagingTemplate smp, String roomId, TTTScoreService tttScoreService) {
        this.smp = smp;
        this.roomId = roomId;
        this.tttScoreService = tttScoreService;
        this.players = new HashMap<>();
        newGame();
    }

    /**
     * Resets the game by starting a new game.
     */
    public void resetGame() {
        newGame();
    }

    /**
     * Makes a move in the Tic Tac Toe game.
     * 
     * @param row      the row index of the move
     * @param col      the column index of the move
     * @param playerId the ID of the player making the move
     */
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
        if (this.winner != '-' && !this.players.get('O').equals(this.players.get('X'))) {
            tttScoreService.persistGameResult(this.winner, this.players);
        }
        this.smp.convertAndSend("/topic/ttt_room/" + roomId, this);
    }

    /**
     * Starts a new game by resetting the game state and initializing the board.
     * Notifies the players about the new game via WebSocket.
     */
    private void newGame() {
        this.board = new char[3][3];
        this.nextSymbol = 'X';
        this.winner = '-';
        initializeBoard();
        this.smp.convertAndSend("/topic/ttt_room/" + roomId, this);
    }

    /**
     * Initializes the game board with empty cells.
     */
    private void initializeBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board[row][col] = '-';
            }
        }
    }

    /**
     * Calculates the winner of the Tic Tac Toe game by checking all possible
     * winning combinations.
     *
     * @return the symbol of the winning player ('X' or 'O'), or '-' if there is no
     *         winner yet.
     */
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

    /**
     * Checks if the given line of cells has a winning combination.
     *
     * @param c1 the symbol in the first cell
     * @param c2 the symbol in the second cell
     * @param c3 the symbol in the third cell
     * @return the symbol of the winning player ('X' or 'O'), or '-' if there is no
     *         winner yet.
     */
    private char checkLine(char c1, char c2, char c3) {
        if (c1 != '-' && c1 == c2 && c2 == c3) {
            return c1;
        }
        return '-';
    }

    /**
     * Registers a player in the Tic Tac Toe game by assigning their username to a
     * player symbol.
     * If the first player registers, they will be assigned 'X'.
     * If the second player registers, they will be assigned 'O' and the game data
     * will be sent to both of them via WebSocket.
     *
     * @param username the username of the player
     */
    public void registerPlayer(String username) {
        if (this.players.size() == 0) {
            players.put('X', username);
            return;
        }
        players.put('O', username);
        // second player registered, send them the game data
        this.smp.convertAndSend("/topic/ttt_room/" + roomId, this);
    }

    
    /** 
     * @return char[][]
     */
    public char[][] getBoard() {
        return board;
    }

    
    /** 
     * @param newBoard
     */
    public void setBoard(char[][] newBoard) {
        board = newBoard;
    }

    
    /** 
     * @return char
     */
    public char getWinner() {
        return this.winner;
    }

    
    /** 
     * @param winner
     */
    public void setWinner(char winner) {
        this.winner = winner;
    }

    
    /** 
     * @return char
     */
    public char getNextSymbol() {
        return this.nextSymbol;
    }

    
    /** 
     * @param nextSymbol
     */
    public void setNextSymbol(char nextSymbol) {
        this.nextSymbol = nextSymbol;
    }

    
    /** 
     * @return Map<Character, String>
     */
    public Map<Character, String> getPlayers() {
        return this.players;
    }

}
