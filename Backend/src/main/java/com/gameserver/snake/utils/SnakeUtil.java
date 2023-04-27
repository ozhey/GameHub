package com.gameserver.snake.utils;

import java.util.List;

import com.gameserver.snake.models.Canvas;
import com.gameserver.snake.models.Point;
import com.gameserver.snake.models.Snake;

public final class SnakeUtil {

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private SnakeUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean checkSnakesHeadCollisionAndKill(List<Snake> snakes) {
        boolean didSnakeDie = false;
        for (int i = 0; i < snakes.size(); i++) {
            Snake snk = snakes.get(i);
            for (int j = 0; j < snakes.size(); j++) {
                Snake snk2 = snakes.get(j);
                if (snk.getHead().getX() == snk2.getHead().getX() && snk.getHead().getY() == snk2.getHead().getY()
                        && i != j) {
                    snk.killSnake();
                    snk2.killSnake();
                    didSnakeDie = true;
                }
            }
        }
        return didSnakeDie;
    }

    public static String getWinnerIfAllDead(List<Snake> snakes) {
        int maxScore = 0;
        boolean allDead = true;
        String potentialWinnerColor = null;
        for (Snake snake : snakes) {
            if (snake.getScore() == maxScore) {
                potentialWinnerColor = "Tie";
            } else if (snake.getScore() > maxScore) {
                potentialWinnerColor = snake.getColor();
                maxScore = snake.getScore();
            }
            maxScore = Math.max(maxScore, snake.getScore());
            if (snake.getSpeed() != 0) {
                allDead = false;
            }
        }
        if (allDead && snakes.size() == 1) {
            return "End";
        } else if (allDead) {
            return potentialWinnerColor;
        }
        return null;
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

}
