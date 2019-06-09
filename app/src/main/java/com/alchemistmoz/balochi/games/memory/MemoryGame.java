package com.alchemistmoz.balochi.games.memory;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.alchemistmoz.balochi.misc.CustomGridLayoutManager;
import com.alchemistmoz.balochi.R;
import com.alchemistmoz.balochi.misc.GameUtils;
import com.alchemistmoz.balochi.misc.SoundPlayback;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This game is run in conjunction with the custom recycler view adapter class MemoryAdapter,
 * SoundPlayback, as well as the MemoryCard class. MemoryCard class is used to create objects
 * that will be shown and used during the game, almost always with an image and sound.
 *
 * The layout of the game is defined in layout_grid.xml and memory_item.xml.
 *
 * Game description: Simple memory game starting with three pairs, workings its way up to seven.
 *
 * The game is to be implemented in the MemoryActivity class from which it will be run.
 *
 * How to implement the game:
 * - Initiate a memoryCards ArrayList<MemoryCard> and all the items to be used in the game.
 *
 * - Initiate RecyclerView with the MemoryGame.initLinearRecyclerView(activity: this,
 *   R.id.recycler_view_grid) static method. recyclerView needs to be a global variable, so that
 *   animations can be run during onResume().
 *
 * - Create a new instance of the game with two argument, the recyclerView and the list
 *   of memoryCards.
 *
 * - Create a new instance of the MemoryAdapter with two arguments, context and the placeholder
 *   cards that will be used. The placeholders are obtained with countGame.getMemoryCardPlaceholders().
 *
 * - Set the adapter for the recyclerView with .setAdapter(adapter) and for the game with
 *   .useAdapter(adapter).
 *
 * - Use ItemClickSupport so that all objects that are selected trigger memoryGame.selectCard(pos).
 *
 * The game generates a list of memory card back sides (placeholders ) to hide all the cards,
 * as well as a list of memory card front sides, which are shown when the user selects a card.
 * 
 */
public class MemoryGame {

    // All the available levels in the game
    private static final int LEVEL_ONE = 2;
    private static final int LEVEL_TWO = 3;
    private static final int LEVEL_THREE = 4;
    private static final int LEVEL_FOUR = 5;
    private static final int LEVEL_FIVE = 6;
    private static final int LEVEL_SIX = 7;

    // Layout manager used to create the memory grid
    private static GridLayoutManager layoutManager;

    // Always begin new game at level one
    private int currentLevel = LEVEL_ONE;

    // Number of matched pairs for current level,
    // helps keep track of when current level is finished.
    private int matchedPairs = 0;

    // User selections of memory cards, initialized to -1
    private int selectionOne = -1;
    private int selectionTwo = -1;

    // Position in the list of current selections
    private int positionOne;
    private int positionTwo;

    // List of objects to be used for initiating the memory game
    private ArrayList<MemoryCard> memoryCards;
    
    // Front and back side of the memory cards for the current level
    private ArrayList<MemoryCard> memoryCardItems;
    private ArrayList<MemoryCard> memoryCardPlaceholders;

    // RecyclerView from Activity needed for running animations and fetching context
    private RecyclerView recyclerView;

    // Adapter to be used for updating the UI
    private MemoryAdapter viewAdapter;

    // Context of the activity from which the memory game is run
    private Context context;

    // To be used for delaying posts
    private Handler handler;

    /**
     * Constructor that initiates the first round of the game.
     *
     * @param recyclerView - store recyclerView to enable animations.
     */
    public MemoryGame(RecyclerView recyclerView, ArrayList<MemoryCard> memoryCards) {
        this.recyclerView = recyclerView;
        this.memoryCards = memoryCards;

        // Context of the activity the game is run from
        context = recyclerView.getContext();

        // testMode(LEVEL_SIX, 5);

        // Generate new list of memory cards to be used for the current level
        memoryCardItems = new ArrayList<>();
        memoryCardPlaceholders = new ArrayList<>();

        generateMemoryCardFronts();
        generateMemoryCardBacks();
    }

