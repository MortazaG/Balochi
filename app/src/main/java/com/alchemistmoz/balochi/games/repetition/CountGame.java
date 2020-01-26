package com.alchemistmoz.balochi.games.repetition;

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

/**
 * This game is run in conjunction with the custom RecyclerView viewAdapter class GameAdapter,
 * SoundPlayback, as well as the GameItem class. GameItem class is used to create objects
 * that will be shown and used during the game, almost always with an image and sound.
 *
 * The layout of the game is defined in layout_count_game.xml and count_item.xml.
 *
 * Game description: This is a simple counting game, where the current number is shown to the left
 *                   of the screen and a corresponding amount of actualItems are shown at the right.
 *                   The purpose of the game is to click the actualItems one by one, until you reach
 *                   the final count.
 *
 * The game is implemented in the Activity class from which it will be run.
 *
 * How to implement the game:
 * - Initiate RecyclerView with the CountGame.initLinearRecyclerView(activity: this,
 *   R.id.recycler_view_count_game) static method.
 *
 * - Create a new instance of the game with two arguments, recycleView and the ImageView where
 *   the numberItems will be displayed.
 *
 * - Create a new instance of the GameAdapter with two arguments, context and the list of actualItems
 *   that will be used during the game. The list is obtained with countGame.getActualItems().
 *
 * - Set the adapter for the recyclerView with .setAdapter(adapter) and for the game with
 *   .useAdapter(adapter).
 *
 * - Run slide up animation for all items:
 *          GameUtils.runSlideUpAnim(recyclerView, GameUtils.COUNT);
 *
 * - Add ItemClickSupport via GameUtils:
 *          GameUtils.addCountItemClickSupport(recyclerView, countGame);
 *
 * - Add the following in onPause() of the activity:
 *           // Release the media player resources
 *         SoundPlayback.releaseMediaPlayer();
 *
 *         if (isFinishing()) {
 *             // Remove all pending posts of callbacks and sent messages.
 *             countGame.removePendingPosts();
 *         }
 *
 */
public class CountGame {

    // The current count value in the game
    private int count;

    // The number to be counted up to
    private int countGoal;

    // List of numbers Audio reference for counting
    private int[] numbersAudio;

    // The numberItems to be displayed to the left of the screen
    private ArrayList<GameItem> numberItems;

    // The gameItems that will be used for generating an actualItems list
    private ArrayList<GameItem> countItems;

    // The actualItems that will be counted at each new round
    private ArrayList<GameItem> actualItems;

    // The number image to be displayed to the left of the screen
    private ImageView numberImage;

    // The current number object to be displayed to the left of the screen
    private GameItem currentNumberItem;

    // RecyclerView from Activity needed for running animations and fetching context
    private RecyclerView recyclerView;

    // Adapter to be used for updating the RecyclerView UI
    private GameAdapter viewAdapter;

    // Used to store the context of the activity
    private Context context;

    // To be used for delaying posts
    private Handler handler;

    /**
     * Sets the game off by initiating default values and the ArrayLists that will be used
     * in the game.
     *
     * First number to be displayed is set and click listener for its ImageView is set to play
     * the corresponding sound.
     *
     * @param recyclerView - The games recyclerView
     * @param numberImage - the ImageView where the number objects will be shown
     */
    public CountGame(RecyclerView recyclerView, ImageView numberImage) {
        this.recyclerView = recyclerView;
        this.numberImage = numberImage;

        context = recyclerView.getContext();

        count = 0;
        countGoal = 1;

        handler = new Handler();

        // Initiate the lists that will be used for the game
        initiateLists();

        // Initiate the first round of the game with a list of actualItems
        generateActualItems();

        // Initiate the view to the left which will be showing the current number
        initiateNumbersView();
    }

