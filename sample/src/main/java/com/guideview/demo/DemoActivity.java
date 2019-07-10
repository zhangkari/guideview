package com.guideview.demo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.karic.guideview.Guide;
import com.karic.guideview.GuideView;

public class DemoActivity extends AppCompatActivity {

    private GuideView guideView;
    private View tvLeftTop;
    private View tvRightTop;
    private View tvLeftBottom;
    private View tvRightBottom;
    private View ivCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        findViews();


        TextView bubbleView = new TextView(this);
        bubbleView.setText("~ 气泡提示 ~");
        bubbleView.setTextColor(Color.BLUE);

        AnimatorSet animators = new AnimatorSet();
        Animator scaleX = ObjectAnimator.ofFloat(tvLeftTop, "scaleX", 1.0f, 1.2f, 1.0f);
        Animator scaleY = ObjectAnimator.ofFloat(tvLeftTop, "scaleY", 1.0f, 1.2f, 1.0f);
        animators.play(scaleX).with(scaleY);
        animators.setDuration(600);

        guideView.addGuide(new Guide.Builder(tvLeftTop).build())
                .addGuide(new Guide.Builder(tvRightTop)
                        .setAnimator(animators)
                        .build())
                .addGuide(new Guide.Builder(ivCenter)
                        .setGuideShape(Guide.SHAPE_CIRCLE)
                        .build())
                .addGuide(new Guide.Builder(tvLeftBottom)
                        .setCustomView(bubbleView)
                        .setViewPosition(Guide.POS_RIGHT_TOP)
                        .setAnimator(animators)
                        .build())
                .addGuide(new Guide.Builder(tvRightBottom)
                        .setCustomView(bubbleView)
                        .setViewPosition(Guide.POS_LEFT_TOP)
                        .setAnimator(animators)
                        .build());

        guideView.setOnGuideFinishListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
            }
        });

        guideView.post(new Runnable() {
            @Override
            public void run() {
                guideView.startGuide();
            }
        });
    }

    private void findViews() {
        guideView = findViewById(R.id.guideView);
        tvLeftTop = findViewById(R.id.tv_left_top);
        tvRightTop = findViewById(R.id.tv_right_top);
        tvLeftBottom = findViewById(R.id.tv_left_bottom);
        tvRightBottom = findViewById(R.id.tv_right_bottom);
        ivCenter = findViewById(R.id.iv_center);
    }
}