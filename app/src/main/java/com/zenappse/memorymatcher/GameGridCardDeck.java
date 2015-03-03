package com.zenappse.memorymatcher;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Patrick Ganson on 2/21/15.
 *
 * Copyright 2015
 */
public class GameGridCardDeck implements Serializable {

    private ArrayList<Card> deckOfCardsBoardOne = null;
    private ArrayList<Card> deckOfCardsBoardTwo = null;

    public GameGridCardDeck() {
        deckOfCardsBoardOne = new ArrayList<>();
        deckOfCardsBoardTwo = new ArrayList<>();
    }

    public ArrayList<Card> getDeckOfCards(int gameGrid) {
        if (gameGrid == 0) {
            return deckOfCardsBoardOne;
        } else {
            return deckOfCardsBoardTwo;
        }
    }

    /**
     * Sets the deck of cards ArrayList to the given deck for the specified game grid
     *
     * @param gameGrid    Which game grid is the deck for
     * @param deckOfCards ArrayList of Cards that is a deck
     */
    public void setDeckOfCards(int gameGrid, ArrayList<Card> deckOfCards) {
        if (gameGrid == 0) {
            this.deckOfCardsBoardOne = deckOfCards;
        } else {
            this.deckOfCardsBoardTwo = deckOfCards;
        }
    }

    public void resetDecks() {
        deckOfCardsBoardOne = null;
        deckOfCardsBoardTwo = null;

        deckOfCardsBoardOne = new ArrayList<>();
        deckOfCardsBoardTwo = new ArrayList<>();

        seedDeck(0);
    }

    /**
     * Initialize the card decks of cards
     *
     * @param gameGrid Default gameGrid for initialization
     */
    public void seedDeck(int gameGrid) {
            deckOfCardsBoardOne.add(new Card("1", true, false, 0, gameGrid));
            deckOfCardsBoardOne.add(new Card("2", true, false, 1, gameGrid));
            deckOfCardsBoardOne.add(new Card("3", true, false, 2, gameGrid));
            deckOfCardsBoardOne.add(new Card("4", true, false, 3, gameGrid));
            deckOfCardsBoardOne.add(new Card("5", true, false, 4, gameGrid));
            deckOfCardsBoardOne.add(new Card("6", true, false, 5, gameGrid));

            deckOfCardsBoardOne.add(new Card("1", false, false, 6, gameGrid));
            deckOfCardsBoardOne.add(new Card("2", false, false, 7, gameGrid));
            deckOfCardsBoardOne.add(new Card("3", false, false, 8, gameGrid));
            deckOfCardsBoardOne.add(new Card("4", false, false, 9, gameGrid));
            deckOfCardsBoardOne.add(new Card("5", false, false, 10, gameGrid));
            deckOfCardsBoardOne.add(new Card("6", false, false, 11, gameGrid));

            deckOfCardsBoardTwo.add(new Card("1", true, false, 0, gameGrid));
            deckOfCardsBoardTwo.add(new Card("2", true, false, 1, gameGrid));
            deckOfCardsBoardTwo.add(new Card("3", true, false, 2, gameGrid));
            deckOfCardsBoardTwo.add(new Card("4", true, false, 3, gameGrid));
            deckOfCardsBoardTwo.add(new Card("5", true, false, 4, gameGrid));
            deckOfCardsBoardTwo.add(new Card("6", true, false, 5, gameGrid));

            deckOfCardsBoardTwo.add(new Card("1", false, false, 6, gameGrid));
            deckOfCardsBoardTwo.add(new Card("2", false, false, 7, gameGrid));
            deckOfCardsBoardTwo.add(new Card("3", false, false, 8, gameGrid));
            deckOfCardsBoardTwo.add(new Card("4", false, false, 9, gameGrid));
            deckOfCardsBoardTwo.add(new Card("5", false, false, 10, gameGrid));
            deckOfCardsBoardTwo.add(new Card("6", false, false, 11, gameGrid));
    }
}
