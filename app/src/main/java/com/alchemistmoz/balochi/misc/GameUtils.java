package com.alchemistmoz.balochi.misc;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.alchemistmoz.balochi.R;
import com.alchemistmoz.balochi.games.audiomatch.AudioMatchGame;
import com.alchemistmoz.balochi.games.memory.MemoryGame;
import com.alchemistmoz.balochi.games.repetition.CountGame;
import com.alchemistmoz.balochi.games.repetition.RepetitionGame;

/**
 * Utilities class with various useful methods to be used
 * across different games.
 *
 */
public final class GameUtils {

    // All the available games
    public final static int REPETITION = 0;
    public final static int COUNT = 1;
    public final static int MEMORY = 2;

    // Allows for touch events to be temporarily disabled
    private static boolean touchEnabled = true;

    /**
     * Prevent user from instantiating the class.
     */
    private GameUtils() {
        // Empty
    }

    /**
     * @param enabled - Status of touch events.
     */
    public static void setTouchEnabled(boolean enabled) {
        touchEnabled = enabled;
    }

    /**
     * @return current status of touch events.
     */
    public static boolean isTouchEnabled() {
        return touchEnabled;
    }

    /**
     * Initializes the recycler view with GridLayoutManager.
     *
     * The contentView for the activity has to set a layout that contains the given
     * recyclerViewId.
     *
     * @param activity - Can be either this or e.g. MainActivity.this.
     * @param recyclerViewId - Id for the recycler view to use.
     * @param spanCount - The spanCount to be used for the grid.
     * @return recyclerView to be used for the adapter in the activity.
     */
    public static RecyclerView initGridRecyclerView(AppCompatActivity activity, int recyclerViewId, int spanCount) {

        // Store activity context
        Context context = activity.getApplicationContext();

        // Find and store the recycler view
        RecyclerView recyclerView = activity.findViewById(recyclerViewId);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // A LayoutManager is responsible for measuring and positioning item views
        // within a RecyclerView as well as determining the policy for when to recycle
        // item views that are no longer visible to the user
        recyclerView.setLayoutManager(new CustomGridLayoutManager(context, spanCount));

        return recyclerView;
    }

    /**
     * Add ItemClickSupport for the count game items.
     *
     * @param recyclerView of the RepetitionActivity.
     * @param countGame instance of the game.
     */
    public static void addCountItemClickSupport(RecyclerView recyclerView, final CountGame countGame) {

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                countGame.selectItem(position, v);

            }
        });
    }

    /**
     * Add ItemClickSupport for the repetition game items.
     *
     * @param recyclerView of the RepetitionActivity.
     * @param repetitionGame instance of the game.
     */
    public static void addRepetitionItemClickSupport(RecyclerView recyclerView, final RepetitionGame repetitionGame) {

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                repetitionGame.selectItem(position, v);

            }
        });
    }

    /**
     * Add ItemClickSupport for the memory cards in the game.
     * Clicks are only registered if touchEnabled is true.
     *
     * @param recyclerView of the MemoryActivity.
     * @param memoryGame instance of the game.
     */
    public static void addMemoryCardClickSupport(RecyclerView recyclerView, final MemoryGame memoryGame) {

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                // Show the front of the selected card at the given position
                memoryGame.selectCard(position);
            }
        });
    }

    /**
     * Add ItemClickSupport for AudioMatch game items.
     *
     * @param recyclerView of the RepetitionActivity.
     * @param audioMatchGame instance of the game.
     */
    public static void addAudioMatchItemClickSupport(RecyclerView recyclerView, final AudioMatchGame audioMatchGame) {

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                audioMatchGame.selectItem(position, v);

            }
        });
    }

    /**
     * This animation is used for recyclerView items in from bottom to top.
     *
     * @param recyclerView - The recyclerView to apply animation to
     */
    public static void runSlideUpAnim(RecyclerView recyclerView, final int game) {
        int resId = R.anim.layout_animation_slide_up;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(), resId);

        Animation.AnimationListener animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                if (game == MEMORY) {
                    setTouchEnabled(false);
                } else if (game == COUNT) {
                    setTouchEnabled(false);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if (game == MEMORY) {
                    setTouchEnabled(true);
                } else if (game == COUNT) {
                    setTouchEnabled(true);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        if (animation != null) {
            recyclerView.setLayoutAnimation(animation);
            recyclerView.setLayoutAnimationListener(animationListener);
        }
    }

}
