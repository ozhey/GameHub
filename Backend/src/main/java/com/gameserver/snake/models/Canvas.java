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

    public Canvas(int numOfPlayers) {
        this.width = BASE_WIDTH + numOfPlayers * 3;
        this.height = BASE_HEIGHT + numOfPlayers * 3;
        this.color = BASE_COLOR;
        this.scale = BASE_SCALE;
    }


    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getScale() {
        return this.scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

}
