package com.alchemistmoz.balochi.misc;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;


/**
 * Utility class for handling playback of sound files.
 *
 * Use as follows:
 * 1. Initialize AudioManager outside the clickListener with initializeManagerService(context),
 *    in onCreate() of the activity.
 *
 * 2. Play the desired sound within the onClick method with play(context, audioResourceID).
 *
 * Use releaseMediaPlayer within onStop() of game activities.
 *
 */
public final class SoundPlayback {

    /** provides access to volume and ringer mode control. */
    private static AudioManager audioManager;

    /** Handles playback of all the sound files */
    private static MediaPlayer mediaPlayer;

    /**
     * The listener gets triggered when the mediaPlayer has completed
     * playing the audio file.
     */
    private static MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };

    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., we gain or lose audio focus because of another app or device).
     */
    private static AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus
                // Stop playback and clean up resources
                releaseMediaPlayer();
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mediaPlayer.start();
            }
        }
    };


    /**
     * Prevent user from instantiating the class.
     */
    private SoundPlayback() {
        // Empty
    }

    /**
     * Create and setup the Audio Manager to request audio focus and set to music stream.
     * @param activity where the sound will be played.
     */
    public static void initializeManagerService(AppCompatActivity activity) {
        Context context = activity.getApplicationContext();
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        // Set the stream to music so that volume controls modify media volume
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    /**
     * Simple way to playback sound from other activities.
     *
     * @param context of the activity.
     * @param audioResourceId - resource to be played.
     */
    public static void play(Context context, int audioResourceId) {
        releaseMediaPlayer();
        playSound(context, audioResourceId);
    }

    /**
     * @return duration of sound in milliseconds.
     */
    public static int getSoundDuration() {
        return mediaPlayer.getDuration();
    }

    /** Clean up the media player by releasing its resources */
    public static void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mediaPlayer != null) {

            // Stop any media and send the MediaPlayer instance back to the idle state.
            // Exactly in the same state as when it was created.
            mediaPlayer.reset();

            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment. This makes the remainder
            // of the object is a candidate for garbage collection.
            mediaPlayer = null;

            // Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
            audioManager.abandonAudioFocus(audioFocusChangeListener);
        }
    }

    /**
     * Request audiofocus and initiate playback of the given sound.
     *
     * @param context of the activity.
     * @param audioResourceID - resource to be played.
     */
    private static void playSound(Context context, int audioResourceID) {
        // Request audio focus in order to play the audio file. The app needs to play a
        // short audio file, so we will request audio focus with a short amount of time
        // with AUDIOFOCUS_GAIN_TRANSIENT.
        int result =  audioManager.requestAudioFocus(audioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // Using the factory method create() we set the audio resource to be played when
            // chosen category is selected.
            mediaPlayer = MediaPlayer.create(context, audioResourceID);

            // Stat playback of audio
            mediaPlayer.start();

            // Listen for when the playback has finished
            mediaPlayer.setOnCompletionListener(completionListener);

        }
    }

}
