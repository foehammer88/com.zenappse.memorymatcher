package com.zenappse.memorymatcher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Patrick Ganson on 2/21/15.
 *
 * Copyright 2015
 */
public class CardGridAdapter extends BaseAdapter{
    private int NUM_CARDS = 12;

    private Context context;
    private LayoutInflater layoutInflater;
    private GameGridCardDeck gameGridCardDeck;
    private ArrayList<Card> deckOfCards;
    private ArrayList<Integer> cardIds;
    private int gameBoard = 0;
    private View rootView;

    public CardGridAdapter(Context context, LayoutInflater layoutInflater, GameGridCardDeck gameGridCardDeck, int gameBoard) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.gameGridCardDeck = gameGridCardDeck;
        this.gameBoard = gameBoard;

        deckOfCards = gameGridCardDeck.getDeckOfCards(gameBoard);
        cardIds = new ArrayList<>();
    }

    public int getCount() {
        return NUM_CARDS;
    }

    public Object getItem(int position) {
        return deckOfCards.get(position);
    }

    public long getItemId(int position) {
        return cardIds.get(position);
    }

    public View getView(int position, View view, ViewGroup parent) {
        View cardView;
        Card currentCard = deckOfCards.get(position);

        if (rootView == null) {
            rootView = parent;
        }

        if (view == null) {
            cardView = layoutInflater.inflate(R.layout.game_card, parent, false);

            int id = View.generateViewId();
            cardView.setId(id);
            cardIds.add(id);
        } else {
            cardView = view;
        }

        TextView cardNumberTextView = (TextView) cardView.findViewById(R.id.game_card_textview);

        if (currentCard.getGameBoard() == gameBoard) {
            cardNumberTextView.setText(currentCard.getCardValue());
        }

        if (currentCard.isFlipped()) {
            if (currentCard.isRed()) {
                cardView.setBackgroundResource(R.drawable.card_red);
            } else {
                cardView.setBackgroundResource(R.drawable.card_black);
            }
            cardNumberTextView.setVisibility(View.VISIBLE);
        } else {
            cardView.setBackgroundResource(R.drawable.card_back);
            cardNumberTextView.setVisibility(View.INVISIBLE);
        }

        return cardView;
    }
}