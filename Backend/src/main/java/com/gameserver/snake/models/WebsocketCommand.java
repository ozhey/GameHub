package com.gameserver.snake.models;

public class WebsocketCommand {
    private String type;
    private String content;

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
