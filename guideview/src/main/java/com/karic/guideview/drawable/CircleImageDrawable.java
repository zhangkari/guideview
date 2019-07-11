package com.karic.guideview.drawable;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import androidx.annotation.NonNull;

public class CircleImageDrawable extends OvalImageDrawable {

    public CircleImageDrawable(Bitmap bitmap) {
        super(bitmap);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        float radius = rectF.width() <= rectF.height() ? rectF.width() / 2 : rectF.height() / 2;
        canvas.drawCircle(rectF.centerX(), rectF.centerY(), radius, paint);
    }
}
