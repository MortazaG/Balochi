package com.alchemistmoz.balochi.games.repetition;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.alchemistmoz.balochi.R;
import com.alchemistmoz.balochi.misc.SoundPlayback;
import com.alchemistmoz.balochi.misc.Utilities;

import java.util.ArrayList;


/**
 * This game is run in conjunction with the custom RecyclerView Adapter class GameAdapter,
 * SoundPlayback, as well as the GameItem class. GameItem class is used to create objects
 * that will be shown and used during the game, almost always with an image and sound.
 *
 * The layout of the game is defined in layout_grid.xml and game_item.xml.
 *
 * Game description: A descriptive intro sound is played and two related items are shown.
 *                   After the items have been selected by the user, the above is repeated.
 *                   This repeats until the end of the items list is reached and then the game
 *                   starts from the beginning.
 *
 * The game is implemented in the Activity class from which it will be run.
 *
 * How to implement the game:
 * - Two lists are needed: * Sound intros list
 *                         * Game items list
 *
 * - Initiate RecyclerView with Utilities.initGridRecyclerView(this, R.id.recycler_view_grid, 2).
 *
 * - Create a new instance of the game with three arguments, recycleView, intros and gameItems.
 *
 * - Create a new instance of the GameAdapter with three arguments, context, the list of actualItems
 *   that will be used during the game (obtained with repetitionGame.getActualItems()) and
 *   R.layout.xxx for the game item layout.
 *
 * - Set the adapter for the recyclerView with .setAdapter(adapter) and for the game with
 *   .useAdapter(adapter).
 *
 * - Use ItemClickSupport so that all objects that are selected triggers repetitionGame.selectItem(pos).
 *   Utilities.runOnTouchAnim(RepetitionActivity.this, v); should be run first, then
 *   run .selectItem(pos) with handler.postDelayed after Utilities.ON_TOUCH_ANIM_LENGTH.
 *
 */
public class RepetitionGame {

    // The number of selected views
    private int nrOfSelectedItems;

    // The total number of selected views
    private int totalNrOfSelectedItems;

    // Index number of the intro for the current round
    private int currentIntroIndex;

    // List of introductory sounds that will be played at the start of each round
    private ArrayList<GameItem> intros;

    // The gameItems that will be used for generating an actualItems list
    private ArrayList<GameItem> gameItems;

    // The gameItems that will be visible during each round of the game
    private ArrayList<GameItem> actualItems;

    // RecyclerView from Activity needed for running animations and fetching context
    private RecyclerView recyclerView;

    // Adapter to be used for updating the RecyclerView UI
    private GameAdapter viewAdapter;

    // Used to store the context of the activity
    private Context context;

    // Allows for "disabling" touch events during sound playback
    private boolean touchEnabled;

    // To be used for delaying posts
    private Handler handler;

    /**
     * Sets the game off by initiating default values and the ArrayLists that will be used
     * in the game.
     *
     * @param recyclerView - The games recyclerView
     */
    public RepetitionGame(RecyclerView recyclerView, ArrayList<GameItem> intros, ArrayList<GameItem> gameItems) {
        this.recyclerView = recyclerView;
        this.intros = intros;
        this.gameItems = gameItems;

        context = recyclerView.getContext();

        nrOfSelectedItems = 0;
        totalNrOfSelectedItems = 0;
        currentIntroIndex = 0;

        touchEnabled = true;

        actualItems = new ArrayList<>();

        // Initiate the first round of the game with a list of actualItems
        generateActualItems();

        playCurrentRoundIntro();
    }

    /**
     * Generate the game Items that will be used to interact with in the game for one round.
     */
    private void generateActualItems() {

        for (int x = 0; x < 2; x++) {

            // The totalNrOfSelectedItems allows for going through the gameItems in concurrent order
            int index = x + totalNrOfSelectedItems;

            GameItem currentItem = gameItems.get(index);

            // Add a new instance of the current item
            actualItems.add(new GameItem(currentItem.getImageResourceID(), currentItem.getAudioResourceID()));
        }
    }

    /**
     * Play a descriptive sound intro for the items of the current round.
     */
    private void playCurrentRoundIntro() {

        // Disable touch events
        setTouchEnabled(false);

        // Initialize playback of the intro
        SoundPlayback.play(context, intros.get(currentIntroIndex).getAudioResourceID());

        // Increment index number with one, to be used for the next round
        currentIntroIndex += 1;

        // Enable touch events after sound playback
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setTouchEnabled(true);
            }
        }, SoundPlayback.getSoundDuration());

    }

    /**
     * Used in the activity when creating an instance of the adapter.
     *
     * @return list of items to be used during the game.
     */
    public ArrayList<GameItem> getActualItems() {
        return actualItems;
    }

    /**
     * @param recyclerViewAdapter - The RecyclerView viewAdapter to be used for updating the UI.
     */
    public void useAdapter(GameAdapter recyclerViewAdapter) {
        viewAdapter = recyclerViewAdapter;
    }

    /**
     * Play the sound of the selected item and make it INVISIBLE,
     *  then check for the current status of the game.
     *
     * @param position - of the view that the user has selected
     */
    public void selectItem(final int position, View view) {

        // Store the gameItem that the user has currently selected
        GameItem selectedItem = actualItems.get(position);

        if (isTouchEnabled() && !selectedItem.isSelected()) {

            // Disable further touch events
            setTouchEnabled(false);

            Utilities.runOnTouchAnim(context, view);

            // Increase the number of selected items for this round
            nrOfSelectedItems += 1;

            // Incrementally increase the total number of selected items
            totalNrOfSelectedItems += 1;

            // Initialize playback of the sound related to the item the user has selected
            SoundPlayback.play(context, selectedItem.getAudioResourceID());

            // Set selected to true so that visibility changes to INVISIBLE
            selectedItem.setSelected(true);

            // Check game status and enable touch events after sound playback
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    setTouchEnabled(true);

                    // Update the UI
                    viewAdapter.notifyItemChanged(position);

                    checkGameStatus();

                }
            }, SoundPlayback.getSoundDuration());
        }
    }

    /**
     * @param status of touch events.
     */
    private void setTouchEnabled(boolean status) {
        touchEnabled = status;
    }

    /**
     * @return current status of touch events.
     */
    private boolean isTouchEnabled() {
        return touchEnabled;
    }


    /**
     * Check the current status of the game. If both items have been selected,
     * then bring the next two i.e. start the next round.
     */
    private void checkGameStatus() {
        if (nrOfSelectedItems >= 2) {
            nextRound();
        }
    }

    /**
     * Initiate the next round of the game by generating new items and updating the UI.
     */
    private void nextRound() {

        if (totalNrOfSelectedItems == gameItems.size()) {

            resetGame();

        }

        // Clear actualItems and generate new ones
        actualItems.clear();
        generateActualItems();

        // Reset the number of selected items
        nrOfSelectedItems = 0;

        // Update UI
        viewAdapter.notifyDataSetChanged();

        // Run the slide up animation for the new items
        runLayoutAnimation();

        playCurrentRoundIntro();

    }

    /**
     * Reset the initial values in order to restart the game.
     */
    private void resetGame() {
        nrOfSelectedItems = 0;
        totalNrOfSelectedItems = 0;
        currentIntroIndex = 0;
    }

    /**
     * Run the slide up layout animation for all items upon each new round.
     */
    private void runLayoutAnimation() {
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_up);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.scheduleLayoutAnimation();
    }

    /**
     * Remove all pending posts of callbacks and sent messages.
     */
    public void removePendingPosts() {

        if (handler != null) {

            handler.removeCallbacksAndMessages(null);

        }
    }
}
