package com.gameserver.tictactoe.models;

public class Move {
    private String command;
    private int row;
    private int col;

    public Move(String command, int row, int col) {
        this.command = command;
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return this.row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return this.col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

}
