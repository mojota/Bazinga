package com.mojota.bazinga;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

/**
 * Created by jamie on 16-7-26.
 */
public class AnimateActivity extends ToolBarActivity implements View.OnClickListener {

    private String TAG = this.getClass().getSimpleName();
    private ImageView mIvCenter;
    private ImageView mIvLeft;
    private ImageView mIvRight;
    private ImageView mIvTop;
    private ImageView mIvBottom;
    private boolean mFlag = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);
        mIvCenter = (ImageView) findViewById(R.id.iv_anim_center);
        mIvLeft = (ImageView) findViewById(R.id.iv_anim_left);
        mIvRight = (ImageView) findViewById(R.id.iv_anim_right);
        mIvTop = (ImageView) findViewById(R.id.iv_anim_top);
        mIvBottom = (ImageView) findViewById(R.id.iv_anim_bottom);

        mIvCenter.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_anim_center:
                if (mFlag) {
                    startAnim();
                } else {
                    closeAnim();
                }
                break;
            default:
                break;
        }
    }


    private void startAnim() {

        ObjectAnimator animCenter = ObjectAnimator.ofFloat(mIvCenter, "alpha", 1f, 0.5f);
        ObjectAnimator animLeft = ObjectAnimator.ofFloat(mIvLeft, "translationX", -150f);
        ObjectAnimator animRight = ObjectAnimator.ofFloat(mIvRight, "translationX", 150f);
        ObjectAnimator animTop = ObjectAnimator.ofFloat(mIvTop, "translationY", -150f);
        ObjectAnimator animBottom = ObjectAnimator.ofFloat(mIvBottom, "translationY", 150f);

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(500);
        animSet.setInterpolator(new BounceInterpolator());
        animSet.playTogether(animCenter, animLeft, animRight, animTop, animBottom);
        animSet.start();

        mFlag = false;
    }

    private void closeAnim() {
        ObjectAnimator animCenter = ObjectAnimator.ofFloat(mIvCenter, "alpha", 0.5f, 1f);
        ObjectAnimator animLeft = ObjectAnimator.ofFloat(mIvLeft, "translationX", 0f);
        ObjectAnimator animRight = ObjectAnimator.ofFloat(mIvRight, "translationX", 0f);
        ObjectAnimator animTop = ObjectAnimator.ofFloat(mIvTop, "translationY", 0f);
        ObjectAnimator animBottom = ObjectAnimator.ofFloat(mIvBottom, "translationY", 0f);

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(500);
        animSet.setInterpolator(new BounceInterpolator());
        animSet.playTogether(animCenter, animLeft, animRight, animTop, animBottom);
        animSet.start();

        mFlag = true;
    }
}
