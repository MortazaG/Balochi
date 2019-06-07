package com.alchemistmoz.balochi.misc;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
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
public class Utilities {

    // Allows for "disabling" touch events
    private static boolean touchEnabled = true;

    /**
     * Prevent user from instantiating the class.
     */
    private Utilities() {
        // Empty
    }

    /**
     * @param status of touch events.
     */
    private static void setTouchEnabled(boolean status) {
        touchEnabled = status;
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

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // A LayoutManager is responsible for measuring and positioning item views
        // within a RecyclerView as well as determining the policy for when to recycle
        // item views that are no longer visible to the user
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        return recyclerView;
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

    public static void addMenuItemClickSupport(RecyclerView recyclerView) {

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                // Store the recyclerView adapter
                MainAdapter adapter = (MainAdapter) recyclerView.getAdapter();

                // Play the sound of the selected menu item and start the correlated activity
                Utilities.startMenuActivity(v.getContext(), v, adapter, position);

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
            Handler handler = new Handler();
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

    /**
     * This animation is used for recyclerView items in from bottom to top.
     *
     * @param recyclerView - The recyclerView to apply animation to
     */
    public static void runSlideUpAnim(RecyclerView recyclerView) {
        int resId = R.anim.layout_animation_slide_up;
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

    /**
     * Get activity instance from desired context.
     */
    public static Activity getActivity(Context context) {
        if (context == null) return null;
        if (context instanceof Activity) return (Activity) context;
        if (context instanceof ContextWrapper) return getActivity(((ContextWrapper)context).getBaseContext());
        return null;
    }
}
