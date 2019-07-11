package com.karic.guideview;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.karic.guideview.drawable.CircleImageDrawable;
import com.karic.guideview.drawable.OvalImageDrawable;

import java.util.ArrayList;
import java.util.List;

public class GuideView extends FrameLayout {
    private ImageView mImageView;
    private List<Guide> mGuides;
    private int mCurrent;
    private View.OnClickListener mOnGuideFinishListener;
    private OnGuideItemClickListener mGuideItemClickListener;

    public GuideView(Context context) {
        this(context, null);
    }

    public GuideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mImageView = new ImageView(context);
        addView(mImageView);
        mGuides = new ArrayList<>(8);
        mCurrent = 0;
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guideNextStep();
            }
        });
    }

    public GuideView addGuide(Guide guide) {
        mGuides.add(guide);
        return this;
    }

    public void startGuide() {
        post(new Runnable() {
            @Override
            public void run() {
                guideCurrent();
            }
        });
    }

    public void setOnGuideFinishListener(View.OnClickListener listener) {
        mOnGuideFinishListener = listener;
    }

    public void setGuideItemClickListener(OnGuideItemClickListener listener) {
        mGuideItemClickListener = listener;
    }

    private void guideCurrent() {
        removeCustomView();
        if (mGuides.size() < 1 || mCurrent < 0 || mCurrent >= mGuides.size()) {
            return;
        }
        Guide guide = mGuides.get(mCurrent);
        refreshImageView(guide);
        animateImageView(guide);
        layoutCustomView(guide);
        if (mGuideItemClickListener != null) {
            mGuideItemClickListener.onGuideItemClick(mGuides.size(), mCurrent, guide);
        }
    }

    private void removeCustomView() {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) == mImageView) {
                continue;
            }
            removeViewAt(i);
        }
    }

    private void refreshImageView(final Guide guide) {
        recycleBitmap();
        Bitmap bitmap = guide.snapshot();
        if (guide.getShape() == Guide.SHAPE_CIRCLE) {
            mImageView.setImageDrawable(new CircleImageDrawable(bitmap));
        } else if (guide.getShape() == Guide.SHAPE_OVAL) {
            mImageView.setImageDrawable(new OvalImageDrawable(bitmap));
        } else {
            mImageView.setImageBitmap(bitmap);
        }
        ViewGroup.LayoutParams p = guide.getAnchor().getLayoutParams();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(p.width, p.height);
        mImageView.setX(guide.getAnchor().getX());
        mImageView.setY(guide.getAnchor().getY());
        mImageView.setLayoutParams(params);
    }

    private void recycleBitmap() {
        Drawable drawable = mImageView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bmp = (BitmapDrawable) drawable;
            bmp.getBitmap().recycle();
        }
    }

    private void animateImageView(final Guide guide) {
        if (guide.getAnimator() == null) {
            return;
        }
        guide.getAnimator().setTarget(mImageView);
        guide.getAnimator().addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                guide.getAnimator().removeListener(this);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        guide.getAnimator().start();
    }

    private void layoutCustomView(final Guide guide) {
        if (guide.getView() == null) {
            return;
        }
        final View view = guide.getView();
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        addView(view);
        view.post(new Runnable() {
            @Override
            public void run() {
                Point pt = locateCustomView(guide);
                view.setX(pt.x);
                view.setY(pt.y);
            }
        });
    }

    @Override
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private Point locateCustomView(Guide guide) {
        Point pt = new Point();
        switch (guide.getPosition()) {
            case Guide.POS_LEFT_TOP:
                pt.x = guide.getAnchor().getLeft() - guide.getView().getWidth();
                pt.y = guide.getAnchor().getTop() - guide.getView().getHeight();
                break;

            case Guide.POS_LEFT_CENTER:
                pt.x = guide.getAnchor().getLeft() - guide.getView().getWidth();
                pt.y = guide.getAnchor().getBottom() - guide.getAnchor().getHeight() / 2 - guide.getView().getHeight() / 2;
                break;

            case Guide.POS_LEFT_BOTTOM:
                pt.x = guide.getAnchor().getLeft() - guide.getView().getWidth();
                pt.y = guide.getAnchor().getBottom();
                break;

            case Guide.POS_CENTER_TOP:
                pt.x = guide.getAnchor().getLeft() + guide.getAnchor().getWidth() / 2 - guide.getView().getWidth() / 2;
                pt.y = guide.getAnchor().getTop() - guide.getView().getHeight();
                break;

            case Guide.POS_CENTER_BOTTOM:
                pt.x = guide.getAnchor().getLeft() + guide.getAnchor().getWidth() / 2 - guide.getView().getWidth() / 2;
                pt.y = guide.getAnchor().getBottom();
                break;

            case Guide.POS_RIGHT_TOP:
                pt.x = guide.getAnchor().getRight();
                pt.y = guide.getAnchor().getTop() - guide.getView().getHeight();
                break;

            case Guide.POS_RIGHT_CENTER:
                pt.x = guide.getAnchor().getRight();
                pt.y = guide.getAnchor().getBottom() - guide.getAnchor().getHeight() / 2 - guide.getView().getHeight() / 2;
                break;

            case Guide.POS_RIGHT_BOTTOM:
                pt.x = guide.getAnchor().getRight();
                pt.y = guide.getAnchor().getBottom();
                break;

            default:
                pt.x = guide.getAnchor().getRight();
                pt.y = guide.getAnchor().getBottom();
        }
        return pt;
    }

    private void guideNextStep() {
        mCurrent++;
        if (mCurrent >= mGuides.size()) {
            if (mOnGuideFinishListener != null) {
                mOnGuideFinishListener.onClick(this);
            }
            return;
        }
        guideCurrent();
    }

    public interface OnGuideItemClickListener {
        void onGuideItemClick(int total, int index, Guide guide);
    }
}