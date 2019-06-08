package com.alchemistmoz.balochi.categories.family;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.alchemistmoz.balochi.misc.CustomToolbar;
import com.alchemistmoz.balochi.R;
import com.alchemistmoz.balochi.misc.SoundPlayback;
import com.alchemistmoz.balochi.misc.GameUtils;
import com.alchemistmoz.balochi.games.repetition.GameAdapter;
import com.alchemistmoz.balochi.games.repetition.GameItem;
import com.alchemistmoz.balochi.games.repetition.RepetitionGame;

import java.util.ArrayList;

public class RepetitionActivity extends AppCompatActivity {

    // Recycler view to be used for the game
    RecyclerView recyclerView;

    // Allow for global access to the game
    RepetitionGame repetitionGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_grid);

        // Initiate and setup custom toolbar
        CustomToolbar toolbar = new CustomToolbar(RepetitionActivity.this);
        toolbar.initGameToolbar();

        // Initialize the Audio Manager AUDIO_SERVICE
        SoundPlayback.initializeManagerService(this);

        initiateGame();

        GameUtils.addRepetitionItemClickSupport(recyclerView, repetitionGame);

    }

    /**
     * Initiate the game with:
     * - Intro and game items lists
     * - Setup RecyclerView
     * - New instance of the RepetitionGame
     * - New instance of the recyclerView GameAdapter
     */
    private void initiateGame() {

        // List of sound intros to be played before each round
        ArrayList<GameItem> intros = new ArrayList<>();

        intros.add(new GameItem(R.raw.family_intro_parents));
        intros.add(new GameItem(R.raw.family_intro_children));
        intros.add(new GameItem(R.raw.family_intro_mgrandparents));
        intros.add(new GameItem(R.raw.family_intro_pgrandparents));
        intros.add(new GameItem(R.raw.family_intro_msiblings));
        intros.add(new GameItem(R.raw.family_intro_psiblings));

        // List of items to be used in the game
        ArrayList<GameItem> familyItems = new ArrayList<>();

        // Add new GameItem objects to the ArrayList
        familyItems.add(new GameItem(R.drawable.family_mother1, R.raw.family_mother));
        familyItems.add(new GameItem(R.drawable.family_father2, R.raw.family_father));
        familyItems.add(new GameItem(R.drawable.family_brother1, R.raw.family_brother));
        familyItems.add(new GameItem(R.drawable.family_sister1, R.raw.family_sister));
        familyItems.add(new GameItem(R.drawable.family_mgrandmother, R.raw.family_grandmother));
        familyItems.add(new GameItem(R.drawable.family_mgrandfather, R.raw.family_grandfather));
        familyItems.add(new GameItem(R.drawable.family_pgrandmother, R.raw.family_grandmother));
        familyItems.add(new GameItem(R.drawable.family_pgrandfather, R.raw.family_grandfather));
        familyItems.add(new GameItem(R.drawable.family_muncle1, R.raw.family_uncle));
        familyItems.add(new GameItem(R.drawable.family_maunt1, R.raw.family_aunt));
        familyItems.add(new GameItem(R.drawable.family_puncle1, R.raw.family_uncle));
        familyItems.add(new GameItem(R.drawable.family_paunt1, R.raw.family_aunt));

        // Setup recycler view as a grid layout
        recyclerView = GameUtils.initGridRecyclerView(this, R.id.recycler_view_grid, 2);

        // New instance of the game
        repetitionGame = new RepetitionGame(recyclerView, intros, familyItems);

        GameAdapter adapter = new GameAdapter(this, repetitionGame.getActualItems(), R.layout.game_item);

        recyclerView.setAdapter(adapter);

        // Set adapter to be used for updating the UI during the game
        repetitionGame.useAdapter(adapter);

        GameUtils.runSlideUpAnim(recyclerView);

    }

    /**
     * Clean up the media player when activity is paused and remove
     * the pending posts when activity is set to finish.
     */
    @Override
    protected void onPause() {
        super.onPause();

        // Release the media player resources
        SoundPlayback.releaseMediaPlayer();

        if (isFinishing()) {
            // Remove all pending posts of callbacks and sent messages.
            repetitionGame.removePendingPosts();
        }
    }
}