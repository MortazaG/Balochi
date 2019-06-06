package com.alchemistmoz.balochi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.alchemistmoz.balochi.categories.colors.ColorsActivity;
import com.alchemistmoz.balochi.categories.family.FamilyActivity;
import com.alchemistmoz.balochi.categories.fruitveggies.FruitVeggiesActivity;
import com.alchemistmoz.balochi.categories.numbers.NumbersActivity;
import com.alchemistmoz.balochi.misc.CustomToolbar;
import com.alchemistmoz.balochi.misc.SoundPlayback;
import com.alchemistmoz.balochi.misc.Utilities;

import java.util.ArrayList;

/**
 * SoundPlayback.initializeManagerService(getApplicationContext()); needs to be called in
 * all the activities of the app, in order to allow for sound playback.
 */
public class MainActivity extends AppCompatActivity {

    // Make recyclerView global so that animations can be run from onResume
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list);

        // Initiate and setup custom toolbar
        CustomToolbar toolbar = new CustomToolbar(MainActivity.this);
        toolbar.initBasicToolbar();

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
        categories.add(new Category(R.drawable.numbers5, R.raw.menu_numbers, NumbersActivity.class));
        categories.add(new Category(R.drawable.colors3, R.raw.menu_colors, ColorsActivity.class));
        categories.add(new Category(R.drawable.family1, R.raw.menu_family, FamilyActivity.class));
        categories.add(new Category(R.drawable.fruitveggies1, R.raw.menu_fruitveggies, FruitVeggiesActivity.class));

        // Setup the recycler view with the id of the recycler view to be used and type of layout
        recyclerView = Utilities.initLinearRecyclerView(this, R.id.recycler_view_list);

        // Instantiate an adapter with the given data set
        MainAdapter adapter = new MainAdapter(this, categories);

        // Specify an adapter
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
