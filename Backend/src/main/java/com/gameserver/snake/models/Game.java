package com.gameserver.snake.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.gameserver.snake.persistence.SnakeScoreService;
import com.gameserver.snake.utils.SnakeUtil;

/**
 * The Game class represents a game instance. It manages the game state,
 * including the players, snakes, apple, canvas, and
 * other attributes. The game logic is executed in steps, and the state changes
 * are communicated over a WebSocket.
 */
public class Game {

    // internal attributes - not sent over websocket
    private static final int INTERVAL = 30;
    private static final int BASE_SPEED = 180;
    private static final int MAX_SPEED = 90;
    private Timer timer = null;
    private String roomId;
    private SimpMessagingTemplate smp;
    private SnakeScoreService snakeScoreService;

    // game state attributes - sent over websocket
    private int time;
    private Map<String, Snake> snakes;
    private Apple apple;
    private Canvas canvas;
    private int playersCount;
    private String winner;

    /**
     * Constructs a new Game instance.
     * 
     * @param smp               the SimpMessagingTemplate for sending messages over
     *                          WebSocket
     * @param players           the list of player names
     * @param roomId            the unique identifier of the game room
     * @param snakeScoreService the service for managing snake scores
     */
    public Game(SimpMessagingTemplate smp, ArrayList<String> players, String roomId,
            SnakeScoreService snakeScoreService) {
        this.snakeScoreService = snakeScoreService;
        this.roomId = roomId;
        this.smp = smp;
        newGame(players);
    }

    /**
     * Initializes a new game with the specified players.
     * 
     * @param players the list of player names
     */
    private void newGame(ArrayList<String> players) {
        this.playersCount = players.size();
        this.canvas = new Canvas(playersCount);
        this.time = 0;
        this.snakes = new HashMap<>(playersCount);
        for (int i = 0; i < playersCount; i++) {
            this.snakes.put(players.get(i), new Snake(playersCount, i, players.get(i), canvas, BASE_SPEED));
        }
        this.winner = null;
        this.apple = new Apple(snakes, canvas);
    }

    /**
     * Resets the game with the specified players.
     * 
     * @param players the list of player names
     */
    public void resetGame(ArrayList<String> players) {
        resetTimer();
        newGame(players);
    }

    /**
     * Starts the game with the specified players.
     * This method will start a timer that ticks every 30ms the gamestep function.
     * 
     * @param players the list of player names
     */
    public void start(ArrayList<String> players) {
        this.timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                gameStep();
            }
        }, 600L, 30L);
    }

    /**
     * Performs a single step of the game logic.
     */
    private void gameStep() {
        if (this.winner != null) {
            resetTimer();
            snakeScoreService.persistGameResult(this.snakes, this.winner);
        }
        Map<String, Snake> newSnakes = new HashMap<>(this.snakes.size());
        boolean shouldCreateApple = false;
        boolean didSnakeDie = false;
        for (Map.Entry<String, Snake> snakeEntry : this.snakes.entrySet()) {
            Snake newSnake = new Snake(snakeEntry.getValue());
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
                    newSnakes.put(snakeEntry.getKey(), newSnake);
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

            newSnakes.put(snakeEntry.getKey(), newSnake);
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

    /**
     * Resets the game timer.
     */
    public void resetTimer() {
        if (this.timer != null) {
            this.timer.cancel();
        }
        this.timer = new Timer();
    }

    /**
     * Changes the direction of the snake for the specified player.
     * 
     * @param playerId the ID of the player controlling the snake
     * @param dir      the new direction for the snake
     */
    public void changeSnakeDir(String playerId, String dir) {
        this.snakes.get(playerId).changeDir(dir);
    }

    /**
     * Retrieves a map that maps each snake's player ID to its color.
     * 
     * @return a map of player ID to color
     */
    public Map<String, String> FetchSnakePlayerIdToColor() {
        Map<String, String> playerIdToColor = new HashMap<>(this.snakes.size());
        for (Map.Entry<String, Snake> snakeEntry : this.snakes.entrySet()) {
            playerIdToColor.put(snakeEntry.getKey(), snakeEntry.getValue().getColor());
        }
        return playerIdToColor;
    }

    
    /** 
     * @return Map<String, Snake>
     */
    public Map<String, Snake> getSnakes() {
        return this.snakes;
    }

    
    /** 
     * @param snakes
     */
    public void setSnakes(Map<String, Snake> snakes) {
        this.snakes = snakes;
    }

    
    /** 
     * @return Apple
     */
    public Apple getApple() {
        return this.apple;
    }

    
    /** 
     * @param apple
     */
    public void setApple(Apple apple) {
        this.apple = apple;
    }

    
    /** 
     * @return Canvas
     */
    public Canvas getCanvas() {
        return this.canvas;
    }

    
    /** 
     * @param canvas
     */
    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    
    /** 
     * @return String
     */
    public String getWinner() {
        return this.winner;
    }

    
    /** 
     * @param winner
     */
    public void setWinner(String winner) {
        this.winner = winner;
    }

    
    /** 
     * @return int
     */
    public int getPlayersCount() {
        return this.playersCount;
    }

    
    /** 
     * @param playersCount
     */
    public void setPlayersCount(int playersCount) {
        this.playersCount = playersCount;
    }

    
    /** 
     * @return int
     */
    public int getTime() {
        return this.time;
    }

}
