package com.example.administrator.chotot.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.administrator.chotot.fragment.ScreenSlidePageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 20/10/2016.
 */

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    private List<String> picList = new ArrayList<>();
    private String idProduct;

    public ScreenSlidePagerAdapter(FragmentManager fm, String _idProduct) {
        super(fm);
        this.idProduct = _idProduct;
    }

    @Override
    public Fragment getItem(int i) {
        return ScreenSlidePageFragment.newInstance(picList.get(i), i, idProduct);
    }

    @Override
    public int getCount() {
        return picList.size();
    }

    public void addAll(List<String> picList) {
        this.picList = picList;
    }
}