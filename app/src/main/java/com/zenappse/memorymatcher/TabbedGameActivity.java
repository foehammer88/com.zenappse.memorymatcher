package com.zenappse.memorymatcher;

import java.io.IOException;
import java.util.Locale;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;


public class TabbedGameActivity extends ActionBarActivity implements ActionBar.TabListener, GameGridFragment.OnGameGridInteractionListener {

    private static final String TAG = "TabbedGameActivity";

    private static final int GAME_GRID_ONE = 0;
    private static final int GAME_GRID_TWO = 1;

    private static final String GAME_GRID_FRAGMENT_TAG = "android:switcher:" + R.id.pager;

    private static final String EXTRA_USERNAME = "username";
    private static final String EXTRA_GAMEBOARD = "gameboard";
    private static final String EXTRA_CARD_DECK = "carddeck";
    private static final String EXTRA_SCORE = "score";
    private static final String EXTRA_RECORD_HOLDER = "recordholder";
    private static final String EXTRA_HIGHSCORE = "highscore";

    private GameStorageManager gameStorageManager;

    private GameGridCardDeck gameGridCardDeck;

    private GameController gameController;
    private GameState gameState;

    private String username;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private GameGridFragment mGridOneFragment;
    private GameGridFragment mGridTwoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_game);

        Bundle extras = getIntent().getExtras();

        username = "";
        if (extras != null) {
            username = extras.getString(EXTRA_USERNAME);
            Log.d(TAG, username);
        }

        setupGame();

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        // TODO Use non-default fragment tags
        mGridOneFragment = (GameGridFragment) fragmentManager.findFragmentByTag(GAME_GRID_FRAGMENT_TAG + ":0");
        mGridTwoFragment = (GameGridFragment) fragmentManager.findFragmentByTag(GAME_GRID_FRAGMENT_TAG + ":1");

        Bundle bundle;
        if (mGridOneFragment == null) {
            mGridOneFragment = new GameGridFragment();
            bundle = new Bundle();
            bundle.putString(EXTRA_USERNAME, username);
            bundle.putString(EXTRA_RECORD_HOLDER, gameController.getRecordHolder());
            bundle.putInt(EXTRA_SCORE, gameController.getScore());
            bundle.putInt(EXTRA_HIGHSCORE, gameController.getHighscore());
            bundle.putInt(EXTRA_GAMEBOARD, 0);
            try {
                // This is slower than other methods, but the object isn't /that/ big
                bundle.putString(EXTRA_CARD_DECK, ObjectSerializer.serialize(gameGridCardDeck));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mGridOneFragment.setArguments(bundle);
        }

        if (mGridTwoFragment == null) {
            mGridTwoFragment = new GameGridFragment();
            bundle = new Bundle();
            bundle.putString(EXTRA_USERNAME, username);
            bundle.putString(EXTRA_RECORD_HOLDER, gameController.getRecordHolder());
            bundle.putInt(EXTRA_SCORE, gameController.getScore());
            bundle.putInt(EXTRA_HIGHSCORE, gameController.getHighscore());
            bundle.putInt(EXTRA_GAMEBOARD, 1);
            try {
                bundle.putString(EXTRA_CARD_DECK, ObjectSerializer.serialize(gameGridCardDeck));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mGridTwoFragment.setArguments(bundle);
        }

        // Create the adapter that will return a fragment for each of the sections
        mSectionsPagerAdapter = new SectionsPagerAdapter(fragmentManager);

        mViewPager.setAdapter(mSectionsPagerAdapter);

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(actionBar.newTab().setText(mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
        }
    }

    @Override
    protected void onDestroy() {
        gameState.saveState();
        super.onDestroy();
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        gameState.saveState();
        super.onPause();
    }

    /**
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.  This means
     * that in some cases the previous state may still be saved, not allowing
     * fragment transactions that modify the state.  To correctly interact
     * with fragments in their proper state, you should instead override
     * {@link #onResumeFragments()}.
     */
    @Override
    protected void onResume() {
        super.onResume();
        gameState.loadState();
    }

    /**
     * Dispatch onStart() to all fragments.  Ensure any created loaders are
     * now started.
     */
    @Override
    protected void onStart() {
        super.onStart();
        gameState.loadState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabbed_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_trigger_win:
                showGameOverDialog();
                return true;
            case R.id.action_reset_highscore:
                resetHighScore();
                return true;
            case R.id.action_logout:
                gameState.saveState();
                finish();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    private void setupGame() {
        gameStorageManager = new GameStorageManager(this);
        gameState = new GameState(gameStorageManager);

        gameState.setUsername(username);

        gameState.loadState();

        gameController = gameState.getGameController();

        gameController.setHighscore(gameState.getHighscore());

        if (gameController.isGameInProgress()) {
            gameGridCardDeck = gameController.getGameGridCardDeck();
        } else {
            gameGridCardDeck = gameController.shuffleDeck();
        }

        if (TextUtils.isEmpty(gameState.getRecordHolder())) {
            gameController.setRecordHolder(username);
            gameState.setRecordHolder(username);
        } else {
            gameController.setRecordHolder(gameState.getRecordHolder());
        }

        gameState.saveState();
    }

    private void resetHighScore() {
        gameState.setHighscore(0);
        gameState.setRecordHolder("");
        gameController.setHighscore(gameState.getHighscore());
        gameController.setRecordHolder(gameState.getRecordHolder());

        mGridOneFragment.onHighScoreChange(gameController.getHighscore(), gameController.getRecordHolder());
        mGridTwoFragment.onHighScoreChange(gameController.getHighscore(), gameController.getRecordHolder());

        gameState.saveState();
    }

    @Override
    public void onCardItemClickInteraction(Card cardTapped) {
        gameController.cardTapped(cardTapped);

        final Card currentCard = cardTapped;
        if (gameController.isMatchingComplete() && !gameController.isMatchFound()) {

            runOnUiThread(new Runnable(){
                public void run() {
                    if (currentCard.getGameBoard() == 0) {
                        mGridOneFragment.onCardMatchNotFound(currentCard);
                    } else {
                        mGridTwoFragment.onCardMatchNotFound(currentCard);
                    }

                    Card previouslyTappedCard = gameController.getPreviousCardTapped();
                    if (previouslyTappedCard.getGameBoard() == 0) {
                        mGridOneFragment.onCardMatchNotFound(previouslyTappedCard);
                    } else {
                        mGridTwoFragment.onCardMatchNotFound(previouslyTappedCard);
                    }

                    mGridOneFragment.onScoreChange(gameController.getScore());
                    mGridTwoFragment.onScoreChange(gameController.getScore());
                }
            });

            gameController.resetCardMatching();

        } else if (gameController.isMatchingComplete()) {
            // Match found reset controller and increment score
            final boolean highscoreChanged = gameController.hasHighscoreChanged();
            runOnUiThread(new Runnable(){
                public void run() {
                    mGridOneFragment.onScoreChange(gameController.getScore());
                    mGridTwoFragment.onScoreChange(gameController.getScore());

                    if (highscoreChanged) {
                        mGridOneFragment.onHighScoreChange(gameController.getHighscore(), username);
                        mGridTwoFragment.onHighScoreChange(gameController.getHighscore(), username);
                    }
                }
            });

            if (highscoreChanged) {
                gameState.setRecordHolder(username);
                gameState.setHighscore(gameController.getHighscore());
            }

            gameController.resetCardMatching();
        }

        if (gameController.gameOver()) {
            showGameOverDialog();
        }
        gameState.setGameController(gameController);
        gameState.setCurrentScore(gameController.getScore());
        gameState.saveState();
    }

    private void showGameOverDialog() {
        final MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(this)
            .title(R.string.congratulations_text)
            .content(R.string.winning_text_content)
            .positiveText(R.string.play_again)
            .negativeText(R.string.not_now);

        dialogBuilder.callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                resetGame();
            }
        });

        dialogBuilder.show();
    }

    private void resetGame() {
        gameController.resetGame();

        mGridOneFragment.onGameReset(gameController.getGameGridCardDeck());
        mGridTwoFragment.onGameReset(gameController.getGameGridCardDeck());

        gameState.saveState();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case GAME_GRID_ONE:
                    return mGridOneFragment;
                case GAME_GRID_TWO:
                    return mGridTwoFragment;
            }
            
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case GAME_GRID_ONE:
                    return getString(R.string.title_grid_tab_one).toUpperCase(l);
                case GAME_GRID_TWO:
                    return getString(R.string.title_grid_tab_two).toUpperCase(l);
            }
            return null;
        }
    }
}
