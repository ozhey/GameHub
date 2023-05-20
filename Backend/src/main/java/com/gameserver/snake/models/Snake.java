package com.gameserver.snake.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a snake in the game.
 */
public class Snake {
    private static final String[] COLORS = { "Green", "Brown", "Aquamarine", "Pink", "Gold", "Orange", "Red", "Blue",
            "Purple", "Cyan" };
    private static final Map<String, Point> DIRECTIONS = Map.of(
            "ArrowUp", new Point(0, -1),
            "ArrowDown", new Point(0, 1),
            "ArrowLeft", new Point(-1, 0),
            "ArrowRight", new Point(1, 0));
    private List<Point> body;
    private int speed;
    private String color;
    private int score;
    private Point direction;
    private String playerId;

    /**
     * Constructs a snake object with the specified parameters.
     * 
     * @param numOfPlayers the total number of players in the game
     * @param playerNum    the player number of the snake, dictates the color
     * @param playerId     the ID of the player controlling the snake
     * @param canvas       the canvas object representing the game area
     * @param speed        the initial speed of the snake
     */
    public Snake(int numOfPlayers, int playerNum, String playerId, Canvas canvas, int speed) {
        body = new ArrayList<>();
        this.body.add(new Point(
                (int) Math.floor(canvas.getWidth() / canvas.getScale() * (playerNum + 1) / (numOfPlayers + 1)),
                (int) Math.floor(canvas.getHeight() / canvas.getScale() * 1 / 2)));
        this.body.add(new Point(
                (int) Math.floor(canvas.getWidth() / canvas.getScale() * (playerNum + 1) / (numOfPlayers + 1)),
                (int) Math.floor(canvas.getHeight() / canvas.getScale() * 1 / 2) + 1));
        this.playerId = playerId;
        this.speed = speed;
        this.color = COLORS[playerNum];
        this.score = 0;
        this.direction = DIRECTIONS.get("ArrowUp");
    }

    /**
     * Constructs a copy of the specified snake object.
     * 
     * @param other the snake to be copied
     */
    public Snake(Snake other) {
        this.body = new ArrayList<>();
        for (Point point : other.body) { // deep copy
            this.body.add(new Point(point));
        }
        this.speed = other.getSpeed();
        this.color = other.getColor();
        this.score = other.getScore();
        this.playerId = other.playerId;
        this.direction = new Point(other.getDirection());
    }

    /**
     * 
     * Changes the direction of the snake based on the specified direction string.
     * 
     * @param dir the new direction for the snake ("ArrowUp", "ArrowDown",
     *            "ArrowLeft", or "ArrowRight")
     */
    public void changeDir(String dir) {
        if (DIRECTIONS.get(dir) != null) {
            this.direction = DIRECTIONS.get(dir);
        }
    }

    /**
     * Adds a new head point to the snake's body.
     * 
     * @param head the new head point to add
     */
    public void addHead(Point head) {
        this.body.add(0, head);
    }

    /**
     * Removes the tail point from the snake's body.
     */
    public void removeTail() {
        this.body.remove(this.body.size() - 1);
    }

    /**
     * Retrieves the head point of the snake.
     * 
     * @return the head point
     */
    public Point getHead() {
        return this.body.get(0);
    }

    
    /** 
     * @return List<Point>
     */
    public List<Point> getBody() {
        return this.body;
    }

    
    /** 
     * @param body
     */
    public void setBody(List<Point> body) {
        this.body = body;
    }

    public void killSnake() {
        this.speed = 0;
    }

    
    /** 
     * @return int
     */
    public int getSpeed() {
        return this.speed;
    }

    
    /** 
     * @param speed
     */
    public void setSpeed(int speed) {
        this.speed = speed;
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
    public int getScore() {
        return this.score;
    }

    
    /** 
     * @param score
     */
    public void setScore(int score) {
        this.score = score;
    }

    
    /** 
     * @return Point
     */
    public Point getDirection() {
        return this.direction;
    }

    
    /** 
     * @param direction
     */
    public void setDirection(Point direction) {
        this.direction = direction;
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
