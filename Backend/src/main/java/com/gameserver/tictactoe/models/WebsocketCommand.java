package com.gameserver.tictactoe.models;

/**
 * Represents a WebSocket command for the Tic Tac Toe game.
 * It is the only command structure that clients can use for this game.
 */
public class WebsocketCommand {
    private String command;
    private String content;
    private int row;
    private int col;

    /**
     * Constructs a WebsocketCommand object with the specified command, content,
     * row, and column.
     *
     * @param command The command associated with the WebSocket message.
     * @param content The content of the WebSocket message.
     * @param row     The row index associated with the command.
     * @param col     The column index associated with the command.
     */
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
