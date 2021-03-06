package com.spaceleague;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

/**
 * Helper class that holds functions that are used between different activities.
 * @author Harshil Surendralal
 */
public class Helper {

    /**
     * Hide the navigation bar from the screen.
     * @param activity The activity on which the navigation bar will be hidden.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected static void hideNavigationBar(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    /**
     * Continuously move the background image to the left.
     * @param activity The activity on which the background will be animated.
     */
    protected static void animateBackground(Activity activity) {
        final ImageView bg1 = (ImageView) activity.findViewById(R.id.backgroundOne);
        final ImageView bg2 = (ImageView) activity.findViewById(R.id.backgroundTwo);

        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);

        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(10000L);
        animator.addUpdateListener(animation -> {
            final float progress = (float) animation.getAnimatedValue();
            final float width = bg1.getWidth();
            final float translationX = width * progress;

            bg1.setTranslationX(-translationX);
            bg2.setTranslationX(-(translationX - width));
        });
        animator.start();
    }
}
