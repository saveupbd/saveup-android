package com.saveup.autoslide;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Message;
import androidx.viewpager.widget.PagerAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;

import com.saveup.autoslide.Animations.BaseAnimationInterface;
import com.saveup.autoslide.SliderTypes.BaseSliderView;
import com.saveup.autoslide.Transformers.BaseTransformer;
import com.saveup.autoslide.Transformers.FadeTransformer;
import com.saveup.autoslide.Tricks.FixedSpeedScroller;
import com.saveup.autoslide.Tricks.InfinitePagerAdapter;
import com.saveup.autoslide.Tricks.InfiniteViewPager;
import com.saveup.autoslide.Tricks.ViewPagerEx;
import com.saveup.android.R;
import com.saveup.autoslide.Animations.DescriptionAnimation;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

/**
 * SliderLayout is compound layout. This is combined with {@link com.aapbd.autoslide.Indicators.PagerIndicator}
 * and {@link ViewPagerEx} .
 * <p>
 * There is some properties you can set in XML:
 * <p>
 * indicator_visibility
 * visible
 * invisible
 * <p>
 * indicator_shape
 * oval
 * rect
 * <p>
 * indicator_selected_color
 * <p>
 * indicator_unselected_color
 * <p>
 * indicator_selected_drawable
 * <p>
 * indicator_unselected_drawable
 * <p>
 * pager_animation
 * Default
 * Accordion
 * Background2Foreground
 * CubeIn
 * DepthPage
 * Fade
 * FlipHorizontal
 * FlipPage
 * Foreground2Background
 * RotateDown
 * RotateUp
 * Stack
 * Tablet
 * ZoomIn
 * ZoomOutSlide
 * ZoomOut
 * <p>
 * pager_animation_span
 */
public class SliderLayout extends RelativeLayout {

    private Context mContext;
    /**
     * InfiniteViewPager is extended from ViewPagerEx. As the name says, it can scroll without bounder.
     */
    private InfiniteViewPager mViewPager;

    /**
     * InfiniteViewPager adapter.
     */
    private SliderAdapter mSliderAdapter;

    /**
     * {@link ViewPagerEx} indicator.
     */


    /**
     * A timer and a TimerTask using to cycle the {@link ViewPagerEx}.
     */
    private Timer mCycleTimer;
    private TimerTask mCycleTask;

    /**
     * For resuming the cycle, after user touch or click the {@link ViewPagerEx}.
     */
    private Timer mResumingTimer;
    private TimerTask mResumingTask;

    /**
     * If {@link ViewPagerEx} is Cycling
     */
    private boolean mCycling;

    /**
     * Determine if auto recover after user touch the {@link ViewPagerEx}
     */
    private boolean mAutoRecover = true;

    private int mTransformerId;

    /**
     * {@link ViewPagerEx} transformer time span.
     */
    private int mTransformerSpan = 1100;

    private boolean mAutoCycle;

    /**
     * the duration between animation.
     */
    private long mSliderDuration = 4000;

    /**
     * Visibility of {@link com.aapbd.autoslide.Indicators.PagerIndicator}
     */

    /**
     * {@link ViewPagerEx} 's transformer
     */
    private BaseTransformer mViewPagerTransformer;

    /**
     * @see BaseAnimationInterface
     */
    private BaseAnimationInterface mCustomAnimation;

    /**
     * {@link com.aapbd.autoslide.Indicators.PagerIndicator} shape, rect or oval.
     */

    public SliderLayout(Context context) {
        this(context, null);
    }

