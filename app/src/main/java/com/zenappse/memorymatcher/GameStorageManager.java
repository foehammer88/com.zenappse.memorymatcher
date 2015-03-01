package com.zenappse.memorymatcher;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Patrick Ganson on 2/21/15.
 *
 * Copyright 2015
 */
public class GameStorageManager {

    private static final String PREFS_NAME = "MemoryMatcherPrefs";
    private static final String PREFS_USERNAMES = "Usernames";
    private static final String PREFS_LAST_USED_USERNAME = "LastUsedUsername";

    private static final String PREFS_HIGHSCORE = "highscore";
    private static final String PREFS_RECORD_HOLDER = "recordholder";
    private static final String PREFS_CARD_DECK = "carddeck";
    private static final String PREFS_GAME_CONTROLLER = "gamecontroller";
    private static final String PREFS_SCORE = "score";

    private SharedPreferences sharedPreferences = null;

    public GameStorageManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public Set<String> getUsernames() {
        Set<String> usernames = null;

        if (sharedPreferences != null) {
            usernames = sharedPreferences.getStringSet(PREFS_USERNAMES, new HashSet<String>());
        }

        return usernames;
    }

    public String getLastUsedUsername() {
        String lastUsedUsername = "";

        if (sharedPreferences != null) {
            lastUsedUsername = sharedPreferences.getString(PREFS_LAST_USED_USERNAME, "");
        }

        return lastUsedUsername;
    }

    public boolean registerUsername(String username) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor prefEditor = sharedPreferences.edit();

            Set<String> usernames = getUsernames();

            if (!TextUtils.isEmpty(username) && usernames.add(username)) {
                prefEditor.putStringSet(PREFS_USERNAMES, usernames);
            } else {
                return false;
            }

            prefEditor.apply();

            return true;
        }

        return false;
    }

    public boolean setLastUsedUsername(String username) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor prefEditor = sharedPreferences.edit();

            if (!TextUtils.isEmpty(username)) {
                prefEditor.putString(PREFS_LAST_USED_USERNAME, username);
            } else {
                return false;
            }

            prefEditor.apply();

            return true;
        }

        return false;
    }

    public boolean setCurrentScore(int score) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor prefEditor = sharedPreferences.edit();

            prefEditor.putInt(PREFS_SCORE, score);
            prefEditor.apply();

            return true;
        }

        return false;
    }

    public int getCurrentScore() {
        int score = 0;

        if (sharedPreferences != null) {
            score = sharedPreferences.getInt(PREFS_SCORE, 0);
        }

        return score;
    }
    
    public boolean setHighScore(int highscore) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor prefEditor = sharedPreferences.edit();

            prefEditor.putInt(PREFS_HIGHSCORE, highscore);
            prefEditor.apply();

            return true;
        }

        return false;
    }

    public int getHighScore() {
        int highscore = 0;

        if (sharedPreferences != null) {
            highscore = sharedPreferences.getInt(PREFS_HIGHSCORE, 0);
        }

        return highscore;
    }

    public boolean setRecordHolder(String username) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor prefEditor = sharedPreferences.edit();

            if (!TextUtils.isEmpty(username)) {
                prefEditor.putString(PREFS_RECORD_HOLDER, username);
            } else {
                return false;
            }

            prefEditor.apply();

            return true;
        }

        return false;
    }

    public String getRecordHolder() {
        String recordHolder = "";

        if (sharedPreferences != null) {
            recordHolder = sharedPreferences.getString(PREFS_RECORD_HOLDER, "");
        }

        return recordHolder;
    }

    public boolean setGameGridCardDeck(GameGridCardDeck gameGridCardDeck) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor prefEditor = sharedPreferences.edit();
            try {
                prefEditor.putString(PREFS_CARD_DECK, ObjectSerializer.serialize(gameGridCardDeck));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            prefEditor.apply();

            return true;
        }

        return false;
    }

    public GameGridCardDeck getGameGridCardDeck() {
        GameGridCardDeck gameGridCardDeck;
        if (sharedPreferences != null) {
            try {
                gameGridCardDeck = (GameGridCardDeck) ObjectSerializer.deserialize(sharedPreferences.getString(PREFS_CARD_DECK, ObjectSerializer.serialize(new GameGridCardDeck())));
            } catch (IOException e) {
                e.printStackTrace();
                gameGridCardDeck = new GameGridCardDeck();
            }
        } else {
            gameGridCardDeck = new GameGridCardDeck();
        }

        return gameGridCardDeck;
    }

    public boolean setGameController(GameController gameController, String username) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor prefEditor = sharedPreferences.edit();
            try {
                prefEditor.putString(PREFS_GAME_CONTROLLER + username, ObjectSerializer.serialize(gameController));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            prefEditor.apply();

            return true;
        }

        return false;
    }

    public GameController getGameController(String username) {
        GameController gameController;
        if (sharedPreferences != null) {
            try {
                gameController = (GameController) ObjectSerializer.deserialize(sharedPreferences.getString(PREFS_GAME_CONTROLLER + username, ObjectSerializer.serialize(new GameController())));
            } catch (IOException e) {
                e.printStackTrace();
                gameController = new GameController();
            }
        } else {
            gameController = new GameController();
        }

        return gameController;
    }
}
