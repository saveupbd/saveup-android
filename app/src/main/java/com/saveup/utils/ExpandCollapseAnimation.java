package com.saveup.utils;

/**
 * Created by AAPBD on 13/2/17.
 */

import android.app.Activity;
import androidx.core.widget.NestedScrollView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

/**
 * Class for handling collapse and expand animations.
 * @author Esben Gaarsmand
 *
 */
public class ExpandCollapseAnimation extends Animation {
    private View mAnimatedView;
    private int mEndHeight;
    private int mType;
    private NestedScrollView mScrollView;

    public ExpandCollapseAnimation(View view, int duration, int type, Activity activity, NestedScrollView scrollView) {
        setDuration(duration);
        mAnimatedView = view;
        mScrollView  = scrollView;
        setHeightForWrapContent(activity, view);

        mEndHeight = mAnimatedView.getLayoutParams().height;

        mType = type;
        if(mType == 0) {
            mAnimatedView.getLayoutParams().height = 0;
            mAnimatedView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        if (interpolatedTime < 1.0f) {
            if(mType == 0) {
                mAnimatedView.getLayoutParams().height = (int) (mEndHeight * interpolatedTime);
            } else {
                mAnimatedView.getLayoutParams().height = mEndHeight - (int) (mEndHeight * interpolatedTime);
            }
            mAnimatedView.requestLayout();
            mScrollView.post(new Runnable() {
                @Override
                public void run() {
                    mScrollView.fullScroll(View.FOCUS_DOWN);
                }
            });
        } else {
            if(mType == 0) {
                mAnimatedView.getLayoutParams().height = mEndHeight;
                mAnimatedView.requestLayout();
                mScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        mScrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });

            } else {
                mAnimatedView.getLayoutParams().height = 0;
                mAnimatedView.setVisibility(View.GONE);
                mAnimatedView.requestLayout();
                mAnimatedView.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;     // Return to wrap
                mScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        mScrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        }
    }

    public static void setHeightForWrapContent(Activity activity, View view) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int screenWidth = metrics.widthPixels;

        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(screenWidth, View.MeasureSpec.EXACTLY);

        view.measure(widthMeasureSpec, heightMeasureSpec);
        int height = view.getMeasuredHeight();
        view.getLayoutParams().height = height;
    }
}