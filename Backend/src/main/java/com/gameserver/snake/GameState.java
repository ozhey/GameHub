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
    private Timer timer = new Timer();;
    private String roomId;

    // game state attributes
    private List<Snake> snakes;
    private Apple apple;
    private int time;
    private Canvas canvas;
    private int playersCount;

    public GameState(int playersCount, String roomId) {
        this.roomId = roomId;
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

    public void start(SimpMessagingTemplate smp, int playersCount) {
        newGame(playersCount);
        startGameLoop(smp);
    }

    private void startGameLoop(SimpMessagingTemplate smp) {
        TimerTask step = new TimerTask() {
            public void run() {
                gameStep(smp);
            }
        };
        this.timer.scheduleAtFixedRate(step, 0L, 30L);

    }

    private void gameStep(SimpMessagingTemplate smp) {
        smp.convertAndSend("/topic/snake_room/" + roomId, this);
    }

    public void stopTimer() {
        this.timer.cancel();
        this.timer.purge();
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
