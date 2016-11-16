package com.example.administrator.chotot.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.chotot.R;

/**
 * Created by Administrator on 15/10/2016.
 */

public class SellFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell, container, false);

        init(view);

        return view;
    }

    private void init(View view){
        tabLayout = (TabLayout)view.findViewById(R.id.tabs);
        viewPager = (ViewPager)view.findViewById(R.id.viewpager);

        setViewPager();
    }

    private void setViewPager(){
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.selling)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.approval)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.denied)));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        FragmentAdapterClass fragmentAdapter = new FragmentAdapterClass(getChildFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(fragmentAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab LayoutTab) {

                viewPager.setCurrentItem(LayoutTab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab LayoutTab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab LayoutTab) {

            }
        });
    }

    class FragmentAdapterClass extends FragmentStatePagerAdapter {

        int TabCount;

        public FragmentAdapterClass(FragmentManager fragmentManager, int CountTabs) {

            super(fragmentManager);

            this.TabCount = CountTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    SellingFragment tab1 = new SellingFragment();
                    return tab1;

                case 1:
                    ApprovalFragment tab2 = new ApprovalFragment();
                    return tab2;

                case 2:
                    DeniedFragment tab3 = new DeniedFragment();
                    return tab3;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return TabCount;
        }
    }
}
