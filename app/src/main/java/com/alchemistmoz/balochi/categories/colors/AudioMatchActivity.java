package com.alchemistmoz.balochi.categories.colors;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.alchemistmoz.balochi.R;
import com.alchemistmoz.balochi.games.audiomatch.AudioMatchGame;
import com.alchemistmoz.balochi.games.GameAdapter;
import com.alchemistmoz.balochi.games.GameItem;
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
     * - New instance of the MemoryGame
     * - New instance of the recyclerView MemoryAdapter
     */
    private void initiateGame() {

        // Initiate GameItems list to be used in the game
        ArrayList<GameItem> GameItems = new ArrayList<>();

        // Add new GameItem objects to the ArrayList
        GameItems.add(new GameItem(R.drawable.colors_memory_red, R.raw.colors_red));
        GameItems.add(new GameItem(R.drawable.colors_memory_green, R.raw.colors_green));
        GameItems.add(new GameItem(R.drawable.colors_memory_blue, R.raw.colors_blue));
        GameItems.add(new GameItem(R.drawable.colors_memory_yellow, R.raw.colors_yellow));
        GameItems.add(new GameItem(R.drawable.colors_memory_orange, R.raw.colors_orange));
        GameItems.add(new GameItem(R.drawable.colors_memory_brown, R.raw.colors_brown));
        GameItems.add(new GameItem(R.drawable.colors_memory_black, R.raw.colors_black));
        GameItems.add(new GameItem(R.drawable.colors_memory_white, R.raw.colors_white));
        GameItems.add(new GameItem(R.drawable.colors_memory_grey, R.raw.colors_grey));

        // Setup recycler view as a gridlayout
        recyclerView = GameUtils.initGridRecyclerView(this, R.id.recycler_view_audio_match, 2);

        // Find and store the image view so that it can be set by the game
        ImageView speakerPhoneView = findViewById(R.id.speaker_phone_image);

        // Initiate new round of MemoryGame game
        audioMatchGame = new AudioMatchGame(recyclerView, GameItems, speakerPhoneView);

        // Create a new recycler view adapter
        GameAdapter adapter = new GameAdapter(this, audioMatchGame.getActualItems(), R.layout.audio_match_item);
        recyclerView.setAdapter(adapter);

        // Set adapter to be used for the game
        audioMatchGame.useAdapter(adapter);

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
            audioMatchGame.removePendingPosts();
        }
    }
}
