package com.alchemistmoz.balochi.categories.face;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.alchemistmoz.balochi.R;
import com.alchemistmoz.balochi.games.repetition.FaceGame;
import com.alchemistmoz.balochi.misc.CustomToolbar;
import com.alchemistmoz.balochi.misc.GameUtils;
import com.alchemistmoz.balochi.misc.SoundPlayback;

public class RepetitionActivity extends AppCompatActivity {

    // Allow for global access to the game
    FaceGame faceGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_face_game);

        // Initiate and setup custom toolbar
        CustomToolbar toolbar = new CustomToolbar(RepetitionActivity.this);
        toolbar.initGameToolbar();

        // Initialize the Audio Manager AUDIO_SERVICE
        SoundPlayback.initializeManagerService(this);

        // Initiate a new round of the counting game
        faceGame = new FaceGame();

//        GameUtils.addFaceItemClickSupport(recyclerView, faceGame);
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
            faceGame.removePendingPosts();
        }
    }
}
