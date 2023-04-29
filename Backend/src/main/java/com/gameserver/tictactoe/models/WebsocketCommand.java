package com.gameserver.tictactoe.models;

public class WebsocketCommand {
    private String command;
    private String content;
    private int row;
    private int col;

    public WebsocketCommand(String command, String content, int row, int col) {
        this.command = command;
        this.content = content;
        this.row = row;
        this.col = col;
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
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

}
