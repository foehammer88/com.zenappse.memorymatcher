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
    private int lastGameScore = 0;
    private int highscore = 0;

    private int numMatches = 0;

    private String recordHolder = "";
    private boolean highscoreChanged = false;
    private boolean hasNewHighscore = false;

    private boolean gameInProgress = false;

    public GameController() {
        gameGridCardDeck = new GameGridCardDeck();

        gameGridCardDeck.seedDeck(0);
    }

    /**
     * Resets the controller to a fresh game
     *
     * @return GameGridCardDeck
     */
    public GameGridCardDeck resetGame() {
        gameGridCardDeck.resetDecks();

        shuffleDeck();

        lastGameScore = score;

        score = 0;
        numMatches = 0;

        matchFound = false;
        matchingComplete = false;
        highscoreChanged = false;
        hasNewHighscore = false;

        gameInProgress = false;

        return gameGridCardDeck;
    }

    /**
     * Shuffles the cards based on the Collections.shuffle() method
     *
     * @return GameGridCardDeck
     */
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

    /**
     * Method to compare two cards that were flipped over. If they are a pair
     * return true
     *
     * @return boolean true if match, false otherwise
     *
     * @param cardOne Previously tapped card
     * @param cardTwo Current card tapped
     */
    public boolean compareCards(Card cardOne, Card cardTwo) {
        return ((cardOne.isRed() == cardTwo.isRed()) && cardOne.getCardValue().equals(cardTwo.getCardValue()));
    }

    /**
     * Processes a card tap, if there was no previous card tapped, then set
     * it to be the current card, else check for matches and update score
     *
     * @param cardTapped card that is currently being tapped
     */
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

                updateDeck(previousCardTapped, cardTapped);
            } else {
                score--;
            }

            if (score > highscore) {
                highscore = score;
                highscoreChanged = true;
                hasNewHighscore = true;
            }
        }

        gameInProgress = true;
    }

    /**
     * Method to update the card deck with what has been flipped over with a current
     * match
     *
     * @param cardOne Previously tapped card
     * @param cardTwo Current card tapped
     */
    private void updateDeck(Card cardOne, Card cardTwo) {
        int cardOneGameBoard = cardOne.getGameBoard();
        int cardOnePos = cardOne.getPosition();

        int cardTwoGameBoard = cardTwo.getGameBoard();
        int cardTwoPos = cardTwo.getPosition();

        gameGridCardDeck.getDeckOfCards(cardOneGameBoard).get(cardOnePos).setFlipped(true);
        gameGridCardDeck.getDeckOfCards(cardTwoGameBoard).get(cardTwoPos).setFlipped(true);
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

    public boolean hasNewHighScoreBeenSet() {
        return hasNewHighscore;
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

    public boolean isGameInProgress() {
        return gameInProgress;
    }

    public void setGameInProgress(boolean gameInProgress) {
        this.gameInProgress = gameInProgress;
    }

    public int getLastGameScore() {
        return lastGameScore;
    }

    public void setLastGameScore(int lastGameScore) {
        this.lastGameScore = lastGameScore;
    }
}
