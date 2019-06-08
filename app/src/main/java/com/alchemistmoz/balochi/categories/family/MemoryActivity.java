package com.alchemistmoz.balochi.categories.family;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alchemistmoz.balochi.misc.CustomToolbar;
import com.alchemistmoz.balochi.misc.ItemClickSupport;
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
        memoryCards.add(new MemoryCard(R.drawable.family_memory_mother, R.raw.family_mother));
        memoryCards.add(new MemoryCard(R.drawable.family_memory_father, R.raw.family_father));
        memoryCards.add(new MemoryCard(R.drawable.family_memory_sister, R.raw.family_sister));
        memoryCards.add(new MemoryCard(R.drawable.family_memory_brother, R.raw.family_brother));
        memoryCards.add(new MemoryCard(R.drawable.family_memory_mgrandmother, R.raw.family_grandmother));
        memoryCards.add(new MemoryCard(R.drawable.family_memory_mgrandfather, R.raw.family_grandfather));
        memoryCards.add(new MemoryCard(R.drawable.family_memory_pgrandmother, R.raw.family_grandmother));
        memoryCards.add(new MemoryCard(R.drawable.family_memory_pgrandfather, R.raw.family_grandfather));
        memoryCards.add(new MemoryCard(R.drawable.family_memory_maunt, R.raw.family_aunt));
        memoryCards.add(new MemoryCard(R.drawable.family_memory_muncle, R.raw.family_uncle));
        memoryCards.add(new MemoryCard(R.drawable.family_memory_paunt, R.raw.family_aunt));
        memoryCards.add(new MemoryCard(R.drawable.family_memory_puncle, R.raw.family_uncle));

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
