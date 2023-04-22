package com.gameserver.snake;

import java.util.List;

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

    public static Point createNewSnakeHead(Point currentHead, Point dir, Canvas canvas) {
        Point newHead = new Point(currentHead.getX() + dir.getX(), currentHead.getY() + dir.getY());

        // wall collision makes snake head go to the other side
        if (newHead.getX() * canvas.getScale() >= canvas.getWidth()) {
            return new Point(0, newHead.getY());
        } else if (newHead.getX() < 0) {
            return new Point((canvas.getWidth() / canvas.getScale()) - 1, newHead.getY());
        } else if (newHead.getY() * canvas.getScale() >= canvas.getHeight()) {
            return new Point(newHead.getX(), 0);
        } else if (newHead.getY() < 0) {
            return new Point(newHead.getX(), (canvas.getHeight() / canvas.getScale()) - 1);
        } else {
            return newHead;
        }
    }

    public Boolean collidesWithAnySnake(List<Snake> snakes) {
        for (Snake snake : snakes) {
            for (Point segment : snake.getBody()) {
                if (this.x == segment.getX() && this.y == segment.getY()) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

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
