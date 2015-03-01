package com.zenappse.memorymatcher;

/**
 * Created by Patrick Ganson on 2/22/15.
 *
 * Copyright 2015
 */
public class GameState {

    private GameStorageManager gameStorageManager;

    private GameController gameController;
    private GameGridCardDeck gameGridCardDeck;

    private int currentScore;
    private int highscore;
    private String recordHolder;

    private String username;

    public GameState(GameStorageManager gameStorageManager) {
        gameController = null;
        gameGridCardDeck = null;

        this.gameStorageManager = gameStorageManager;

        currentScore = 0;
        highscore = 0;

        recordHolder = "";

        username = "";
    }

    public boolean saveState(){
        boolean saveSuccessful;
        saveSuccessful = gameStorageManager.setGameGridCardDeck(gameGridCardDeck);
        saveSuccessful = (gameStorageManager.setGameController(gameController, username) && saveSuccessful);
        saveSuccessful = (gameStorageManager.setHighScore(highscore) && saveSuccessful);
        saveSuccessful = (gameStorageManager.setCurrentScore(currentScore) && saveSuccessful);
        saveSuccessful = (gameStorageManager.setRecordHolder(recordHolder) && saveSuccessful);
        return saveSuccessful;
    }

    public void loadState(){
        gameGridCardDeck = gameStorageManager.getGameGridCardDeck();
        gameController = gameStorageManager.getGameController(username);
        currentScore = gameStorageManager.getCurrentScore();
        highscore = gameStorageManager.getHighScore();
        recordHolder = gameStorageManager.getRecordHolder();
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public int getHighscore() {
        return highscore;
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;
    }

    public String getRecordHolder() {
        return recordHolder;
    }

    public void setRecordHolder(String recordHolder) {
        this.recordHolder = recordHolder;
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public GameGridCardDeck getGameGridCardDeck() {
        return gameGridCardDeck;
    }

    public void setGameGridCardDeck(GameGridCardDeck gameGridCardDeck) {
        this.gameGridCardDeck = gameGridCardDeck;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
