package com.alchemistmoz.balochi.categories.numbers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.alchemistmoz.balochi.misc.GameUtils;
import com.alchemistmoz.balochi.misc.ItemClickSupport;
import com.alchemistmoz.balochi.misc.CustomToolbar;
import com.alchemistmoz.balochi.R;
import com.alchemistmoz.balochi.misc.SoundPlayback;
import com.alchemistmoz.balochi.games.repetition.GameAdapter;
import com.alchemistmoz.balochi.games.repetition.CountGame;

public class RepetitionActivity extends AppCompatActivity {

    // Recycler view to be used for the game
    RecyclerView recyclerView;

    // Allow for global access to the game
    CountGame countGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_count_game);

        // Initiate and setup custom toolbar
        CustomToolbar toolbar = new CustomToolbar(RepetitionActivity.this);
        toolbar.initGameToolbar();

        // Initialize the Audio Manager AUDIO_SERVICE
        SoundPlayback.initializeManagerService(this);

        initiateGame();

        addItemClickSupport();

    }

    /**
     * Initiate the game with:
     * - Setup RecyclerView
     * - New instance of the CountGame
     * - New instance of the recyclerView GameAdapter
     */
    private void initiateGame() {

        // Setup recycler view as a grid layout
        recyclerView = GameUtils.initGridRecyclerView(this, R.id.recycler_view_count_game, 3);

        // Find and store the image view so that it can be set by the count game
        ImageView numberImage = findViewById(R.id.number_image);

        // Initiate a new round of the counting game
        countGame = new CountGame(recyclerView, numberImage);

        GameAdapter adapter = new GameAdapter(this, countGame.getActualItems(), R.layout.count_item);

        recyclerView.setAdapter(adapter);

        // Set adapter to be used for updating the UI during the game
        countGame.useAdapter(adapter);

        GameUtils.runSlideUpAnim(recyclerView);

    }

    /**
     * Add click support for the game, by animating touch events and calling upon
     * countGame.selectItem(position).
     */
    private void addItemClickSupport() {

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                countGame.selectItem(position, v);

            }
        });
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
            countGame.removePendingPosts();
        }
    }
}