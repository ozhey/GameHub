package com.gameserver.snake.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Snake {
    private static final int BASE_SPEED = 1;
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
    private int playerId;

    public Snake(int numOfPlayers, int playerId, Canvas canvas, int speed) {
        body = new ArrayList<>();
        this.body.add(new Point(
                (int) Math.floor(canvas.getWidth() / canvas.getScale() * (playerId + 1) / (numOfPlayers + 1)),
                (int) Math.floor(canvas.getHeight() / canvas.getScale() * 1 / 2)));
        this.body.add(new Point(
                (int) Math.floor(canvas.getWidth() / canvas.getScale() * (playerId + 1) / (numOfPlayers + 1)),
                (int) Math.floor(canvas.getHeight() / canvas.getScale() * 1 / 2) + 1));
        this.playerId = playerId;
        this.speed = speed;
        this.color = COLORS[playerId];
        this.score = 0;
        this.direction = DIRECTIONS.get("ArrowUp");
    }

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

    public void changeDir(String dir) {
        this.direction = DIRECTIONS.get(dir);
    }

    public void addHead(Point head) {
        this.body.add(0, head);
    }

    public void removeTail() {
        this.body.remove(this.body.size() - 1);
    }

    public Point getHead() {
        return this.body.get(0);
    }

    public List<Point> getBody() {
        return this.body;
    }

    public void setBody(List<Point> body) {
        this.body = body;
    }

    public void killSnake() {
        this.speed = 0;
    }

    public int getSpeed() {
        return this.speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Point getDirection() {
        return this.direction;
    }

    public void setDirection(Point direction) {
        this.direction = direction;
    }

    public int getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

}
