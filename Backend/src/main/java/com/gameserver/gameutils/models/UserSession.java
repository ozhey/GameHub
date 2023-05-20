package com.gameserver.gameutils.models;

/**
 * Represents a user session in one of the games.
 */
public class UserSession {
    private String roomId;
    private String playerId;

    /**
     * Constructs a user session object with the specified room ID and player ID.
     * 
     * @param roomId   the ID of the room
     * @param playerId the ID of the player
     */
    public UserSession(String roomId, String playerNum) {
        this.roomId = roomId;
        this.playerId = playerNum;
    }

    
    /** 
     * @return String
     */
    public String getRoomId() {
        return this.roomId;
    }

    
    /** 
     * @param roomId
     */
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    
    /** 
     * @return String
     */
    public String getPlayerId() {
        return this.playerId;
    }

    
    /** 
     * @param playerId
     */
    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

}
