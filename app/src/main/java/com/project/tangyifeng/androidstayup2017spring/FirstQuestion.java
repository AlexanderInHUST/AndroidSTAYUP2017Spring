package com.project.tangyifeng.androidstayup2017spring;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Author: Alexander
 * Email: yifengtang@hustunique.com
 * Since: 17/2/28.
 */

public class FirstQuestion extends LinearLayout {

    private Paint paint;
    private ObjectAnimator animatorX;
    private ObjectAnimator animatorY;
    private PosContainer container;
    private float aim = -1;
    private float maxHeight = -1;
    private float curHeight = -1;
    private float y0 = -1;
    private float v;
    private boolean isAc;
    private float lastAim = -1;
    private float acceleration;

    private float startPosX;
    private float startPosY;

    public FirstQuestion(Context context) {
        this(context, null);
    }

    public FirstQuestion(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        paint = new Paint();
        initial();
        container = new PosContainer();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (aim == -1) {
            aim = getHeight();
            maxHeight = aim - container.y;
            curHeight = maxHeight;
            y0 = container.y;
        }
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(container.x, container.y, 50, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        if(animatorX != null && animatorX.isRunning()) {
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            container = new PosContainer();
            v = 0;
            isAc = true;
            startAnimation(acceleration, container.x, aim, isAc, animatorListener);
        }
        return super.onTouchEvent(event);
    }

    private void startAnimation(float acceleration, float endX, float endY, boolean isAc,
                                Animator.AnimatorListener listener) {
        float time;
        if (v != 0) {
            time = v / acceleration;
            v = 0f;
        } else {
            time = (float) Math.sqrt(2 * (endY - container.y) / acceleration);
            v = acceleration * time;
        }
        animatorY = ObjectAnimator.ofFloat(container, "y", container.y, endY);
        animatorX = ObjectAnimator.ofFloat(container, "x", container.x, endX);
        animatorY.setDuration((long) time);
        animatorX.setDuration((long) time);
        animatorX.setInterpolator(new LinearInterpolator());
        animatorY.setInterpolator((isAc) ? new AccelerateInterpolator() : new DecelerateInterpolator());
        animatorX.start();
        animatorY.start();
        animatorX.addListener(listener);
        postInvalidate();
    }

    private void initial() {
        startPosX = 200;
        startPosY = 200;
        acceleration = 0.001f;
    }

    private Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {

        }

        @Override
        public void onAnimationEnd(Animator animator) {
            if (v != 0) {
                curHeight /= 2;
                aim = y0 + (maxHeight - curHeight);
                isAc = false;
            } else {
                aim = y0 + maxHeight;
                isAc = true;
            }
            if (lastAim != aim) {
                startAnimation(acceleration, container.x, aim, isAc, this);
            }
            lastAim = aim;
        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    };

    private class Event {
        float startX;
        float startY;
        float endX;
        float endY;
        float vx;
        float vy;
    }

    private class PosContainer {
        float x;
        float y;
        PosContainer() {
            x = startPosX;
            y = startPosY;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }

}
