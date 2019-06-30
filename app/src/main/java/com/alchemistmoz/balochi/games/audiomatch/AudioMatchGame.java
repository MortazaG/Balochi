package com.alchemistmoz.balochi.games.audiomatch;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;

import com.alchemistmoz.balochi.R;
import com.alchemistmoz.balochi.games.GameAdapter;
import com.alchemistmoz.balochi.games.GameItem;
import com.alchemistmoz.balochi.misc.GameUtils;
import com.alchemistmoz.balochi.misc.SoundPlayback;
import com.alchemistmoz.balochi.misc.Utilities;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;


/**
 * This game is run in conjunction with the custom RecyclerView Adapter class GameAdapter,
 * SoundPlayback, as well as the GameItem class. GameItem class is used to create objects
 * that will be shown and used during the game, almost always with an image and sound.
 *
 * The layout of the game is defined in layout_audio_match.xml and audio_match_item.xml.
 *
 * Game description: An intro sound is played and two to four items are shown.
 *                   The player has to guess which item matches the intro sound.
 *                   The intro sound can be played again by touching the big speaker phone image.
 *                   If it's a match, then a new round is started.
 *
 * The game is implemented in the Activity class from which it will be run.
 *
 * How to implement the game:
 * - One list is needed: Memory cards items for respective category, called gameItems.
 *
 * - Initiate RecyclerView with
 *         GameUtils.initGridRecyclerView(this, R.id.recycler_view_audio_match, 2).
 *
 * - Find and store the image view so that it can be set by the game
 *         ImageView speakerPhoneView = findViewById(R.id.speaker_phone_image);
 *
 * - Create a new instance of the game with three arguments, recycleView, gameItems and imageView.
 *
 * - Create a new instance of the GameAdapter with three arguments, context, the list of actualItems
 *   that will be used during the game (obtained with repetitionGame.getActualItems()) and
 *   R.layout.xxx for the game item layout.
 *
 * - Set the adapter for the recyclerView with .setAdapter(adapter) and for the game with
 *   .useAdapter(adapter).
 *
 * - Run slide up animations:
 *          GameUtils.runSlideUpAnim(recyclerView, GameUtils.AUDIOMATCH);
 *
 * - Add ItemClickSupport via GameUtils:
 *          GameUtils.addAudioMatchItemClickSupport(recyclerView, audioMatchGame);
 *
 * - Add the following in onPause() of the activity:
 *           // Release the media player resources
 *         SoundPlayback.releaseMediaPlayer();
 *
 *         if (isFinishing()) {
 *             // Remove all pending posts of callbacks and sent messages.
 *             audioMatchGame.removePendingPosts();
 *         }
 *
 */
public class AudioMatchGame {

    // All the available levels in the game
    private static final int LEVEL_ONE = 2;
    private static final int LEVEL_TWO = 4;

    // Current level in the game
    private int currentLevel;

    // The users guess of the correctItemId
    private int selectedItemId;

    // Store the current rounds correct word
    private int correctItemId;

    // The gameItems that will be used for generating an actualItems list
    private ArrayList<GameItem> gameItems;

    // The gameItems that will be visible during each round of the game
    private ArrayList<GameItem> actualItems;

    // Image of the speaker phone to be displayed to the top of the screen
    private ImageView speakerPhoneView;

    private GameItem speakerPhoneObject;

    // RecyclerView from Activity needed for running animations and fetching context
    private RecyclerView recyclerView;

    // Adapter to be used for updating the RecyclerView UI
    private GameAdapter viewAdapter;

    // Used to store the context of the activity
    private Context context;

    // To be used for delaying posts
    private Handler handler;

    // Allow for delaying the intro for each round and thus providing a better user experience
    private Runnable introRunnable;

    /**
     * Sets the game off by initiating default values and the ArrayLists that will be used
     * in the game.
     *
     * @param recyclerView - The games recyclerView
     * @param gameItems - List of items to be used in the game
     * @param speakerPhoneView - Speaker phone ImageView
     */
    public AudioMatchGame(RecyclerView recyclerView, ArrayList<GameItem> gameItems, ImageView speakerPhoneView) {
        this.recyclerView = recyclerView;
        this.gameItems = gameItems;
        this.speakerPhoneView = speakerPhoneView;

        context = recyclerView.getContext();

        // Start the game of with the first level
        currentLevel = LEVEL_ONE;

        // Placeholder for the sound of the correctItemId
        speakerPhoneObject = new GameItem(R.drawable.speaker_phone, R.raw.menu_colors);

        correctItemId = 0;
        selectedItemId = 0;

        handler = new Handler();
        introRunnable = new Runnable() {
            @Override
            public void run() {
                playCorrectItemIntro();
            }
        };

        actualItems = new ArrayList<>();

        // Initiate the first round of the game
        initiateSpeakerPhoneView();
        generateActualItems();

        // Disable touch during intro
        GameUtils.setTouchEnabled(false);

        handler.postDelayed(introRunnable, 800);
    }


