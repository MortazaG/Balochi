package com.alchemistmoz.balochi.categories.colors;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.alchemistmoz.balochi.misc.CustomToolbar;
import com.alchemistmoz.balochi.R;
import com.alchemistmoz.balochi.misc.SoundPlayback;
import com.alchemistmoz.balochi.misc.Utilities;
import com.alchemistmoz.balochi.games.memory.MemoryAdapter;
import com.alchemistmoz.balochi.games.memory.MemoryCard;
import com.alchemistmoz.balochi.games.memory.MemoryGame;

import java.util.ArrayList;

public class MemoryActivity extends AppCompatActivity {

    // Recycler view to be used for the game
    RecyclerView recyclerView;

    // Allow for global access to the game
    MemoryGame memoryGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_grid);

        // Initiate and setup custom toolbar
        CustomToolbar toolbar = new CustomToolbar(MemoryActivity.this);
        toolbar.initGameToolbar();

        // Initialize the Audio Manager AUDIO_SERVICE
        SoundPlayback.initializeManagerService(this);

        initiateGame();

        Utilities.addMemoryCardClickSupport(recyclerView, memoryGame);
    }

    /**
     * Initiate the game with:
     * - MemoryCard items lists
     * - Setup RecyclerView
     * - New instance of the MemoryGame
     * - New instance of the recyclerView MemoryAdapter
     */
    private void initiateGame() {

        // Initiate memoryCards list
        ArrayList<MemoryCard> memoryCards = new ArrayList<>();

        // Add the MemoryCard objects that will be used to create card pairs
        memoryCards.add(new MemoryCard(R.drawable.colors_memory_red, R.raw.colors_red));
        memoryCards.add(new MemoryCard(R.drawable.colors_memory_green, R.raw.colors_green));
        memoryCards.add(new MemoryCard(R.drawable.colors_memory_blue, R.raw.colors_blue));
        memoryCards.add(new MemoryCard(R.drawable.colors_memory_yellow, R.raw.colors_yellow));
        memoryCards.add(new MemoryCard(R.drawable.colors_memory_orange, R.raw.colors_orange));
        memoryCards.add(new MemoryCard(R.drawable.colors_memory_brown, R.raw.colors_brown));
        memoryCards.add(new MemoryCard(R.drawable.colors_memory_black, R.raw.colors_black));
        memoryCards.add(new MemoryCard(R.drawable.colors_memory_white, R.raw.colors_white));
        memoryCards.add(new MemoryCard(R.drawable.colors_memory_grey, R.raw.colors_grey));

        // Setup recycler view using the games built-in initiator
        recyclerView = MemoryGame.initRecyclerView(this, R.id.recycler_view_grid);

        // Initiate new round of MemoryGame game
        memoryGame = new MemoryGame(recyclerView, memoryCards);

        // Create a new recycler view adapter with memory card backs as array list
        MemoryAdapter adapter = new MemoryAdapter(this, memoryGame.getMemoryCardPlaceholders());
        recyclerView.setAdapter(adapter);

        // Set adapter to be used for the memory game
        memoryGame.useAdapter(adapter);

        Utilities.runSlideUpAnim(recyclerView);

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
            memoryGame.removePendingPosts();
        }
    }
}
