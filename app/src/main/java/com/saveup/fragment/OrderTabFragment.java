package com.saveup.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.saveup.activity.MainActivity;
import com.saveup.android.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AAPBD on 26/5/17.
 */

public class OrderTabFragment extends Fragment {


    private ArrayList<String> viewPagerList = new ArrayList<>();

    @BindView(R.id.view_pager)
    ViewPager pager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ordertabfragment, container, false);
        ButterKnife.bind(this, view);
        viewPagerList.add(getString(R.string.unredeemed));
        viewPagerList.add(getString(R.string.redeemed));

      //  viewPagerList.add(""+getActivity().getResources().getString(R.string.paypal));

        reloadTabs(0);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).setTag = "orderList";
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
        mainActivity.toolbar.setTitle("" + getResources().getString(R.string.myorders));
        ((MainActivity) getActivity()).toolbar.setLogo(R.drawable.transpent_logo);
        final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
        mainActivity.toggle.setHomeAsUpIndicator(upArrow);
        mainActivity.toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.onBackPressed();
            }
        });
    }

    public void reloadTabs(int pageIndex) {
        List<Fragment> fragments = buildFragments();

        MyFragmentPageAdapter mPageAdapter = new MyFragmentPageAdapter(getActivity(), getChildFragmentManager(), fragments, viewPagerList);
        pager.setClipToPadding(false);
        pager.setOffscreenPageLimit(0);
        pager.setAdapter(mPageAdapter);
        pager.setCurrentItem(pageIndex);

        tabLayout.setupWithViewPager(pager);
    }

    private List<Fragment> buildFragments() {
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < viewPagerList.size(); i++) {
            Bundle b = new Bundle();
            b.putInt("position", i);
            fragments.add(Fragment.instantiate(getActivity(), OrderTabDynamicFragment.class.getName(), b));
        }

        return fragments;
    }

    public class MyFragmentPageAdapter extends FragmentPagerAdapter {

        public int position = 0;
        private List<Fragment> myFragments;
        private ArrayList<String> viewPagerList;
        private Context context;

        MyFragmentPageAdapter(Context c, FragmentManager fragmentManager, List<Fragment> myFrags, ArrayList<String> viewPagerList) {
            super(fragmentManager);
            myFragments = myFrags;
            this.viewPagerList = viewPagerList;
            this.context = c;
        }

        @Override
        public Fragment getItem(int position) {

            return myFragments.get(position);

        }

        @Override
        public int getCount() {

            return myFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {

            setPos(position);
            return viewPagerList.get(position);
        }

        public int getPos() {
            return position;
        }

        public void add(Class<Fragment> c, String title, Bundle b) {
            myFragments.add(Fragment.instantiate(context, c.getName(), b));
            viewPagerList.add(title);
        }

        void setPos(int pos) {
            position = pos;
        }
    }

}

