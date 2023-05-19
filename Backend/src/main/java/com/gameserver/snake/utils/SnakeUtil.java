package com.gameserver.snake.utils;

import java.util.Map;

import com.gameserver.snake.models.Canvas;
import com.gameserver.snake.models.Point;
import com.gameserver.snake.models.Snake;

/**
 * This class provides utility methods for the Snake game.
 */
public final class SnakeUtil {

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private SnakeUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Checks if any snakes have collided with each other. If so, the snakes are
     * killed.
     * 
     * @param snakes The map of snakes.
     * @return True if any snakes collided, false otherwise.
     */
    public static boolean checkSnakesHeadCollisionAndKill(Map<String, Snake> snakes) {
        boolean didSnakeDie = false;
        for (Snake snk : snakes.values()) {
            for (Snake snk2 : snakes.values()) {
                if (snk.getHead().getX() == snk2.getHead().getX() && snk.getHead().getY() == snk2.getHead().getY()
                        && snk != snk2) {
                    snk.killSnake();
                    snk2.killSnake();
                    didSnakeDie = true;
                }
            }
        }

        return didSnakeDie;
    }

    /**
     * Gets the winner of the game, if any. If all snakes are dead, the winner is
     * the snake with the highest score.
     * 
     * @param snakes The map of snakes.
     * @return The winner of the game, or null if there is no winner.
     */
    public static String getWinnerIfAllDead(Map<String, Snake> snakes) {
        int maxScore = 0;
        boolean allDead = true;
        String potentialWinnerColor = null;
        for (Snake snake : snakes.values()) {
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

    /**
     * Creates a new head for the snake, based on the current head, direction, and
     * canvas.
     * 
     * @param currentHead The current head of the snake.
     * @param dir         The direction of the snake.
     * @param canvas      The canvas on which the snake is drawn.
     * @return The new head of the snake.
     */
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
