package com.alchemistmoz.balochi.categories.fruitveggies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.alchemistmoz.balochi.R;
import com.alchemistmoz.balochi.games.GameAdapter;
import com.alchemistmoz.balochi.games.GameItem;
import com.alchemistmoz.balochi.games.audiomatch.AudioMatchGame;
import com.alchemistmoz.balochi.misc.CustomToolbar;
import com.alchemistmoz.balochi.misc.GameUtils;
import com.alchemistmoz.balochi.misc.SoundPlayback;

import java.util.ArrayList;

public class AudioMatchActivity extends AppCompatActivity {

    // Recycler view to be used for the game
    RecyclerView recyclerView;

    // Allow for global access to the game
    AudioMatchGame audioMatchGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_audio_match);

        // Initiate and setup custom toolbar
        CustomToolbar toolbar = new CustomToolbar(AudioMatchActivity.this);
        toolbar.initGameToolbar();

        // Initialize the Audio Manager AUDIO_SERVICE
        SoundPlayback.initializeManagerService(this);

        initiateGame();

        GameUtils.addAudioMatchItemClickSupport(recyclerView, audioMatchGame);
    }
    
    /**
     * Initiate the game with:
     * - Game items list
     * - Setup RecyclerView
     * - New instance of the Game
     * - New instance of the recyclerView GameAdapter
     */
    private void initiateGame() {

        // Initiate GameItems list to be used in the game
        ArrayList<GameItem> gameItems = new ArrayList<>();

        // Add new GameItem objects to the ArrayList
        gameItems.add(new GameItem(R.drawable.fruitveggies_memory_apple, R.raw.fruitveggies_apple));
        gameItems.add(new GameItem(R.drawable.fruitveggies_memory_banana, R.raw.fruitveggies_banana));
        gameItems.add(new GameItem(R.drawable.fruitveggies_memory_broccoli, R.raw.fruitveggies_broccoli));
        gameItems.add(new GameItem(R.drawable.fruitveggies_memory_carrot, R.raw.fruitveggies_carrot));
        gameItems.add(new GameItem(R.drawable.fruitveggies_memory_lemon, R.raw.fruitveggies_lemon));
        gameItems.add(new GameItem(R.drawable.fruitveggies_memory_mango, R.raw.fruitveggies_mango));
        gameItems.add(new GameItem(R.drawable.fruitveggies_memory_orange, R.raw.fruitveggies_orange));
        gameItems.add(new GameItem(R.drawable.fruitveggies_memory_strawberry, R.raw.fruitveggies_strawberry));
        gameItems.add(new GameItem(R.drawable.fruitveggies_memory_tomato, R.raw.fruitveggies_tomato));
        gameItems.add(new GameItem(R.drawable.fruitveggies_memory_watermelon, R.raw.fruitveggies_watermelon));

        // Setup recycler view as a gridlayout
        recyclerView = GameUtils.initGridRecyclerView(this, R.id.recycler_view_audio_match, 2);

        // Find and store the image view so that it can be set by the game
        ImageView speakerPhoneView = findViewById(R.id.speaker_phone_image);

        // Initiate new round of MemoryGame game
        audioMatchGame = new AudioMatchGame(recyclerView, gameItems, speakerPhoneView);

        // Create a new recycler view adapter
        GameAdapter adapter = new GameAdapter(this, audioMatchGame.getActualItems(), R.layout.audio_match_item);
        recyclerView.setAdapter(adapter);

        // Set adapter to be used for the game
        audioMatchGame.useAdapter(adapter);

        GameUtils.runSlideUpAnim(recyclerView, GameUtils.AUDIOMATCH);

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
            audioMatchGame.removePendingPosts();
        }
    }
}
