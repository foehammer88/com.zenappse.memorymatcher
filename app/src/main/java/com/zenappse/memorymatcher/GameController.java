package com.zenappse.memorymatcher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Patrick Ganson on 2/22/15.
 *
 * Copyright 2015
 */
public class GameController implements Serializable{

    private GameGridCardDeck gameGridCardDeck;
    private Card previousCardTapped = null;
    private boolean matchFound = false;
    private boolean matchingComplete = false;

    private int score = 0;
    private int highscore = 0;

    private int numMatches = 0;

    private String recordHolder = "";
    private boolean highscoreChanged = false;

    public GameController() {
        gameGridCardDeck = new GameGridCardDeck();

        gameGridCardDeck.seedDeck(0);
    }

    public GameGridCardDeck resetGame() {
        gameGridCardDeck.resetDecks();

        shuffleDeck();

        score = 0;
        numMatches = 0;

        matchFound = false;
        matchingComplete = false;
        highscoreChanged = false;

        return gameGridCardDeck;
    }

    public GameGridCardDeck shuffleDeck() {
        ArrayList<Card> joinedDeck = new ArrayList<>(gameGridCardDeck.getDeckOfCards(0));
        joinedDeck.addAll(gameGridCardDeck.getDeckOfCards(1));

        Collections.shuffle(joinedDeck);

        ArrayList<Card> deckOne = new ArrayList<>();
        ArrayList<Card> deckTwo = new ArrayList<>();

        int countOfCards = 0;
        int position = 0;

        // Separate deck of cards into respective game board array lists
        for (Card card : joinedDeck) {
            if (countOfCards < 12) {
                card.setGameBoard(0);
                deckOne.add(card);
            } else {
                card.setGameBoard(1);
                deckTwo.add(card);
            }

            if (position >= 12) {
                position = 0;
            }
            card.setPosition(position);

            countOfCards++;
            position++;
        }

        gameGridCardDeck.setDeckOfCards(0, deckOne);
        gameGridCardDeck.setDeckOfCards(1, deckTwo);

        return gameGridCardDeck;
    }

    public boolean compareCards(Card cardOne, Card cardTwo) {
        return ((cardOne.isRed() == cardTwo.isRed()) && cardOne.getCardValue().equals(cardTwo.getCardValue()));
    }

    public void cardTapped(Card cardTapped) {
        if (previousCardTapped == null) {
            previousCardTapped = cardTapped;
            matchingComplete = false;
        } else {
            matchFound = compareCards(previousCardTapped, cardTapped);
            matchingComplete = true;

            if (matchFound) {
                score += 10;
                numMatches++;
            } else {
                score--;
            }

            if (score > highscore) {
                highscore = score;
                highscoreChanged = true;
            }
        }
    }

    public boolean gameOver() {
        return (numMatches == 12);
    }

    public void resetCardMatching() {
        previousCardTapped = null;
        matchingComplete = false;
        matchFound = false;
    }

    public boolean isMatchingComplete() {
        return matchingComplete;
    }

    public boolean hasHighscoreChanged() {
        boolean changed = highscoreChanged;
        highscoreChanged = false;
        return changed;
    }

    public GameGridCardDeck getGameGridCardDeck() {
        return gameGridCardDeck;
    }

    public void setGameGridCardDeck(GameGridCardDeck gameGridCardDeck) {
        this.gameGridCardDeck = gameGridCardDeck;
    }

    public Card getPreviousCardTapped() {
        return previousCardTapped;
    }

    public void setPreviousCardTapped(Card previousCardTapped) {
        this.previousCardTapped = previousCardTapped;
    }

    public boolean isMatchFound() {
        return matchFound;
    }

    public void setMatchFound(boolean matchFound) {
        this.matchFound = matchFound;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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

}