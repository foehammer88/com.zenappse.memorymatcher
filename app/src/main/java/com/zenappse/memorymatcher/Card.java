package com.zenappse.memorymatcher;

import java.io.Serializable;

/**
 * Created by Patrick Ganson on 2/21/15.
 *
 * Copyright 2015
 */
public class Card implements Serializable{

    private String cardValue = "";
    private boolean isRed = true;
    private boolean isFlipped = false;
    private int position = 0;
    private int gameBoard = 0;
    private int viewId = 0;

    public Card(String cardValue, boolean isRed, boolean isFlipped, int position, int gameBoard) {
        this.cardValue = cardValue;
        this.isRed = isRed;
        this.isFlipped = isFlipped;

        this.position = position;
        this.gameBoard = gameBoard;
    }

    public boolean isRed() {
        return isRed;
    }

    public void setRed(boolean isRed) {
        this.isRed = isRed;
    }

    public String getCardValue() {
        return cardValue;
    }

    public void setCardValue(String cardValue) {
        this.cardValue = cardValue;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(int gameBoard) {
        this.gameBoard = gameBoard;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean isFlipped) {
        this.isFlipped = isFlipped;
    }

    public int getViewId() {
        return viewId;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }
}
