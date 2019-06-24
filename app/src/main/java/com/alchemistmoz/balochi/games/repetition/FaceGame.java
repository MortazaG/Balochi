package com.alchemistmoz.balochi.games.repetition;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.alchemistmoz.balochi.R;
import com.alchemistmoz.balochi.misc.GameUtils;
import com.alchemistmoz.balochi.misc.SoundPlayback;


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
    
    private ImageView imageAreasView;

    // To be used for delaying posts
    private Handler handler;

    /**
     * Sets the game off by initiating default values and the ArrayLists that will be used
     * in the game.
     *
     */
    public FaceGame(ImageView imageAreasView) {
        this.imageAreasView = imageAreasView;
        
        handler = new Handler();
    }

    /**
     * Play the sound of the selected item and make it INVISIBLE,
     * then check for the current status of the game.
     *
     */
    public void selectItem(View view, MotionEvent motionEvent) {

        final int action = motionEvent.getAction();
        final int eventX = (int) motionEvent.getX();
        final int eventY = (int) motionEvent.getY();

        Context context = view.getContext();

        if (GameUtils.isTouchEnabled() && (action == MotionEvent.ACTION_UP)) {

            int touchColor = getHotspotColor(eventX, eventY);

            playSelectedAreaSound(context, touchColor);

        }
    }

    private void playSelectedAreaSound(Context context, int touchColor) {

        // Disable further touch events
        GameUtils.setTouchEnabled(false);

        if (closeMatch(Color.RED, touchColor)) SoundPlayback.play(context, R.raw.colors_red);
        if (closeMatch(Color.MAGENTA, touchColor)) SoundPlayback.play(context, R.raw.colors_white);
        if (closeMatch(Color.GREEN, touchColor)) SoundPlayback.play(context, R.raw.colors_green);
        if (closeMatch(Color.BLUE, touchColor)) SoundPlayback.play(context, R.raw.colors_blue);
        if (closeMatch(Color.BLACK, touchColor)) SoundPlayback.play(context, R.raw.colors_black);
        if (closeMatch(Color.YELLOW, touchColor)) SoundPlayback.play(context, R.raw.colors_yellow);
        if (closeMatch(Color.LTGRAY, touchColor)) SoundPlayback.play(context, R.raw.colors_grey);
        if (closeMatch(Color.GRAY, touchColor)) SoundPlayback.play(context, R.raw.colors_grey);
        if (closeMatch(Color.DKGRAY, touchColor)) SoundPlayback.play(context, R.raw.colors_grey);
        if (closeMatch(Color.CYAN, touchColor)) SoundPlayback.play(context, R.raw.colors_blue);

        // Enable touch events after sound playback
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                GameUtils.setTouchEnabled(true);

            }
        }, SoundPlayback.getSoundDuration());

    }

    private int getHotspotColor(int x, int y) {
        imageAreasView.setDrawingCacheEnabled(true);
        Bitmap hotspots = Bitmap.createBitmap(imageAreasView.getDrawingCache());
        imageAreasView.setDrawingCacheEnabled(false);
        return hotspots.getPixel(x, y);
    }

    private boolean closeMatch (int color1, int color2) {

        int tolerance = 25;

        if (Math.abs (Color.red (color1) - Color.red (color2)) > tolerance ) return false;
        if (Math.abs (Color.green (color1) - Color.green (color2)) > tolerance ) return false;
        if (Math.abs (Color.blue (color1) - Color.blue (color2)) > tolerance ) return false;

        return true;
    }
}