    /**
     * Test different levels at the start of the game.
     */
    private void testMode(int level, int span) {
        currentLevel = level;
        layoutManager.setSpanCount(span);
    }

    /**
     * Creates a list of memory cards to be used from the list of memoryCards,
     * according to the current level.
     */
    private void generateMemoryCardFronts() {

        // Shuffle MemoryCard objects in order to randomize each new level
        Collections.shuffle(memoryCards);

        for (int x = 0; x < currentLevel; x++) {
            memoryCardItems.add(memoryCards.get(x));
            memoryCardItems.add(memoryCards.get(x));
        }

        // Shuffle the new list so the pairs are spread around
        Collections.shuffle(memoryCardItems);
    }

    /**
     * Create a list of memory card place holders, which will only show the "back" of the card.
     */
    private void generateMemoryCardBacks() {

        // Generate list of cards according to level
        for (int x = 0; x < currentLevel; x++) {
            memoryCardPlaceholders.add(new MemoryCard(R.drawable.memory_card_back3, 0));
            memoryCardPlaceholders.add(new MemoryCard(R.drawable.memory_card_back3, 0));
        }

    }

    /**
     * @return list of memoryCardPlaceholders, the memoryCard placeholders.
     */
    public ArrayList<MemoryCard> getMemoryCardPlaceholders() {
        return memoryCardPlaceholders;
    }

    /**
     * @param recyclerViewAdapter - The RecyclerView adapter to be used for updating the UI.
     */
    public void useAdapter(MemoryAdapter recyclerViewAdapter) {
        viewAdapter = recyclerViewAdapter;
    }

