package com.alchemistmoz.balochi.categories.face;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.alchemistmoz.balochi.Category;
import com.alchemistmoz.balochi.MainAdapter;
import com.alchemistmoz.balochi.R;
import com.alchemistmoz.balochi.misc.CustomToolbar;
import com.alchemistmoz.balochi.misc.SoundPlayback;
import com.alchemistmoz.balochi.misc.Utilities;

import java.util.ArrayList;

public class FaceActivity extends AppCompatActivity {

    // Make recyclerView global so that animations can be run from onResume
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list);

        // Initiate and setup custom toolbar
        CustomToolbar toolbar = new CustomToolbar(FaceActivity.this);
        toolbar.initCategoryToolbar();

        // Initialize the Audio Manager AUDIO_SERVICE
        SoundPlayback.initializeManagerService(this);

        initiateMenu();

        Utilities.addMenuItemClickSupport(recyclerView);

    }


    /**
     * Initiate the menu with:
     * - Category list
     * - Setup RecyclerView
     * - New instance of the recyclerView MainAdapter
     */
    private void initiateMenu() {

        // Create an ArrayList object that will contain Category objects
        // Make it final so that it can be accessed by anonymous class
        final ArrayList<Category> categories = new ArrayList<>();

        // Add new Category objects to the ArrayList
        categories.add(new Category(R.drawable.face_item4, R.raw.menu_repetition, RepetitionActivity.class));
        categories.add(new Category(R.drawable.colors_menu_memory, R.raw.menu_repetition, MemoryActivity.class));
        categories.add(new Category(R.drawable.colors_menu_audiomatch5, R.raw.menu_repetition, AudioMatchActivity.class));

        // Setup the recycler view
        recyclerView = Utilities.initLinearRecyclerView(this, R.id.recycler_view_list);

        final MainAdapter adapter = new MainAdapter(this, categories);
        recyclerView.setAdapter(adapter);

        Utilities.runSlideLeftAnim(recyclerView);
    }

    /**
     * Clean up the media player and remove pending posts
     * when activity is paused.
     */
    @Override
    protected void onPause() {
        super.onPause();

        // Release the media player resources
        SoundPlayback.releaseMediaPlayer();

        // Remove all pending posts of callbacks and sent messages.
        Utilities.removePendingPosts();

        // Enable all touch events (in case touch was disabled right before onPause)
        Utilities.setTouchEnabled(true);
    }
}
