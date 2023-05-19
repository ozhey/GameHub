package com.gameserver.snake.models;

/**
 * 
 * Represents a websocket command for the Snake game.
 */
public class WebsocketCommand {
    private String type;
    private String content;

    /**
     * Constructs a websocket command object with the specified type and content.
     * 
     * @param type    the type of the command. The options are "startGame",
     *                "changeDir", and "registerPlayer"
     * @param content the content of the command
     */
    public WebsocketCommand(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
