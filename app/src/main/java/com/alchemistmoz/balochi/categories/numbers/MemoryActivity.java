package com.alchemistmoz.balochi.categories.numbers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alchemistmoz.balochi.misc.CustomToolbar;
import com.alchemistmoz.balochi.misc.ItemClickSupport;
import com.alchemistmoz.balochi.misc.Utilities;
import com.alchemistmoz.balochi.games.memory.MemoryAdapter;
import com.alchemistmoz.balochi.R;
import com.alchemistmoz.balochi.misc.SoundPlayback;
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

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, final int position, View v) {

                // Show the front of the selected card at the given position
                memoryGame.revealCard(position);

            }
        });

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
        memoryCards.add(new MemoryCard(R.drawable.numbers_memory_one1, R.raw.numbers_one));
        memoryCards.add(new MemoryCard(R.drawable.numbers_memory_two, R.raw.numbers_two));
        memoryCards.add(new MemoryCard(R.drawable.numbers_memory_three, R.raw.numbers_three));
        memoryCards.add(new MemoryCard(R.drawable.numbers_memory_four, R.raw.numbers_four));
        memoryCards.add(new MemoryCard(R.drawable.numbers_memory_five, R.raw.numbers_five));
        memoryCards.add(new MemoryCard(R.drawable.numbers_memory_six, R.raw.numbers_six));
        memoryCards.add(new MemoryCard(R.drawable.numbers_memory_seven, R.raw.numbers_seven));
        memoryCards.add(new MemoryCard(R.drawable.numbers_memory_eight, R.raw.numbers_eight));
        memoryCards.add(new MemoryCard(R.drawable.numbers_memory_nine, R.raw.numbers_nine));
        memoryCards.add(new MemoryCard(R.drawable.numbers_memory_ten1, R.raw.numbers_ten));

        // Setup recycler view using the games built-in initiator
        recyclerView = MemoryGame.initRecyclerView(this, R.id.recycler_view_grid);

        // Initiate new round of MemoryGame game
        memoryGame = new MemoryGame(recyclerView, memoryCards);

        // Create a new recycler view adapter with memory card backs as array list
        MemoryAdapter adapter = new MemoryAdapter(this, memoryGame.getMemoryCardPlaceholders());
        recyclerView.setAdapter(adapter);

        // Set adapter to be used for the memory game
        memoryGame.useAdapter(adapter);

    }

    /**
     * Override onResume in order to run the slide Animation
     * after onCreate and onStart.
     */
    @Override
    protected void onResume() {
        super.onResume();

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
