package com.alchemistmoz.balochi.misc;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import com.alchemistmoz.balochi.Category;
import com.alchemistmoz.balochi.MainAdapter;
import com.alchemistmoz.balochi.R;

/**
 * Utilities class with various useful methods to be used
 * across different activities.
 *
 */
public final class Utilities {

    // Allows for touch events to be temporarily disabled
    private static boolean touchEnabled = true;

    // To be used for delaying posts
    private static Handler handler = new Handler();

    /**
     * Prevent user from instantiating the class.
     */
    private Utilities() {
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
    private static boolean isTouchEnabled() {
        return touchEnabled;
    }

    /**
     * Initializes the recycler view with LinearLayoutManager.
     *
     * The contentView for the activity has to set a layout that contains the given
     * recyclerViewId.
     *
     * @param activity - Can be either this or e.g. MainActivity.this.
     * @param recyclerViewId - Id for the recycler view to use.
     * @return recyclerView to be used for the adapter in the activity.
     */
    public static RecyclerView initLinearRecyclerView(AppCompatActivity activity, int recyclerViewId) {

        // Store activity context
        Context context = activity.getApplicationContext();

        // Find and store the recycler view
        RecyclerView recyclerView = activity.findViewById(recyclerViewId);

        // A LayoutManager is responsible for measuring and positioning item views
        // within a RecyclerView as well as determining the policy for when to recycle
        // item views that are no longer visible to the user
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        return recyclerView;
    }

    /**
     * Add ItemClickSupport for menu items.
     *
     * @param recyclerView of the menu activity.
     */
    public static void addMenuItemClickSupport(RecyclerView recyclerView) {

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                // Store the recyclerView adapter
                MainAdapter adapter = (MainAdapter) recyclerView.getAdapter();

                // Play the sound of the selected menu item and start the correlated activity
                if (adapter != null) {
                    startMenuActivity(v.getContext(), v, adapter, position);
                }

            }
        });
    }

    /**
     * Play the sound correlated to the selected menu item and then start its activity.
     *
     * @param context - Activity context
     * @param view - Selected view
     * @param adapter - recyclerView adapter
     * @param position - Position of the users selection
     */
    private static void startMenuActivity(final Context context, View view, MainAdapter adapter, int position) {
        // Store the current Category object that the MainAdapter is iterating, using
        // the ArrayList method get(). Input parameter is int index, which corresponds
        // to the given position the user clicked on.
        final Category selectedCategory = adapter.getItem(position);

        if (isTouchEnabled()) {

            setTouchEnabled(false);

            Utilities.runOnTouchAnim(context, view);

            // Initialize playback of the sound related to the item the user has selected
            SoundPlayback.play(context, selectedCategory.getAudioResourceID());

            // Delay menu item touch execution so that the onTouchAnimation runs more smoothly
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    setTouchEnabled(true);

                    // Create a new intent to open the selected category activity
                    Intent intent = new Intent(context, selectedCategory.getClassName());

                    // Start the new activity
                    context.startActivity(intent);

                }
            }, SoundPlayback.getSoundDuration());

        }
    }

    /**
     * Remove all pending posts of callbacks and sent messages.
     */
    public static void removePendingPosts() {

        if (handler != null) {

            handler.removeCallbacksAndMessages(null);

        }
    }

    /**
     * This animation is used on most recyclerView items, scaling them up in size
     * upon touch.
     *
     * @param context - Activity context
     * @param view - The selected view
     */
    public static void runOnTouchAnim(final Context context, final View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_touch_animation_scale);

        if (animation != null) {
            view.startAnimation(animation);
        }
    }

    /**
     * This animation is used for sliding recyclerView items in from left to right.
     *
     * @param recyclerView - The recyclerView to apply animation to
     */
    public static void runSlideLeftAnim(final RecyclerView recyclerView) {
        int resId = R.anim.layout_animation_slide_left;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(), resId);

        Animation.AnimationListener animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setTouchEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setTouchEnabled(true);
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
