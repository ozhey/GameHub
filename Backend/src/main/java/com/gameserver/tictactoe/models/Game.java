package com.gameserver.tictactoe.models;

public class Game {
    private char[][] board;
    private char currentPlayer;
    private char winner;

    public Game() {
        this.board = new char[3][3];
        this.currentPlayer = 'X';
        this.winner = '-';
        initializeBoard();
    }

    private void initializeBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board[row][col] = '-';
            }
        }
    }

    public void makeMove(int row, int col) {
        if ((row < 0 || row > 2 || col < 0 || col > 2) || (board[row][col] != '-')) {
            return;
        }
        board[row][col] = this.currentPlayer;
        this.currentPlayer = this.currentPlayer == 'X' ? 'O' : 'X';
        this.winner = calcWinner();
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

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(char player) {
        currentPlayer = player;
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
}
