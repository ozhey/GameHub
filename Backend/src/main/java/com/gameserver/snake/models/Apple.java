package com.gameserver.snake.models;

import java.util.Map;

public class Apple {
    private Point location;

    public Apple(Map<String, Snake> snakes, Canvas canvas) {
        do {
            int x = (int) Math.floor(Math.random() * canvas.getWidth() / canvas.getScale());
            int y = (int) Math.floor(Math.random() * canvas.getHeight() / canvas.getScale());
            this.location = new Point(x, y);
        } while (checkCollision(location, snakes));
    }

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