    /**
     * Initiate list of numberItems and items to be used in the game.
     */
    private void initiateLists() {

        // Initiate and populate
        numbersAudio = new int[] {  R.raw.numbers_one,
                                    R.raw.numbers_two,
                                    R.raw.numbers_three,
                                    R.raw.numbers_four,
                                    R.raw.numbers_five,
                                    R.raw.numbers_six,
                                    R.raw.numbers_seven,
                                    R.raw.numbers_eight,
                                    R.raw.numbers_nine,
                                    R.raw.numbers_ten};

        // Initiate numberItems list
        numberItems = new ArrayList<>();

        // Add the number actualItems, which will be displayed to the left of the screen
        numberItems.add(new GameItem(R.drawable.numbers_one, numbersAudio[0]));
        numberItems.add(new GameItem(R.drawable.numbers_two, numbersAudio[1]));
        numberItems.add(new GameItem(R.drawable.numbers_three, numbersAudio[2]));
        numberItems.add(new GameItem(R.drawable.numbers_four, numbersAudio[3]));
        numberItems.add(new GameItem(R.drawable.numbers_five, numbersAudio[4]));
        numberItems.add(new GameItem(R.drawable.numbers_six, numbersAudio[5]));
        numberItems.add(new GameItem(R.drawable.numbers_seven, numbersAudio[6]));
        numberItems.add(new GameItem(R.drawable.numbers_eight, numbersAudio[7]));
        numberItems.add(new GameItem(R.drawable.numbers_nine, numbersAudio[8]));
        numberItems.add(new GameItem(R.drawable.numbers_ten, numbersAudio[9]));

        // Initiate countItems list
        countItems = new ArrayList<>();

        // Add all the count actualItems that will be used for counting
        countItems.add(new GameItem(R.drawable.fruitveggies_strawberry1, R.raw.numbers_one_strawberry));
        countItems.add(new GameItem(R.drawable.fruitveggies_carrot1, R.raw.numbers_two_carrots));
        countItems.add(new GameItem(R.drawable.fruitveggies_lemon1, R.raw.numbers_three_lemons));
        countItems.add(new GameItem(R.drawable.fruitveggies_mango1, R.raw.numbers_four_mangos));
        countItems.add(new GameItem(R.drawable.fruitveggies_orange1, R.raw.numbers_five_oranges));
        countItems.add(new GameItem(R.drawable.fruitveggies_apple1, R.raw.numbers_six_apples));
        countItems.add(new GameItem(R.drawable.fruitveggies_tomato1, R.raw.numbers_seven_tomatoes));
        countItems.add(new GameItem(R.drawable.fruitveggies_watermelon1, R.raw.numbers_eight_watermelons));
        countItems.add(new GameItem(R.drawable.fruitveggies_strawberry1, R.raw.numbers_nine_strawberries));
        countItems.add(new GameItem(R.drawable.fruitveggies_apple1, R.raw.numbers_ten_apples));

        // Initiate actualItems list which will store the countable actualItems
        actualItems = new ArrayList<>();


    }

    /**
     * Generate a list of identical actualItems to be used for counting.
     * Changes the actualItems each round by going through the countItems list.
     */
    private void generateActualItems() {

        // The current countGoal indicates how many actualItems that are needed
        for (int x = 0; x < countGoal; x++) {

            // The current count allows for going through the countItems in concurrent order
            int nextCountItemIndex = count;

            GameItem currentItem = countItems.get(nextCountItemIndex);

            // Add a new instance of the current item in the countItems list
            actualItems.add(new GameItem(currentItem.getImageResourceID(), currentItem.getAudioResourceID()));
        }
    }

