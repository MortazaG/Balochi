package com.alchemistmoz.balochi.categories.fruitveggies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.alchemistmoz.balochi.games.memory.MemoryAdapter;
import com.alchemistmoz.balochi.games.memory.MemoryCard;
import com.alchemistmoz.balochi.games.memory.MemoryGame;
import com.alchemistmoz.balochi.misc.CustomToolbar;
import com.alchemistmoz.balochi.misc.SoundPlayback;
import com.alchemistmoz.balochi.R;
import com.alchemistmoz.balochi.misc.GameUtils;

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

        GameUtils.addMemoryCardClickSupport(recyclerView, memoryGame);
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

        memoryCards.add(new MemoryCard(R.drawable.fruitveggies_memory_apple, R.raw.fruitveggies_apple));
        memoryCards.add(new MemoryCard(R.drawable.fruitveggies_memory_banana, R.raw.fruitveggies_banana));
        memoryCards.add(new MemoryCard(R.drawable.fruitveggies_memory_broccoli, R.raw.fruitveggies_broccoli));
        memoryCards.add(new MemoryCard(R.drawable.fruitveggies_memory_carrot, R.raw.fruitveggies_carrot));
        memoryCards.add(new MemoryCard(R.drawable.fruitveggies_memory_lemon, R.raw.fruitveggies_lemon));
        memoryCards.add(new MemoryCard(R.drawable.fruitveggies_memory_mango, R.raw.fruitveggies_mango));
        memoryCards.add(new MemoryCard(R.drawable.fruitveggies_memory_orange, R.raw.fruitveggies_orange));
        memoryCards.add(new MemoryCard(R.drawable.fruitveggies_memory_strawberry, R.raw.fruitveggies_strawberry));
        memoryCards.add(new MemoryCard(R.drawable.fruitveggies_memory_tomato, R.raw.fruitveggies_tomato));
        memoryCards.add(new MemoryCard(R.drawable.fruitveggies_memory_watermelon, R.raw.fruitveggies_watermelon));

        // Setup recycler view using the games built-in initiator
        recyclerView = MemoryGame.initRecyclerView(this, R.id.recycler_view_grid);

        // Initiate new round of MemoryGame game
        memoryGame = new MemoryGame(recyclerView, memoryCards);

        // Create a new recycler view adapter with memory card backs as array list
        MemoryAdapter adapter = new MemoryAdapter(this, memoryGame.getMemoryCardPlaceholders());
        recyclerView.setAdapter(adapter);

        // Set adapter to be used for the memory game
        memoryGame.useAdapter(adapter);

        GameUtils.runSlideUpAnim(recyclerView, GameUtils.MEMORY);

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
