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

    
    /** 
     * @return String
     */
    public String getCommand() {
        return this.command;
    }

    
    /** 
     * @param command
     */
    public void setCommand(String command) {
        this.command = command;
    }

    
    /** 
     * @return String
     */
    public String getContent() {
        return this.content;
    }

    
    /** 
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    
    /** 
     * @return int
     */
    public int getRow() {
        return this.row;
    }

    
    /** 
     * @param row
     */
    public void setRow(int row) {
        this.row = row;
    }

    
    /** 
     * @return int
     */
    public int getCol() {
        return this.col;
    }

    
    /** 
     * @param col
     */
    public void setCol(int col) {
        this.col = col;
    }

}
