package com.gameserver.snake.models;

public class Canvas {
    private int width;
    private int height;
    private int scale;
    private String color;
    private static final int BASE_WIDTH = 15;
    private static final int BASE_HEIGHT = 15;
    private static final int BASE_SCALE = 1;
    private static final String BASE_COLOR = "oldlace";

    /**
     * Creates a new canvas with size according to the specified number of players.
     *
     * @param numOfPlayers The number of players.
     */
    public Canvas(int numOfPlayers) {
        this.width = BASE_WIDTH + numOfPlayers * 3;
        this.height = BASE_HEIGHT + numOfPlayers * 3;
        this.color = BASE_COLOR;
        this.scale = BASE_SCALE;
    }

    
    /** 
     * @return int
     */
    public int getWidth() {
        return this.width;
    }

    
    /** 
     * @param width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    
    /** 
     * @return int
     */
    public int getHeight() {
        return this.height;
    }

    
    /** 
     * @param height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    
    /** 
     * @return String
     */
    public String getColor() {
        return this.color;
    }

    
    /** 
     * @param color
     */
    public void setColor(String color) {
        this.color = color;
    }

    
    /** 
     * @return int
     */
    public int getScale() {
        return this.scale;
    }

    
    /** 
     * @param scale
     */
    public void setScale(int scale) {
        this.scale = scale;
    }

}
