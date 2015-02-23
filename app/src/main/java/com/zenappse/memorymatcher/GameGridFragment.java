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

    private GameGridCardDeck gameGridCardDeck = null;

    private OnGameGridInteractionListener mListener;
    private String username = "";
    private int gameBoard = 0;

    private CardGridAdapter cardGridAdapter;

    private LayoutInflater layoutInflater;
    private View gameBoardView;
    private GridView gridView;
    private TextView scoreTextView;
    private TextView highscoreTextView;
    private TextView recordHolderTextView;

    private int currentScore = 0;
    private int highscore = 0;
    private String recordHolder = "";

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
            highscoreTextView = (TextView) gameBoardView.findViewById(R.id.highscore_label_textview);
            recordHolderTextView = (TextView) gameBoardView.findViewById(R.id.highscore_recordholder_textview);

            scoreTextView.setText("Score: " + currentScore);
            highscoreTextView.setText("High score: " + highscore);

            String recordHolderText = "";
            if (!TextUtils.isEmpty(recordHolder)) {
                recordHolderText = "by " + recordHolder;
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

            TextView cardNumberTextView = (TextView) view.findViewById(R.id.game_card_textview);

            if (!tappedCard.isFlipped()) {
                tappedCard.setFlipped(true);
                if (tappedCard.isRed()) {
                    view.setBackgroundResource(R.drawable.card_red);
                } else {
                    view.setBackgroundResource(R.drawable.card_black);
                }
                cardNumberTextView.setVisibility(View.VISIBLE);

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

        TextView cardNumberTextView = (TextView) cardView.findViewById(R.id.game_card_textview);

        cardToFlipBack.setFlipped(false);
        cardView.setBackgroundResource(R.drawable.card_back);
        cardNumberTextView.setVisibility(View.INVISIBLE);
    }

    public void onScoreChange(int score) {
        currentScore = score;
        scoreTextView.setText("Score: " + score);
    }

    public void onHighScoreChange(int highscore, String recordHolder) {
        this.highscore = highscore;
        highscoreTextView.setText("High score: " + highscore);

        if (!this.recordHolder.equals(recordHolder)) {
            this.recordHolder = recordHolder;

            String recordHolderText = "";
            if (!TextUtils.isEmpty(recordHolder)) {
                recordHolderText = "by " + recordHolder;
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