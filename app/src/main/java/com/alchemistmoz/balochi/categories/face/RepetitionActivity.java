package com.alchemistmoz.balochi.categories.face;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.alchemistmoz.balochi.R;
import com.alchemistmoz.balochi.games.GameAdapter;
import com.alchemistmoz.balochi.games.repetition.RepetitionGame;
import com.alchemistmoz.balochi.misc.CustomToolbar;
import com.alchemistmoz.balochi.misc.GameUtils;
import com.alchemistmoz.balochi.misc.SoundPlayback;

public class RepetitionActivity extends AppCompatActivity {

    // Recycler view to be used for the game
    RecyclerView recyclerView;

    // Allow for global access to the game
    // FaceGame faceGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_grid);

        // Initiate and setup custom toolbar
        CustomToolbar toolbar = new CustomToolbar(RepetitionActivity.this);
        toolbar.initGameToolbar();

        // Initialize the Audio Manager AUDIO_SERVICE
        SoundPlayback.initializeManagerService(this);

        // initiateGame();

        // GameUtils.addRepetitionItemClickSupport(recyclerView, faceGame);
    }

    /**
     * Initiate the game with:
     * - Setup RecyclerView
     * - New instance of the FaceGame
     * - New instance of the recyclerView GameAdapter
     */
    private void initiateGame() {

        // Setup recycler view as a grid layout
        recyclerView = GameUtils.initGridRecyclerView(this, R.id.recycler_view_grid, 3);

        // Initiate a new round of the counting game
//        faceGame = new faceGame(recyclerView, numberImage);
//
//        GameAdapter adapter = new GameAdapter(this, faceGame.getActualItems(), R.layout.count_item);

//        recyclerView.setAdapter(adapter);

        // Set adapter to be used for updating the UI during the game
//        faceGame.useAdapter(adapter);

        GameUtils.runSlideUpAnim(recyclerView, GameUtils.COUNT);

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

//        if (isFinishing()) {
//            // Remove all pending posts of callbacks and sent messages.
//            faceGame.removePendingPosts();
//        }
    }
}
