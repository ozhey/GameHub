package com.gameserver.snake.models;

public class Session {
    private String roomId;
    private int playerId;

    public Session(String roomId, int playerNum) {
        this.roomId = roomId;
        this.playerId = playerNum;
    }

    public String getRoomId() {
        return this.roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId(int playerNum) {
        this.playerId = playerNum;
    }

}
