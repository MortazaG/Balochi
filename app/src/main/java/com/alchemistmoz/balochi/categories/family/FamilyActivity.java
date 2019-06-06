package com.alchemistmoz.balochi.categories.family;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.alchemistmoz.balochi.Category;
import com.alchemistmoz.balochi.MainAdapter;
import com.alchemistmoz.balochi.misc.CustomToolbar;
import com.alchemistmoz.balochi.misc.SoundPlayback;
import com.alchemistmoz.balochi.R;
import com.alchemistmoz.balochi.misc.Utilities;

import java.util.ArrayList;

public class FamilyActivity extends AppCompatActivity {

    // Make recyclerView global so that animations can be run from onResume
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list);

        // Initiate and setup custom toolbar
        CustomToolbar toolbar = new CustomToolbar(FamilyActivity.this);
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
        categories.add(new Category(R.drawable.family_menu_repetition1, R.raw.menu_repetition, RepetitionActivity.class));
        categories.add(new Category(R.drawable.family_menu_memory, R.raw.menu_memory, MemoryActivity.class));

        // Setup the recycler view
        recyclerView = Utilities.initLinearRecyclerView(this, R.id.recycler_view_list);

        final MainAdapter adapter = new MainAdapter(this, categories);
        recyclerView.setAdapter(adapter);

    }

    /**
     * Override onResume in order to run the slide Animation
     * after onCreate and onStart.
     */
    @Override
    protected void onResume() {
        super.onResume();

        Utilities.runSlideLeftAnim(recyclerView);
    }

    /**
     * Clean up the media player when activity is paused.
     */
    @Override
    protected void onPause() {
        super.onPause();
        // When the activity is stopped, release the media player resources because we won't
        // be playing any more sounds.
        SoundPlayback.releaseMediaPlayer();
    }
}