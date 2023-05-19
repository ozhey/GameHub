package com.gameserver.snake.models;

import java.util.Map;

/**
 * This class represents an apple in the Snake game.
 */
public class Apple {
    private Point location;

    /**
     * Creates a new apple at a random location on the canvas.
     * 
     * @param snakes The map of snakes.
     * @param canvas The canvas on which the apple is drawn.
     */
    public Apple(Map<String, Snake> snakes, Canvas canvas) {
        do {
            int x = (int) Math.floor(Math.random() * canvas.getWidth() / canvas.getScale());
            int y = (int) Math.floor(Math.random() * canvas.getHeight() / canvas.getScale());
            this.location = new Point(x, y);
        } while (checkCollision(location, snakes));
    }

    /**
     * Checks if the apple is colliding with any snakes.
     * 
     * @param snakes The map of snakes.
     * @return True if the apple is colliding with any snakes, false otherwise.
     */
    private Boolean checkCollision(Point newApplePoint, Map<String, Snake> snakes) {
        for (Snake snake : snakes.values()) {
            for (Point segment : snake.getBody()) {
                if (newApplePoint.getX() == segment.getX() && newApplePoint.getY() == segment.getY()) {
                    return true;
                }
            }
        }
        return false;
    }

    public Point getLocation() {
        return this.location;
    }

    public void setLocation(Point appleCoordinates) {
        this.location = appleCoordinates;
    }

}
