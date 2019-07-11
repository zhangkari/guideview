package com.karic.guideview;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Guide {

    public static final int SHAPE_RECT = 0;
    public static final int SHAPE_CIRCLE = 1;
    public static final int SHAPE_OVAL = 2;

    @IntDef({SHAPE_RECT, SHAPE_CIRCLE, SHAPE_OVAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Shape {

    }

    public static final int POS_LEFT_TOP = 0;
    public static final int POS_LEFT_CENTER = 1;
    public static final int POS_LEFT_BOTTOM = 2;
    public static final int POS_CENTER_TOP = 3;
    public static final int POS_CENTER_BOTTOM = 4;
    public static final int POS_RIGHT_TOP = 5;
    public static final int POS_RIGHT_CENTER = 6;
    public static final int POS_RIGHT_BOTTOM = 7;

    @IntDef({POS_LEFT_TOP, POS_LEFT_CENTER, POS_LEFT_BOTTOM, POS_CENTER_TOP, POS_CENTER_BOTTOM, POS_RIGHT_TOP, POS_RIGHT_CENTER, POS_RIGHT_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Position {

    }

    private final View anchor;
    private int id;
    private Animator animator;
    private View view; // custom View
    private int backgroundColor;
    private @Shape int shape;
    private @Position int position;

    public Guide(@NonNull View anchor) {
        this.anchor = anchor;
        backgroundColor = Color.WHITE;
        shape = SHAPE_RECT;
        position = POS_LEFT_TOP;
    }

    @Deprecated
    /**
     * @deprecated Use snapshot instead
     */
    public Bitmap snapshotAnchor() {
        anchor.buildDrawingCache();
        return anchor.getDrawingCache();
    }

    public Bitmap snapshot() {
        Bitmap bmp = Bitmap.createBitmap(anchor.getWidth(), anchor.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(backgroundColor);
        anchor.draw(c);
        return bmp;
    }

    public int getId() {
        return id;
    }

    public View getAnchor() {
        return anchor;
    }

    public Animator getAnimator() {
        return animator;
    }

    public View getView() {
        return view;
    }

    public int getShape() {
        return shape;
    }

    public int getPosition() {
        return position;
    }

    public static class Builder {
        private Guide guide;

        public Builder(View anchor) {
            guide = new Guide(anchor);
        }

        public Builder setId(int id) {
            guide.id = id;
            return this;
        }

        public Builder setAnimator(Animator animator) {
            guide.animator = animator;
            return this;
        }

        public Builder setGuideShape(@Shape int shape) {
            guide.shape = shape;
            return this;
        }

        public Builder setCustomView(View view) {
            guide.view = view;
            return this;
        }

        public Builder setViewPosition(@Position int position) {
            guide.position = position;
            return this;
        }

        public Builder setBackgroundColor(int color) {
            guide.backgroundColor = color;
            return this;
        }

        public Guide build() {
            return guide;
        }
    }
}