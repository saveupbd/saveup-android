package com.saveup.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import androidx.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * @author AAPBD on 05,March,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.nyotshong.views
 */


public class RippleView extends Button {

    private float duration = 100;

    private float speed = 1;
    private float radius = 0;
    private Paint paint = new Paint();
    private float endRadius = 0;
    private float rippleX = 0;
    private float rippleY = 0;
    private int width = 0;
    private int height = 0;
    private OnClickListener clickListener = null;
    private Handler handler;
    private int touchAction;
    private RippleView thisRippleView = this;

    public RippleView(Context context)
    {
        this(context, null, 0);
    }

    public RippleView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public RippleView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        if (isInEditMode())
            return;

        handler = new Handler();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas)
    {
        super.onDraw(canvas);

        if(radius > 0 && radius < endRadius)
        {
            canvas.drawCircle(rippleX, rippleY, radius, paint);
            if(touchAction == MotionEvent.ACTION_UP)
                invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event)
    {
        rippleX = event.getX();
        rippleY = event.getY();

        switch(event.getAction())
        {
            case MotionEvent.ACTION_UP:
            {
                getParent().requestDisallowInterceptTouchEvent(false);
                touchAction = MotionEvent.ACTION_UP;

                radius = 1;
                endRadius = Math.max(Math.max(Math.max(width - rippleX, rippleX), rippleY), height - rippleY);
                speed = endRadius / duration * 10;
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if(radius < endRadius)
                        {
                            radius += speed;
                            paint.setAlpha(90 - (int) (radius / endRadius * 90));
                            handler.postDelayed(this, 1);
                        }
                        else
                        {

                            try{
                                clickListener.onClick(thisRippleView);
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }

                        }
                    }
                }, 10);
                invalidate();
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            {
                getParent().requestDisallowInterceptTouchEvent(false);
                touchAction = MotionEvent.ACTION_CANCEL;
                radius = 0;
                invalidate();
                break;
            }
            case MotionEvent.ACTION_DOWN:
            {
                getParent().requestDisallowInterceptTouchEvent(true);
                touchAction = MotionEvent.ACTION_UP;
                endRadius = Math.max(Math.max(Math.max(width - rippleX, rippleX), rippleY), height - rippleY);
                paint.setAlpha(90);
                radius = endRadius/4;
                invalidate();
                return true;
            }
            case MotionEvent.ACTION_MOVE:
            {
                if(rippleX < 0 || rippleX > width || rippleY < 0 || rippleY > height)
                {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    touchAction = MotionEvent.ACTION_CANCEL;
                    radius = 0;
                    invalidate();
                    break;
                }
                else
                {
                    touchAction = MotionEvent.ACTION_MOVE;
                    invalidate();
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void setOnClickListener(OnClickListener l)
    {
       try{
           clickListener = l;
       }catch (Exception ex){
           ex.printStackTrace();
       }

    }
}
