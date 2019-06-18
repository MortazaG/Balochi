package com.alchemistmoz.balochi.categories.family;

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
        gameItems.add(new GameItem(R.drawable.family_memory_mother, R.raw.family_mother));
        gameItems.add(new GameItem(R.drawable.family_memory_father, R.raw.family_father));
        gameItems.add(new GameItem(R.drawable.family_memory_sister, R.raw.family_sister));
        gameItems.add(new GameItem(R.drawable.family_memory_brother, R.raw.family_brother));
        gameItems.add(new GameItem(R.drawable.family_memory_mgrandmother, R.raw.family_grandmother));
        gameItems.add(new GameItem(R.drawable.family_memory_mgrandfather, R.raw.family_grandfather));
        gameItems.add(new GameItem(R.drawable.family_memory_pgrandmother, R.raw.family_grandmother));
        gameItems.add(new GameItem(R.drawable.family_memory_pgrandfather, R.raw.family_grandfather));
        gameItems.add(new GameItem(R.drawable.family_memory_maunt, R.raw.family_aunt));
        gameItems.add(new GameItem(R.drawable.family_memory_muncle, R.raw.family_uncle));
        gameItems.add(new GameItem(R.drawable.family_memory_paunt, R.raw.family_aunt));
        gameItems.add(new GameItem(R.drawable.family_memory_puncle, R.raw.family_uncle));

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