    public SliderLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.SliderStyle);
    }

    public SliderLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.slider_layout, this, true);

        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SliderLayout,
                defStyle, 0);

        mTransformerSpan = attributes.getInteger(R.styleable.SliderLayout_pager_animation_span, 1100);
        mTransformerId = attributes.getInt(R.styleable.SliderLayout_pager_animation, Transformer.Default.ordinal());
        mAutoCycle = attributes.getBoolean(R.styleable.SliderLayout_auto_cycle, true);
        int visibility = attributes.getInt(R.styleable.SliderLayout_indicator_visibility, 0);

        mSliderAdapter = new SliderAdapter(mContext);
        PagerAdapter wrappedAdapter = new InfinitePagerAdapter(mSliderAdapter);

        mViewPager = findViewById(R.id.daimajia_slider_viewpager);
        mViewPager.setAdapter(wrappedAdapter);
        mViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        recoverCycle();
                        break;
                }
                return false;
            }
        });

        attributes.recycle();
        setPresetTransformer(mTransformerId);
        setSliderTransformDuration(mTransformerSpan, null);
        if (mAutoCycle) {
            startAutoCycle();
        }
    }

    public void addOnPageChangeListener(ViewPagerEx.OnPageChangeListener onPageChangeListener) {
        if (onPageChangeListener != null) {
            mViewPager.addOnPageChangeListener(onPageChangeListener);
        }
    }

    public void removeOnPageChangeListener(ViewPagerEx.OnPageChangeListener onPageChangeListener) {
        mViewPager.removeOnPageChangeListener(onPageChangeListener);
    }


    public <T extends BaseSliderView> void addSlider(T imageContent) {
        mSliderAdapter.addSlider(imageContent);
    }

    private android.os.Handler mh = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            moveNextPosition(true);
        }
    };

    public void startAutoCycle() {
        startAutoCycle(mSliderDuration, mSliderDuration, mAutoRecover);
    }

    /**
     * start auto cycle.
     *
     * @param delay       delay time
     * @param duration    animation duration time.
     * @param autoRecover if recover after user touches the slider.
     */
    public void startAutoCycle(long delay, long duration, boolean autoRecover) {
        if (mCycleTimer != null) mCycleTimer.cancel();
        if (mCycleTask != null) mCycleTask.cancel();
        if (mResumingTask != null) mResumingTask.cancel();
        if (mResumingTimer != null) mResumingTimer.cancel();
        mSliderDuration = duration;
        mCycleTimer = new Timer();
        mAutoRecover = autoRecover;
        mCycleTask = new TimerTask() {
            @Override
            public void run() {
                mh.sendEmptyMessage(0);
            }
        };
        mCycleTimer.schedule(mCycleTask, delay, mSliderDuration);
        mCycling = true;
        mAutoCycle = true;
    }

    /**
     * pause auto cycle.
     */
    private void pauseAutoCycle() {
        if (mCycling) {
            mCycleTimer.cancel();
            mCycleTask.cancel();
            mCycling = false;
        } else {
            if (mResumingTimer != null && mResumingTask != null) {
                recoverCycle();
            }
        }
    }

    /**
     * set the duration between two slider changes. the duration value must >= 500
     *
     * @param duration
     */
    public void setDuration(long duration) {
        if (duration >= 500) {
            mSliderDuration = duration;
            if (mAutoCycle && mCycling) {
                startAutoCycle();
            }
        }
    }

    /**
     * stop the auto circle
     */
    public void stopAutoCycle() {
        if (mCycleTask != null) {
            mCycleTask.cancel();
        }
        if (mCycleTimer != null) {
            mCycleTimer.cancel();
        }
        if (mResumingTimer != null) {
            mResumingTimer.cancel();
        }
        if (mResumingTask != null) {
            mResumingTask.cancel();
        }
        mAutoCycle = false;
        mCycling = false;
    }

    /**
     * when paused cycle, this method can weak it up.
     */
    private void recoverCycle() {
        if (!mAutoRecover || !mAutoCycle) {
            return;
        }

        if (!mCycling) {
            if (mResumingTask != null && mResumingTimer != null) {
                mResumingTimer.cancel();
                mResumingTask.cancel();
            }
            mResumingTimer = new Timer();
            mResumingTask = new TimerTask() {
                @Override
                public void run() {
                    startAutoCycle();
                }
            };
            mResumingTimer.schedule(mResumingTask, 6000);
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return false;
    }

    /**
     * set ViewPager transformer.
     *
     * @param reverseDrawingOrder
     * @param transformer
     */
    public void setPagerTransformer(boolean reverseDrawingOrder, BaseTransformer transformer) {
        mViewPagerTransformer = transformer;
        mViewPagerTransformer.setCustomAnimationInterface(mCustomAnimation);
        mViewPager.setPageTransformer(reverseDrawingOrder, mViewPagerTransformer);
    }


    /**
     * set the duration between two slider changes.
     *
     * @param period
     * @param interpolator
     */
    public void setSliderTransformDuration(int period, Interpolator interpolator) {
        try {
            Field mScroller = ViewPagerEx.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(mViewPager.getContext(), interpolator, period);
            mScroller.set(mViewPager, scroller);
        } catch (Exception e) {

        }
    }

    /**
     * preset transformers and their names
     */
    public enum Transformer {
        Default("Default"),
        Accordion("Accordion"),
        Background2Foreground("Background2Foreground"),
        CubeIn("CubeIn"),
        DepthPage("DepthPage"),
        Fade("Fade"),
        FlipHorizontal("FlipHorizontal"),
        FlipPage("FlipPage"),
        Foreground2Background("Foreground2Background"),
        RotateDown("RotateDown"),
        RotateUp("RotateUp"),
        Stack("Stack"),
        Tablet("Tablet"),
        ZoomIn("ZoomIn"),
        ZoomOutSlide("ZoomOutSlide"),
        ZoomOut("ZoomOut");

        private final String name;

        Transformer(String s) {
            name = s;
        }

        public String toString() {
            return name;
        }

        public boolean equals(String other) {
            return (other != null) && name.equals(other);
        }
    }

    /**
     * set a preset viewpager transformer by id.
     *
     * @param transformerId
     */
    public void setPresetTransformer(int transformerId) {
        for (Transformer t : Transformer.values()) {
            if (t.ordinal() == transformerId) {
                setPresetTransformer(t);
                break;
            }
        }
    }

    /**
     * set preset PagerTransformer via the name of transforemer.
     *
     * @param transformerName
     */
    public void setPresetTransformer(String transformerName) {
        for (Transformer t : Transformer.values()) {
            if (t.equals(transformerName)) {
                setPresetTransformer(t);
                return;
            }
        }
    }

    /**
     * Inject your custom animation into PageTransformer, you can know more details in
     * {@link BaseAnimationInterface},
     * and you can see a example in {@link DescriptionAnimation}
     *
     * @param animation
     */
    public void setCustomAnimation(BaseAnimationInterface animation) {
        mCustomAnimation = animation;
        if (mViewPagerTransformer != null) {
            mViewPagerTransformer.setCustomAnimationInterface(mCustomAnimation);
        }
    }

    /**
     * pretty much right? enjoy it. :-D
     *
     * @param ts
     */
    public void setPresetTransformer(Transformer ts) {
        //
        // special thanks to https://github.com/ToxicBakery/ViewPagerTransforms
        //
        BaseTransformer t = null;
        switch (ts) {

            case Fade:
                t = new FadeTransformer();
                break;
            default:
                t = new FadeTransformer();
                break;

        }
        setPagerTransformer(true, t);
    }





    private InfinitePagerAdapter getWrapperAdapter() {
        PagerAdapter adapter = mViewPager.getAdapter();
        if (adapter != null) {
            return (InfinitePagerAdapter) adapter;
        } else {
            return null;
        }
    }

    private SliderAdapter getRealAdapter() {
        PagerAdapter adapter = mViewPager.getAdapter();
        if (adapter != null) {
            return ((InfinitePagerAdapter) adapter).getRealAdapter();
        }
        return null;
    }

    /**
     * get the current item position
     *
     * @return
     */
    public int getCurrentPosition() {

        if (getRealAdapter() == null)
            throw new IllegalStateException("You did not set a slider adapter");

        return mViewPager.getCurrentItem() % getRealAdapter().getCount();

    }

    /**
     * get current slider.
     *
     * @return
     */
    public BaseSliderView getCurrentSlider() {

        if (getRealAdapter() == null)
            throw new IllegalStateException("You did not set a slider adapter");

        int count = getRealAdapter().getCount();
        int realCount = mViewPager.getCurrentItem() % count;
        return getRealAdapter().getSliderView(realCount);
    }

    /**
     * remove  the slider at the position. Notice: It's a not perfect method, a very small bug still exists.
     */
    public void removeSliderAt(int position) {
        if (getRealAdapter() != null) {
            getRealAdapter().removeSliderAt(position);
            mViewPager.setCurrentItem(mViewPager.getCurrentItem(), false);
        }
    }

    /**
     * remove all the sliders. Notice: It's a not perfect method, a very small bug still exists.
     */
    public void removeAllSliders() {
        if (getRealAdapter() != null) {
            int count = getRealAdapter().getCount();
            getRealAdapter().removeAllSliders();
            //a small bug, but fixed by this trick.
            //bug: when remove adapter's all the sliders.some caching slider still alive.
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + count, false);
        }
    }

    /**
     * set current slider
     *
     * @param position
     */
    public void setCurrentPosition(int position, boolean smooth) {
        if (getRealAdapter() == null)
            throw new IllegalStateException("You did not set a slider adapter");
        if (position >= getRealAdapter().getCount()) {
            throw new IllegalStateException("Item position is not exist");
        }
        int p = mViewPager.getCurrentItem() % getRealAdapter().getCount();
        int n = (position - p) + mViewPager.getCurrentItem();
        mViewPager.setCurrentItem(n, smooth);
    }

    public void setCurrentPosition(int position) {
        setCurrentPosition(position, true);
    }

    /**
     * move to prev slide.
     */
    public void movePrevPosition(boolean smooth) {

        if (getRealAdapter() == null)
            throw new IllegalStateException("You did not set a slider adapter");

        mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, smooth);
    }

    public void movePrevPosition() {
        movePrevPosition(true);
    }

    /**
     * move to next slide.
     */
    public void moveNextPosition(boolean smooth) {

        if (getRealAdapter() == null)
            throw new IllegalStateException("You did not set a slider adapter");

        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, smooth);
    }

    public void moveNextPosition() {
        moveNextPosition(true);
    }
}
