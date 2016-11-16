package com.example.administrator.chotot.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.administrator.chotot.fragment.ZoomImageSliderFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 22/10/2016.
 */

public class ZoomImageSliderAdapter  extends FragmentStatePagerAdapter {
    private List<String> picList = new ArrayList<>();

    public ZoomImageSliderAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        //Resimleri Image loader kütüphanesini kullanarak yüklenmesi icin resim url, ScreenSlidePageFragment sınıfına atadık.
        return ZoomImageSliderFragment.newInstance(picList.get(i));
    }

    @Override
    public int getCount() {
        return picList.size();
    }

    public void addAll(List<String> picList) {
        this.picList = picList;
    }
}