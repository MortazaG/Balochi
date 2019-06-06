package com.alchemistmoz.balochi.misc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.alchemistmoz.balochi.InfoActivity;
import com.alchemistmoz.balochi.MainActivity;
import com.alchemistmoz.balochi.R;

/**
 * Custom toolbar with view objects as menu items.
 * Requires a toolbar.xml file which consists of a root view and child view(s) with action id('s).
 * Child views should have visibility set to invisible, as they are later activated via
 * an action call in this class.
 *
 * Currently there exists two toolbars within this class, category and game toolbar.
 *
 * How to implement the game:
 * - Instantiate CustomToolbar in onCreate() of the activity.
 *
 * - Initialize one of the available toolbars, initCategoryToolbar() or initGameToolbar().
 *
 * Custom toolbar is instantiated in onCreate() of activity and then initialized
 * to the type of toolbar as is available in this class.
 */
public class CustomToolbar {

    private AppCompatActivity activity;

    /**
     * Store the given activity so that it can be used to
     * find views etc.
     *
     * @param activity - Name of the activity where the toolbar is to be set.
     */
    public CustomToolbar(AppCompatActivity activity) {
        this.activity = activity;
    }

    /**
     * Initialize the given action and make it visible in the toolbar.
     *
     * @param actionId - Id of the action as set in
     * @return action - The Image View of the action.
     */
    private ImageView initAction(int actionId) {
        ImageView action = activity.findViewById(actionId);

        if (action.getVisibility() == View.INVISIBLE) {

            action.setVisibility(View.VISIBLE);
        }

        return action;
    }

    /**
     * Adds home button to toolbar, which takes the user back to MainActivity
     * and then calls finish() on the current activity.
     */
    private void addActionHome() {

        initAction(R.id.action_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startGivenActivity(MainActivity.class);
            }
        });
    }

    /**
     * Adds back button to toolbar, which calls finish() on the current activity.
     */
    private void addActionBack() {

        initAction(R.id.action_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // finish() leads the current activity to the end of its life cycle,
                // by leading to onDestroy(). This leads to the activity shutting down.
                activity.finish();
            }
        });
    }

    /**
     * The only toolbar action that is always visible.
     * Directs the user to the information activity.
     */
    private void addActionInfo() {

        initAction(R.id.action_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startGivenActivity(InfoActivity.class);
            }
        });
    }

    private void startGivenActivity(Class activityName) {
        // Create a new intent to open MainActivity
        Intent intent = new Intent(activity.getApplicationContext(), activityName);
        activity.startActivity(intent);

        // finish() leads the current activity to the end of its life cycle,
        // by leading to onDestroy(). This leads to the activity shutting down.
        activity.finish();
    }

    /**
     * Simplest form of the toolbar with only the info button.
     */
    public void initBasicToolbar() {
        addActionInfo();
    }

    /**
     * Toolbar to be used in each category menu activity, with back and info button.
     */
    public void initCategoryToolbar() {
        addActionHome();
    }

    /**
     * Toolbar to be used in most games, with back, home button and info button.
     */
    public void initGameToolbar() {
        addActionHome();
        addActionBack();
    }

}
