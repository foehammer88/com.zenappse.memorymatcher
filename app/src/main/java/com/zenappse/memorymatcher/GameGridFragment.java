package com.zenappse.memorymatcher;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.io.IOException;

/**
 * Auto-generated Class by Android Studio, modified by Patrick Ganson
 *
 * Class that represents a playable game screen of the app
 */
public class GameGridFragment extends Fragment implements AdapterView.OnItemClickListener{
    private static final String TAG = "GameGridFragment";
    private static final String EXTRA_USERNAME = "username";
    private static final String EXTRA_GAMEBOARD = "gameboard";
    private static final String EXTRA_CARD_DECK = "carddeck";
    private static final String EXTRA_SCORE = "score";
    private static final String EXTRA_RECORD_HOLDER = "recordholder";
    private static final String EXTRA_HIGHSCORE = "highscore";
    private static final String EXTRA_LAST_GAME_SCORE = "lastgamescore";

    private GameGridCardDeck gameGridCardDeck = null;

    private OnGameGridInteractionListener mListener;
    private String username = "";
    private int gameBoard = 0;

    private CardGridAdapter cardGridAdapter;

    private LayoutInflater layoutInflater;
    private View gameBoardView;
    private GridView gridView;
    private TextView scoreTextView;
    private TextView lastGameScoreTextView;
    private TextView highscoreTextView;
    private TextView recordHolderTextView;

    private int currentScore = 0;
    private int highscore = 0;
    private int lastGameScore = 0;
    private String recordHolder = "";

    private String scoreText = "Score: ";
    private String lastGameText = "Last game: ";
    private String highscoreText = "High score: ";
    private String byText = "by ";

    public GameGridFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (getArguments() != null) {
            username = getArguments().getString(EXTRA_USERNAME);
            currentScore = getArguments().getInt(EXTRA_SCORE);
            lastGameScore = getArguments().getInt(EXTRA_LAST_GAME_SCORE);
            highscore = getArguments().getInt(EXTRA_HIGHSCORE);
            recordHolder = getArguments().getString(EXTRA_RECORD_HOLDER);
            gameBoard = getArguments().getInt(EXTRA_GAMEBOARD);

            try {
                gameGridCardDeck = (GameGridCardDeck) ObjectSerializer.deserialize(getArguments().getString(EXTRA_CARD_DECK, ObjectSerializer.serialize(new GameGridCardDeck())));
            } catch (IOException e) {
                e.printStackTrace();
                gameGridCardDeck = new GameGridCardDeck();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutInflater = inflater;
        gameBoardView = inflater.inflate(R.layout.fragment_game_grid, container, false);

        gridView = null;

        if (gameBoardView != null) {
            gridView = (GridView) gameBoardView.findViewById(R.id.gridview);
            scoreTextView = (TextView) gameBoardView.findViewById(R.id.current_score_textview);
            lastGameScoreTextView = (TextView) gameBoardView.findViewById(R.id.last_game_score);
            highscoreTextView = (TextView) gameBoardView.findViewById(R.id.highscore_label_textview);
            recordHolderTextView = (TextView) gameBoardView.findViewById(R.id.highscore_recordholder_textview);

            scoreTextView.setText(scoreText + currentScore);
            lastGameScoreTextView.setText(lastGameText + lastGameScore);
            highscoreTextView.setText(highscoreText + highscore);

            String recordHolderText = "";
            if (!TextUtils.isEmpty(recordHolder)) {
                recordHolderText = byText + recordHolder;
            }

            recordHolderTextView.setText(recordHolderText);
        }

        if (gridView != null) {
            cardGridAdapter = new CardGridAdapter(getActivity(), inflater, gameGridCardDeck, gameBoard);
            gridView.setAdapter(cardGridAdapter);

            gridView.setOnItemClickListener(this);
        }

        return gameBoardView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnGameGridInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnGameGridInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

        layoutInflater = null;
        gameBoardView = null;
        gridView = null;
        scoreTextView = null;
        lastGameScoreTextView = null;
        highscoreTextView = null;
        recordHolderTextView = null;
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     *
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(gameGridCardDeck != null) {
            final Card tappedCard = (Card) parent.getItemAtPosition(position);

            if (!tappedCard.isFlipped()) {
                flipCard(view, tappedCard);
                tappedCard.setFlipped(true);

                Handler handler = new Handler();

                final Runnable runnable = new Runnable() {
                    public void run() {
                        mListener.onCardItemClickInteraction(tappedCard);
                    }
                };
                // 750ms delay so that the user can have time to view the cards before they are turned over
                handler.postDelayed(runnable, 750);
            }
        }
        Log.d(TAG, "Grid: " + gameBoard + " - Card position: " + position);
    }

    public void onCardMatchNotFound(Card cardToFlipBack) {
        View cardView = gridView.getChildAt(cardToFlipBack.getPosition());

        flipCard(cardView, cardToFlipBack);
        cardToFlipBack.setFlipped(false);
    }

    /**
     * Method to call to flip a card given the View of the card and the actual Card
     * representation object
     *
     * @param cardView   View that represents the card
     * @param cardToFlip Card object of card to flip
     */
    private void flipCard(View cardView, Card cardToFlip) {
        FlipAnimation flipAnimation = new FlipAnimation(cardView);

        if (cardToFlip.isRed()) {
            // Card is red
            flipAnimation.setColorCard(true);
        } else {
            // Card is black
            flipAnimation.setColorCard(false);
        }

        if (cardToFlip.isFlipped()) {
            // Card is flipped over
            flipAnimation.setFlippedState(true);
            flipAnimation.reverse();
        } else {
            // Card is showing it's back face
            flipAnimation.setFlippedState(false);
        }

        cardView.startAnimation(flipAnimation);
    }

    public void onScoreChange(int score) {
        currentScore = score;
        scoreTextView.setText(scoreText + score);
    }

    public void onLastGameScoreChange(int score) {
        lastGameScore = score;
        lastGameScoreTextView.setText(lastGameText + score);
    }

    public void onHighScoreChange(int highscore, String recordHolder) {
        this.highscore = highscore;
        highscoreTextView.setText(highscoreText + highscore);

        if (!this.recordHolder.equals(recordHolder)) {
            this.recordHolder = recordHolder;

            String recordHolderText = "";
            if (!TextUtils.isEmpty(recordHolder)) {
                recordHolderText = byText + recordHolder;
            }

            recordHolderTextView.setText(recordHolderText);
        }
    }

    public void onGameReset(GameGridCardDeck gameGridCardDeck) {
        if (gameBoardView != null) {
            this.gameGridCardDeck = gameGridCardDeck;
            currentScore = 0;
            scoreTextView.setText("Score: " + currentScore);

            cardGridAdapter = new CardGridAdapter(getActivity(), layoutInflater, gameGridCardDeck, gameBoard);
            gridView.setAdapter(cardGridAdapter);
        }
    }

    public interface OnGameGridInteractionListener {
        public void onCardItemClickInteraction(Card cardTapped);
    }
}
