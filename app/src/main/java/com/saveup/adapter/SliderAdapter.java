package com.saveup.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.saveup.android.R;

import java.util.List;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private List<Drawable> colorName;

    public SliderAdapter(Context context, List<Drawable> colorName) {
        this.context = context;
        this.colorName = colorName;
    }

    @Override
    public int getCount() {
        return colorName.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, null);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        linearLayout.setBackground(colorName.get(position));
        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
