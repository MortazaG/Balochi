package com.alchemistmoz.balochi.categories.colors;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alchemistmoz.balochi.misc.ItemClickSupport;
import com.alchemistmoz.balochi.misc.CustomToolbar;
import com.alchemistmoz.balochi.R;
import com.alchemistmoz.balochi.misc.SoundPlayback;
import com.alchemistmoz.balochi.misc.Utilities;
import com.alchemistmoz.balochi.games.repetition.GameAdapter;
import com.alchemistmoz.balochi.games.repetition.GameItem;
import com.alchemistmoz.balochi.games.repetition.RepetitionGame;

import java.util.ArrayList;

public class RepetitionActivity extends AppCompatActivity {

    // Recycler view to be used for the game
    RecyclerView recyclerView;

    // Allow for global access to the game
    RepetitionGame repetitionGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_grid);

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
     * - Intro and game items lists
     * - Setup RecyclerView
     * - New instance of the RepetitionGame
     * - New instance of the recyclerView GameAdapter
     */
    private void initiateGame() {

        // List of sound intros to be played before each round
        ArrayList<GameItem> intros = new ArrayList<>();

        intros.add(new GameItem(R.raw.colors_green));
        intros.add(new GameItem(R.raw.colors_blue));
        intros.add(new GameItem(R.raw.colors_red));
        intros.add(new GameItem(R.raw.colors_yellow));
        intros.add(new GameItem(R.raw.colors_orange));
        intros.add(new GameItem(R.raw.colors_intro_blackwhite));
        intros.add(new GameItem(R.raw.colors_intro_greybrown));

        // List of items to be used in the game
        ArrayList<GameItem> colorItems = new ArrayList<>();

        // Add new GameItem objects to the ArrayList
        colorItems.add(new GameItem(R.drawable.colors_green, R.raw.colors_green));
        colorItems.add(new GameItem(R.drawable.fruitveggies_broccoli1, R.raw.colors_green_broccoli));
        colorItems.add(new GameItem(R.drawable.colors_blue, R.raw.colors_blue));
        colorItems.add(new GameItem(R.drawable.colors_blue_flower, R.raw.colors_blue_flower));
        colorItems.add(new GameItem(R.drawable.colors_red, R.raw.colors_red));
        colorItems.add(new GameItem(R.drawable.fruitveggies_tomato1, R.raw.colors_red_tomato));
        colorItems.add(new GameItem(R.drawable.colors_yellow, R.raw.colors_yellow));
        colorItems.add(new GameItem(R.drawable.fruitveggies_banana1, R.raw.colors_yellow_banana));
        colorItems.add(new GameItem(R.drawable.colors_orange, R.raw.colors_orange));
        colorItems.add(new GameItem(R.drawable.fruitveggies_orange1, R.raw.colors_orange_orange));
        colorItems.add(new GameItem(R.drawable.colors_black, R.raw.colors_black));
        colorItems.add(new GameItem(R.drawable.colors_white, R.raw.colors_white));
        colorItems.add(new GameItem(R.drawable.colors_grey, R.raw.colors_grey));
        colorItems.add(new GameItem(R.drawable.colors_brown, R.raw.colors_brown));

        // Setup recycler view as a grid layout
        recyclerView = Utilities.initGridRecyclerView(this, R.id.recycler_view_grid, 2);

        // New instance of the game
        repetitionGame = new RepetitionGame(recyclerView, intros, colorItems);

        GameAdapter adapter = new GameAdapter(this, repetitionGame.getActualItems(), R.layout.game_item);
        recyclerView.setAdapter(adapter);

        // Set adapter to be used for updating the UI during the game
        repetitionGame.useAdapter(adapter);
    }

    /**
     * Add click support for the game, by animating touch events and calling upon
     * repetitionGame.selectItem(position).
     */
    private void addItemClickSupport() {

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                repetitionGame.selectItem(position, v);

            }
        });
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
            repetitionGame.removePendingPosts();
        }
    }
}