    /**
     * Set the speaker phone image to be shown and make it clickable
     */
    private void initiateSpeakerPhoneView() {

        // Set the speaker phone image
        Glide.with(context)
                .load(speakerPhoneObject.getImageResourceID())
                .into(speakerPhoneView);

        // Set click listener in order to allow the correct sound to be played
        speakerPhoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (GameUtils.isTouchEnabled()) {

                    Utilities.runOnTouchAnim(context, speakerPhoneView);

                    // Initialize playback of the sound of the correctItem
                    SoundPlayback.play(context, speakerPhoneObject.getAudioResourceID());
                }
            }
        });
    }

    /**
     * Generate the game Items that will be used to interact with in the game for one round.
     */
    private void generateActualItems() {

        // Shuffle the game items in order to randomize the words of
        // each new round.
        Collections.shuffle(gameItems);

        // Generate list of items to be used for the current level
        for (int x = 0; x < currentLevel; x++) {

            GameItem currentItem = gameItems.get(x);

            // Add a new instance of the current item
            actualItems.add(new GameItem(currentItem.getImageResourceID(), currentItem.getAudioResourceID()));
        }

        // First item in the list is chosen as the correct word item
        correctItemId = actualItems.get(0).getAudioResourceID();
        speakerPhoneObject.setAudioResourceID(correctItemId);

        // Shuffle list again so that they are presented
        // in a random order.
        Collections.shuffle(actualItems);
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
     * Play sound intro for the correct item of the current round.
     */
    private void playCorrectItemIntro() {

        // Initialize playback of the intro if window is in focus
        playSound(correctItemId);

        // Enable touch events after sound playback
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                GameUtils.setTouchEnabled(true);
            }
        }, SoundPlayback.getSoundDuration());

    }

    /**
     * Play the sound of the selected item and check if it
     * matches with the correct item.
     *
     * @param position - of the view that the user has selected
     * @param view - actual view the user has selected
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

            // Store the resource id for the selected item
            selectedItemId = selectedItem.getAudioResourceID();

            // Check game status and enable touch events after sound playback
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    GameUtils.setTouchEnabled(true);
                    checkSelections();

                }
            }, SoundPlayback.getSoundDuration());
        }
    }

    /**
     * Check if the selected items are a match.
     */
    private void checkSelections() {
        if (selectedItemId == correctItemId) {

            // Celebrate and initiate the next round
            playSound(R.raw.correct_answer3);
            nextRound();

        } else {

            // Playback of the error sound
            playSound(R.raw.wrong_answer);

            // Reset the current selection
            selectedItemId = 0;
        }
    }

    /**
     * Play the given sound only if the window currently has focus.
     * The sound to be played should be either in celebration or for
     * wrong answers.
     *
     * @param audioResourceId - Resource Id for the sound to be played.
     */
    private void playSound(int audioResourceId) {

        if (recyclerView.hasWindowFocus()) {
            SoundPlayback.play(context, audioResourceId);
        }
    }

    /**
     * Initiate the next round of the game by generating new items and updating the UI.
     */
    private void nextRound() {

        // Reset the initial values
        resetGame();

        nextLevel();

        // Initiate a new round of the game
        generateActualItems();

        // Update UI
        viewAdapter.notifyDataSetChanged();

        // Run the slide up animation for the new items
        runLayoutAnimation();

        // Disable touch during intro
        GameUtils.setTouchEnabled(false);

        handler.postDelayed(introRunnable, 800);

    }

    /**
     * Reset the initial values in order to restart the game.
     */
    private void resetGame() {

        correctItemId = 0;
        selectedItemId = 0;

        // Clear list of actualItems
        actualItems.clear();
    }

    /**
     * Set the next level to be played.
     * Always goes from LEVEL_ONE to LEVEL_TWO and
     * then in reverse.
     */
    private void nextLevel() {
        if (currentLevel == LEVEL_ONE) {

            currentLevel = LEVEL_TWO;

        } else if (currentLevel == LEVEL_TWO) {

            currentLevel = LEVEL_ONE;
        }
    }

    /**
     * Run the slide up layout animation for all items.
     * To be used in the beginning of every round.
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