    /**
     * Set the first number image to be shown in the view to the left and
     * make it clickable.
     */
    private void initiateNumbersView() {
        // Initiate currentNumberItem with the first item from the numberItems list
        currentNumberItem = numberItems.get(0);

        // Set the first number as the current image to be displayed to the left
        Glide.with(context)
                .load(currentNumberItem.getImageResourceID())
                .into(numberImage);

        // Set click lister in order to allow the correct numberImage sound to be played
        numberImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (GameUtils.isTouchEnabled()) {

                    // Disable further touch events
                    GameUtils.setTouchEnabled(false);

                    Utilities.runOnTouchAnim(context, numberImage);

                    // Initialize playback of the sound related to the current count
                    SoundPlayback.play(context, currentNumberItem.getAudioResourceID());

                    // Enable touch events after playback is finished
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            GameUtils.setTouchEnabled(true);

                        }
                    }, SoundPlayback.getSoundDuration());

                }
            }
        });
    }

    /**
     * @return list of identical items to be used during the game.
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
     * then check for the current status of the game.
     *
     * @param position - of the view that the user has selected
     */
    public void selectItem(final int position, View view) {

        // "Catch" ArrayIndexOutOfBoundsException when index = -1
        // and basically make the selection void.
        if (position == -1) return;

        // Store the gameItem that the user has currently selected
        GameItem selectedItem = actualItems.get(position);

        if (GameUtils.isTouchEnabled() && !selectedItem.isSelected()) {

            // Disable further touch events
            GameUtils.setTouchEnabled(false);

            Utilities.runOnTouchAnim(context, view);

            // Increase the current count value
            count += 1;

            // Play the sound for the count item the user has selected
            playSelectedCountNumberSound(selectedItem.getAudioResourceID());

            // Set selected to true so that visibility changes to INVISIBLE
            selectedItem.setSelected(true);

            // Execute the following after sound playback
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    GameUtils.setTouchEnabled(true);

                    // Update the UI
                    viewAdapter.notifyItemChanged(position);

                    checkGoal();

                }
            }, SoundPlayback.getSoundDuration());
        }
    }

    /**
     * Initiate playback for the user selected count number in the right order.
     *
     * @param audioResourceId - audio res id for the current countItem.
     */
    private void playSelectedCountNumberSound(int audioResourceId) {

        if (count == countGoal) {
            // Playback of the sound for the final number in the count series
            SoundPlayback.play(context, audioResourceId);

        } else {

            // Current count value -1, represents the index for the numbers audio to be played
            int currentAudioIndex = (count - 1);

            // Playback of the sound for the current number in the count series.
            SoundPlayback.play(context, numbersAudio[currentAudioIndex]);
        }
    }

    /**
     * Check if the counting goal has been reached and then initiate the next round.
     */
    private void checkGoal() {
        if (count == countGoal) {
            nextGoal();
            nextRound();
        }
    }

    /**
     * Update the countGoal for the next round. Unless the game has reached its final goal,
     * in that case, play long celebration sound and reset the game.
     */
    private void nextGoal() {

        if (countGoal == 10) {

            // Play long celebration sound
            playCelebrationSound(R.raw.celebration_short);

            // Reset the initial values for count and countGoal.
            count = 0;
            countGoal = 1;

        } else if (countGoal < 10) {

            // Play short celebration sound
            playCelebrationSound(R.raw.celebration_short);

            countGoal += 1;
        }
    }

    /**
     * Play the given celebration sound only if the window currently has focus.
     *
     * @param audioResourceId - Resource Id for the celebration sound to be played.
     */
    private void playCelebrationSound(int audioResourceId) {

        if (recyclerView.hasWindowFocus()) {
            SoundPlayback.play(context, audioResourceId);
        }
    }

    /**
     * Initiate the next round of the game by generating new items and updating the UI.
     */
    private void nextRound() {

        // Clear actualItems and generate new ones
        actualItems.clear();
        generateActualItems();

        // Next position in the numberItems list is the same as the current count value
        int nextNrPos = count;

        // Update currentNumberItem
        currentNumberItem.setImageResourceID(numberItems.get(nextNrPos).getImageResourceID());
        currentNumberItem.setAudioResourceID(numberItems.get(nextNrPos).getAudioResourceID());

        // Load the image for the currentNumberItem
        Glide.with(numberImage.getContext())
                .load(numberItems.get(nextNrPos).getImageResourceID())
                .into(numberImage);

        // Reset the count
        count = 0;

        // Update UI
        viewAdapter.notifyDataSetChanged();

        // Run the slide up animation for the new items
        runLayoutAnimation();

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
