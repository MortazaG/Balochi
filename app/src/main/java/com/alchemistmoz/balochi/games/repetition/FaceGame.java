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
 * This game is run in conjunction with SoundPlayback and GameUtils.
 * The layout of the game is defined in layout_face_game.xml.
 *
 * Game description: Clickable picture of the face is presented to the user.
 *                   Clicking a specific part of the face will initiate playback
 *                   of the word for that part.
 *
 * The game is implemented in the Activity class from which it will be run.
 *
 * How to implement the game:
 *
 * - Use the following in onCreate():
 *
 *         // Find and store the image views to be used for the game
 *         ImageView imageView = findViewById(R.id.face_image);
 *         ImageView imageAreasView = findViewById(R.id.face_image_areas);
 *
 *         // Initiate a new round of the counting game
 *         faceGame = new FaceGame(imageAreasView);
 *
 *         imageView.setOnTouchListener(new View.OnTouchListener() {
 *             // @Override
 *             public boolean onTouch(View view, MotionEvent motionEvent) {
 *
 *                 faceGame.selectItem(view, motionEvent);
 *                 return true;
 *             }
 *         });
 *
 */
public class FaceGame {

    // The image map with the clickable areas
    private ImageView imageAreasView;

    // To be used for delaying posts
    private Handler handler;

    /**
     * Sets the game off by initiating default values.
     *
     * @param imageAreasView - Image map with the clickable areas.
     */
    public FaceGame(ImageView imageAreasView) {
        this.imageAreasView = imageAreasView;
        
        handler = new Handler();
    }

    /**
     * Identify where the user has touched and play the
     * corresponding body part sound.
     *
     * @param view - The face image view.
     * @param motionEvent - The action and position of the users touch.
     */
    public void selectItem(View view, MotionEvent motionEvent) {

        // Store the users action and X/Y coordinates of the touch event
        final int action = motionEvent.getAction();
        final int eventX = (int) motionEvent.getX();
        final int eventY = (int) motionEvent.getY();

        // Context of the activity fetched from the view
        Context context = view.getContext();


        if ((action == MotionEvent.ACTION_UP) && GameUtils.isTouchEnabled()) {

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
