package com.gameserver.snake;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.messaging.simp.SimpMessagingTemplate;

public class GameState {

    // internal attributes
    private static final int INTERVAL = 30;
    private static final int BASE_SPEED = 180;
    private static final int MAX_SPEED = 90;
    private Timer timer = null;
    private TimerTask timerTask;
    private String roomId;
    private SimpMessagingTemplate smp;

    // game state attributes
    private List<Snake> snakes;
    private Apple apple;
    private int time;
    private Canvas canvas;
    private int playersCount;

    public GameState(SimpMessagingTemplate smp, int playersCount, String roomId) {
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
        List<Snake> newSnakes = new ArrayList<>(this.snakes.size());
        Boolean shouldCreateApple = false;
        Boolean didSnakeDie = false;
        for (Snake snake : this.snakes) {
            Snake newSnake = new Snake(snake);
            if (this.time % newSnake.getSpeed() == 0) {
                Point newHead = Point.createNewSnakeHead(snake.getHead(), snake.getDirection(), this.canvas);
                if (newHead.equals(newSnake.getHead())) {
                    Point oppositeDir = new Point(newSnake.getDirection().getX() * -1,
                            newSnake.getDirection().getY() * -1);
                    newSnake.setDirection(oppositeDir);
                    newHead = Point.createNewSnakeHead(snake.getHead(), snake.getDirection(), this.canvas);
                }
                if (newHead.collidesWithAnySnake(this.snakes)) { //check collision with old snakes
                    newSnake.setSpeed(0);
                    newSnakes.add(newSnake);
                    didSnakeDie = true;
                    continue;        
                }
                newSnake.addHead(newHead);
                if (newHead.equals(apple.getLocation())) { // snake ate an apple
                    shouldCreateApple = true;
                    newSnake.setScore(newSnake.getScore() + 1);
                    newSnake.setSpeed(Math.max((newSnake.getScore() % 5 == 0) ? BASE_SPEED - newSnake.getScore() * 6 : newSnake.getSpeed(), MAX_SPEED));
                } else {
                    newSnake.removeTail();
                }
            }
            newSnakes.add(newSnake);
        }
        this.snakes = newSnakes;
        this.time += INTERVAL;
        this.smp.convertAndSend("/topic/snake_room/" + roomId, this);
    }

    public void resetTimer() {
        if (this.timer != null) {
            this.timer.cancel();
        }
        this.timer = new Timer();
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

    public int getTime() {
        return this.time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Canvas getCanvas() {
        return this.canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public int getPlayersCount() {
        return this.playersCount;
    }

    public void setPlayersCount(int playersCount) {
        this.playersCount = playersCount;
    }

}
