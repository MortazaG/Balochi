package com.alchemistmoz.balochi.games.repetition;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alchemistmoz.balochi.R;
import com.alchemistmoz.balochi.games.GameItem;
import com.alchemistmoz.balochi.misc.GameUtils;
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
public class FaceGame {

    // The gameItems that will be visible during the game
    private ArrayList<GameItem> actualItems;

    // Used to store the context of the activity
    private Context context;

    // To be used for delaying posts
    private Handler handler;

    /**
     * Sets the game off by initiating default values and the ArrayLists that will be used
     * in the game.
     *
     * @param recyclerView - The games recyclerView
     */
    public FaceGame(RecyclerView recyclerView) {
        context = recyclerView.getContext();

        handler = new Handler();
        actualItems = new ArrayList<>();

        // Initiate the first round of the game with a list of actualItems
        initiateActualItems();

    }

    /**
     * Generate the game Items that will be used to interact with in the game for one round.
     */
    private void initiateActualItems() {
        
        // Add new GameItem objects to the ArrayList
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));

        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));

        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));

        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));

        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));
        actualItems.add(new GameItem(R.drawable.face_item_blank, R.raw.colors_green));

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
     * Play the sound of the selected item and make it INVISIBLE,
     * then check for the current status of the game.
     *
     * @param position - of the view that the user has selected
     */
    public void selectItem(final int position, View view) {

        // Store the gameItem that the user has currently selected
        GameItem selectedItem = actualItems.get(position);

        if (GameUtils.isTouchEnabled()) {

            // Disable further touch events
            GameUtils.setTouchEnabled(false);

            Utilities.runOnTouchAnim(context, view);

            // Initialize playback of the sound related to the item the user has selected
            SoundPlayback.play(context, selectedItem.getAudioResourceID());

            // Check game status and enable touch events after sound playback
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    GameUtils.setTouchEnabled(true);

                }
            }, SoundPlayback.getSoundDuration());
        }
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
