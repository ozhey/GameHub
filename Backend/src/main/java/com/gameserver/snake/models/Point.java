package com.gameserver.snake.models;

import java.util.Map;

public class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point other) {
        this.x = other.getX();
        this.y = other.getY();
    }

    /**
     * Checks if the current point collides with any snake in the specified map of
     * snakes.
     * 
     * @param snakes the map of snakes to check collision against
     * @return true if the point collides with any snake, false otherwise
     */
    public Boolean collidesWithAnySnake(Map<String, Snake> snakes) {
        for (Snake snake : snakes.values()) {
            for (Point segment : snake.getBody()) {
                if (this.x == segment.getX() && this.y == segment.getY()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return int
     */
    public int getX() {
        return x;
    }

    /**
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return int
     */
    public int getY() {
        return y;
    }

    /**
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @param o the object to be checked against
     * @return true if the argument is a point with the same coordinates as the
     *         argument
     */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Point)) {
            return false;
        }
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

}
