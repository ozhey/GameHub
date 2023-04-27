package com.gameserver.snake.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.gameserver.snake.utils.SnakeUtil;

public class Game {

    // internal attributes
    private static final int INTERVAL = 30;
    private static final int BASE_SPEED = 180;
    private static final int MAX_SPEED = 90;
    private Timer timer = null;
    private String roomId;
    private SimpMessagingTemplate smp;
    private String lastSurvivor;
    
    // game state attributes
    private int time;
    private List<Snake> snakes;
    private Apple apple;
    private Canvas canvas;
    private int playersCount;
    private String winner;

    public Game(SimpMessagingTemplate smp, int playersCount, String roomId) {
        this.roomId = roomId;
        this.smp = smp;
        newGame(playersCount);
    }

    private void newGame(int playersCount) {
        this.playersCount = playersCount;
        this.canvas = new Canvas(playersCount);
        this.time = 0;
        this.snakes = new ArrayList<>(playersCount);
        for (int i = 0; i < playersCount; i++) {
            this.snakes.add(new Snake(playersCount, i, canvas, BASE_SPEED));
        }
        this.winner = null;
        this.apple = new Apple(snakes, canvas);
    }

    public void start(int playersCount) {
        resetTimer();
        newGame(playersCount);
        this.timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                gameStep();
            }
        }, 600L, 30L);
    }

    private void gameStep() {
        if (this.winner != null) {
            resetTimer();
        }
        List<Snake> newSnakes = new ArrayList<>(this.snakes.size());
        boolean shouldCreateApple = false;
        boolean didSnakeDie = false;
        for (Snake snake : this.snakes) {
            Snake newSnake = new Snake(snake);
            if (newSnake.getSpeed() != 0 && this.time % newSnake.getSpeed() == 0) {
                Point newHead = SnakeUtil.createNewSnakeHead(newSnake.getHead(), newSnake.getDirection(), this.canvas);
                if (newHead.equals(newSnake.getBody().get(1))) {
                    Point oppositeDir = new Point(newSnake.getDirection().getX() * -1,
                            newSnake.getDirection().getY() * -1);
                    newSnake.setDirection(oppositeDir);
                    newHead = SnakeUtil.createNewSnakeHead(newSnake.getHead(), oppositeDir, this.canvas);
                }
                if (newHead.collidesWithAnySnake(this.snakes)) { // check collision with old snakes
                    newSnake.killSnake();
                    newSnakes.add(newSnake);
                    didSnakeDie = true;
                    continue;
                }
                newSnake.addHead(newHead);
                if (newHead.equals(apple.getLocation())) { // snake ate an apple
                    shouldCreateApple = true;
                    newSnake.setScore(newSnake.getScore() + 1);
                    newSnake.setSpeed(Math.max(
                            (newSnake.getScore() % 5 == 0) ? BASE_SPEED - newSnake.getScore() * 6 : newSnake.getSpeed(),
                            MAX_SPEED));
                } else {
                    newSnake.removeTail();
                }
            }

            newSnakes.add(newSnake);
        }
        if (SnakeUtil.checkSnakesHeadCollisionAndKill(newSnakes)) {
            didSnakeDie = true;
        }
        this.snakes = newSnakes;
        if (shouldCreateApple) {
            this.apple = new Apple(this.snakes, canvas);
        }
        this.time += INTERVAL;
        if (didSnakeDie) {
            this.winner = SnakeUtil.getWinnerIfAllDead(this.snakes);
        }
        this.smp.convertAndSend("/topic/snake_room/" + roomId, this);
    }

    public void resetTimer() {
        if (this.timer != null) {
            this.timer.cancel();
        }
        this.timer = new Timer();
    }

    public void changeSnakeDir(int playerId, String dir) {
        this.snakes.get(playerId).changeDir(dir);
    }

    public List<Snake> getSnakes() {
        return this.snakes;
    }

    public void setSnakes(List<Snake> snakes) {
        this.snakes = snakes;
    }

    public Apple getApple() {
        return this.apple;
    }

    public void setApple(Apple apple) {
        this.apple = apple;
    }

    public Canvas getCanvas() {
        return this.canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public String getWinner() {
        return this.winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public int getPlayersCount() {
        return this.playersCount;
    }

    public void setPlayersCount(int playersCount) {
        this.playersCount = playersCount;
    }

    public int getTime() {
        return this.time;
    }

}
