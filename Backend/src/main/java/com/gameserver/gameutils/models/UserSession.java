package com.gameserver.gameutils.models;

public class UserSession {
    private String roomId;
    private String playerId;

    public UserSession(String roomId, String playerNum) {
        this.roomId = roomId;
        this.playerId = playerNum;
    }

    public String getRoomId() {
        return this.roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

}