    /**
     * Reveal the selected card and check if a pair match has been made.
     *
     * @param position of the view that the user has selected
     */
    public void selectCard(int position) {

        MemoryCard selectedCard = memoryCardItems.get(position);

        if (GameUtils.isTouchEnabled()) {

            // Disable further touch events
            GameUtils.setTouchEnabled(false);

            revealCard(selectedCard, position);

            // Execute the following after sound playback
            handler = new Handler();
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
     * Reveal and play the sound of the selected card.
     *
     * @param selectedCard from the user input.
     * @param position of the view that the user has selected.
     */
    private void revealCard(MemoryCard selectedCard, int position) {
        // Check status of selectionOne, if less than 0, it means that
        // we're in a new round of selections.
        if (selectionOne < 0) {

            // Reveal the card at the selected position
            memoryCardPlaceholders.get(position).setImageResourceID(selectedCard.getImageResourceID());

            SoundPlayback.play(context, selectedCard.getAudioResourceID());

            // Store info about current selection
            selectionOne = selectedCard.getImageResourceID();
            positionOne = position;

            viewAdapter.notifyItemChanged(position);

            // selectionOne > 0 true indicates that one card has already been selected,
            // second card is being selected.
        } else if (selectionOne > 0 && selectionTwo < 0) {

            // Reveal the card at the selected position
            memoryCardPlaceholders.get(position).setImageResourceID(selectedCard.getImageResourceID());

            SoundPlayback.play(context, selectedCard.getAudioResourceID());

            // Store info about current selection
            selectionTwo = selectedCard.getImageResourceID();
            positionTwo = position;

            viewAdapter.notifyItemChanged(position);

        }

    }

    /**
     * Check if the selected cards are a match and make them INVISIBLE if true.
     * Check the current status of the game if a match has been made.
     */
    private void checkSelections() {

        // If cards are a match
        if (selectionOne == selectionTwo && positionOne != positionTwo) {

            // Make the cards INVISIBLE
            memoryCardPlaceholders.get(positionOne).setPaired();
            memoryCardPlaceholders.get(positionTwo).setPaired();

            // Update the UI for each card
            viewAdapter.notifyItemChanged(positionOne);
            viewAdapter.notifyItemChanged(positionTwo);

            // Increase the total amount of matched pairs
            matchedPairs += 1;

            checkGameStatus();
            resetSelectionValues();

        // If cards are not a match
        } else if (selectionOne > 0 && selectionTwo > 0) {

            resetCards();
            resetSelectionValues();

        }
    }

    /**
     * Check if current level has reached its end and then initiate the next level.
     */
    private void checkGameStatus() {

        if (matchedPairs == currentLevel) {

            updateNextLevel();
            initiateNextLevel();

        }
    }

    /**
     * Reset the temporary values for the current selections
     */
    private void resetSelectionValues() {
        selectionOne = -1;
        selectionTwo = -1;
        positionOne = 0;
        positionTwo = 0;
    }

    /**
     * Turn a pair that is not a match back to the backside of the memory card.
     */
    private void resetCards() {
        memoryCardPlaceholders.get(positionOne).setImageResourceID(R.drawable.memory_card_back3);
        memoryCardPlaceholders.get(positionTwo).setImageResourceID(R.drawable.memory_card_back3);

        viewAdapter.notifyItemChanged(positionOne);
        viewAdapter.notifyItemChanged(positionTwo);
    }

    /**
     * Update the currentLevel to the next in line and play celebration sound.
     */
    private void updateNextLevel() {
        if (currentLevel == LEVEL_ONE) {
            currentLevel = LEVEL_TWO;

            playCelebrationSound();

            layoutManager.setSpanCount(3);

        } else if (currentLevel == LEVEL_TWO) {
            currentLevel = LEVEL_THREE;

            playCelebrationSound();

            layoutManager.setSpanCount(4);

        } else if (currentLevel == LEVEL_THREE){
            currentLevel = LEVEL_FOUR;

            playCelebrationSound();

        } else if (currentLevel == LEVEL_FOUR){
            currentLevel = LEVEL_FIVE;

            playCelebrationSound();

            layoutManager.setSpanCount(5);

        } else if (currentLevel == LEVEL_FIVE){
            currentLevel = LEVEL_SIX;

            playCelebrationSound();

            layoutManager.setSpanCount(5);

        } else {
            playCelebrationSound();

        }
    }

    /**
     * Play the celebration sound only if the window currently has focus.
     */
    private void playCelebrationSound() {

        if (recyclerView.hasWindowFocus()) {
            SoundPlayback.play(context, R.raw.celebration_short);
        }
    }


    /**
     * Initiate the next level of the game by generating new memory cards and
     * updating the UI.
     */
    private void initiateNextLevel() {

        // Clear the items and placeholders, then generate new ones
        memoryCardItems.clear();
        memoryCardPlaceholders.clear();

        generateMemoryCardFronts();
        generateMemoryCardBacks();

        // Reset the matchedPairs counter
        matchedPairs = 0;

        // Update the UI
        viewAdapter.notifyDataSetChanged();

        // Run the slide up animation for the new items
        runLayoutAnimation();
    }

    /**
     * Run the slide up layout animation for all memory cards.
     */
    private void runLayoutAnimation() {
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_up);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.scheduleLayoutAnimation();
    }

    /**
     * Initializes the RecyclerView for the game, with a grid view. Initialization is done
     * from within the game, in order to make it easier to update the spanCount, i.e. the
     * game grid.
     *
     * @param activity - e.g. 'this' or 'GameActivity.this'.
     * @param recyclerViewId - Id for the RecyclerView to use.
     * @return RecyclerView to be used for the viewAdapter in the activity.
     */
    public static RecyclerView initRecyclerView(AppCompatActivity activity, int recyclerViewId) {
        // Store activity context
        Context context = activity.getApplicationContext();

        // Find and store the recycler view
        RecyclerView recyclerView = activity.findViewById(recyclerViewId);

        layoutManager = new CustomGridLayoutManager(context, 2);
        recyclerView.setLayoutManager(layoutManager);

        return recyclerView;
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
