package com.saveup.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.saveup.activity.MainActivity;
import com.saveup.adapter.SliderAdapter;
import com.saveup.android.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;

/**
 * Created by AAPBD on 26/5/17.
 */

public class HowToFragment extends Fragment {


    private final String TAG = "HowToFragment";

    ViewPager viewPager;
    TabLayout indicator;

    List<Drawable> images;

    Timer timer;

    public HowToFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.how_to_fragment, container, false);
        ButterKnife.bind(this, view);

        viewPager=view.findViewById(R.id.viewPager);
        indicator=view.findViewById(R.id.indicator);

        images = new ArrayList<>();
        images.add(getResources().getDrawable(R.drawable.how_to_1));
        images.add(getResources().getDrawable(R.drawable.how_to_2));
        images.add(getResources().getDrawable(R.drawable.how_to_3));
        images.add(getResources().getDrawable(R.drawable.how_to_4));
        images.add(getResources().getDrawable(R.drawable.how_to_5));
        images.add(getResources().getDrawable(R.drawable.how_to_6));


        viewPager.setAdapter(new SliderAdapter(getContext(), images));
        indicator.setupWithViewPager(viewPager, true);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).setTag = "howTo";
        ((MainActivity) getActivity()).searchIcon.setVisible(false);
        final MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.search_layout.setVisibility(View.GONE);
        mainActivity.menu_Name.setVisible(false);
        mainActivity.toggle.setDrawerIndicatorEnabled(false);
        mainActivity.toolbar.setVisibility(View.VISIBLE);
        mainActivity.disableDrawer(true);
        mainActivity.cartIconempty.setVisible(false);
     //   mainActivity.cartItem.setVisible(false);
        mainActivity.editIcon.setVisible(false);
        mainActivity.toolbar.setTitle("" + getResources().getString(R.string.how_to));
        ((MainActivity) getActivity()).toolbar.setLogo(R.drawable.transpent_logo);
        final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
        mainActivity.toggle.setHomeAsUpIndicator(upArrow);
        mainActivity.toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timer != null){
                    timer.cancel();
                }
                mainActivity.onBackPressed();
            }
        });

        //timer = new Timer();
        //timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer != null){
            timer.cancel();
        }
    }

    private class SliderTimer extends TimerTask {

        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(viewPager != null) {
                        if (viewPager.getCurrentItem() < images.size() - 1) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                }
            });
        }
    }

}

