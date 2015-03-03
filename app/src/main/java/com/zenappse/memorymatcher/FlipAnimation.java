package com.zenappse.memorymatcher;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

/**
 * Taken from StackOverflow
 * modified by Patrick Ganson on 3/2/15.
 *
 * http://stackoverflow.com/a/16044189
 */
public class FlipAnimation extends Animation {
    private final View cardView;
    private Camera camera;

    private float centerX;
    private float centerY;

    private boolean forward = true;

    private boolean isRed = true;
    private boolean isFlipped = false;

    /**
     * Creates a 3D flip animation on a Card view.
     *
     * @param cardView Card view to flip.
     */
    public FlipAnimation(View cardView) {
        this.cardView = cardView;

        setDuration(300);
        setFillAfter(false);
        setInterpolator(new AccelerateDecelerateInterpolator());
    }

    public void setColorCard(boolean isRed) {
        this.isRed = isRed;
    }

    public void setFlippedState(boolean isFlipped) {
        this.isFlipped = isFlipped;
    }

    public void reverse() {
        forward = false;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        centerX = width / 2;
        centerY = height / 2;
        camera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        // Angle around the y-axis of the rotation at the given time
        // calculated both in radians and degrees.
        final double radians = Math.PI * interpolatedTime;
        float degrees = (float) (180.0 * radians / Math.PI);

        // Once we reach the midpoint in the animation, we need to hide the
        // source view and show the destination view. We also need to change
        // the angle by 180 degrees so that the destination does not come in
        // flipped around
        if (interpolatedTime >= 0.5f) {
            degrees -= 180.f;

            TextView cardNumberTextView = (TextView) cardView.findViewById(R.id.game_card_textview);

            if (!isFlipped) {
                if (isRed) {
                    cardView.setBackgroundResource(R.drawable.card_red);
                } else {
                    cardView.setBackgroundResource(R.drawable.card_black);
                }
                cardNumberTextView.setVisibility(View.VISIBLE);
            } else {
                cardView.setBackgroundResource(R.drawable.card_back);
                cardNumberTextView.setVisibility(View.INVISIBLE);
            }
        }

        if (forward) {
            //determines direction of rotation when flip begins
            degrees = -degrees;
        }

        final Matrix matrix = t.getMatrix();
        camera.save();
        camera.translate(0, 0, Math.abs(degrees)*2);
        camera.getMatrix(matrix);
        camera.rotateY(degrees);
        camera.getMatrix(matrix);
        camera.restore();
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }
}